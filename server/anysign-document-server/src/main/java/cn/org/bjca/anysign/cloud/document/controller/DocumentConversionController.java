package cn.org.bjca.anysign.cloud.document.controller;

import cn.org.bjca.anysign.cloud.document.bean.DocumentFormatBean;
import cn.org.bjca.anysign.cloud.document.enumeration.ImageMergeType;
import cn.org.bjca.anysign.cloud.document.message.DocumentReqMessage;
import cn.org.bjca.anysign.cloud.document.enumeration.Convert2PicScopeType;
import cn.org.bjca.anysign.cloud.document.message.DocumentRespMessage;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Stream;

/***************************************************************************
 * <pre>文档转换服务管理</pre>
 * @文件名称: DocumentConversionController
 * @包路径: cn.org.bjca.anysign.seal.document.controller
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 13:29
 ***************************************************************************/
@RestController
@Api(value = "文档格式化相关接口", consumes = MediaType.APPLICATION_JSON_VALUE, protocols = "HTTP", description = "文档格式化相关接口")
public class DocumentConversionController extends BaseController {

    /**
     * <p>文档格式转换 word2Pdf、html2Pdf、pic2Pdf、pdf2Pic</p>
     * @param reqMessage 请求消息
     * @return 响应消息
     */
    @PostMapping(value = "doc/v1/docTransformation", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "文档格式化", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "根据请求内容对文档进行格式转换")
    public ObjectRestResponse<DocumentRespMessage> docTransformation(@RequestBody DocumentReqMessage reqMessage){
        // 检测请求参数
        checkDocumentReqParams(reqMessage);
        // 响应消息
        DocumentRespMessage respMessage = new DocumentRespMessage();
        respMessage.setTransId(reqMessage.getTransId());
        // 请求文档
        byte[] reqFileBty;
        // 文档传输类型
        String fileTransmissionType = reqMessage.getFileTransmissionType();
        if (FileTransmissionType.PATH.name().equalsIgnoreCase(fileTransmissionType)) {
            String reqFileId = reqMessage.getRequestFileId();
            String reqFileToken = reqMessage.getRequestFileToken();
            reqFileBty = systemRequestService.download(DownloadRequestBean.build(httpConfig.appId, httpConfig.deviceId,
                    httpConfig.version, httpConfig.secret, reqFileId, reqFileToken, reqMessage.getTransId()));
        } else {
            String base64FileContent = reqMessage.getRequestFileContent();
            reqFileBty = Base64Utils.base64String2ByteFun(base64FileContent);
        }
        Assert.notNull(reqFileBty, StatusConstants.DOCUMENT_FILE_NOTNULL, "文档格式化服务", "文档转换");
        // 响应文件
        byte[] respFileBty;
        // 请求文件类型
        String reqFileType = reqMessage.getRequestFileType();
        // 响应文件类型
        String respFileType = reqMessage.getResponseFileType();
        DocumentFormatBean documentFormatBean = new DocumentFormatBean();
        documentFormatBean.setSourceFileBty(reqFileBty);
        // 请求或响应文件是否为图片类型
        boolean isImageFile = judgeImageFileType(reqFileType, respFileType);
        if (isImageFile) { // 图片类型
            String reqConvertScopeType = reqMessage.getConvert2PicScopeType();
            boolean reqScopeFlag = Stream.of(Convert2PicScopeType.values())
                    .anyMatch(convertScopeType -> convertScopeType.name().equalsIgnoreCase(reqConvertScopeType));

            if (DocType.PDF.name().equalsIgnoreCase(respFileType)) { // 图片转PDF
                respFileBty = iDocumentFormatService.img2Pdf(documentFormatBean);
            } else if(DocType.PDF.name().equalsIgnoreCase(reqFileType)){ // PDF转图片
                Assert.state(!checkPdfType(reqFileBty), StatusConstants.DOCUMENT_INCORRECT_PDF_FORMAT, "");
                Assert.state(reqScopeFlag, StatusConstants.DOCUMENT_PIC_CONVERT_SCOPE_ERROR, "");
                documentFormatBean.setStartIndex(reqMessage.getConvert2PicStartPageNum());
                documentFormatBean.setEndIndex(reqMessage.getConvert2PicEndPageNum());
                documentFormatBean.setJudgeScope(reqConvertScopeType.equalsIgnoreCase(Convert2PicScopeType.PART.name()));
                documentFormatBean.setImgType(reqMessage.getResponseFileType());
                documentFormatBean.setImgMergeType(ImageMergeType.VERTICAL.getTypeCode());
                respFileBty = iDocumentFormatService.pdf2Img(documentFormatBean);
            } else if(DocType.WORD.name().equalsIgnoreCase(reqFileType)){ // Word转图片
                Assert.state(reqScopeFlag, StatusConstants.DOCUMENT_PIC_CONVERT_SCOPE_ERROR, "");
                byte[] pdfBty = iDocumentFormatService.word2Pdf(documentFormatBean);
                documentFormatBean.setSourceFileBty(pdfBty);
                documentFormatBean.setStartIndex(reqMessage.getConvert2PicStartPageNum());
                documentFormatBean.setEndIndex(reqMessage.getConvert2PicEndPageNum());
                documentFormatBean.setJudgeScope(reqConvertScopeType.equalsIgnoreCase(Convert2PicScopeType.PART.name()));
                documentFormatBean.setImgType(reqMessage.getResponseFileType());
                documentFormatBean.setImgMergeType(ImageMergeType.VERTICAL.getTypeCode());
                respFileBty = iDocumentFormatService.pdf2Img(documentFormatBean);
            } else { // 其他类型
                logger.error("************* The type of document to be converted is not supported! " +
                        "The reqFileType is {}, and the respFileType is {}.", reqFileType, respFileType);
                throw new BaseRuntimeException(StatusConstants.DOCUMENT_CONVERT_TYPE_ERROR,
                        String.format("The reqFileType is %s, and the respFileType is %s.",
                                reqFileType, respFileType), "文档格式化服务", "文档转换");
            }
        } else {
            if (DocType.WORD.name().equalsIgnoreCase(reqFileType) &&
                    DocType.PDF.name().equalsIgnoreCase(respFileType)) { // WORD转PDF
                respFileBty = iDocumentFormatService.word2Pdf(documentFormatBean);
            } else if (DocType.HTML.name().equalsIgnoreCase(reqFileType) &&
                    DocType.PDF.name().equalsIgnoreCase(respFileType)){ // HTML转PDF
                respFileBty = iDocumentFormatService.html2Pdf(documentFormatBean);
            } else { // 其他类型
                logger.error("************* The type of document to be converted is not supported! " +
                        "The reqFileType is {}, and the respFileType is {}.", reqFileType, respFileType);
                throw new BaseRuntimeException(StatusConstants.DOCUMENT_CONVERT_TYPE_ERROR,
                        String.format("The reqFileType is %s, and the respFileType is %s.",
                                reqFileType, respFileType), "文档格式化服务", "文档转换");
            }
        }
        Assert.notNull(respFileBty, StatusConstants.DOCUMENT_CONVERT_FAIL);
        buildRespInfo(reqMessage, respMessage, respFileBty);
        return new ObjectRestResponse<>(respMessage, true);
    }

    @Override
    void checkDocumentReqParams(DocumentReqMessage documentReqMessage){
        super.checkDocumentReqParams(documentReqMessage);
        if (FileTransmissionType.PATH.name().equalsIgnoreCase(documentReqMessage.getFileTransmissionType())) {
            // 文件传输类型为PATH，则fileId不能为空
            Assert.notNull(documentReqMessage.getRequestFileId(),
                    StatusConstants.DOCUMENT_PARAMETER_ERROR, "The requestFileId is null.",
                    "文档格式化服务", "文档转换");

        } else {
            // 文件传输类型为CONTENT，则fileContent不能为空
            Assert.notNull(documentReqMessage.getRequestFileContent(),
                    StatusConstants.DOCUMENT_PARAMETER_ERROR, "The requestFileContent is null.",
                    "文档格式化服务", "文档转换");
        }
    }

}
