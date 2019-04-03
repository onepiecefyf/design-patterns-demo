package cn.org.bjca.anysign.seal.global.tools.exception;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import lombok.Data;

/***************************************************************************
 * <pre>自定义异常类</pre>
 *
 * @author july_whj
 * @文件名称: BaseRuntimeException.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.exception
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 10:29
 ***************************************************************************/
@Data
public class BaseRuntimeException extends RuntimeException {

    /**
     * 状态码
     */
    private String status = "200";
    /**
     * 返回消息
     */
    private static final String MESSAGE = "%s[%s]";
    /**
     * 返回错误详细信息
     */
    private String msg = null;
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 方法名称
     */
    private String methodName;


    /**
     * 状态枚举构造
     *
     * @param statusConstants 枚举状态码
     */
    public BaseRuntimeException(StatusConstants statusConstants) {
        super(statusConstants.getMessage());
        this.status = statusConstants.getStatus();
        this.msg = this.status.concat(":").concat(statusConstants.getMessage());
    }

    /**
     * 状态枚举构造
     *
     * @param statusConstants 枚举状态码
     * @param serverName      服务名称
     * @param methodName      方法名称
     */
    public BaseRuntimeException(StatusConstants statusConstants, String serverName, String methodName) {
        super(statusConstants.getMessage());
        this.status = statusConstants.getStatus();
        this.serverName = serverName;
        this.methodName = methodName;
        this.msg = this.status.concat(":").concat(statusConstants.getMessage());
    }

    /**
     * 状态枚举构造+外传错误信息
     *
     * @param statusConstants 枚举类
     * @param message         错误信息
     */
    public BaseRuntimeException(StatusConstants statusConstants, String message) {
        super(String.format(MESSAGE, statusConstants.getMessage(), message));
        this.status = statusConstants.getStatus();
        this.msg = this.status.concat(":").concat(String.format(MESSAGE, statusConstants.getMessage(), message));
    }

    /**
     * 状态枚举构造+外传错误信息 +服务名称+ 方法名称
     *
     * @param statusConstants 状态枚举类
     * @param message         错误信息
     * @param serverName      服务名称
     * @param methodName      方法名称
     */
    public BaseRuntimeException(StatusConstants statusConstants, String message, String serverName, String methodName) {
        super(String.format(MESSAGE, statusConstants.getMessage(), message));
        this.status = statusConstants.getStatus();
        this.serverName = serverName;
        this.methodName = methodName;
        this.msg = this.status.concat(":").concat(String.format(MESSAGE, statusConstants.getMessage(), message));
    }


    /**
     * 自定义状态构造
     *
     * @param message 错误详细
     * @param status  无系统状态码
     */
    public BaseRuntimeException(String status, String message) {
        super(message);
        this.status = status;
        this.msg = this.status.concat(":").concat(message);
    }

    /**
     * 自定义状态构造 + 服务名称+ 方法名称
     *
     * @param status     状态码
     * @param message    错误详细
     * @param serverName 服务名称
     * @param methodName 方法名称
     */
    public BaseRuntimeException(String status, String message, String serverName, String methodName) {
        super(message);
        this.status = status;
        this.serverName = serverName;
        this.methodName = methodName;
        this.msg = this.status.concat(":").concat(message);
    }

    /**
     * 消息构造
     *
     * @param message 错误详细
     */
    public BaseRuntimeException(String message) {
        super(message);
        this.msg = this.status.concat(":").concat(message);
    }

    /**
     * 消息构造
     *
     * @param message    错误信息
     * @param serverName 服务名称
     * @param methodName 方法名称
     */
    public BaseRuntimeException(String message, String serverName, String methodName) {
        super(message);
        this.serverName = serverName;
        this.methodName = methodName;
        this.msg = this.status.concat(":").concat(message);
    }


    /**
     * 自定义异常
     *
     * @param message 错误详细
     * @param cause   异常
     */
    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.msg = this.status.concat(":").concat(message);
    }

    /**
     * 自定义异常
     *
     * @param cause 异常
     */
    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 自定义异常
     *
     * @param cause 异常
     */
    public BaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause);
    }


    @Override
    public String toString() {
        return this.msg;
    }
}