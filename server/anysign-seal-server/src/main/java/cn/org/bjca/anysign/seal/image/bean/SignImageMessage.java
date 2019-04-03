package cn.org.bjca.anysign.seal.image.bean;

import java.io.Serializable;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: SignImageMessage.class
 * @包 路   径：  cn.org.bjca.anysign.image.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/7 14:08
 ***************************************************************************/
public class SignImageMessage implements Serializable{

    /**
     * 交易参考号
     */
    private String transId;

    /**
     * 生成图片路径
     */
    private String fileId;
    /**
     * 生成图片BASE64
     */
    private String sealContent;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSealContent() {
        return sealContent;
    }

    public void setSealContent(String sealContent) {
        this.sealContent = sealContent;
    }
}