package com.newcoder.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 姓名处理
 * @author fengyafei
 */
public class NameRule {

  private static final String NAME = "^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$";

  public static void main(String[] args) {
    String name = "哈哈11哈";
    System.out.println(desensitizationName(name));
  }

  private static String desensitizationName(String name) {
    Pattern pattern = Pattern.compile(NAME);
    Matcher matcher = pattern.matcher(name);
    String captureWord = "";
    while (matcher.find()) {
      captureWord = matcher.group();
    }
    return captureWord;
  }

}
