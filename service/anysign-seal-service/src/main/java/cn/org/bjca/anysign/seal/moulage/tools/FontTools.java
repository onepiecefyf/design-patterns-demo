package cn.org.bjca.anysign.seal.moulage.tools;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/***************************************************************************
 * <pre>字体工具类</pre>
 *
 * @文件名称: FontTools.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/28 20:33
 ***************************************************************************/
public class FontTools {

  /**
   * 获取所有服务器字体
   */
  public static List getAllFont() {
    List list = new ArrayList();
    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    for (int i = 0; i < fonts.length; i++) {
      list.add(fonts[i].getFamily());
    }
    List array = removeDeuplicate(list);
    String[] result = new String[array.size()];
    for (int i = 0; i < array.size(); i++) {
      result[i] = array.get(i).toString();
    }
    //按首字母排序开始
    Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
    Arrays.sort(result, com);
    List font = new ArrayList();
    for (String i : result) {
      font.add(i);
    }
    //按首字母排序结束
    return font;
  }


  private static List removeDeuplicate(List arlList) {
    HashSet h = new HashSet(arlList);
    arlList.clear();
    arlList.addAll(h);
    List list = arlList;
    return list;
  }

  /**
   * 检查服务器字体支持
   *
   * @param fontName 字体名称
   * @return font
   */
  public static Font cheakFont(String fontName) {
    return new Font(fontName, Font.BOLD, 18);
  }

}