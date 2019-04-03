package cn.org.bjca.anysign.seal.moulage.defaultImpl;


import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;

/***************************************************************************
 * <pre>印章生成工厂</pre>
 *
 * @author july_whj
 * @文件名称: SealImageFactory
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 17:45
 ***************************************************************************/
public class SealImageFactory {

  public static byte[] creatSealImage(SealImageBean sealImageBean) {
    switch (sealImageBean.getShape()) {
      case circle:
        return SealImageCircle.creatCircle(sealImageBean);
      case rectangle:
        return SealImageRectangle.creatRectangle(sealImageBean);
      case ellipse:
        return SealImageEllipse.creatEllipse(sealImageBean);
      default:
        return null;
    }
  }


}