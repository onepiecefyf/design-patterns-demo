package cn.org.bjca.anysign.seal.web.controller;

import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.moulage.convert.ITemplateService;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.server.common.message.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <pre>印模模板服务</pre>
 *
 * @author july_whj
 * @文件名称: TemplateController.class
 * @包 路   径：  cn.org.bjca.anysign.seal.web.controller
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/8 13:50
 ***************************************************************************/
@Api(value = "印模模板相关接口", consumes = MediaType.APPLICATION_JSON_VALUE, protocols = "HTTP", description = "印模模板相关接口")
@RestController
@RequestMapping("template/v1")
@Slf4j
public class TemplateController {

    @Autowired
    private ITemplateService templateService;

    @ApiOperation(value = "刷新模板信息", produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET", notes = "刷新模板信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "string", paramType = "query")})
    @RequestMapping("/refresh")
    public BaseResponse templateRefresh() {
        templateService.templateRefresh();
        log.info("templateRefresh successful ...");
        return new BaseResponse(StatusConstants.SUCCESS);
    }

    @RequestMapping(value = "/templatePersistence", consumes = "application/json;charset=utf-8")
    @ApiOperation(value = "持久化模板数据", produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "POST", notes = "持久化模板数据")
    public BaseResponse templatePersistence(@RequestBody TemplateBean templateBean) {
        templateService.templatePersistence(templateBean);
        log.info("templatePersistence successful ...");
        return new BaseResponse(StatusConstants.SUCCESS);
    }

    @ApiOperation(value = "根据模板ID删除模板信息", produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "GET", notes = "根据模板ID删除模板信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "temId", value = "temId", dataType = "string", paramType = "query")})
    @RequestMapping("/delByTemId")
    public BaseResponse delTemplateById(String temId) {
        templateService.delTemplateById(temId);
        log.info("delTemplateById successful ...");
        return new BaseResponse(StatusConstants.SUCCESS);
    }

    @RequestMapping(value = "/updateTemplateById", consumes = "application/json;charset=utf-8")
    @ApiOperation(value = "更新模板数据", produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "POST", notes = "更新模板数据")
    public BaseResponse updateTemplateById(@RequestBody TemplateBean templateBean) {
        templateService.updateTemplateById(templateBean);
        log.info("updateTemplateById successful ...");
        return new BaseResponse(StatusConstants.SUCCESS);
    }
}