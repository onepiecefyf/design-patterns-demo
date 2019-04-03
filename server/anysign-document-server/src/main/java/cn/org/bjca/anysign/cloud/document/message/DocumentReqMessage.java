package cn.org.bjca.anysign.cloud.document.message;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;

import java.io.Serializable;
import java.util.List;

/***************************************************************************
 * <pre>文档请求消息</pre>
 * @文件名称: DocumentReqMessage
 * @包路径: cn.org.bjca.anysign.seal.document.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 11:29
 ***************************************************************************/
public class DocumentReqMessage extends BaseRequest implements Serializable {

    /** 交易参考号*/
    private String transId;

    /** 文件传输类型 必填*/
    private String fileTransmissionType;

    /** 请求文件token*/
    private String requestFileToken;

    /** 请求文件ID*/
    private String requestFileId;

    /** 文件base64编码*/
    private String requestFileContent;

    /** 请求文件Token列表*/
    private List<String> requestFileTokenList;

    /** 文件列表 base64编码*/
    private List<String> requestFileContentList;

    /** 请求文件ID列表，与Token对应*/
    private List<String> requestFileIdList;

    /** 请求文档格式类型 必填*/
    private String requestFileType;

    /** 响应文档格式类型 必填*/
    private String responseFileType;

    /** 文件归属组*/
    private String group;

    /** 文件归属用户*/
    private String user;

    /** 文件有效期*/
    private String expire;

    /** 转换图片范围*/
    private String convert2PicScopeType;

    /** 转换图片起始页码*/
    private int convert2PicStartPageNum;

    /** 转换图片起始页码*/
    private int convert2PicEndPageNum;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getFileTransmissionType() {
        return fileTransmissionType;
    }

    public void setFileTransmissionType(String fileTransmissionType) {
        this.fileTransmissionType = fileTransmissionType;
    }

    public String getRequestFileToken() {
        return requestFileToken;
    }

    public void setRequestFileToken(String requestFileToken) {
        this.requestFileToken = requestFileToken;
    }

    public String getRequestFileContent() {
        return requestFileContent;
    }

    public void setRequestFileContent(String requestFileContent) {
        this.requestFileContent = requestFileContent;
    }

    public String getResponseFileType() {
        return responseFileType;
    }

    public void setResponseFileType(String responseFileType) {
        this.responseFileType = responseFileType;
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

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getRequestFileId() {
        return requestFileId;
    }

    public void setRequestFileId(String requestFileId) {
        this.requestFileId = requestFileId;
    }

    public List<String> getRequestFileTokenList() {
        return requestFileTokenList;
    }

    public void setRequestFileTokenList(List<String> requestFileTokenList) {
        this.requestFileTokenList = requestFileTokenList;
    }

    public List<String> getRequestFileContentList() {
        return requestFileContentList;
    }

    public void setRequestFileContentList(List<String> requestFileContentList) {
        this.requestFileContentList = requestFileContentList;
    }

    public List<String> getRequestFileIdList() {
        return requestFileIdList;
    }

    public void setRequestFileIdList(List<String> requestFileIdList) {
        this.requestFileIdList = requestFileIdList;
    }

    public String getRequestFileType() {
        return requestFileType;
    }

    public void setRequestFileType(String requestFileType) {
        this.requestFileType = requestFileType;
    }

    public String getConvert2PicScopeType() {
        return convert2PicScopeType;
    }

    public void setConvert2PicScopeType(String convert2PicScopeType) {
        this.convert2PicScopeType = convert2PicScopeType;
    }

    public int getConvert2PicStartPageNum() {
        return convert2PicStartPageNum;
    }

    public void setConvert2PicStartPageNum(int convert2PicStartPageNum) {
        this.convert2PicStartPageNum = convert2PicStartPageNum;
    }

    public int getConvert2PicEndPageNum() {
        return convert2PicEndPageNum;
    }

    public void setConvert2PicEndPageNum(int convert2PicEndPageNum) {
        this.convert2PicEndPageNum = convert2PicEndPageNum;
    }
}
