package cn.org.bjca.anysign.cloud.document.test;

import cn.org.bjca.anysign.cloud.document.message.DocumentReqMessage;
import cn.org.bjca.anysign.cloud.document.message.DocumentRespMessage;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/***************************************************************************
 * <pre>文档格式转化服务测试</pre>
 * @文件名称: DocumentConversionTest
 * @包路径: cn.org.bjca.anysign.seal.document.test
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/14 11:20
 ***************************************************************************/
public class DocumentConversionTest {

    private static final String _URL = "http://localhost:10502/doc/v1/docTransformation";
//    private static final String _URL = "http://beta.isignet.cn:28081/doc/v1/docTransformation";

    private static final String DEV_ID = "DEV_57AEEEA417B6484FACD2CC29AEA1B938";

    private static final String APP_ID = "APP_493DB260D7DD4BFEA01CAF17FFC99550";

    private static final String TRANS_ID = "123456789";


    /**
     * <p>Word转PDF测试</p>
     */
    @Test
    public void testWord2Pdf() throws IOException, URISyntaxException {
        // base64编码word文档
        String docxBase64 = FileUtils.readFileToString(
                new File(DocumentConversionTest.class.getResource("/base64file/convert/docxBase64.txt").toURI()),
                "UTF-8");
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        reqMessage.setDeviceId(DEV_ID);
        /*上传本地文件转换*/
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        reqMessage.setRequestFileContent(docxBase64);
        /*根据服务器文件转换*/
        /*reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileId("d0ac589ed0bc4460a7f27b7d23264ec7");*/

        reqMessage.setRequestFileType(DocType.WORD.name());
        reqMessage.setResponseFileType(DocType.PDF.name());
        // 调用服务
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(JSON.toJSONString(reqMessage)));
        // 结果
        System.out.println(JSON.toJSON(result));
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        if (reqMessage.getFileTransmissionType().equals(FileTransmissionType.PATH.name())) {
            System.out.println("*************文件ID ： " + resp.getFileId());
        } else {
            System.out.println("*************文件base64长度 ： " + resp.getFileContent().length());
            FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/1.pdf"),
                    Base64Utils.base64String2ByteFun(resp.getFileContent()));
        }
    }

    /**
     * <p>Html转PDF测试</p>
     */
    @Test
    public void testHtml2Pdf() throws IOException, URISyntaxException {
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        /*上传本地文件转换*/
        String htmlBase64 = FileUtils.readFileToString(
                new File(DocumentConversionTest.class.getResource("/base64file/convert/htmlBase64.txt").toURI()),
                "UTF-8");
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        reqMessage.setRequestFileContent(htmlBase64);

        /*根据服务器文件转换*/
        /*reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileId("735f1ac45c18418b8cded37a08508848-9");*/

        reqMessage.setRequestFileType(DocType.HTML.name());
        reqMessage.setResponseFileType(DocType.PDF.name());
        for (int i = 0; i < 1; i++) {
            // 调用服务
            long start = System.currentTimeMillis();
            JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(JSON.toJSONString(reqMessage)));
            // 结果
            DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
            long end = System.currentTimeMillis();
            System.out.println("----------- 耗时： " + (end - start));
            System.out.println("*************交易参考号： " + resp.getTransId());
            if (reqMessage.getFileTransmissionType().equals(FileTransmissionType.PATH.name())) {
                System.out.println("*************文件ID ： " + resp.getFileId());
            } else {
                FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/html2pdf.pdf"),
                        Base64Utils.base64String2ByteFun(resp.getFileContent()));
            }
        }
    }

    /**
     * <p>PDF转图片(JPG\PNG\GIF\TIFF)测试</p>
     */
    @Test
    public void testPdf2Pic() throws IOException, URISyntaxException {
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        reqMessage.setDeviceId(DEV_ID);
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        String pdfBase64 = FileUtils.readFileToString(
                new File(DocumentConversionTest.class.getResource("/base64file/convert/pdfBase64.txt").toURI()),
                "UTF-8");
        reqMessage.setRequestFileContent(pdfBase64);
        /*reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileId("1ec9e66e388942788f68cd299b20ef62");
        reqMessage.setRequestFileToken("54a145f89f36421ca98b1462afe78bb1");*/
        reqMessage.setRequestFileType(DocType.PDF.name());
        reqMessage.setResponseFileType(DocType.PNG.name());// JPG\GIF\PNG\TIFF
        // 设置转换图片范围 ALL 或 PART
        reqMessage.setConvert2PicScopeType("ALL");
        // 当convert2PicScopeType设置为PART时，可设置起始、结束范围；当convert2PicScopeType设置为ALL时，设置无效
        int start = 1, end = 1;
        reqMessage.setConvert2PicStartPageNum(start);
        reqMessage.setConvert2PicEndPageNum(end);
        // 调用服务
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(JSON.toJSONString(reqMessage)));
        // 结果
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        if (reqMessage.getFileTransmissionType().equals(FileTransmissionType.PATH.name())) {
            System.out.println("*************文件ID ： " + resp.getFileId());
        } else {
            System.out.println("*************Base64 文件长度 ： " + resp.getFileContent().length());
            FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/pdf1.png"),
                Base64Utils.base64String2ByteFun(resp.getFileContent()));

        }
    }

    /**
     * <p>DOCX转图片(JPG\PNG\GIF\TIFF)测试</p>
     */
    @Test
    public void testDocx2Pic() throws IOException, URISyntaxException {
        String docxBase64 = FileUtils.readFileToString(
                new File(DocumentConversionTest.class.getResource("/base64file/convert/docxBase64.txt").toURI()),
                "UTF-8");
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        reqMessage.setDeviceId(DEV_ID);
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        reqMessage.setRequestFileContent(docxBase64);
        /*reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileId("832eeb02356e4d3e9c406b4093bd4372");*/
        reqMessage.setRequestFileType(DocType.WORD.name());
        reqMessage.setResponseFileType(DocType.JPG.name());// JPG\GIF\PNG\TIFF
        // 设置转换图片范围 ALL 或 PART
        reqMessage.setConvert2PicScopeType("PART");
        // 当convert2PicScopeType设置为PART时，可设置起始、结束范围；当convert2PicScopeType设置为ALL时，设置无效
        int start = 2, end = 2;
        reqMessage.setConvert2PicStartPageNum(start);
        reqMessage.setConvert2PicEndPageNum(end);
        // 调用服务
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(JSON.toJSONString(reqMessage)));
        // 结果
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        if (reqMessage.getFileTransmissionType().equals(FileTransmissionType.PATH.name())) {
            System.out.println("*************文件ID ： " + resp.getFileId());
        } else {
            System.out.println("*************Base64 文件长度 ： " + resp.getFileContent().length());
            FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/3.jpg"),
                    Base64Utils.base64String2ByteFun(resp.getFileContent()));
        }
    }

    /**
     * <p>图片(JPG\PNG\GIF\TIFF)转PDF测试</p>
     */
    @Test
    public void testPic2Pdf() throws IOException, URISyntaxException {
        String imgBase64 =  FileUtils.readFileToString(
                new File(DocumentConversionTest.class.getResource("/base64file/convert/jpgBase64.txt").toURI()),
                "UTF-8");
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);

        /*上传本地文件转换*/
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        reqMessage.setRequestFileContent(imgBase64);

        /*根据服务器文件转换*/
        /*reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileId("c07aa353a1a347c1a5b23cf1141f16f2");*/

        reqMessage.setRequestFileType(DocType.JPG.name());// JPG\GIF\PNG\TIFF
        reqMessage.setResponseFileType(DocType.PDF.name());
        // 调用服务
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(JSON.toJSONString(reqMessage)));
        // 结果
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        if (reqMessage.getFileTransmissionType().equals(FileTransmissionType.PATH.name())) {
            System.out.println("*************文件ID ： " + resp.getFileId());
        } else {
            System.out.println("*************文件base64长度 ： " + resp.getFileContent().length());
            FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/1.pdf"),
                    Base64Utils.base64String2ByteFun(resp.getFileContent()));
        }
    }
}
