package cn.org.bjca.anysign.seal.moulage;

import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;

/***************************************************************************
 * <pre></pre>
 *
 * @author july_whj
 * @文件名称: ICreateImage.inf
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 17:27
 ***************************************************************************/
public interface ICreateImage {

    /**
     * 根据模板创建印模
     * @param templateBean 模板对象
     * @return
     */
    byte[] creatrImage(TemplateBean templateBean);

    /**
     * 根据模板创建印模
     * @param templateBean 模板对象
     * @param dpi 分辨率
     * @return
     */
    byte[] creatrImage(TemplateBean templateBean,int dpi);

    /**
     * 校验函数
     * @param templateBean
     */
    void verification(TemplateBean templateBean);

}
