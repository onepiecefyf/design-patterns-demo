package cn.org.bjca.anysign.seal.server.common.message;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;

/***************************************************************************
 * <pre>错误信息返回</pre>
 *
 * @文件名称: ErrorResponse.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.message
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 11:44
 ***************************************************************************/
public class ErrorResponse extends BaseResponse {

  /**
   * 错误信息
   *
   * @param statusConstants 返回状态码
   * @param trace 异常信息
   */
  public ErrorResponse(StatusConstants statusConstants, String trace) {
    super(statusConstants, trace);
  }

  /**
   * 错误信息
   *
   * @param statusConstants 返回状态码
   */
  public ErrorResponse(StatusConstants statusConstants) {
    super(statusConstants);
  }

  /**
   * 错误信息
   *
   * @param status 状态码（无系统编码）
   * @param message 错误信息
   * @param trace 异常信息
   */
  public ErrorResponse(String status, String message, String trace) {
    super(status, message, trace);
  }
}