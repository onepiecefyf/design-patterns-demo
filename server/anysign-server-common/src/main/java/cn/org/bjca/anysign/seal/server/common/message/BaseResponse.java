package cn.org.bjca.anysign.seal.server.common.message;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;

import java.io.Serializable;

/***************************************************************************
 * <pre>响应报文对象</pre>
 *
 * @author july_whj
 * @文件名称: BaseResponse.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 16:21
 ***************************************************************************/
public class BaseResponse  implements Serializable {

    /**
     * 响应码 (*)
     */
    private int status;

    /**
     * 响应信息 (*)
     */
    private String message;

    /**
     * 错误堆栈信息
     */
    private String trace;

    /**
     * 枚举构造
     * @param statusConstants 状态枚举类
     * @param trace 异常寨
     */
    public BaseResponse(StatusConstants statusConstants, String trace) {
        this.status = Integer.parseInt(statusConstants.getStatus());
        this.message = statusConstants.getMessage();
        this.trace = trace;
    }

    /**
     * 枚举构造
     * @param statusConstants
     */
    public BaseResponse(StatusConstants statusConstants) {
        this.status = Integer.parseInt(statusConstants.getStatus());
        this.message = statusConstants.getMessage();
    }

    /**
     * 自定义构造
     *
     * @param status 无系统编码
     * @param message
     * @param trace
     */
    public BaseResponse(String status, String message, String trace) {
        this.status = Integer.parseInt(status);
        this.message = message;
        this.trace = trace;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

}