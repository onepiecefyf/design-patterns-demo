package cn.org.bjca.anysign.seal.global.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/***************************************************************************
 * <pre>配置文件读取工具类</pre>
 *
 * @文件名称: PropertiesUtils.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 14:14
 ***************************************************************************/
public class PropertiesUtils {

  private static Properties properties;

  private String properResource;

  public void setProperResource(String properResource) {
    this.properResource = properResource;
  }

  public PropertiesUtils(String properResource) {
    this.properResource = properResource;
    loadResource(properResource);
  }

  public static Properties getInstance() {
    if (null == properties) {
      properties = new Properties();
    }
    return properties;
  }

  public static Properties loadResource(String file) {
    properties = getInstance();
    InputStream inputStream = null;
    try {
      inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
      if (properties != null) {
        properties.load(inputStream);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {

      }
    }
    return properties;
  }

  /**
   * 获取int类型配置信息
   *
   * @param key key
   * @param defvalue 默认值
   * @return value
   */
  public int getInt(String key, int defvalue) {
    return (StringUtils.isEmpty(properties.getProperty(key))) ? defvalue
        : Integer.valueOf(properties.getProperty(key));
  }

  /**
   * 获取long类型配置
   *
   * @param key key
   * @param defvalue 默认值
   * @return value
   */
  public long getLong(String key, long defvalue) {
    return (StringUtils.isEmpty(properties.getProperty(key))) ? defvalue
        : Integer.valueOf(properties.getProperty(key));
  }

  /**
   * 获取String类型配置
   *
   * @param key key
   * @param defvalue 默认值
   * @return value
   */
  public String getString(String key, String defvalue) {
    return (StringUtils.isEmpty(properties.getProperty(key))) ? defvalue
        : properties.getProperty(key);
  }

  /**
   * 获取Boolean类型配置值
   *
   * @param key key
   * @param defvalue 默认值
   * @return value
   */
  public boolean getBoolean(String key, boolean defvalue) {
    return (StringUtils.isEmpty(properties.getProperty(key))) ? defvalue
        : Boolean.valueOf(properties.getProperty(key));
  }

  /**
   * 获取Boolean类型值
   *
   * @param key key
   * @return value
   */
  public boolean getBoolean(String key) {
    return Boolean.valueOf(properties.getProperty(key));
  }

  /**
   * 获取int类型配置信息
   *
   * @param key key
   */
  public int getInt(String key) {
    return Integer.valueOf(properties.getProperty(key));
  }

  /**
   * 获取long类型配置
   *
   * @param key key
   */
  public long getLong(String key) {
    return Integer.valueOf(properties.getProperty(key));
  }

  /**
   * 获取String类型配置
   */
  public String getString(String key) {
    return properties.getProperty(key);
  }

}