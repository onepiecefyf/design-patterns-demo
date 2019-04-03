package cn.org.bjca.anysign.seal.global.tools.controller;

import java.io.Serializable;
import java.util.List;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 19:03
 ***************************************************************************/
public class User implements Serializable {

  private String userName;

  private String phone;

  private List<User> userList;

  private int age;

  private boolean sex;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<User> getUserList() {
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isSex() {
    return sex;
  }

  public void setSex(boolean sex) {
    this.sex = sex;
  }
}