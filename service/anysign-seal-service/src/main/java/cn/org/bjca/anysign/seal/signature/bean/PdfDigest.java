package cn.org.bjca.anysign.seal.signature.bean;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zjgao
 * @create 2018/9/20.
 * @description
 */
@Data
@Getter
@Setter
public class PdfDigest implements Serializable {

  private byte[] hash;
  private byte[] pdfBty;
}
