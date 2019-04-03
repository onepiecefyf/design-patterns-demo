package cn.org.bjca.anysign.seal.server.common.controller;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.server.common.message.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/***************************************************************************
 * <pre>404异常处理类</pre>
 *
 * @author july_whj
 * @文件名称: ErrorController.class
 * @包 路   径：  cn.org.bjca.anysign.seal.server.common.exception
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/8 11:12
 ***************************************************************************/
@RestController
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    @RequestMapping(value = "/error")
    public BaseResponse defaultError(HttpServletRequest request, Exception e) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404) {
            return new BaseResponse(StatusConstants.ERROR_404_STATUS, e.getMessage());
        } else {
            return new BaseResponse(StatusConstants.ERROR_5XX_STATUS, e.getMessage());
        }
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }


}