package cn.org.bjca.anysign.seal.server.common.handler;


import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.server.common.environment.ServerName;
import cn.org.bjca.anysign.seal.server.common.message.BaseResponse;
import cn.org.bjca.footstone.metrics.client.metrics.Metrics;
import cn.org.bjca.footstone.metrics.client.metrics.QPS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/***************************************************************************
 * <pre>全局异常定义</pre>
 *
 * @author july_whj
 * @文件名称: GlobalDefultExceptionHandler.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 15:50
 ***************************************************************************/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalDefultExceptionHandler {

    @Autowired
    private ServerName serverName;

    /**
     * 处理BaseException异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseRuntimeException.class)
    @ResponseBody
    public BaseResponse baseExceptionHandler(BaseRuntimeException e) {
        log.error("BaseRuntimeException information is captured : status is {} , message is {}", e.getStatus(), e.getMessage(), e);
        QPS qps;
        if (StringUtils.isNotEmpty(e.getMethodName()) && StringUtils.isNotEmpty(e.toString())) {
            if (StringUtils.isNotEmpty(e.getServerName())) {
                qps = Metrics.QPS(e.getServerName(), e.getMethodName(), e.toString());
            } else {
                qps = Metrics.QPS(serverName.getServerName(), e.getMethodName(), e.toString());
            }
            qps.record();
        }
        return new BaseResponse(e.getStatus(), e.getMessage(), e.getClass().getName().concat(" : ").concat(e.getMessage()));
    }

    /**
     * 处理 HttpMessageNotReadableException 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResponse baseHttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        return new BaseResponse(StatusConstants.PARAMETER_ERROR.getStatus(), StatusConstants.PARAMETER_ERROR.getMessage(),
                e.getClass().getName().concat(" : ").concat(e.getMessage()));
    }

    /**
     * 定义Exception请求异常
     *
     * @param e 异常信息
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse defaultExceptionHandler(Exception e) throws IOException {
        log.error("Exception information is captured :  message is {}", e.getMessage(), e);
        return new BaseResponse(StatusConstants.FAIL, e.getClass().getName().concat(" : ").concat(e.getMessage()));
    }


}