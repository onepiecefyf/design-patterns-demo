package cn.org.bjca.anysign.seal.signimage;


import cn.org.bjca.anysign.seal.baes.BaseJunit;
import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.FileUtils;
import cn.org.bjca.anysign.seal.image.bean.*;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: SignImageTest.class
 * @包 路   径：  cn.org.bjca.anysign.seal.signimage
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/12 9:42
 ***************************************************************************/
public class SignImageTest extends BaseJunit {


    @Test
    public void createImage() {
        try {
            String responseString = mockMvc.perform(post("/seal/v1/sealGeneration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(getImageBean())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            JSONObject jsonObject = JSON.parseObject(responseString);
            SignImageMessage signImageMessage = jsonObject.getObject("data", SignImageMessage.class);
            byte[] bytes = Base64Utils.base64String2ByteFun(signImageMessage.getSealContent());
            FileUtils fileUtils = new FileUtils();
            fileUtils.writeFile(bytes, outFilePath.concat("ttt.gif"));
            System.out.println("图片保存路径：".concat(outFilePath.concat("ttt.gif")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createImageTem() {
        try {
            String responseString = mockMvc.perform(post("/seal/v1/sealGenerationTemplate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(getImageBean())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            JSONObject jsonObject = JSON.parseObject(responseString);
            SignImageMessage signImageMessage = jsonObject.getObject("data", SignImageMessage.class);
            byte[] bytes = Base64Utils.base64String2ByteFun(signImageMessage.getSealContent());
            FileUtils fileUtils = new FileUtils();
            fileUtils.writeFile(bytes, outFilePath.concat("ttt2.gif"));
            System.out.println("图片保存路径：".concat(outFilePath.concat("ttt2.gif")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SignImage getImageBean() {
        SignImage imageBean = new SignImage();
        imageBean.setTransId("99999999");
        imageBean.setFileTransmissionType(FileTransmissionType.CONTENT);
        imageBean.setGroup("01");
        imageBean.setUser("01");
        imageBean.setExpire(2000);
        imageBean.setAppId("001");
//        imageBean.setMoulage(getMoulage());
        imageBean.setTemplate(getTemplate());
        return imageBean;
    }

    public static Template getTemplate() {
        Template template = new Template();
        template.setTempId("9999991");
        Map<String, String> map = new HashMap<>(16);
        map.put("2", "模板符文替换测试");
        map.put("1", "北京 北 每 兆 业 有 限 公司");
        template.setRider(map);
        return template;
    }

    public static Moulage getMoulage() {
        Moulage moulage = new Moulage();
        moulage.setFileType(DocType.GIF);
        moulage.setSealshape(MoulageShape.RECTANGLE);
        moulage.setSealWidth(100);
        moulage.setSealHeight(50);
        moulage.setBorderWeight(5);
        moulage.setSealColor("rgb(255, 0, 0)");
        moulage.setFontFamily(MoulageFontFamily.LONGFANGSONG);
        moulage.setRiders(getRiders());
        return moulage;
    }


    public static List<RidersBean> getRiders() {
        List<RidersBean> ridersBeens = new ArrayList<>();
        RidersBean ridersBean = new RidersBean();
        ridersBean.setAppendixContent("张三三");
        ridersBean.setFontSize(25);
        ridersBean.setRadian("M10.0,35.0 90.0,35.0");
        ridersBean.setFontWeight(true);
        ridersBeens.add(ridersBean);
        return ridersBeens;
    }


    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(getImageBean()));
//        String url = "http://127.0.0.1:80/seal/v1/sealGeneration";
//        JSONObject object = HttpUtils.doPost(url, JSON.parseObject(JSON.toJSONString(getImageBean())));
//        System.out.println(object);

    }

}