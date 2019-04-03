package cn.org.bjca.anysign.seal.global.tools.controller;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.controller
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/9 20:06
 ***************************************************************************/
@RestController
public class IndexController {

    @RequestMapping("/ex")
    public void ex(){
        throw new BaseRuntimeException(StatusConstants.FAIL);
    }

    @RequestMapping("/exnull")
    public void exNull(){
      int a = 10/0;
    }


    @RequestMapping("/getUser")
    public ObjectRestResponse<User> getUser (){
        User user = new User();
        user.setUserName("张大哈");
        user.setPhone("12332112363");
        user.setAge(1);
        user.setSex(true);
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user2 = new User();
            user2.setUserName("张大哈");
            user2.setPhone("12332112363");
            user2.setAge(1);
            user2.setSex(true);
            userList.add(user2);
        }
        user.setUserList(userList);
        return new ObjectRestResponse<>(user);
    }


    @RequestMapping("/getUsers")
    public ObjectRestResponse<List<User>> getUsers (){
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user2 = new User();
            user2.setUserName("王大".concat(String.valueOf(i)));
            user2.setPhone("12332112363");
            user2.setAge(1);
            user2.setSex(true);
            userList.add(user2);
        }
        return new ObjectRestResponse<>(userList);
    }

}