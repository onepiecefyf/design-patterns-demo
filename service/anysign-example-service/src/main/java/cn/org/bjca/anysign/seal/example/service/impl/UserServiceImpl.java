package cn.org.bjca.anysign.seal.example.service.impl;

import cn.org.bjca.anysign.seal.example.dao.UserMapper;
import cn.org.bjca.anysign.seal.example.model.User;
import cn.org.bjca.anysign.seal.example.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/***************************************************************************
 * <pre>用户service</pre>
 *
 * @author july_whj
 * @文件名称: UserServiceImpl.class
 * @包 路   径：  cn.org.bjca.anysign.seal.service.impl
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/9 17:31
 ***************************************************************************/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Override
    public User getByAccount(@Param("account") String account) {
        return baseMapper.getByAccount(account);
    }
}