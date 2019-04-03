package cn.org.bjca.anysign.cloud.document.service;

import cn.org.bjca.anysign.cloud.document.bean.DocumentFormatBean;

import java.io.IOException;
import java.util.List;

/***************************************************************************
 * <pre>文档服务</pre>
 * @文件名称: IDocumentFormatService
 * @包路径: cn.org.bjca.anysign.cloud.document.service
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 14:00
 ***************************************************************************/
public interface IDocumentFormatService {

    /**
     * <p>PDF格式化为图片</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    byte[] pdf2Img(DocumentFormatBean documentFormatBean);

    /**
     * <p>PDF格式化为图片列表</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    List<byte[]> pdf2ImgList(DocumentFormatBean documentFormatBean);

    /**
     * <p>Html 文档转换Pdf</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     * @throws IOException
     */
    byte[] html2Pdf(DocumentFormatBean documentFormatBean);

    /**
     * <p>Word文档转换Pdf</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    byte[] word2Pdf(DocumentFormatBean documentFormatBean);

    /**
     * <p>Image文档转换Pdf</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    byte[] img2Pdf(DocumentFormatBean documentFormatBean);

    /**
     * <p>合并PDF</p>
     *
     * @param pdfBtyList PDF文档Bty列表
     * @return
     */
    byte[] mergePdf(List<byte[]> pdfBtyList);
}
