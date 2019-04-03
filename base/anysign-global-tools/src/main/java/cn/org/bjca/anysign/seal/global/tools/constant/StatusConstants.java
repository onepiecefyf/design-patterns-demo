package cn.org.bjca.anysign.seal.global.tools.constant;

/***************************************************************************
 * <pre>状态码</pre>
 *
 * @author july_whj
 * @文件名称: StatusConstants.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.constant
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述: 状态码8位，前三位为系统编码，枚举中定义五位状态码
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 10:08
 ***************************************************************************/
public enum StatusConstants {
  /*******************************************
   * 定义系统编码
   **************************************************/

  SIGN_SYSTEM_CODE("", "501", "电子签章服务编码前缀"),
  DOCUMENT_SYSTEM_CODE("", "502", "文档格式化服务编码前缀"),
  TEMPLATE_SYSTEM_CODE("", "503", "PDF模板服务编码前缀"),

  /******************************************电子签章服务编码前缀****************************************/
  /**
   * 成功
   */
  SUCCESS("", "200", "success"),
  /**
   * 失败
   */
  FAIL(SIGN_SYSTEM_CODE.getStatus(), "50000", "服务异常，请联系系统管理员。"),
  /**
   * 参数错误
   */
  PARAMETER_ERROR(SIGN_SYSTEM_CODE.getStatus(), "00300", "参数错误。"),
  /**
   * version 校验
   */
  VERSION(SIGN_SYSTEM_CODE.getStatus(), "50000", "请求版本号(version)不能为空。"),
  /**
   * 设备ID 校验
   */
  DEVICE_ID(SIGN_SYSTEM_CODE.getStatus(), "50000", "设备ID(deviceId)不能为空。"),
  /**
   * appId 校验
   */
  APP_ID(SIGN_SYSTEM_CODE.getStatus(), "50000", "应用ID(appId)不能为空。"),

  RESULTS_STATUS(SIGN_SYSTEM_CODE.getStatus(), "50000", "返回状态码不能为空。"),

  RESULTS_MESSAGE(SIGN_SYSTEM_CODE.getStatus(), "50000", "返回消息不能为空。"),

