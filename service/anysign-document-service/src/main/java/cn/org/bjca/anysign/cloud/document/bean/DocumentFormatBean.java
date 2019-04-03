package cn.org.bjca.anysign.cloud.document.bean;

import lombok.Getter;
import lombok.Setter;

/***************************************************************************
 * <pre>文档格式Bean</pre>
 * @文件名称: DocumentFormatBean
 * @包路径: cn.org.bjca.anysign.cloud.document.bean
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 14:33
 ***************************************************************************/
public class DocumentFormatBean {

    /**
     * 源文件Bty
     */
    @Getter
    @Setter
    private byte[] sourceFileBty;

    /**
     * 目标文件类型
     */
    @Getter
    @Setter
    private String targetFileType;

    /**
     * 图片起始
     */
    @Getter
    @Setter
    private int startIndex;

    /**
     * 图片结束
     */
    @Getter
    @Setter
    private int endIndex;

    /**
     * 是否判断范围
     */
    @Getter
    @Setter
    boolean judgeScope;

    /**
     * 图片类型
     */
    @Getter
    @Setter
    String imgType;

    /**
     * 图片合并类型   1：横向  2：纵向
     */
    @Getter
    @Setter
    int imgMergeType;

    /**
     * html宽度，默认1000
     */
    @Getter
    @Setter
    int htmlWidth = 1000;
}
