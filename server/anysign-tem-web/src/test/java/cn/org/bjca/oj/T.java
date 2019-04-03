package cn.org.bjca.oj;

import cn.org.bjca.anysign.seal.moulage.bean.RiderBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.oj
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/12 16:04
 ***************************************************************************/
public class T {

  public static void main(String[] args) {
    for (int i = 0; i < 1; i++) {
      TemplateBean templateBean = new TemplateBean();
      templateBean.setTempId("999999".concat(i + ""));
      templateBean.setWidth(945);
      templateBean.setHeight(709);
      templateBean.setCenterX(473);
      templateBean.setCenterY(354);
      templateBean.setRx(460);
      templateBean.setRy(343);
      templateBean.setColor("rgb(255,0,0)");
      templateBean.setStroke(24);
      templateBean.setShapeEnum(ShapeEnum.ellipse);
      List<RiderBean> riderBeanList = new ArrayList<>();
      RiderBean riderBeanUP = new RiderBean();
      riderBeanUP.setAppendixContent("北京数字 认 证 股 份 有限公司");
      riderBeanUP.setFontSize(85);
      riderBeanUP.setFontWeight("bold");
      riderBeanUP.setFontFamily("公章刻章字体");
      riderBeanUP.setColor("rgb(255,0,0)");
      riderBeanUP.setRadian("M170,470 A355,225 0 1,1,775,470");
      riderBeanUP.setOrder("1");
      riderBeanList.add(riderBeanUP);
      RiderBean riderBeanDown = new RiderBean();
      riderBeanDown.setAppendixContent("91110108722619411AX");
      riderBeanDown.setFontSize(70);
      riderBeanDown.setFontWeight("bold");
      riderBeanDown.setFontFamily("arial");
      riderBeanDown.setColor("rgb(255,0,0)");
      riderBeanDown.setRadian("M155,375 L810,375");
      riderBeanDown.setOrder("2");
      riderBeanList.add(riderBeanDown);
      RiderBean riderBean = new RiderBean();
      riderBean.setAppendixContent("发票专用章");
      riderBean.setFontSize(110);
      riderBean.setFontWeight("bold");
      riderBean.setFontFamily("公章刻章字体");
      riderBean.setColor("rgb(255,0,0)");
      riderBean.setRadian("M165,550 L800,550");
      riderBean.setOrder("3");
      riderBeanList.add(riderBean);

      templateBean.setRiderBeans(riderBeanList);

      String json = JSON.toJSONString(templateBean);

      System.out.println(json);
//            te.templatePersistence(templateBean);
    }
//        te.templateRefresh();
//        Template t = new Template();
//        t.setTempId("9999991");
//        Map<String, String> map = new HashMap<>(16);
//        map.put("2", "模板符文替换测试");
//        t.setRider(map);
//        TemplateBean temp = te.readTemplateById(t);
//        ICreateImage createImage = new TemplateImpression();
//        byte[] bytes = createImage.creatrImage(temp);
//        try {
//            FileUtils.writeByteArrayToFile(new File("E:\\test\\tem.png"), bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
  }

}