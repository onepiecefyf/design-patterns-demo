package cn.org.bjca.anysign.cloud.template.message;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;

import java.io.Serializable;

/***************************************************************************
 * <pre>文件上传请求消息</pre>
 * @文件名称: FileUploadReqMessage
 * @包路径: cn.org.bjca.anysign.seal.template.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 11:07
 ***************************************************************************/
public class FileUploadReqMessage extends BaseRequest implements Serializable {

    /** 文件*/
    private String file;

    /** 文件MD5*/
    private String md5;

    /** 文件名称*/
    private String fileName;

    /** 文件归属平台Id*/
    private String ownAppId;

    /** 文件归属组*/
    private String group;

    /** 文件归属用户*/
    private String user;

    /** 文件有效期*/
    private String expire;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwnAppId() {
        return ownAppId;
    }

    public void setOwnAppId(String ownAppId) {
        this.ownAppId = ownAppId;
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
}
