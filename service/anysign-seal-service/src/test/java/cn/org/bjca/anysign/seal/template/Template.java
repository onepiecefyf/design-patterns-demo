package cn.org.bjca.anysign.seal.template;

import cn.org.bjca.anysign.seal.global.tools.utils.FileUtils;
import cn.org.bjca.anysign.seal.moulage.ICreateImage;
import cn.org.bjca.anysign.seal.moulage.bean.RiderBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.moulage.convert.FileTemplateService;
import cn.org.bjca.anysign.seal.moulage.template.impl.CircleImpression;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.template
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/8 15:17
 ***************************************************************************/
public class Template {

  @Autowired
  private ICreateImage createImage;

  @Test
  public void getEcData() {
    TemplateBean templateBean = new TemplateBean();
    templateBean.setTempId("9999991");
    templateBean.setWidth(945);
    templateBean.setHeight(709);
    templateBean.setCenterX(473);
    templateBean.setCenterY(354);
    templateBean.setRx(460);
    templateBean.setRy(343);
    templateBean.setColor("rgb(255,0,0)");
    templateBean.setStroke(24);
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
    templateBean.setShapeEnum(ShapeEnum.ellipse);
    String json = JSON.toJSONString(templateBean);
    System.out.println(json);
  }

  @Test
  public void getRectangData() {
    TemplateBean templateBean = new TemplateBean();
    templateBean.setTempId("9999992");
    templateBean.setWidth(472);
    templateBean.setHeight(236);
    templateBean.setStroke(24);
    templateBean.setColor("rgb(255,0,0)");
    List<RiderBean> riderBeanList = new ArrayList<>();
    RiderBean riderBean = new RiderBean();
    riderBean.setAppendixContent("张三三");
    riderBean.setFontSize(135);
    riderBean.setFontWeight("bold");
    riderBean.setFontFamily("公章刻章字体");
    riderBean.setColor("rgb(255,0,0)");
    riderBean.setRadian("M35,170 L435,170");
    riderBean.setOrder("1");
    riderBeanList.add(riderBean);
    templateBean.setRiderBeans(riderBeanList);
    templateBean.setShapeEnum(ShapeEnum.rectangle);
    String json = JSON.toJSONString(templateBean);
    System.out.println(json);
    byte[] bytes = createImage.creatrImage(templateBean);
    FileUtils fileUtils = new FileUtils();
    fileUtils.writeFile(bytes, "E:\\test\\name.png");
  }

  /**
   * 圆形无五角星
   */
  public void getYDataNOLL() {
    TemplateBean templateBean = new TemplateBean();
    templateBean.setTempId("9999991");
    templateBean.setWidth(945);
    templateBean.setHeight(945);
    templateBean.setCenterX(473);
    templateBean.setCenterY(473);
    templateBean.setRx(460);
    templateBean.setColor("rgb(255,0,0)");
    templateBean.setStroke(24);
    List<RiderBean> riderBeanList = new ArrayList<>();
    RiderBean riderBeanUP = new RiderBean();
    riderBeanUP.setAppendixContent("北京 数 字 认 证 股 份 有 限公司");
    riderBeanUP.setFontSize(100);
    riderBeanUP.setFontWeight("bold");
    riderBeanUP.setFontFamily("公章刻章字体");
    riderBeanUP.setColor("rgb(255,0,0)");
    riderBeanUP.setRadian("M140,510 A250,275 0 1,1,800,510");
    riderBeanUP.setOrder("1");
    riderBeanList.add(riderBeanUP);
    RiderBean riderBeanDown = new RiderBean();
    riderBeanDown.setAppendixContent("91110108722619411AX");
    riderBeanDown.setFontSize(70);
    riderBeanDown.setFontWeight("bold");
    riderBeanDown.setFontFamily("arial");
    riderBeanDown.setColor("rgb(255,0,0)");
    riderBeanDown.setRadian("M155,490 L810,490");
    riderBeanDown.setOrder("2");
    riderBeanList.add(riderBeanDown);
    RiderBean riderBean = new RiderBean();
    riderBean.setAppendixContent("发票专用章");
    riderBean.setFontSize(110);
    riderBean.setFontWeight("bold");
    riderBean.setFontFamily("公章刻章字体");
    riderBean.setColor("rgb(255,0,0)");
    riderBean.setRadian("M165,800 L800,800");
    riderBean.setOrder("3");
    riderBeanList.add(riderBean);
    templateBean.setRiderBeans(riderBeanList);
    templateBean.setShapeEnum(ShapeEnum.ellipse);
    String json = JSON.toJSONString(templateBean);
    System.out.println(json);
  }

  /**
   * 圆形，有五角星
   */
  @Test
  public void getYData() {
    TemplateBean templateBean = new TemplateBean();
    templateBean.setTempId("111111");
    templateBean.setWidth(945);
    templateBean.setHeight(945);
    templateBean.setCenterX(473);
    templateBean.setCenterY(473);
    templateBean.setRx(450);
    templateBean.setColor("rgb(255,0,0)");
    templateBean.setStroke(24);
    List<RiderBean> riderBeanList = new ArrayList<>();
    RiderBean riderBeanUP = new RiderBean();
    riderBeanUP.setAppendixContent("北京 数 字 认 证 股 份 有 限公司");
    riderBeanUP.setFontSize(100);
    riderBeanUP.setFontWeight("bold");
    riderBeanUP.setFontFamily("ChangFangSong");
    riderBeanUP.setColor("rgb(255,0,0)");
    riderBeanUP.setRadian("M445,800 A320,320 0 1,1,500,800");
    riderBeanUP.setOrder("1");
    riderBeanList.add(riderBeanUP);
    RiderBean riderBean = new RiderBean();
    riderBean.setAppendixContent("发票专用章");
    riderBean.setFontSize(110);
    riderBean.setFontWeight("bold");
    riderBean.setFontFamily("ChangFangSong");
    riderBean.setColor("rgb(255,0,0)");
    riderBean.setRadian("M165,800 L800,800");
    riderBean.setOrder("2");
    riderBeanList.add(riderBean);
    templateBean.setRiderBeans(riderBeanList);
    templateBean.setShapeEnum(ShapeEnum.circle);
    templateBean.setIsStar(true);
    templateBean.setStarValue("<svg x=\"335\" y=\"345\">\n" +
        "        <polygon points=\"400,0 160,720 760,240 40,240 640,720 400,0\" style=\"fill:red;\" transform=\"scale(0.333,0.333)\"/>\n"
        +
        "    </svg>");
    String json = JSON.toJSONString(templateBean);
    System.out.println(json);
  }

  @Test
  public void Te() {
    cn.org.bjca.anysign.seal.moulage.bean.Template t = new cn.org.bjca.anysign.seal.moulage.bean.Template();
    t.setTempId("9999991");
    Map<String, String> map = new HashMap<>(16);
    map.put("1", "XXXXXX有限公司");
    map.put("2", "323XXXXXXXX455");
    map.put("3", "发票专用章");
    t.setRider(map);
    System.out.println(JSON.toJSONString(t));
  }

  @Test
  public void Circle() {
    String[] strings = new String[4];
    strings[0] = "北京数字认证股份有限公司";
    strings[1] = "财务专用章";
    strings[2] = "123312122312112113";
    strings[3] = "true";
    byte[] bytes = CircleImpression.createCircleImpression(strings);
    FileUtils fileUtils = new FileUtils();
    fileUtils.writeFile(bytes, "E:\\test\\Circle.png");
  }

  @Test
  public void temIdCheak() {
    System.out.println(FileTemplateService.findStringInFile("9999992"));
  }

}