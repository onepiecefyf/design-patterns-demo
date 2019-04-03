package cn.org.bjca.anysign.seal.moulage.convert;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.constant.SystemConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.moulage.bean.RiderBean;
import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/***************************************************************************
 * <pre>文件转换模板</pre>
 *
 * @author july_whj
 * @文件名称: FileTemplateService.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.convert
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 18:37
 ***************************************************************************/
@Slf4j
@Service
public class FileTemplateService implements ITemplateService {

  /**
   * 文件模板路径
   */
  private static final String FILEPATH = SystemConstants.HOMEPATH
      .concat("/BJCAROOT/template/templateImpression.dat");
  /**
   * 模板备份
   */
  private static final String FILEPATHBAK = SystemConstants.HOMEPATH
      .concat("/BJCAROOT/template/templateImpression_bak.dat");

  /**
   * 文件内容分隔符
   */
  private static final String FILE_SEPARATOR = "0x0f";
  /**
   * 换行符
   */
  private static final String RETURNLINE = System.getProperty("line.separator");
  /**
   * 存储模板数据
   */
  private static Map<String, TemplateBean> templateBeanMap = new HashMap<>(16);

  @Override
  public void templatePersistence(TemplateBean templateBean) {
    String content = JSON.toJSONString(templateBean);
    log.info("template content is {}", content);
    templatePersistence(FILEPATH, content.concat(FILE_SEPARATOR).concat(RETURNLINE));
    templateBeanMap.put(templateBean.getTempId(), templateBean);
  }

  public void templatePersistenceBak(TemplateBean templateBean) {
    String content = JSON.toJSONString(templateBean);
    log.info("template content is {}", content);
    templatePersistence(FILEPATHBAK, content.concat(FILE_SEPARATOR).concat(RETURNLINE));
    templateBeanMap.put(templateBean.getTempId(), templateBean);
  }

  @Override
  public TemplateBean readTemplateById(Template template) {
    log.info("tempId is {} ,transId is {} ", template.getTempId(), template.getTransId());
    TemplateBean oldTemplateBean = templateBeanMap.get(template.getTempId());
    TemplateBean templateBean = cloneTemplate(oldTemplateBean);
    if (templateBean != null) {
      List<RiderBean> list = templateBean.getRiderBeans();
      Map<String, String> map = template.getRider();
      if (null != map) {
        list.forEach(rider -> {
          String value = map.get(rider.getOrder());
          if (StringUtils.isNotEmpty(value)) {
            rider.setAppendixContent(value);
          } else {
            log.warn("Template id is {}, but rider Order {} value is null", template.getTempId(),
                rider.getOrder());
          }
        });
      } else {
        log.warn("template rider is null , use default template value");
      }
    } else {
      log.error("Template id does not exist ! TempId is {}", template.getTempId());
      throw new BaseRuntimeException(StatusConstants.TEMPLATEID_NULL);
    }
    return templateBean;
  }

  @Override
  public void delTemplateById(String templateId) {
    //TODO: 系统异常，会造成数据丢失,删除前先备份。
    templateRefresh();
    templateBeanMap.forEach((key, value) -> templatePersistenceBak(value));
    templateBeanMap.remove(templateId);
    delDateFile();
    templateBeanMap.forEach((key, value) -> templatePersistence(value));
  }

  @Override
  public void updateTemplateById(TemplateBean templateBean) {
    delTemplateById(templateBean.getTempId());
    if (findStringInFile(templateBean.getTempId())) {
      templatePersistence(templateBean);
    } else {
      log.error("Template id exists, please re-enter ! temID is {}", templateBean.getTempId());
      throw new BaseRuntimeException(StatusConstants.TEMPLATEID_REPEAT);
    }
  }

  @Override
  public void templateRefresh() {
    String content = readFileToString(FILEPATH);
    templateBeanMap.clear();
    if (StringUtils.isNotEmpty(content)) {
      content = content.replaceAll(RETURNLINE, "");
      String[] strings = content.split(FILE_SEPARATOR);
      for (String str : strings) {
        TemplateBean templateBean = JSON.parseObject(str, TemplateBean.class);
        templateBeanMap.put(templateBean.getTempId(), templateBean);
      }
    }
  }


  /**
   * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
   *
   * @param filePath 文件路径
   * @param content 内容
   */
  private void templatePersistence(String filePath, String content) {
    BufferedWriter out = null;
    File file = new File(filePath);
    if (!file.getParentFile().exists()) {
      log.info("template outPath is not exists, create it ... {}", file.getParentFile());
      file.getParentFile().mkdirs();
    }
    try {
      out = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(filePath, true)));
      out.write(content);
    } catch (Exception e) {
      log.error("file persistence fail ! ", e);
      throw new BaseRuntimeException(StatusConstants.PERSISTENCE_ERROR);
    } finally {
      try {
        out.close();
      } catch (IOException ignored) {
      }
    }
  }

  /**
   * 一次读取全都文件到内存
   *
   * @param fileName 文件名称
   * @return 内容
   */
  private String readFileToString(String fileName) {
    File file = new File(fileName);
    FileInputStream in = null;
    if (!file.exists()) {
      log.error("template file does not exist !");
      return "";
    }
    Long fileLength = file.length();
    byte[] fileContent = new byte[fileLength.intValue()];
    try {
      in = new FileInputStream(file);
      in.read(fileContent);
    } catch (IOException e) {
      log.error("read file fail ! ", e);
      throw new BaseRuntimeException(StatusConstants.PERSISTENCE_ERROR);
    } finally {
      try {
        in.close();
      } catch (IOException e) {
      }
    }
    try {
      return new String(fileContent, SystemConstants.ENCODING);
    } catch (UnsupportedEncodingException e) {
      log.error("The OS does not support {}", SystemConstants.ENCODING);
      throw new BaseRuntimeException(StatusConstants.PERSISTENCE_ERROR);
    }
  }

  /**
   * 将模板数据存储到内存，操作内存中对象
   *
   * @param templateBean 模板数据
   * @return 内存数据
   */
  private TemplateBean cloneTemplate(TemplateBean templateBean) {
    TemplateBean template = null;
    if (null != templateBean) {
      template = (TemplateBean) templateBean.clone();
    }
    return template;
  }

  /**
   * 删除模板数据文件
   */
  private void delDateFile() {
    File file = new File(FILEPATH);
    if (file.exists()) {
      log.info("del data file...");
      FileUtils.deleteQuietly(file);
    }
  }

  /**
   * 判断文件是否存在模板ID
   *
   * @param temId 模板ID
   */
  public static boolean findStringInFile(String temId) {
    File file = new File(FILEPATH);
    //考虑到编码格式
    InputStreamReader read = null;
    try {
      read = new InputStreamReader(new FileInputStream(file), SystemConstants.ENCODING);
      BufferedReader bufferedReader = new BufferedReader(read);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.startsWith(FILE_SEPARATOR)) {
          continue;
        }
        //指定字符串判断处
        if (line.contains(temId)) {
          return false;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        read.close();
      } catch (IOException e) {
      }
    }
    return true;
  }

}