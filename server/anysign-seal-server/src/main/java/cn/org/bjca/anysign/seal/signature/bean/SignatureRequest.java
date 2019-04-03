package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;

import java.io.Serializable;
import java.util.List;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */

public class SignatureRequest extends BaseRequest implements Serializable {

    /**
     * 交易参考号
     */
    private String transId;

    /**
     * 文档传输类型
     */
    private FileTransmissionType fileTransmissionType;

    /**
     * 请求文件下载token
     */
    private String requestFileToken;

    /**
     * 请求文件id
     */
    private String requestFileId;

    /**
     * 文件base64编码
     */
    private String requestFileContent;

    /**
     * 文件归属组
     */
    private  String group;

    /**
     * 文件归属用户
     */
    private String user;

    /**
     * 文件有效期
     */
    private int  expire;

    /**
     * 签章规则实体-seal
     */
    private List<Seal> sealList;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public FileTransmissionType getFileTransmissionType() {
        return fileTransmissionType;
    }

    public void setFileTransmissionType(FileTransmissionType fileTransmissionType) {
        this.fileTransmissionType = fileTransmissionType;
    }

    public String getRequestFileToken() {
        return requestFileToken;
    }

    public void setRequestFileToken(String requestFileToken) {
        this.requestFileToken = requestFileToken;
    }

    public String getRequestFileId() {
        return requestFileId;
    }

    public void setRequestFileId(String requestFileId) {
        this.requestFileId = requestFileId;
    }

    public String getRequestFileContent() {
        return requestFileContent;
    }

    public void setRequestFileContent(String requestFileContent) {
        this.requestFileContent = requestFileContent;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public List<Seal> getSealList() {
        return sealList;
    }

    public void setSealList(List<Seal> sealList) {
        this.sealList = sealList;
    }
}
