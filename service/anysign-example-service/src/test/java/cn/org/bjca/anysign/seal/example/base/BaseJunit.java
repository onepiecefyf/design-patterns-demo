package cn.org.bjca.anysign.seal.example.base;

import cn.org.bjca.anysign.seal.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/***************************************************************************
 * <pre>基础测试</pre>
 *
 * @文件名称: BaseJunit.class
 * @包 路   径：  cn.org.bjca.anysign.esal.example.base
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/8 21:24
 ***************************************************************************/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
//@Transactional //打开的话测试之后数据可自动回滚
public class BaseJunit {

  @Autowired
  WebApplicationContext webApplicationContext;

  protected MockMvc mockMvc;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Before
  public void initDatabase() {
  }

}