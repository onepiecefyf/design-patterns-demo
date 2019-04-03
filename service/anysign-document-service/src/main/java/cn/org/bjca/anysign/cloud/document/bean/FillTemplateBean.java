package cn.org.bjca.anysign.cloud.document.bean;

import cn.org.bjca.seal.esspdf.bean.ImageAppearanceBean;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/***************************************************************************
 * <pre></pre>
 * @文件名称: FillTemplateBean
 * @包路径: cn.org.bjca.anysign.cloud.document.bean
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 14:21
 ***************************************************************************/
public class FillTemplateBean {

    @Getter
    @Setter
    private byte[] docxBty;

    @Getter
    @Setter
    private byte[] xmlBty;

    @Getter
    @Setter
    private List<ImageAppearanceBean> imageAppearanceBeanList;
}