  ERROR_404_STATUS(SIGN_SYSTEM_CODE.getStatus(), "404", "您访问的地址不存在，请检测url信息。"),
  ERROR_5XX_STATUS(SIGN_SYSTEM_CODE.getStatus(), "500", "请求服务器异常。"),
  IMAGE_PARAMETER_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10001", "请求参数不能空，请检查参数信息。"),
  TRANSID_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00301", "transId 不能为空。"),
  FILETRANSMISSIONTYPE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00302", "印章文件传输类型不能为空。"),
  FILETYPE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00303", "文件类型不能为空。"),
  SEALSHAPE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00304", "印章形状不能为空。"),
  SEALHEIGHT_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00305", "印章高度不能为空。"),
  SEALWIDTH_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00306", "印章宽度不能为空。"),
  BORDERWEIGHT_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00307", "印章边框厚度不能为空。"),
  SEALCOLOR_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00308", "印章颜色不能为空。"),
  FONTFAMILY_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00309", "印章字体不能为空。"),
  APPENDIXCONTENT_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00310", "附文信息不能为空。"),
  RADIAN_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00311", "弧度不能为空。"),
  FONTWEIGHT_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00312", "加粗不能为空。"),
  FONTSIZE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00313", "字体大小不能为空。"),
  RIDERS_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "00314", "签章附文列表不能为空。"),
  PERSISTENCE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10315", "模板数据持久化失败，请联系系统管理员。"),
  TEMPLATEID_NULL(SIGN_SYSTEM_CODE.getStatus(), "10316", "模板ID不存在，请联系系统管理员。"),
  TEMPLATEID_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10317", "模板ID不能为空。"),
  TEMPLATEID_REPEAT(SIGN_SYSTEM_CODE.getStatus(), "10318", "模板ID已存在，请重新输入。"),
  FILETRANSMISSIONTYPE_FAIL(SIGN_SYSTEM_CODE.getStatus(), "00319", "文件传输类型错误，请检查输入信息。"),
  SEALSHAPE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "00320", "印章形状不正确，请检查。"),

  IMAGE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10500", "生成图片失败，请联系系统管理员。"),
  FILE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10501", "文件格式错误"),
  CERT_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10502", "证书文件错误"),
  ALG_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10503", "算法错误"),
  SIGNATURE_VERIFY_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10504", "签名验证不通过"),
  FILE_TOKEN_NOTNUll(SIGN_SYSTEM_CODE.getStatus(), "10505", "文件token不允许为空"),
  FILE_ID_NOTNUll(SIGN_SYSTEM_CODE.getStatus(), "10506", "文件id不允许为空"),
  FILE_CONTENT_NOTNUll(SIGN_SYSTEM_CODE.getStatus(), "10506", "文件内容不允许为空"),
  KEYWORD_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10507", "未找到关键字"),
  WATERMARK_DEAL_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10508", "添加水印错误"),
  PROTECTED_DEAL_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10509", "文档保护处理错误"),

  PDF_SIGNATURE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10512", "调用签名服务签名失败"),
  PDF_SIGNATURE_VERIFY_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10513", "调用签名服务验证签名失败"),
  TSS_SIGNATURE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10514", "调用时间戳服务签名失败"),
  TSS_SIGNATURE_PARSE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10515", "调用时间戳服务解析签名失败"),
  TSS_SIGNATURE_VERIFY_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10516", "调用时间戳服务验证签名失败"),
  DIGEST_DECRYPTION_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10517", "解析证书失败。"),
  FILE_DOWNLOAD_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10518", "文件下载失败。"),
  FILE_UPLOAD_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10519", "文件上传失败。"),

  SIGNCERT_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10520", "签名证书不能为空。"),
  PROTECTEDMODE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10521", "文档保护模式不能为空。"),
  SEALPOSITION_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10522", "签章定位规则。"),
  POSITIONTYPE_NOTNULL(SIGN_SYSTEM_CODE.getStatus(), "10523", "定位规则不能为空。"),
  POSITIONTYPE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10524", "定位规则类型错误。"),
  KWRULE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10525", "关键字定位规则错误。"),
  KW_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10526", "关键字不能为空。"),
  XYZRULEINFO_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10527", "定位规则类型错误。"),
  ACROSSPAGE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10528", "定位规则类型错误。"),
  ADDIMAGETOPDF_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10529", "PDF插入图片错误。"),
  GETPDFSIGN_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10530", "获取PDF签名对象异常。"),
  GENSIGNHASH_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10531", "PDF HASH异常。"),
  PDF_SIGN_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10532", "PDF签名异常。"),
  PDF_SIGNDATA_ISNULL(SIGN_SYSTEM_CODE.getStatus(), "10533", "PDF产生签名数据为空。"),
  PDF_READER_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10534", "reader 读取PDF失败，请检查PDF文件。"),


  ACROSS_CUTIMAGE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10535", "骑缝章切分图片失败。"),
  ACROSS_AVERAGE_CUTIMAGE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10536", "骑缝章均分图片失败。"),
  INSTANCEIMAGE_ERROR(SIGN_SYSTEM_CODE.getStatus(), "10537", "骑缝章，实例化图片失败。"),

  /********************************
   * 文档服务状态码（1001~2000）
   ***********************************/
  DOCUMENT_PARAMETER_ERROR(DOCUMENT_SYSTEM_CODE.getStatus(), "01300", "参数错误"),
  DOCUMENT_FILE_NOTNULL(DOCUMENT_SYSTEM_CODE.getStatus(), "01401", "文件不能为空。"),
  DOCUMENT_CONVERT_FAIL(DOCUMENT_SYSTEM_CODE.getStatus(), "01402", "文件转换错误。"),
  DOCUMENT_CONVERT_TYPE_ERROR(DOCUMENT_SYSTEM_CODE.getStatus(), "01403", "文件转换类型暂不支持。"),
  DOCUMENT_PDFMERGE_FAIL(DOCUMENT_SYSTEM_CODE.getStatus(), "01405", "PDF文件合并失败。"),
  DOCUMENT_INCORRECT_PDF_FORMAT(DOCUMENT_SYSTEM_CODE.getStatus(), "01406", "PDF文件格式不正确。"),
  DOCUMENT_PIC_CONVERT_SCOPE_ERROR(DOCUMENT_SYSTEM_CODE.getStatus(), "01407", "转换图片范围不确定。"),
  /********************************
   * 模板服务状态码（2001~3000）
   ***********************************/
  TEMPLATE_PARAMETER_ERROR(TEMPLATE_SYSTEM_CODE.getStatus(), "02300", "参数错误"),
  TEMPLATE_FILE_NOTNULL(TEMPLATE_SYSTEM_CODE.getStatus(), "02301", "模板文件不能为空。"),
  TEMPLATE_XML_FILLERROR(TEMPLATE_SYSTEM_CODE.getStatus(), "02302", "填充模板文件错误。");

  StatusConstants(String systemCode, String status, String message) {
    this.status = statusFilter(systemCode, status);
    this.message = message;
  }

  /**
   * 状态码拼接过滤 成功返回 200 不做系统编码拼接 失败返回 11111 不做系统编码拼接
   */
  private String statusFilter(String systemCode, String status) {
    return systemCode.concat(status);
  }

  private String status;
  private String message;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}