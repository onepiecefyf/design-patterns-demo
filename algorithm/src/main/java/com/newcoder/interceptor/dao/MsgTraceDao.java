package com.newcoder.interceptor.dao;


import com.newcoder.interceptor.entity.MsgTrace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("dbSqlSessionTemplate")
public interface MsgTraceDao {

  /**
   * 通过transId查询一条消息记录
   * @param traceId
   * @return
   */
  @Select("SELECT * FROM msg_trace WHERE trace_id = #{traceId}")
  MsgTrace getMsgTraceByTraceId(@Param("traceId") String traceId);


}
