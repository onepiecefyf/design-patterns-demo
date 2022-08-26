package com.newcoder.interceptor.service;

import com.newcoder.interceptor.dao.MsgTraceDao;
import com.newcoder.interceptor.entity.MsgTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短信服务
 * @author yafei.feng
 */
@Slf4j
@Service
public class MsgTraceService {

  @Autowired(required = false)
  private MsgTraceDao msgTraceDao;

  /**
   * 查询短信信息
   * @param traceId
   * @return
   */
  public MsgTrace getMsgTraceByTraceId(String traceId) {
    return msgTraceDao.getMsgTraceByTraceId(traceId);
  }

}
