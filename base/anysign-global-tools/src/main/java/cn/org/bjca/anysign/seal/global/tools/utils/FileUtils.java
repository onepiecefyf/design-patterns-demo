package cn.org.bjca.anysign.seal.global.tools.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/***************************************************************************
 * <pre>文件读取工具类</pre>
 *
 * @文件名称: FileUtils.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/14 17:06
 ***************************************************************************/
public class FileUtils {

  public FileUtils() {
  }

  public byte[] readFile(String file_path) {
    BufferedInputStream bins = null;
    byte[] bfile = (byte[]) null;
    String path = file_path;

    try {
      bins = new BufferedInputStream(new FileInputStream(path), 1024);
      if (bins.available() > 0) {
        bfile = new byte[bins.available()];
        bins.read(bfile);
      }

      return bfile;
    } catch (Exception var14) {
    } finally {
      try {
        if (bins != null) {
          bins.close();
        }
      } catch (Exception var13) {
      }
    }
    return null;
  }

  public int writeFile(byte[] bContent, String filePath) {
    byte result = 1;
    BufferedOutputStream bouts = null;

    try {
      int e = filePath.lastIndexOf("/");
      int index2 = filePath.lastIndexOf("\\");
      String path2;
      File dir2;
      if (e != -1) {
        path2 = filePath.substring(0, e);
        dir2 = new File(path2);
        if (!dir2.exists()) {
          dir2.mkdir();
        }
      }
      if (index2 != -1) {
        path2 = filePath.substring(0, index2);
        dir2 = new File(path2);
        if (!dir2.exists()) {
          dir2.mkdir();
        }
      }

      bouts = new BufferedOutputStream(new FileOutputStream(filePath), 1024);
      bouts.write(bContent);
      result = 0;
    } catch (Exception var17) {
    } finally {
      try {
        if (bouts != null) {
          bouts.close();
        }
      } catch (Exception var16) {
      }
    }
    return result;
  }

}