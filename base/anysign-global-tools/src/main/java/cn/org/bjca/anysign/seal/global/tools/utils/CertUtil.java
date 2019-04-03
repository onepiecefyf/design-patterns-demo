package cn.org.bjca.anysign.seal.global.tools.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.bjca.asn1.ASN1Sequence;
import org.bjca.asn1.x509.X509CertificateStructure;
import org.bjca.jce.fastparser.DerUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************************************************
 * <pre>证书工具类</pre>
 * @文件名称: CertUtil.java
 * @包 路   径：  cn.org.bjca.seal.esspdf.platform.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2013
 *
 * @类描述:
 * @版本: V2.0
 * @创建人： wangwenc
 * @创建时间：2013-3-19 下午8:53:44
 ***************************************************************************/
public final class CertUtil {

  // 日志
  private static Logger logger = LoggerFactory.getLogger(CertUtil.class);

  public static final String CODING = "UTF-8";
  public static final String SIGNALG_SHA1RSA = "SHA1WithRSA";
  public static final String SIGNALG_SHA256RSA = "SHA256WithRSA";
  public static final String SIGNALG_SM3SM2 = "SM3WithSM2";
  public static final String CERT_TYPE_RSA = "RSA";
  public static final String CERT_TYPE_SM2 = "SM2";
  public static final String CERTTYPE_SM2 = "SM2";
  public static final String HASH_SHA1 = "SHA1";
  public static final String HASH_SHA256 = "SHA256";
  public static final String HASH_SM3 = "SM3";
  public static final String CRLPATH = "crlFile/";
  public static final String BC = "BC";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * <p>解析证书文件</p>
   *
   * @param certBty 证书文件字节数组
   * @return X509Certificate 证书对象
   * @Description:仅支持RSA算法证书
   */
  public static X509Certificate readBytesToX509Certificate(byte[] certBty) throws Exception {
    X509Certificate certObj = null;
    BufferedReader bufRdr = null;
    try {
      bufRdr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(certBty), CODING));
      StringBuffer beginKey = new StringBuffer("-----BEGIN CERTIFICATE-----\n");
      String endKey = "-----END CERTIFICATE-----";
      String certStr = bufRdr.readLine();
      // 对非标准的base64证书格式的处理
      if (certStr != null && !certStr.startsWith("-----") && !certStr.startsWith("0")) {
        beginKey.append(certStr);
        beginKey.append("\n");
        while (true) {
          certStr = bufRdr.readLine();
          if (certStr == null) {
            break;
          }
          beginKey.append(certStr);
          beginKey.append("\n");
        }
        beginKey.append(endKey);
        certBty = beginKey.toString().getBytes(CODING);
      }
      CertificateFactory factory = CertificateFactory.getInstance("X509");
      certObj = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBty));
    } finally {
      IOUtils.closeQuietly(bufRdr);
    }
    return certObj;
  }


  /**
   * <p>证书签名验证</p>
   *
   * @param rootCert 根证书二进制数据
   * @param targetCert 目标证书二进制数据
   * @Description:
   */
  public static boolean certVerify(byte[] rootCert, byte[] targetCert) {
    boolean status = false;
    try {
      X509Certificate root = readBytesToX509Certificate(rootCert);
      X509Certificate cert = readBytesToX509Certificate(targetCert);
      cert.verify(root.getPublicKey());
      status = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return status;
  }

  /**
   * <p>证书时间验证</p>
   *
   * @param certBty 证书二进制数据
   * @Description:
   */
  public static boolean certDateVerify(byte[] certBty) {
    boolean status = false;
    try {
      X509Certificate cert = readBytesToX509Certificate(certBty);
      cert.checkValidity(new Date());
      status = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return status;
  }

  /**
   * <p>匹配获取指定的字符串</p>
   *
   * @param reg 正则表达式
   * @param res 数据
   * @Description:
   */
  public static String getKeyWord(String reg, String res) {
    String keyWord = "";
    Matcher matcher = Pattern
        .compile(reg, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
        .matcher(res);
    while (matcher.find()) {
      keyWord = matcher.group(1);
    }
    if ("".equals(keyWord)) {
      reg = "cn=.*";
      matcher = Pattern
          .compile(reg, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
          .matcher(res);
      while (matcher.find()) {
        keyWord = matcher.group();
      }
      if (keyWord != null) {
        keyWord = keyWord.replaceAll("CN=", "").replaceAll("cn=", "");
      }
    }
    return keyWord;
  }

  /**
   * <p>rsa 验签</p>
   *
   * @param bData 原文数据
   * @param bSign 签名数据
   * @param sAlg 签名算法
   * @param cert 证书
   * @Description:
   */
  public static boolean verifySign(byte[] bData, byte[] bSign, String sAlg, Certificate cert) {
    boolean flag = false;
    try {
      if (sAlg == null) {
        sAlg = SIGNALG_SHA1RSA;
      }
      Provider pro = Security.getProvider("BC");
      Signature sign = Signature.getInstance(sAlg, pro);
      sign.initVerify(cert);
      sign.update(bData);
      flag = sign.verify(bSign);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return flag;
  }

  /**
   * <p>从P12文件导出密钥</p>
   *
   * @param p12Bty p12数据
   * @param pwd 密钥口令
   * @Description:
   */
  public static KeyPair exportKeyPairFromP12(byte[] p12Bty, String pwd) {
    KeyPair keyPair = null;
    ByteArrayInputStream is = null;
    try {
      is = new ByteArrayInputStream(p12Bty);
      KeyStore store = KeyStore.getInstance("PKCS12");
      store.load(is, pwd.toCharArray());
      Enumeration<String> aliaseList = store.aliases();
      String alias = null;
      while (aliaseList.hasMoreElements()) {
        alias = aliaseList.nextElement();
        break;
      }
      Key key = store.getKey(alias, pwd.toCharArray());
      if (key instanceof PrivateKey) {
        Certificate cert = store.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();
        keyPair = new KeyPair(publicKey, (PrivateKey) key);
      }
    } catch (Exception ex) {
      logger.error("P12 file failed parsing:", ex);
      ex.printStackTrace();
    } finally {
      IOUtils.closeQuietly(is);
    }
    return keyPair;
  }

  /**
   * <p>获取证书签名算法</p>
   *
   * @param certBty 证书二进制数据
   * @Description:
   */
  public static String getCertSignAlg(byte[] certBty) throws Exception {
    X509CertificateStructure certObj = new X509CertificateStructure(
        (ASN1Sequence) DerUtil.getDerObject(certBty));
    String algID = certObj.getSignatureAlgorithm().getObjectId().toString();
    String algName = null;
    if ("1.2.156.10197.1.501".equals(algID)) {
      algName = SIGNALG_SM3SM2;
    } else if ("1.2.840.113549.1.1.5".equals(algID)) {
      algName = SIGNALG_SHA1RSA;
    } else if ("1.2.840.113549.1.1.11".equals(algID)) {
      algName = SIGNALG_SHA256RSA;
    } else {
      throw new Exception("Analytical certificate file error!");
    }
    return algName;
  }

  /**
   * <p>getCertKeyType</p>
   *
   * @Description:获取证书密钥类型
   */
  public static String getCertKeyType(byte[] certBty) throws Exception {
    X509CertificateStructure certObj = new X509CertificateStructure(
        (ASN1Sequence) DerUtil.getDerObject(certBty));
    String keyOid = certObj.getSubjectPublicKeyInfo().getAlgorithmId().getObjectId().getId();
    if ("1.2.840.113549.1.1.1".equals(keyOid)) {
      return CERT_TYPE_RSA;
    } else if ("1.2.840.10045.2.1".equals(keyOid)) {
      return CERT_TYPE_SM2;
    } else {
      throw new NoSuchAlgorithmException("expect key type is rsa or sm2.");
    }
  }

  public static int getCertKeySize(byte[] certBty) throws Exception {
    String certType = getCertKeyType(certBty);
    if (certType.equals(CERT_TYPE_RSA)) { // RSA
      ByteArrayInputStream inStream = new ByteArrayInputStream(certBty);
      Certificate rsaCertificate = CertificateFactory.getInstance("X.509")
          .generateCertificate(inStream);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      RSAPublicKeySpec publicKeySpec = keyFactory
          .getKeySpec(rsaCertificate.getPublicKey(), RSAPublicKeySpec.class);
      BigInteger prime = publicKeySpec.getModulus();
      return prime.toString(2).length();
    } else { // SM2
      return 256;
    }
  }

  /**
   * <p>格式DER证书</p>
   *
   * @param certBty 证书二进制数据
   * @Description:
   */
  public static byte[] formatDerCert(final byte[] certBty) throws Exception {
    BufferedReader bufRdr = null;
    byte[] derCert = null;
    try {
      bufRdr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(certBty), CODING));
      StringBuffer str = new StringBuffer("");
      String startKey = "-----BEGIN CERTIFICATE-----";
      String endKey = "-----END CERTIFICATE-----";
      String certStr = bufRdr.readLine();
      if (!certStr.startsWith("0")) {// 对非标准的base64证书格式的处理
        str.append(certStr);
        while (true) {
          certStr = bufRdr.readLine();
          if (certStr == null) {
            break;
          }
          str.append(certStr);
        }
        String base64Str = str.toString();
        base64Str = base64Str.replaceAll(startKey, "");
        base64Str = base64Str.replaceAll(endKey, "");
        derCert = Base64.decode(base64Str.trim());
      } else {
        derCert = certBty;
      }
    } finally {
      IOUtils.closeQuietly(bufRdr);
    }
    return derCert;
  }

  /**
   * <p>验证CRL合法性</p>
   *
   * @param crlBty crl文件
   * @Description:
   */
  public static boolean verifyCRL(byte[] crlBty) {
    InputStream in = null;
    boolean result = false;
    try {
      in = new ByteArrayInputStream(crlBty);
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      X509CRL crl = (X509CRL) cf.generateCRL(in);
      crl.getRevokedCertificate(new BigInteger("1", 16));
      result = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      IOUtils.closeQuietly(in);
    }
    return result;
  }


  /**
   * <p>创建信任链</p>
   *
   * @Description:
   */
  public static Certificate[] getSelfChain(byte[] signCertBty) throws Exception {
    Certificate[] chain = new Certificate[1];
    chain[0] = CertUtil.readBytesToX509Certificate(signCertBty);
    return chain;
  }

  /**
   * <p>获取证书撤销列表</p>
   *
   * @return false：表示证书未注销，true：表示证书已注销
   * @Description:
   */
  public static List<CRL> getCRLs() {
    FileInputStream in = null;
    List<CRL> crls = new ArrayList<CRL>();
    try {
      String filePath = CRLPATH;
      File file = new File(filePath);
      if (file.exists()) {
        File[] crlFiles = file.listFiles();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        if (crlFiles != null && crlFiles.length > 0) {
          for (File crlFile : crlFiles) {
            if (crlFile.getName().endsWith(".crl")) {
              in = new FileInputStream(crlFile.getPath());
              X509CRL crl = (X509CRL) cf.generateCRL(in);
              crls.add(crl);
              IOUtils.closeQuietly(in);
            }
          }
        }
      }
    } catch (Exception ex) {
      logger.error("cert crl create error!", ex);
    } finally {
      IOUtils.closeQuietly(in);
    }
    return crls;
  }

}
