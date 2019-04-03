package cn.org.bjca.anysign.seal.pki;


import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.Alg;
import cn.org.bjca.seal.esspdf.itext.bean.SignatureField;
import cn.org.bjca.seal.esspdf.itext.tools.IPKITool;
import cn.org.bjca.seal.esspdf.itext.tools.PDFSM2VerifyTool;
import cn.org.bjca.seal.esspdf.itext.tools.RSAPKITool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.DEROctetString;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.pki
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/16 14:41
 ***************************************************************************/
public class PkiTools {

    IPKITool tool = new RSAPKITool(Alg.SHA256WithRSA.name());

    @Test
    public void verify() {
        byte[] plainData = Base64.decodeBase64("MWkwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTgxMDE2MDcwNTAwWjAvBgkqhkiG9w0BCQQxIgQg83Xhdgcm9ecbcVkoo/r1RTvOr5AwaPy9dyJN1AuXEKw=");
        byte[] signData = Base64.decodeBase64("lFWp6wjhVxLcO1qk0pQh7SLE7a/UJzfrLOStfgNKLs2NjJzkr2d+ukP4ThSZI89ouVqiCWq8IfVoysk9BQkL/1YDt2nIG91bNMhK9hPmttBdMLikF//MxJHSkeiY3Y+MWZTdRbRAxGXA/yiNlr8+0sQkHVtvvLzAxfS17jFVH4Hd1ZjQZMed5MlvbDRBxZh8vK+MytP7Fh8DiaqSK5d5ARB5BrC8HOCOD0uwO38OULA29ZaPgmYe5toQNSBgRtWZhbV1o/CuegWbtQNlY+Orcv2tYr22u9FNlk9nKjXEGlWaYpU0wQQUdXVITCnjMk0WWUeRl28lxqhtejLD269xkg==");
        byte[] certBty = Base64.decodeBase64("MIIFhDCCBGygAwIBAgIKG0AAAAAAAAXauDANBgkqhkiG9w0BAQsFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMjAeFw0xNzExMTUxNjAwMDBaFw0yNDEyMzAxNTU5NTlaMFwxCzAJBgNVBAYTAkNOMS0wKwYDVQQKDCTljJfkuqzmlbDlrZforqTor4HogqHku73mnInpmZDlhazlj7gxHjAcBgNVBAMMFVJTQea1i+ivleWNleS9jeivgeS5pjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMF5K6pfkByAxm6N9fOu6Byd0QAht3P1/rTNQS/lVLxZpdkByvMuKrgTJC13njpcovrqH7udywO3p1kFQSbo4pJAQKtxq/UpqNqkEOxsur82TQn2bu2nuYm80yvTJ8355gbF69qvouSRNNRaDCFQdkBtVCXnJbQidVo4PQS+cqD6Stj8tMQRsJyIMHPmLO1ktsn8ws2r52YSs9F2PjVNwjqMfGd3ugd3LfT3djCQ/tazFEYTsbnE4I4MkN6vsHJ+I2Y0HYDZZK4T77pK6A4cMd5jBJdk1EolHWpeOSSMw3Hn7p7UbgvZ0WebjXNH2/FklDbaoMdexJIq7XCmm+WucNsCAwEAAaOCAlAwggJMMB8GA1UdIwQYMBaAFPu31FYXWIwjfdX4QgHU7XebV+vpMAsGA1UdDwQEAwIGwDCBrQYDVR0fBIGlMIGiMGygaqBopGYwZDELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExGDAWBgNVBAsMD1B1YmxpYyBUcnVzdCBDQTEaMBgGA1UEAwwRUHVibGljIFRydXN0IENBLTIxEDAOBgNVBAMTB2NhNGNybDIwMqAwoC6GLGh0dHA6Ly9sZGFwLmJqY2Eub3JnLmNuL2NybC9wdGNhL2NhNGNybDIuY3JsMAkGA1UdEwQCMAAwgecGA1UdIASB3zCB3DA1BgkqgRwBxTiBFQEwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwNQYJKoEcAcU4gRUCMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuYmpjYS5vcmcuY24vY3BzMDUGCSqBHAHFOIEVAzAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczA1BgkqgRwBxTiBFQQwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwYgYIKwYBBQUHAQEEVjBUMCgGCCsGAQUFBzABhhxPQ1NQOi8vb2NzcC5iamNhLm9yZy5jbjo5MDEyMCgGCCsGAQUFBzAChhxodHRwOi8vY3JsLmJqY2Eub3JnL2NhaXNzdWVyMBMGCiqBHIbvMgIBAR4EBQwDNjU0MA0GCSqGSIb3DQEBCwUAA4IBAQCagto1m9FSAnuo9rBVuKcloTJcRxYWpah4XeLms2TByVk8+ui8eiAlzySgutAc+CJQ6kPqvjyHKIiDGRQII0r4DZF0lKQSiBlotazksNnBkRKbbLmesI5T6omDQh+z7NCKCc3yJn770uVIe7+HsEusHysb7FcRQzkD5b7F/ZJJe/StlXm0DHw4DXtbP5Brt0W0M1EIOzIV4aGd1AxNVPfCt6kuVZ3WyyO8bZOLwzI63e0h1sO/mNwR1s4ouW5DrAfKPI1jqelD7Xgyh9knAyjmvqh1ypJdL6GIZwPziq6exIyb1Jt3Atvpe93tFGUWYLqXxWxy1s/zGfKtxPOYeLGg");
        try {
            boolean b = tool.verify(plainData, signData, certBty);
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getCert() throws Exception {
        byte[] pdfBty = null;
        InputStream is = null;
        try {
            is = new FileInputStream("E://test//1017sm2time.pdf");
            pdfBty = IOUtils.toByteArray(is);
        } catch (Exception e) {
            IOUtils.closeQuietly(is);
        }
        int begin = 0;
        int end = 0;
        StringBuffer signTem = new StringBuffer();
        for (int j = 0; j < 72; j++) {
            signTem.append("10");
        }
        String sign = new DEROctetString(Base64Utils.base64String2ByteFun("MEUCIQCLbfH6+fwKSzJ3zxBrpns23P43YOSntyqai4rZ3VTvqwIgEd4pQL4w7IaX1C4Va3ijpk5quXXOBc7rED2d8SWuRLE=")).toString();
        sign = sign.replaceAll("#", "").trim();
//        if (clientSignBty.length > 72) {
//            PdfReader reader = new PdfReader(pdfBty);
//            AcroFields af = reader.getAcroFields();
//            ArrayList<String> names = af.getSignatureNames();
//            PdfDictionary pdfDictionary = af.getSignatureDictionary(names.get(names.size() - 1));
//            PdfArray arr = pdfDictionary.getAsArray(new PdfName("ByteRange"));
//            String arrData = arr.toString();
//            String[] data = arrData.split(",");
//            begin = Integer.parseInt(data[1].trim());
//            end = Integer.parseInt(data[2].trim());
//        } else {
            PDFSM2VerifyTool tool = new PDFSM2VerifyTool();
            List<SignatureField> beanList = tool.getFields(pdfBty);
            begin = beanList.get(beanList.size() - 1).getByteRange()[1];
            end = beanList.get(beanList.size() - 1).getByteRange()[2];
//        }
        int length = end - begin;
        byte[] allBty = new byte[pdfBty.length];
        System.arraycopy(pdfBty, 0, allBty, 0, begin);
        byte[] optBty = new byte[length];
        System.arraycopy(pdfBty, begin, optBty, 0, length);
        String optStr = new String(optBty);
        int signLength = sign.toString().length();
        for (int k = signLength; k < signTem.toString().length(); k++) {
            sign += "0";
        }
        optStr = optStr.replace(signTem, sign);
        optBty = optStr.getBytes();
        System.arraycopy(optBty, 0, allBty, begin, length);
        System.arraycopy(pdfBty, end, allBty, end, allBty.length - end);
    }


}