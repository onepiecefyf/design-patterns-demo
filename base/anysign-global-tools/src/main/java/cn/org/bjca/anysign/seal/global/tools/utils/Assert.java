package cn.org.bjca.anysign.seal.global.tools.utils;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;

/***************************************************************************
 * <pre></pre>
 *
 * @author july_whj
 * @文件名称: Assert.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/13 13:49
 ***************************************************************************/
public class Assert {

  public Assert() {
  }

  public static void state(boolean expression, String message) {
    if (!expression) {
      throw new BaseRuntimeException(message);
    }
  }

  /**
   * 异常定义加入埋点
   *
   * @param expression 是否异常
   * @param message 消息
   * @param serverName 服务名称
   * @param methodName 调用方法
   */
  public static void state(boolean expression, String message, String serverName,
      String methodName) {
    if (!expression) {
      throw new BaseRuntimeException(message, serverName, methodName);
    }
  }


  public static void state(boolean expression, StatusConstants statusConstants, String message) {
    if (!expression) {
      throw new BaseRuntimeException(statusConstants, message);
    }
  }

  /**
   * 异常定义加入埋点
   *
   * @param expression 是否异常
   * @param statusConstants 状态枚举
   * @param message 消息
   * @param serverName 服务名称
   * @param methodName 调用方法
   */
  public static void state(boolean expression, StatusConstants statusConstants, String message,
      String serverName, String methodName) {
    if (!expression) {
      throw new BaseRuntimeException(statusConstants, message, serverName, methodName);
    }
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void state(boolean expression) {
    state(expression, "[Assertion failed] - this state invariant must be true");
  }

  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new BaseRuntimeException(message);
    }
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void isTrue(boolean expression) {
    isTrue(expression, "[Assertion failed] - this expression must be true");
  }

  public static void isNull(Object object, String message) {
    if (object != null) {
      throw new BaseRuntimeException(message);
    }
  }

  public static void isNull(Object object, StatusConstants statusConstants) {
    if (object != null) {
      throw new BaseRuntimeException(statusConstants);
    }
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void isNull(Object object) {
    isNull(object, "[Assertion failed] - the object argument must be null");
  }

  /**
   * 非空校验
   *
   * @param object 校验对象
   * @param message 错误信息
   * @param serverName 服务名称
   * @param methodName 方法名称
   */
  public static void notNull(Object object, String message, String serverName, String methodName) {
    if (object == null) {
      throw new BaseRuntimeException(message, serverName, methodName);
    }
  }

  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new BaseRuntimeException(message);
    }
  }

  /**
   * 非空校验
   *
   * @param object 校验对象
   * @param statusConstants 状态枚举
   * @param serverName 服务名称
   * @param methodName 方法名称
   */
  public static void notNull(Object object, StatusConstants statusConstants, String serverName,
      String methodName) {
    if (object == null) {
      throw new BaseRuntimeException(statusConstants, serverName, methodName);
    }
  }

  public static void notEntity(String object, StatusConstants statusConstants, String serverName,
      String methodName) {
    if (null == object || object.equals("")) {
      throw new BaseRuntimeException(statusConstants, serverName, methodName);
    }
  }

  public static void notNullBytes(byte[] object, StatusConstants statusConstants, String serverName,
      String methodName) {
    if (object == null || object.length == 0) {
      throw new BaseRuntimeException(statusConstants, serverName, methodName);
    }
  }

  public static void notNull(Object object, StatusConstants statusConstants) {
    if (object == null) {
      throw new BaseRuntimeException(statusConstants);
    }
  }

  /**
   * 非空校验
   *
   * @param object 校验对象
   * @param statusConstants 状态枚举
   * @param message 错误信息
   * @param serverName 服务名称
   * @param methodName 方法名称
   */
  public static void notNull(Object object, StatusConstants statusConstants, String message,
      String serverName, String methodName) {
    if (object == null) {
      throw new BaseRuntimeException(statusConstants, message, serverName, methodName);
    }
  }

  public static void notNull(Object object, StatusConstants statusConstants, String message) {
    if (object == null) {
      throw new BaseRuntimeException(statusConstants, message);
    }
  }


  /**
   * @deprecated
   */
  @Deprecated
  public static void notNull(Object object) {
    notNull(object, "[Assertion failed] - this argument is required; it must not be null");
  }


  public static void notEmpty(Object[] array, String message) {
    if (null == array) {
      throw new BaseRuntimeException(message);
    }
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void notEmpty(Object[] array) {
    notEmpty(array,
        "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
  }

  public static void noNullElements(Object[] array, String message) {
    if (array != null) {
      Object[] var2 = array;
      int var3 = array.length;

      for (int var4 = 0; var4 < var3; ++var4) {
        Object element = var2[var4];
        if (element == null) {
          throw new BaseRuntimeException(message);
        }
      }
    }

  }


  public static void noNullElements(Object[] array, StatusConstants message) {
    if (array != null) {
      Object[] var2 = array;
      int var3 = array.length;

      for (int var4 = 0; var4 < var3; ++var4) {
        Object element = var2[var4];
        if (element == null) {
          throw new BaseRuntimeException(message);
        }
      }
    }

  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void noNullElements(Object[] array) {
    noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
  }


  private static boolean endsWithSeparator(String msg) {
    return msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith(".");
  }

  private static String messageWithTypeName(String msg, Object typeName) {
    return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
  }

}