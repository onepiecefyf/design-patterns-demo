package cn.org.bjca.anysign.cloud.document.enumeration;

import lombok.Getter;
import lombok.Setter;

/***************************************************************************
 * <pre></pre>
 * @文件名称: ImageMergeType
 * @包路径: cn.org.bjca.anysign.cloud.document.enumeration
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/10/16 15:01
 ***************************************************************************/
public enum ImageMergeType {

    /** 横向*/
    HORIZONTAL(1),
    /** 纵向*/
    VERTICAL(2);

    @Getter
    @Setter
    private int typeCode;

    ImageMergeType(int typeCode){
        this.setTypeCode(typeCode);
    }
}
