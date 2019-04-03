package cn.org.bjca.anysign.cloud.document.service;

import cn.org.bjca.anysign.cloud.document.bean.FillTemplateBean;

/***************************************************************************
 * <pre>模板服务</pre>
 * @文件名称:  IDocxTemplateService
 * @包路径:  cn.org.bjca.anysign.cloud.document.service
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:  
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 13:59
 ***************************************************************************/
public interface IDocxTemplateService {

    /**
     * <p>docx模板填充</p>
     * @param fillTemplateBean 模板填充Bean
     * @return
     */
    byte[] docxTemplateFill(FillTemplateBean fillTemplateBean);
}
