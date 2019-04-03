package cn.org.bjca.anysign.seal.moulage.convert;

import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;

/***************************************************************************
 * <pre>模板转换接口</pre>
 *
 * @author july_whj
 * @文件名称: ITemplateService.inf
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.convert
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 18:39
 ***************************************************************************/
public interface ITemplateService {
    /**
     * 模板数据持久化
     *
     * @param templateBean 模板数据
     */
    void templatePersistence(TemplateBean templateBean);

    /**
     * 读取模板数据
     * @param template 模板ID及其附文信息
     * @return
     */
    TemplateBean readTemplateById(Template template);

    /**
     * 删除模板
     * @param templateId 模板ID
     */
    void delTemplateById(String templateId);

    /**
     * 更新模板数据
     */
    void updateTemplateById(TemplateBean templateBean);
    /**
     * 刷新数据到内存
     */
    void templateRefresh();

}
