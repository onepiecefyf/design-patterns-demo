package cn.org.bjca.anysign.seal.global.tools.utils;


import org.apache.commons.codec.binary.Base64;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/13 10:04
 ***************************************************************************/
public class Base64Utils {


    /**
     * base64字符串转byte[]
     * @param base64Str
     * @return
     */
    public static byte[] base64String2ByteFun(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    /**
     * byte[]转base64
     * @param bytes
     * @return
     */
    public static String byte2Base64StringFun(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }


}