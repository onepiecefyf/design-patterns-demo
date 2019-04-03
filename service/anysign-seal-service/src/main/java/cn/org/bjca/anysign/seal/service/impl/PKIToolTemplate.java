package cn.org.bjca.anysign.seal.service.impl;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.service.bean.DepositCertSignRequestBean;
import cn.org.bjca.anysign.seal.service.bean.VerifyCertRequesrBean;
import cn.org.bjca.anysign.seal.service.bean.VerifySignRequestBean;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import cn.org.bjca.seal.esspdf.itext.tools.AbstractPKIToolTemplate;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***************************************************************************
 * @文件名称: RSAPKIToolTemplate.java
 * @包 路   径： cn.org.bjca.seal.esspdf.itext.tools
 * @版权所有：北京数字认证股份有限公司 (C) 2014
 * @类描述:
 * @版本: V2.0
 * @创建人： zjgao
 * @创建时间：2018年9月21日
 ***************************************************************************/

@Slf4j
public class PKIToolTemplate extends AbstractPKIToolTemplate {
    @Setter
    @Getter
    private SystemRequestService systemRequestService;

    public PKIToolTemplate(SystemRequestService systemRequestService) {
        this.systemRequestService = systemRequestService;
    }

    public PKIToolTemplate() {

    }

    /**
     * 托管证书签名请求实体bean
     */
    @Getter
    @Setter
    private DepositCertSignRequestBean depositCertSignRequestBean;

    /**
     * 签名验证请求实体bean
     */
    @Getter
    @Setter
    private VerifySignRequestBean verifySignRequestBean;


    @Override
    public byte[] sign() {
        byte[] signValueBytes = null;
        //digest 不为空为SM2时间戳签名
        if (null != digest && digest.length > 0) {
            depositCertSignRequestBean.setData(Base64Utils.byte2Base64StringFun(digest));
        }
        JSONObject jsonObject = systemRequestService.depositCertSign(depositCertSignRequestBean);
        int status = Integer.parseInt(jsonObject.getString("status"));
        if (Integer.parseInt(StatusConstants.SUCCESS.getStatus()) == status) {
            JSONObject data = jsonObject.getJSONObject("data");
            String signValue = data.getString("signValue");
            signValueBytes = Base64Utils.base64String2ByteFun(signValue);
        } else {
            log.error("invoke signature micro service error, status code : {},message : {}", status, jsonObject.getString("message"));
            throw new BaseRuntimeException(StatusConstants.PDF_SIGNATURE_ERROR, null, "PDF签章");
        }
        return signValueBytes;
    }

    @Override
    public boolean verify() {
        boolean result = false;
        //digest 不为空为MS2验签
        if (null != digest && digest.length > 0) {
            verifySignRequestBean.setData(Base64Utils.byte2Base64StringFun(digest));
        }
        JSONObject jsonObject = systemRequestService.verifySign(verifySignRequestBean);
        int status = Integer.parseInt(jsonObject.getString("status"));
        if (Integer.parseInt(StatusConstants.SUCCESS.getStatus()) == status) {
            result = true;
        } else {
            log.error("invoke signature micro service error, status code : {},message : {}", status, jsonObject.getString("message"));
            throw new BaseRuntimeException(StatusConstants.PDF_SIGNATURE_VERIFY_ERROR, null, "PDF验章");

        }
        return result;
    }

    /**
     * 证书验证接口
     *
     * @param verifyCertRequesrBean 认证对象bean
     * @return
     */
    public boolean verifyCert(VerifyCertRequesrBean verifyCertRequesrBean) {
        boolean result = false;
        JSONObject jsonObject = systemRequestService.verifyCert(verifyCertRequesrBean);
        if (null != jsonObject) {
            int status = Integer.parseInt(jsonObject.getString("status"));
            if (Integer.parseInt(StatusConstants.SUCCESS.getStatus()) == status) {
                result = true;
            } else {
                log.error("invoke signature micro service error, status code : {},message : {}", status, jsonObject.getString("message"));
                throw new BaseRuntimeException(StatusConstants.TSS_SIGNATURE_VERIFY_ERROR, null, "PDF验章");
            }
        } else {
            log.error("invoke signature micro service error");
            throw new BaseRuntimeException(StatusConstants.TSS_SIGNATURE_VERIFY_ERROR, null, "PDF验章");
        }
        return result;
    }


}
