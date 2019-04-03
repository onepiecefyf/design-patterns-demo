package cn.org.bjca.anysign.cloud.document.controller;

import cn.org.bjca.anysign.cloud.document.message.DocumentReqMessage;
import cn.org.bjca.anysign.cloud.document.message.DocumentRespMessage;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/***************************************************************************
 * <pre>文档合并服务管理</pre>
 * @文件名称: DocumentMergeController
 * @包路径: cn.org.bjca.anysign.seal.document.controller
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 13:34
 ***************************************************************************/
@RestController
@Api(value = "文件合并相关接口", consumes = MediaType.APPLICATION_JSON_VALUE, protocols = "HTTP", description = "文件合并相关接口")
public class DocumentMergeController extends BaseController {

    /**
     * <p>文档合并</p>
     * @param reqMessage 请求消息
     * @return 响应消息
     */
    @PostMapping(value = "doc/v1/docMerging", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "文档合并", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "不同PDF文档合并为一个PDF文件")
    public ObjectRestResponse<DocumentRespMessage> docMerging(@RequestBody DocumentReqMessage reqMessage) {
        // 检测请求参数
        checkDocumentReqParams(reqMessage);
        // 响应消息
        DocumentRespMessage respMessage = new DocumentRespMessage();
        respMessage.setTransId(reqMessage.getTransId());

        // 文档传输类型
        String fileTransmissionType = reqMessage.getFileTransmissionType();
        // pdfBty 列表
        List<byte[]> pdfBtyList;
        if (FileTransmissionType.PATH.name().equalsIgnoreCase(fileTransmissionType)) {
            List<String> fileIdList = reqMessage.getRequestFileIdList();
            List<String> fileTokenList = reqMessage.getRequestFileTokenList();
            if (fileTokenList == null || fileTokenList.isEmpty()) {
                fileTokenList = new ArrayList<>(fileIdList.size());
            }
            // 转换final类型
            List<String> finalFileTokenList = fileTokenList;
            // 合并idList与tokenList， 转换为一维数组（String[]{id，token}）列表
            List<String[]> idAndTokenList = fileIdList.parallelStream()
                    .flatMap(id -> finalFileTokenList.parallelStream()
                            .filter(token -> finalFileTokenList.indexOf(token) == fileIdList.indexOf(id))
                            .map(token -> new String[]{id, token}))
                    .collect(toList());
            // 根据id,token 组合list查找所有pdf文件
            pdfBtyList = idAndTokenList.parallelStream()
                    .map(array -> systemRequestService.download(DownloadRequestBean.build(httpConfig.appId, httpConfig.deviceId,
                            httpConfig.version, httpConfig.secret, array[0], array[1], reqMessage.getTransId())))
                    .collect(toList());
        } else {
            List<String> pdfContentList = reqMessage.getRequestFileContentList();
            pdfBtyList = pdfContentList.stream()
                    .map(Base64Utils::base64String2ByteFun) // 解码base64字符串
                    .collect(toList());// 得到list
        }

        Assert.state(pdfBtyList.parallelStream().noneMatch(this::checkPdfType), StatusConstants.DOCUMENT_INCORRECT_PDF_FORMAT,
                "The documents to be merged is not the correct pdf format!", "文档格式化服务", "文档合并");
        Assert.notNull(pdfBtyList, StatusConstants.DOCUMENT_FILE_NOTNULL, "文档格式化服务", "文档合并");
        // 响应PDF文件
        byte[] resultPdfBty = iDocumentFormatService.mergePdf(pdfBtyList);
        Assert.notNull(resultPdfBty, StatusConstants.DOCUMENT_PDFMERGE_FAIL,"文档格式化服务", "文档合并");

        buildRespInfo(reqMessage, respMessage, resultPdfBty);
        return new ObjectRestResponse<>(respMessage, true);
    }

    /**
     * <p>检测请求参数</p>
     * @param documentReqMessage 请求消息
     */
    @Override
    void checkDocumentReqParams(DocumentReqMessage documentReqMessage) {
        super.checkDocumentReqParams(documentReqMessage);
        if (FileTransmissionType.PATH.name().equalsIgnoreCase(documentReqMessage.getFileTransmissionType())) {
            // 文件传输类型为PATH，则fileId不能为空
            Assert.notNull(documentReqMessage.getRequestFileIdList(),
                    StatusConstants.DOCUMENT_PARAMETER_ERROR, "", "文档格式化服务", "文档合并");
        } else {
            // 文件传输类型为CONTENT，则fileContent不能为空
            Assert.notNull(documentReqMessage.getRequestFileContentList(),
                    StatusConstants.DOCUMENT_PARAMETER_ERROR, "", "文档格式化服务", "文档合并");
        }
    }

}
