package cn.org.bjca.anysign.seal.service.bean.enumpackage;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description  算法枚举
 */
public enum Alg {
    RSA,
    SM2,
    SHA1,
    SHA256,
    SM3,
    SHA1WithRSA,
    SHA256WithRSA,
    SM3WithSM2,
    /**
     * 仅用于云签名传递参数使用
     */
    SHA1withRSA,
    /**
     * 仅用于云签名传递参数使用
     */
    SHA256withRSA,
    /**
     * 仅用于云签名传递参数使用
     */
    SM3withSM2
}
