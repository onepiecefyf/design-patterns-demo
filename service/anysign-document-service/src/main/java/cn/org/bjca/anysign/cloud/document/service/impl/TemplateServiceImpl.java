package cn.org.bjca.anysign.cloud.document.service.impl;

import cn.org.bjca.anysign.cloud.document.bean.FillTemplateBean;
import cn.org.bjca.anysign.cloud.document.service.IDocxTemplateService;
import cn.org.bjca.seal.esspdf.convert.DocxTemplateTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/***************************************************************************
 * <pre>填充模板服务</pre>
 * @文件名称: TemplateServiceImpl
 * @包路径: cn.org.bjca.anysign.cloud.document.service.impl
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 14:13
 ***************************************************************************/
@Service
@Slf4j
public class TemplateServiceImpl implements IDocxTemplateService {

    /**
     * <p>docx模板填充</p>
     * @param fillTemplateBean 模板填充Bean
     * @return
     */
    @Override
    public byte[] docxTemplateFill(FillTemplateBean fillTemplateBean) {
        DocxTemplateTool tool = new DocxTemplateTool();
        return tool.xmlParse(fillTemplateBean.getDocxBty(), fillTemplateBean.getXmlBty(),
                fillTemplateBean.getImageAppearanceBeanList());
    }
}
