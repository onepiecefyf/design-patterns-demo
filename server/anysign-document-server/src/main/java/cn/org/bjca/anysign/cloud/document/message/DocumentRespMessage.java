package cn.org.bjca.anysign.cloud.document.message;

import java.io.Serializable;

/***************************************************************************
 * <pre>文档响应消息</pre>
 * @文件名称: DocumentRespMessage
 * @包路径: cn.org.bjca.anysign.seal.document.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 11:44
 ***************************************************************************/
public class DocumentRespMessage implements Serializable {

    /** 交易参考号*/
    private String transId;

    /** 文件ID*/
    private String fileId;

    /** 文件内容Base64*/
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
