package cn.org.bjca.anysign.seal.service.bean;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Getter
@Setter
public class DespositCertSignResponseBean implements Serializable {

  private String signValue;
  private String cert;
  private String transId;
  private String requestId;

}


