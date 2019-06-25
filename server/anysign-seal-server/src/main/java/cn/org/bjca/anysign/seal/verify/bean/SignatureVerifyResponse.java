package cn.org.bjca.anysign.seal.verify.bean;

import cn.org.bjca.seal.esspdf.itext.bean.VerifyMessage;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */
@Data
public class SignatureVerifyResponse implements Serializable {

  private String transId = null;
  private List<VerifyMessage> verifyMessages = null;
}