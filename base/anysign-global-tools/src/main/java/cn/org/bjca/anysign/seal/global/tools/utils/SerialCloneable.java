package cn.org.bjca.anysign.seal.global.tools.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;

/***************************************************************************
 * <pre>克隆对象工具类</pre>
 *
 * @author july_whj
 * @文件名称: SerialCloneable.cls
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/8 9:43
 ***************************************************************************/
@Slf4j
public class SerialCloneable implements Cloneable, Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 深拷贝
   */
  @Override
  public Object clone() {
    try {
      // save the object to a byte array
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bout);
      out.writeObject(this);
      out.close();
      // read a clone of the object from the byte array
      ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
      ObjectInputStream in = new ObjectInputStream(bin);
      Object ret = in.readObject();
      in.close();
      return ret;
    } catch (Exception e) {
      log.error("SerialCloneable clone is fail ! ", e);
      return null;
    }
  }

}