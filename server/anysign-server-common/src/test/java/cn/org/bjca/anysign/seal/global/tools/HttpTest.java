package cn.org.bjca.anysign.seal.global.tools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/***************************************************************************
 * <pre>全局异常和拦截器测试</pre>
 *
 * @文件名称: HttpTest.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 19:47
 ***************************************************************************/
@RunWith(SpringJUnit4ClassRunner.class)
public class HttpTest {


    private final static String url = "http://localhost:8080/";

    private static RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test() {
        ResponseEntity<String> response = restTemplate.exchange(url + "json",
                HttpMethod.GET,
                new HttpEntity(null),
                String.class);
        System.out.println("result: " + response.getBody());
    }

    @Test
    public void ex(){
        ResponseEntity<String> response = restTemplate.exchange(url + "ex?version=1.0&deviceId=11&appId=ew",
                HttpMethod.GET,
                new HttpEntity(null),
                String.class);
        System.out.println("result: " + response.getBody());
    }


}