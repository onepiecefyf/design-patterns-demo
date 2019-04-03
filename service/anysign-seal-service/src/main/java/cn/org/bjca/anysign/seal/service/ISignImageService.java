package cn.org.bjca.anysign.seal.service;

import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.Template;

/***************************************************************************
 * <pre>生成签章图片接口</pre>
 *
 * @author july_whj
 * @文件名称: ISignImageService.inf
 * @包 路   径：  cn.org.bjca.anysign.seal.service
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/12 18:46
 ***************************************************************************/
public interface ISignImageService {

    /**
     * 生成签章图片ByPath
     * @param sealImageBean 印模实体
     * @return 文件路径/appId/fileId
     */
    String createImageByPath(SealImageBean sealImageBean);

    /**
     * 生成印模返回文件base64
     * @param sealImageBean 印模实体
     * @return 图片base64
     */
    String createImageByContent(SealImageBean sealImageBean);


    /**
     * 根据模板生成印模返回文件base64
     * @param template 模板实体
     * @return base64
     */
    String createImageTemplateByContent(Template template);

    /**
     * 根据模板生成印模返回文件ID
     * @param template 模板实体
     * @return 文件ID
     */
    String createImageTemplateByPath(Template template);
}
