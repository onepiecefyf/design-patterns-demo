package com.newcoder.algorithm.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户实体
 * @author yafei.feng
 *
 */
@Data
public class MsgTrace implements Serializable {
  private Integer id;

  private String appId;

  private Integer templateId;

  private String thirdTraceId;

  private String traceId;

  private String param;

  private String sid;

  private Date createTime;

  private Integer sendTimes;

  private String sendResult;

  private String usePlatform;

  private String mobil;

  private String response;

  private String mqSendResult;

  private Integer mqSendTimes;

  private String extension;

  private String queryResult;

  private static final long serialVersionUID = 1L;

  private String updateSql;

}
