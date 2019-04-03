package cn.org.bjca.anysign.cloud.template.message;

import java.io.Serializable;

/***************************************************************************
 * <pre>模板服务响应消息</pre>
 * @文件名称: TemplateRespMessage
 * @包路径: cn.org.bjca.anysign.seal.template.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/12 11:11
 ***************************************************************************/
public class TemplateRespMessage implements Serializable {

    /** 交易参考号 必填*/
    private String transId;

    /** pdf文档文件id*/
    private String fileId;

    /** pdf文档的base64编码*/
    private String fileContent;

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

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
