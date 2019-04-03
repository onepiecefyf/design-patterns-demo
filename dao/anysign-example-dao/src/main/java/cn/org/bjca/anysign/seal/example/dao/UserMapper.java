package cn.org.bjca.anysign.seal.example.dao;

import cn.org.bjca.anysign.seal.example.model.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/***************************************************************************
 * <pre></pre>
 *
 * @author july_whj
 * @文件名称: UserMapper.class
 * @包 路   径：  cn.org.bjca.anysign.esal.example.dao
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/8 21:06
 ***************************************************************************/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户状态
     */
    int setStatus(@Param("userId") Integer userId, @Param("status") int status);

    /**
     * 修改密码
     */
    int changePwd(@Param("userId") Integer userId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(@Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid);

    /**
     * 设置用户的角色
     */
    int setRoles(@Param("userId") Integer userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     */
    User getByAccount(@Param("account") String account);

}