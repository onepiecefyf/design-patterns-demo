package cn.org.bjca.anysign.seal.server.common.message;


import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;

/***************************************************************************
 * <pre>返回对象实体</pre>
 *
 * @author july_whj
 * @文件名称: ObjectRestResponse.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 11:03
 ***************************************************************************/
public class ObjectRestResponse<E> extends BaseResponse {

  /**
   * 返回数据
   */
  private E data;
  /**
   * 数据是否存在
   */
  private boolean rel = false;

  /**
   * 构造方法
   *
   * @param statusConstants 枚举
   * @param trace 异常信息
   * @param data 返回数据
   */
  public ObjectRestResponse(StatusConstants statusConstants, String trace, E data, boolean rel) {
    super(statusConstants, trace);
    this.data = data;
    this.rel = rel;
  }

  /**
   * 构造方法
   *
   * @param statusConstants 枚举类
   * @param data 数据
   */
  public ObjectRestResponse(StatusConstants statusConstants, E data, boolean rel) {
    super(statusConstants);
    this.data = data;
    this.rel = rel;
  }

  /**
   * 构造方法
   *
   * @param status 状态
   * @param message 返回信息
   * @param trace 异常信息
   * @param data 返回数据
   */
  public ObjectRestResponse(String status, String message, String trace, E data, boolean rel) {
    super(status, message, trace);
    this.data = data;
    this.rel = rel;
  }

  /**
   * 构造方法
   *
   * @param data 返回数据
   */
  public ObjectRestResponse(E data, boolean rel) {
    super(StatusConstants.SUCCESS);
    this.data = data;
    this.rel = rel;
  }

  public ObjectRestResponse(StatusConstants statusConstants, E data) {
    super(statusConstants);
    this.data = data;
    if (data != null) {
      rel = true;
    }
  }

  public ObjectRestResponse(E data) {
    super(StatusConstants.SUCCESS);
    this.data = data;
    if (data != null) {
      rel = true;
    }
  }

  /**
   * 默认构造
   */
  public ObjectRestResponse() {
    super(StatusConstants.SUCCESS);
  }


  public E getData() {
    return data;
  }

  public void setData(E date) {
    this.data = date;
    if (date != null) {
      this.rel = true;
    }
  }

  public boolean isRel() {
    return rel;
  }

  public void setRel(boolean rel) {
    this.rel = rel;
  }
}