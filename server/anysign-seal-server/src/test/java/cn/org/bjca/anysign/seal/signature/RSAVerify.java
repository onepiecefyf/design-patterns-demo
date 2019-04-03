package cn.org.bjca.anysign.seal.signature;

import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.seal.esspdf.itext.utils.ItextUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;

public class RSAVerify {
    @Test
    public void rsaVerify() throws Exception {

        byte[] bytes = {66,-84,-121,-63,119,-100,55,61,-35,-74,-86,88,-70,-92,-69,28,6,71,-13,39,-46,-122,43,4,120,-13,-39,19,-73,111,-75,-16,-13,62,-90,-75,-126,126,122,98,39,102,-46,25,-14,-115,73,-22,58,36,111,-6,-47,-128,-113,-9,43,-112,45,-83,-3,4,-51,-24,93,-59,-85,94,2,-56,-5,-66,-123,-112,-103,-108,43,74,-54,99,119,-74,23,-46,-90,86,127,-49,76,7,-27,-124,-45,49,61,-108,-5,99,91,-104,-49,25,102,-36,79,-50,126,54,4,-114,47,12,75,-86,119,-128,118,28,-114,-74,-21,-127,0,75,-80,106,36,29};
        String signValue = Base64Utils.byte2Base64StringFun(bytes);
        System.out.println(signValue);
        byte[] bytes1 = {49, 105, 48, 24, 6, 9, 42, -122, 72, -122, -9, 13, 1, 9, 3, 49, 11, 6, 9, 42, -122, 72, -122, -9, 13, 1, 7, 1, 48, 28, 6, 9, 42, -122, 72, -122, -9, 13, 1, 9, 5, 49, 15, 23, 13, 49, 56, 49, 50, 49, 51, 48, 50, 48, 48, 49, 48, 90, 48, 47, 6, 9, 42, -122, 72, -122, -9, 13, 1, 9, 4, 49, 34, 4, 32, -4, 19, 14, -36, 19, 83, 78, -72, -21, 81, -18, 43, 55, 115, 47, 83, -120, -6, -96, 90, -29, 65, 19, -9, 7, 127, -5, 27, -58, -82, -24, -109};
        String hash = Base64Utils.byte2Base64StringFun(bytes1);
        byte[] hashme = Base64Utils.base64String2ByteFun("1fny0vmRtvaAlzo+qr+FzCbs2nXVgUwWKDhrEhlmOfI=");
        System.out.println(hash);
//        X509Certificate signCert = ItextUtil.readBytesToX509Certificate(FileUtils.readFileToByteArray(
//                new File("E:\\test\\CertExchangewhjw.cer")));
        X509Certificate signCert = ItextUtil.readBytesToX509Certificate(Base64Utils.base64String2ByteFun("MIIDczCCAlugAwIBAgISIAFMDW8YhPLUHvG4o4Q06xvfMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwOTE5MTYwMDAwWhcNMjAwNDI1MTU1OTU5WjAzMREwDwYDVQQDDAhCSkNBYmpjYTERMA8GA1UECgwIQkpDQWJqY2ExCzAJBgNVBAYMAkNOMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYquj6EiELhzqLAXtOFzp96ja50mShcSd8DTtDZN2B1dI64+tkOmehqskEtPN637lQxmyMZVBV9iWftPB2+wW+uhnr9fbkYz9gVmxORRlNjLbgKJKmwk/1kf2ZYP81cv1vMwlYg2BvxkUMlWg0Rnvw/eotDnn0BaP3GSYTScuOjQIDAQABo4H1MIHyMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGXBgNVHR8EgY8wgYwwQKA+oDyGOmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9MT0NBTFJTQTIwNDhfMC5jcmwwSKBGoESGQmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9pbmMvTE9DQUxSU0EyMDQ4X2luY18wLmNybDAdBgNVHQ4EFgQU5oBNMBA0lOTb3bJxkjJLacAHv2MwHwYDVR0jBBgwFoAUxKUDNYz2evtLZyDMutEpkQEj+JUwDQYJKoZIhvcNAQEFBQADggEBAK5E3UQLj4dwqqw3McSW0RqzURPZW5evvXyCypXWBvCu48SIYd6GmJ2rz4o+pu4LSCmroCXnmnY8cH98lKvkpTh7k3zOgK0FoeY6KoqJFgh6FOjCqXLJQ3zZDZkPOzjMhZOCcX2h89Xd3eG+t6ezXjEyMnmCB3SALh8SPwWdlFcG1EMHAlwEKJESKTcsUaGda2ExvVCy3V7sVnUVJTwa4yCA2jBrZRt2kgF74ddKUaH/Pkuu2zrO/PG9SMS2GY6v8N36V8vbQ5f4O3Ju4W0s+rAA0TRC/0X+DAsNqlsnLBEbIsWInDgB/Jh55MSxXrZNrNUv/RNURD9+i2OdfHO8ljQ="));
        Security.addProvider(new BouncyCastleProvider());
        Signature sig = Signature.getInstance("SHA256withRSA", "BC");
        sig.initVerify(signCert.getPublicKey());
        sig.update(Base64Utils.base64String2ByteFun("MWkwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTgxMjEzMDY1MDM0WjAvBgkqhkiG9w0BCQQxIgQguUg37c58ipfJaTcgJ0lgdfodATneVKdT2QsgPFo44zI="));
        boolean boo = sig.verify(Base64Utils.base64String2ByteFun("g5uEBNAEcIZamQmhs1mxXyTV2uLE+y90+XRhaKIyyEev/usweVUTOq05hUMmYxHx5ChiCvwohGKXM+EzPAmfrVci+qwyVuLFZJHwYTfw4dDB+wihvtvtd065skLjPhnw+IZMLxZ/B6hRuG6A60nIpxpOtLchLkNoXdGBrcZVX6Y="));
        System.out.println(boo);
    }

}

