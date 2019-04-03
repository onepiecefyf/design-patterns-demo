package cn.org.bjca.anysign.seal.example.test;

import cn.org.bjca.anysign.seal.example.base.BaseJunit;
import cn.org.bjca.anysign.seal.example.model.User;
import cn.org.bjca.anysign.seal.example.service.IUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/***************************************************************************
 * <pre>用户测试</pre>
 *
 * @文件名称: UserServiceTest.class
 * @包 路   径：  cn.org.bjca.anysign.esal.example.dao
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/8 21:28
 ***************************************************************************/
public class UserServiceTest extends BaseJunit {

    @Resource
    private IUserService userService;

    @Test
    public void addUser(){
        User user = new User();
        user.setName("serviceUserTest");
        user.setPhone("18232533068");
        user.setAccount("1000");
        user.setBirthday(new Date());
        user.setEmail("2935427732@qq.com");
        user.setSex(1);
        user.setStatus(1);
        user.setPassword("123321");
        userService.insert(user);
    }

    @Test
    public void selectUserByAccount(){
        User user = userService.getByAccount("1001");
        assertTrue(user.getName().equals("测试2"));
    }
    @Test
    public void updateUser(){
        User user = userService.getByAccount("1000");
        user.setName("李四");
        userService.updateById(user);
        User upUser = userService.getByAccount("1000");
        assertTrue(upUser.getName().equals("李四"));
    }

    @Test
    public void delUser(){
        User user = userService.getByAccount("1000");
        boolean integer = userService.deleteById(user);
        assertTrue(integer);
    }

    @Test
    public void pageUser(){
        Page<User> page = new Page<>(1,3);
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sex","1");
        page = userService.selectPage(page,entityWrapper);
        for (User user:page.getRecords()){
            System.out.println(user.getName());
        }
    }

}