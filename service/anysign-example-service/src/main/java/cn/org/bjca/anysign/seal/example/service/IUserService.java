package cn.org.bjca.anysign.seal.example.service;

import cn.org.bjca.anysign.seal.example.model.User;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

/***************************************************************************
 * <pre>用户service接口</pre>
 *
 * @文件名称: IUserService.class
 * @包 路   径：  cn.org.bjca.anysign.seal.service
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/9 17:31
 ***************************************************************************/
public interface IUserService extends IService<User> {

  /**
   * 通过账号获取用户
   */
  User getByAccount(@Param("account") String account);

}
