package cn.org.bjca.anysign.seal.fileServer;

import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HMacUtils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.global.tools.utils.SortedMapUtils;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/18 15:39
 ***************************************************************************/
public class FileUtils {

  private static final String url = "http://beta.isignet.cn:10019/upload";

  @Test
  public void upload() {
    String md5 = null;
    try {
      md5 = DigestUtils.md5Hex(new FileInputStream("E:\\test.pdf"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Map<String, String> param = new HashMap<>();
    param.put("version", "1.0");
    param.put("signAlgo", "HmacSHA256");
//        param.put("deviceId", "DEV_E745E51F740E4472AB55D00F3A77163C");//whj
//        param.put("appId", "APP_5439463B5AA7450EA8D784E4AF70D33C");

    param.put("deviceId", "DEV_57AEEEA417B6484FACD2CC29AEA1B938");//whp
    param.put("appId", "APP_493DB260D7DD4BFEA01CAF17FFC99550");

//        param.put("id", "ffa5697b42024f798296ab475af3c080");
    param.put("md5", md5);
    SortedMapUtils sortedMapUtils = new SortedMapUtils(param);
    System.out.println(sortedMapUtils.toString());
    System.out.println(Base64Utils.byte2Base64StringFun(
        HMacUtils.sha256_HMAC(sortedMapUtils.toString(), "Tqn1zejjneKdpqccW2dVW8fpWjRt40CN")));
//        param.put("signature", Base64Utils.byte2Base64StringFun(HMacUtils.sha256_HMAC(sortedMapUtils.toString(), "Tqn1zejjneKdpqccW2dVW8fpWjRt40CN")));//whj
    param.put("signature", Base64Utils.byte2Base64StringFun(
        HMacUtils.sha256_HMAC(sortedMapUtils.toString(), "vbXaD7PQnz1H4zHqG5GFIFBgf23SnXv4")));//whp
    File file = new File("E:\\test.pdf");
    try {
      byte[] bytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
      String str = HttpUtils.post(url, "test.pdf", bytes, param);
      System.out.println(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void download() {
    String url = "http://beta.isignet.cn:10019/download";
    DownloadRequestBean downloadRequestBean = DownloadRequestBean
        .build("APP_5439463B5AA7450EA8D784E4AF70D33C",
            "DEV_E745E51F740E4472AB55D00F3A77163C", "1.0", "Tqn1zejjneKdpqccW2dVW8fpWjRt40CN",
            "8423545cb95b4c718a8e2c16fa40d40a", " ", "");
    Map<String, String> param = downloadRequestBean.toMap("");
    Map<String, String> param2 = new HashMap<>();
    param2.put("appId", "APP_5439463B5AA7450EA8D784E4AF70D33C");
    param2.put("version", "1.0");
    param2.put("signAlgo", "HmacSHA256");
    param2.put("deviceId", "DEV_E745E51F740E4472AB55D00F3A77163C");
    param2.put("id", "b4d80f98dff5406ba2bddebbebb05685");
    SortedMapUtils sortedMapUtils = new SortedMapUtils(param2);
    param2.put("signature",
        Base64Utils.byte2Base64StringFun(HMacUtils.sha256_HMAC(sortedMapUtils.toString(),
            "Tqn1zejjneKdpqccW2dVW8fpWjRt40CN")));
    byte[] str = HttpUtils.doGetByte(url, param2);
    try {
      org.apache.commons.io.FileUtils.writeByteArrayToFile(new File("E:\\test\\cccc.pdf"), str);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(str.length);
  }

  @Test
  public void access() {
    //image
    //fileId = 8086b4cf345f43d9b0aef492503e8858
    //token = c042598154b949bfa84b212b96eb907f

    //pdf
    // fileId = 7ad50127fb4f47a3bc1eb49580c3778d
    // token = 3efd5480f01e4ca09821f8e7273a07cb
    String url = "http://beta.isignet.cn:10019/files/access";
    Map<String, String> param = new HashMap<>();
    param.put("version", "1.0");
    param.put("signAlgo", "HmacSHA256");
    param.put("deviceId", "DEV_57AEEEA417B6484FACD2CC29AEA1B938");
    param.put("appId", "APP_493DB260D7DD4BFEA01CAF17FFC99550");
    param.put("id", "7ad50127fb4f47a3bc1eb49580c3778d");
    param.put("expire", "14400");
    SortedMapUtils sortedMapUtils = new SortedMapUtils(param);
    param.put("signature", Base64Utils.byte2Base64StringFun(
        HMacUtils.sha256_HMAC(sortedMapUtils.toString(), "vbXaD7PQnz1H4zHqG5GFIFBgf23SnXv4")));
    JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(param));
    System.out.println(jsonObject.toString());
    JSONObject jsonObject2 = HttpUtils.doPost(url, jsonObject);
    System.out.println(jsonObject2.toString());
  }

}