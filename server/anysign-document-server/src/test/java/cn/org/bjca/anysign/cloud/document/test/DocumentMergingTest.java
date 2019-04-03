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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/***************************************************************************
 * <pre>文档合并功能测试</pre>
 * @文件名称: DocumentMergingTest
 * @包路径: cn.org.bjca.anysign.seal.document.test
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/14 10:42
 ***************************************************************************/
public class DocumentMergingTest {

    private static final String _URL =
//            "http://localhost:10502/doc/v1/docMerging";
            "http://beta.isignet.cn:28081/doc/v1/docMerging";

    private static final String APP_ID = "APP_99A43C05454F4608B40D724521C36767";

    private static final String TRANS_ID = "DEV_049796F49CAC4B3FB8C7B72758B72E8F";

    /**
     * <p>多PDF合并测试</p>
     *
     * @throws IOException 异常
     */
    @Test
    public void testMultiPdfMerge() throws IOException, URISyntaxException {
        // PDF 文档列表
        List<String> pdfList = new LinkedList<>();
        String pdf1Base64 = FileUtils.readFileToString(
                new File(DocumentMergingTest.class.getResource("/base64file/merge/pdfMerge1Base64.txt").toURI()),
                "UTF-8");
        pdfList.add(pdf1Base64);
        String pdf2Base64 = FileUtils.readFileToString(
                new File(DocumentMergingTest.class.getResource("/base64file/merge/pdfMerge2Base64.txt").toURI()),
                "UTF-8");
        pdfList.add(pdf2Base64);
        String pdf3Base64 = FileUtils.readFileToString(
                new File(DocumentMergingTest.class.getResource("/base64file/merge/pdfMerge3Base64.txt").toURI()),
                "UTF-8");
        pdfList.add(pdf3Base64);
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        reqMessage.setFileTransmissionType(FileTransmissionType.CONTENT.name());
        reqMessage.setRequestFileType(DocType.PDF.name());
        reqMessage.setResponseFileType(DocType.PDF.name());
        reqMessage.setRequestFileContentList(pdfList);
        // 调用服务
        String req = JSON.toJSONString(reqMessage);
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(req));
        // 结果
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        System.out.println("*************文件ID ： " + resp.getFileId());
        FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/merge.pdf"),
                Base64Utils.base64String2ByteFun(resp.getFileContent()));
    }

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("be74984191474727969f0225cf35a63d");
        list.add("be74984191474727969f0225cf35a63d");
        String str = JSON.toJSONString(list);

    }

    /**
     * <p>多PDF合并测试</p>
     *
     * @throws IOException 异常
     */
    @Test
    public void testMultiPdfMergeById() {
        // PDF文档Id列表
        List<String> pdfIdList = new LinkedList<>();
        String pdfId1 = "61794ad3f58f4b8fae74458cc08c8665";
        pdfIdList.add(pdfId1);
        String pdfId2 = "151b508168d3480ea4064bb030de105a";
        pdfIdList.add(pdfId2);

        List<String> pdfTokenList = new LinkedList<>();
        String pdfToken1 = "84eb0c105fdf40b7a5a0c5d1ac38551e";
        pdfTokenList.add(pdfToken1);
        String pdfToken2 = "9feedc5cf2a04c909ad407af0457b359";
        pdfTokenList.add(pdfToken2);
        // 请求信息
        DocumentReqMessage reqMessage = new DocumentReqMessage();
        reqMessage.setAppId(APP_ID);
        reqMessage.setTransId(TRANS_ID);
        reqMessage.setFileTransmissionType(FileTransmissionType.PATH.name());
        reqMessage.setRequestFileType(DocType.PDF.name());
        reqMessage.setResponseFileType(DocType.PDF.name());
        reqMessage.setRequestFileIdList(pdfIdList);
        reqMessage.setRequestFileTokenList(pdfTokenList);
        // 调用服务
        String req = JSON.toJSONString(reqMessage);
        JSONObject result = HttpUtils.doPost(_URL, JSON.parseObject(req));
        // 结果
        DocumentRespMessage resp = result.getObject("data", DocumentRespMessage.class);
        System.out.println("*************交易参考号： " + resp.getTransId());
        System.out.println("*************文件ID ： " + resp.getFileId());
        /*FileUtils.writeByteArrayToFile(new File("C:/Users/rocky/Desktop/merge.pdf"),
                Base64Utils.base64String2ByteFun(resp.getFileContent()));*/
    }

}
