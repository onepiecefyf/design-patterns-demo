### 疑惑
查看项目启动类并没有配置@ComponentScan，很疑惑为什么能够扫描到每一个模块注入到Spring容器。

### 发现
深入到每个模块之后发现，在每一个模块资源文件之下都有一个路径META-INF/spring.factories，在这个配置文件下加载了一个配置类，配置类配置了扫描该模块的注解@ComponentScan实现相关注入。  
> 在Spring Boot中有一种非常解耦的扩展机制：Spring Factories。这种扩展机制实际上是仿照Java中的SPI扩展机制来实现的。

### Java SPI扩展机制

> Java SPI机制SPI的全名为Service Provider Interface.大多数开发人员可能不熟悉，因为这个是针对厂商或者插件的。在java.util.ServiceLoader的文档里有比较详细的介绍。简单的总结下java spi机制的思想。我们系统里抽象的各个模块，往往有很多不同的实现方案，比如日志模块的方案，xml解析模块、jdbc模块的方案等。面向的对象的设计里，我们一般推荐模块之间基于接口编程，模块之间不对实现类进行硬编码。一旦代码里涉及具体的实现类，就违反了可拔插的原则，如果需要替换一种实现，就需要修改代码。为了实现在模块装配的时候能不在程序里动态指明，这就需要一种服务发现机制。 java spi就是提供这样的一个机制：为某个接口寻找服务实现的机制。有点类似IOC的思想，就是将装配的控制权移到程序之外，在模块化设计中这个机制尤其重要。

### Java SPI约定

> java spi的具体约定为:当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。 基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。jdk提供服务实现查找的一个工具类：java.util.ServiceLoader

### SpringBoot SPI扩展机制
在Spring中也有一种类似与Java SPI的加载机制。它在META-INF/spring.factories文件中配置接口的实现类名称，然后在程序中读取这些配置文件并实例化。这种自定义的SPI机制是Spring Boot Starter实现的基础。
```java
/** 在每一个模块资源目录下 */
resources
  META-INF
    spring.factories
```

#### 1、spring.factories配置信息
```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
cn.org.bjca.basic.product.data.security.management.business.capital.DsmsCapitalAutoConfiguration
```

对应加载类信息
```java
@Configuration
@ComponentScan
@AllArgsConstructor
public class DsmsCapitalAutoConfiguration {

}
```

#### 2、Spring Factories实现原理
spring-core包里定义了SpringFactoriesLoader类，这个类实现了检索META-INF/spring.factories文件，并获取指定接口的配置的功能。在这个类中定义了两个对外的方法：  
loadFactories：根据接口类获取其实现类的实例，这个方法返回的是对象列表。  
loadFactoryNames：根据接口获取其接口类的名称，这个方法返回的是类名的列表。  
上面的两个方法的关键都是从指定的ClassLoader中获取spring.factories文件，并解析得到类名列表，具体代码如下  
```java
public final class SpringFactoriesLoader {
  public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
  private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);
  private static final Map<ClassLoader, MultiValueMap<String, String>> cache =
      new ConcurrentReferenceHashMap();

  private SpringFactoriesLoader() {}

  private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = (MultiValueMap) cache.get(classLoader);
    if (result != null) {
      return result;
    } else {
      try {
        Enumeration<URL> urls =
            classLoader != null
                ? classLoader.getResources("META-INF/spring.factories")
                : ClassLoader.getSystemResources("META-INF/spring.factories");
        LinkedMultiValueMap result = new LinkedMultiValueMap();

        while (urls.hasMoreElements()) {
          URL url = (URL) urls.nextElement();
          UrlResource resource = new UrlResource(url);
          Properties properties = PropertiesLoaderUtils.loadProperties(resource);
          Iterator var6 = properties.entrySet().iterator();

          while (var6.hasNext()) {
            Entry<?, ?> entry = (Entry) var6.next();
            String factoryTypeName = ((String) entry.getKey()).trim();
            String[] var9 = StringUtils.commaDelimitedListToStringArray((String) entry.getValue());
            int var10 = var9.length;

            for (int var11 = 0; var11 < var10; ++var11) {
              String factoryImplementationName = var9[var11];
              result.add(factoryTypeName, factoryImplementationName.trim());
            }
          }
        }

        cache.put(classLoader, result);
        return result;
      } catch (IOException var13) {
        throw new IllegalArgumentException(
            "Unable to load factories from location [META-INF/spring.factories]", var13);
      }
    }
  }
}
```

从代码中我们可以知道，在这个方法中会遍历整个ClassLoader中所有jar包下的spring.factories文件。也就是说我们可以在自己的jar中配置spring.factories文件，不会影响到其它地方的配置，也不会被别人的配置覆盖。  

spring.factories的是通过Properties解析得到的，所以我们在写文件中的内容都是安装下面这种方式配置的：  

> com.xxx.interface=com.xxx.classname  

> 如果一个接口希望配置多个实现类，可以使用','进行分割。  

对于loadFactories方法而言，在获取类列表的基础上，还有进行实例化的过程。

#### 3、实例化过程
从这段代码中我们可以知道，它只支持没有参数的构造函数。Spring Factories在Spring Boot中的应用
在Spring Boot的很多包中都能够找到spring.factories文件。
#### 4、spring.factories文件内容
在日常工作中，我们可能需要实现一些SDK或者Spring Boot Starter给被人使用，这个使用我们就可以使用Factories机制。Factories机制可以让SDK或者Starter的使用只需要很少或者不需要进行配置，只需要在服务中引入我们的jar包。









