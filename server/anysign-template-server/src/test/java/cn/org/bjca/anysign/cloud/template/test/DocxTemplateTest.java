package cn.org.bjca.anysign.cloud.template.test;

import cn.org.bjca.anysign.cloud.template.message.TemplateImgMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateReqMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateRespMessage;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/***************************************************************************
 * <pre>模板服务测试类</pre>
 * @文件名称: DocxTemplateTest
 * @包路径: cn.org.bjca.anysign.template.test.template
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/14 10:12
 ***************************************************************************/
public class DocxTemplateTest {

  /**
   * <p>模板生成测试（XML格式报文）</p>
   */
  @Test
  public void templateDocGenerationByXML() throws IOException, URISyntaxException {
    // base64编码word文档
    String docxBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/docxBase64Img.txt").toURI()),
        "UTF-8");
    TemplateReqMessage reqMessage = new TemplateReqMessage();
//        reqMessage.setAppId("APP_493DB260D7DD4BFEA01CAF17FFC99550");
    reqMessage.setAppId("APP_99A43C05454F4608B40D724521C36767");
    reqMessage.setTransId("12345678910");

    // 上传本地文件转换
    reqMessage.setFileTransmissionType("CONTENT");
    reqMessage.setRequestFileContent(docxBase64);

    // 根据服务器文件转换
        /*reqMessage.setFileTransmissionType("PATH");
        reqMessage.setRequestFileId("97545590cf954015aac8d15434ed0d85");
        reqMessage.setRequestFileToken("c290bc5705f547eaaaa6f7c2428b1cc1");*/

    // XML格式数据报文
    reqMessage.setDataType("XML");
    // base64编码xml文档
    String xmlBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/xmlBase64.txt").toURI()), "UTF-8");
    reqMessage.setDataString(xmlBase64);

        /*String imgBase64 = FileUtils.readFileToString(
                new File(DocxTemplateTest.class.getResource("/testData/imgBase64.txt").toURI()), "UTF-8");
        TemplateImgMessage templateImgMessage = new TemplateImgMessage();
        templateImgMessage.setBase64Img(imgBase64);
        templateImgMessage.setMarkName("img1");

        List<TemplateImgMessage> templateImgMessageList = new ArrayList<>();
        templateImgMessageList.add(templateImgMessage);
        reqMessage.setTemplateImgList(templateImgMessageList);*/

    String reqJson = JSON.toJSONString(reqMessage);
    JSONObject result =
//            HttpUtils.doPost("http://localhost:10503/template/v1/docGeneration", JSON.parseObject(reqJson));
        HttpUtils.doPost("http://beta.isignet.cn:28081/template/v1/docGeneration",
            JSON.parseObject(reqJson));
    TemplateRespMessage resp = result.getObject("data", TemplateRespMessage.class);
    System.out.println("*************交易参考号： " + resp.getTransId());
    System.out.println("*************文件ID ： " + resp.getFileId());
    FileUtils.writeByteArrayToFile(new File("c:/Users/rocky/Desktop/12.docx"),
        Base64Utils.base64String2ByteFun(resp.getFileContent()));
  }

  /**
   * <p>模板生成测试(JSON格式数据报文)</p>
   */
  @Test
  public void templateDocGenerationByJSON() throws IOException, URISyntaxException {
    // base64编码word文档
    String docxBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/docxBase64Img.txt").toURI()),
        "UTF-8");
    TemplateReqMessage reqMessage = new TemplateReqMessage();
    reqMessage.setAppId("APP_99A43C05454F4608B40D724521C36767");
    reqMessage.setTransId("12345678910");
    /*上传本地文件转换*/
    reqMessage.setFileTransmissionType("CONTENT");
    reqMessage.setRequestFileContent(docxBase64);
    /* JSON格式数据报文*/
    reqMessage.setDataType("JSON");
    // base64编码json文档
    String jsonBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/jsonBase64.txt").toURI()),
        "UTF-8");
    reqMessage.setDataString(jsonBase64);
    String reqJson = JSON.toJSONString(reqMessage);
    JSONObject result =
//            HttpUtils.doPost("http://localhost:10503/template/v1/docGeneration", JSON.parseObject(reqJson));
        HttpUtils.doPost("http://beta.isignet.cn:28081/template/v1/docGeneration",
            JSON.parseObject(reqJson));
    TemplateRespMessage resp = result.getObject("data", TemplateRespMessage.class);
    System.out.println("*************交易参考号： " + resp.getTransId());
    System.out.println("*************文件ID ： " + resp.getFileId());
    System.out.println("*************文件base64 ： " + resp.getFileContent());
  }

  @Test
  public void testTemplateDocGenerationWithImg() throws IOException, URISyntaxException {
    String docxBase64 = FileUtils.readFileToString(
        new File(DocxTemplateMockTest.class.getResource("/testData/docxBase64Img.txt").toURI()),
        "UTF-8");
    String jsonBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/jsonBase64.txt").toURI()),
        "UTF-8");
    String imgBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/imgBase64.txt").toURI()),
        "UTF-8");
    TemplateReqMessage reqMessage = new TemplateReqMessage();
    reqMessage.setAppId("APP_99A43C05454F4608B40D724521C36767");
    reqMessage.setTransId("12345678910");
    /*上传本地文件转换*/
    reqMessage.setFileTransmissionType("CONTENT");
    reqMessage.setRequestFileContent(docxBase64);
    /* JSON格式数据报文*/
    reqMessage.setDataType("JSON");
    reqMessage.setDataString(jsonBase64);
    /* 封装图片信息*/
    TemplateImgMessage templateImgMessage = new TemplateImgMessage();
    templateImgMessage.setBase64Img(imgBase64);
    templateImgMessage.setMarkName("img1");
    templateImgMessage.setDisplayHeight(60f);
    templateImgMessage.setDisplayWidth(60f);

    List<TemplateImgMessage> templateImgMessageList = new ArrayList<>();
    templateImgMessageList.add(templateImgMessage);

    reqMessage.setTemplateImgList(templateImgMessageList);
    try {
      // 转换json字符串
      String reqJson = JSON.toJSONString(reqMessage);
      JSONObject result =
//            HttpUtils.doPost("http://localhost:10503/template/v1/docGeneration", JSON.parseObject(reqJson));
          HttpUtils.doPost("http://beta.isignet.cn:28081/template/v1/docGeneration",
              JSON.parseObject(reqJson));
      TemplateRespMessage resp = result.getObject("data", TemplateRespMessage.class);
      System.out.println("*************交易参考号： " + resp.getTransId());
      System.out.println("*************文件ID ： " + resp.getFileId());
      FileUtils.writeByteArrayToFile(new File("c:/Users/rocky/Desktop/dynamicImgResult1.docx"),
          Base64Utils.base64String2ByteFun(resp.getFileContent()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
