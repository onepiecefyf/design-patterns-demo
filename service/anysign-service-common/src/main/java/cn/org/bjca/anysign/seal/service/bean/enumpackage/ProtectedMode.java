package cn.org.bjca.anysign.seal.service.bean.enumpackage;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description 文档保护模式 枚举值
 */
public enum ProtectedMode {
  /**
   * 无限制
   */
  NOLIMITED,
  /**
   * 仅允许复制
   */
  COPYABLE,
  /**
   * 仅允许打印
   */
  PRINTABLE,
  /**
   * 允许复制与打印
   */
  COPYABLEANDPRINTABLE,
  /**
   * 全部禁止
   */
  ALLFORBIDEN
}
