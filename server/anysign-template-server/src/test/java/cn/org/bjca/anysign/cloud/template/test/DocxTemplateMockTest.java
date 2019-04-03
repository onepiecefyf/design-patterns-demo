package cn.org.bjca.anysign.cloud.template.test;

import cn.org.bjca.anysign.cloud.template.AnySignTemplateApplication;
import cn.org.bjca.anysign.cloud.template.message.TemplateImgMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateReqMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateRespMessage;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/***************************************************************************
 * <pre>单元测试</pre>
 * @文件名称: DocxTemplateMockTest
 * @包路径: cn.org.bjca.anysign.cloud.template.test
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/9 9:27
 ***************************************************************************/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnySignTemplateApplication.class)
@WebAppConfiguration
public class DocxTemplateMockTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  /**
   * base64编码word文档
   */
  private String docxBase64;

  /**
   * base64编码xml文档
   */
  private String xmlBase64;

  /**
   * base64编码json文档
   */
  private String jsonBase64;

  /**
   * base64编码图片文档
   */
  private String imgBase64;

  /**
   * 请求消息
   */
  private TemplateReqMessage reqMessage;

  @Before
  public void setUp() throws Exception {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();  //构造MockMvc
    reqMessage = new TemplateReqMessage();
    reqMessage.setAppId("APP_99A43C05454F4608B40D724521C36767");
    reqMessage.setTransId("12345678910");
    docxBase64 = FileUtils.readFileToString(
        new File(DocxTemplateMockTest.class.getResource("/testData/docxBase64Img.txt").toURI()),
        "UTF-8");
    xmlBase64 = FileUtils.readFileToString(
        new File(DocxTemplateMockTest.class.getResource("/testData/xmlBase64.txt").toURI()),
        "UTF-8");
    jsonBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/jsonBase64.txt").toURI()),
        "UTF-8");
    imgBase64 = FileUtils.readFileToString(
        new File(DocxTemplateTest.class.getResource("/testData/imgBase64.txt").toURI()),
        "UTF-8");
  }

  /**
   * 测试依据XML报文数据填充模板 content与path两种传输模式
   */
  @Test
  public void testTemplateDocGenerationWithXML() {
    /*上传本地文件转换*/
    reqMessage.setFileTransmissionType("CONTENT");
    reqMessage.setRequestFileContent(docxBase64);
    /*根据服务器文件转换*/
        /*reqMessage.setFileTransmissionType("PATH");
        reqMessage.setRequestFileId("314bff2d6e474a81b7a871f90c73b5b6");
        reqMessage.setRequestFileToken("eb386f2766754228984099df0de77a2b");*/
    /* XML格式数据报文*/
    reqMessage.setDataType("XML");
    reqMessage.setDataString(xmlBase64);
    String reqJson = JSON.toJSONString(reqMessage);
    try {
      String responseString = mvc.perform(MockMvcRequestBuilders.post("/template/v1/docGeneration")
          .contentType(MediaType.APPLICATION_JSON)
          .content(reqJson))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print())
          .andReturn().getResponse().getContentAsString();
      JSONObject jsonObject = JSON.parseObject(responseString);
      TemplateRespMessage resp = jsonObject.getObject("data", TemplateRespMessage.class);
      System.out.println("*************交易参考号： " + resp.getTransId());
      System.out.println("*************文件ID ： " + resp.getFileId());
      FileUtils.writeByteArrayToFile(new File("c:/Users/rocky/Desktop/xmlResult.docx"),
          Base64Utils.base64String2ByteFun(resp.getFileContent()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试依据JSON报文数据填充模板  content与path两种传输模式
   */
  @Test
  public void testTemplateDocGenerationWithJSON() {
    /*上传本地文件转换*/
    reqMessage.setFileTransmissionType("CONTENT");
    reqMessage.setRequestFileContent(docxBase64);
    /*根据服务器文件转换*/
        /*reqMessage.setFileTransmissionType("PATH");
        reqMessage.setRequestFileId("314bff2d6e474a81b7a871f90c73b5b6");
        reqMessage.setRequestFileToken("eb386f2766754228984099df0de77a2b");*/
    /* JSON格式数据报文*/
    reqMessage.setDataType("JSON");
    reqMessage.setDataString(jsonBase64);
    String reqJson = JSON.toJSONString(reqMessage);
    try {
      String responseString = mvc.perform(MockMvcRequestBuilders.post("/template/v1/docGeneration")
          .contentType(MediaType.APPLICATION_JSON)
          .content(reqJson))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print())
          .andReturn().getResponse().getContentAsString();
      JSONObject jsonObject = JSON.parseObject(responseString);
      TemplateRespMessage resp = jsonObject.getObject("data", TemplateRespMessage.class);
      System.out.println("*************交易参考号： " + resp.getTransId());
      System.out.println("*************文件ID ： " + resp.getFileId());
      FileUtils.writeByteArrayToFile(new File("c:/Users/rocky/Desktop/jsonResult.docx"),
          Base64Utils.base64String2ByteFun(resp.getFileContent()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试模板插入动态图片
   */
  @Test
  public void testTemplateDocGenerationWithImg() {
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
    templateImgMessage.setDisplayHeight(400f);
    templateImgMessage.setDisplayWidth(400f);
    List<TemplateImgMessage> templateImgMessageList = new ArrayList<>();
    templateImgMessageList.add(templateImgMessage);
    reqMessage.setTemplateImgList(templateImgMessageList);
    // 转换json字符串
    String reqJson = JSON.toJSONString(reqMessage);
    try {
      String responseString = mvc.perform(MockMvcRequestBuilders.post("/template/v1/docGeneration")
          .contentType(MediaType.APPLICATION_JSON)
          .content(reqJson))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print())
          .andReturn().getResponse().getContentAsString();
      JSONObject jsonObject = JSON.parseObject(responseString);
      TemplateRespMessage resp = jsonObject.getObject("data", TemplateRespMessage.class);
      System.out.println("*************交易参考号： " + resp.getTransId());
      System.out.println("*************文件ID ： " + resp.getFileId());
      FileUtils.writeByteArrayToFile(new File("c:/Users/rocky/Desktop/dynamicImgResult.docx"),
          Base64Utils.base64String2ByteFun(resp.getFileContent()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
