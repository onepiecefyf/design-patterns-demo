package cn.org.bjca.anysign.cloud.template.util;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/***************************************************************************
 * <pre>JSON与XML转换工具</pre>
 * @文件名称: JsonXmlConvertUtil
 * @包路径: cn.org.bjca.anysign.seal.template.util
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/12 17:06
 ***************************************************************************/
public class JsonXmlConvertUtil {

  /**
   * <p>Xml字符串转Json</p>
   *
   * @param xmlStr xml字符串
   */
  public static String xml2Json(String xmlStr) {
    StringReader input = new StringReader(xmlStr);
    StringWriter output = new StringWriter();
    JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true)
        .prettyPrint(true).build();
    try {
      XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);
      XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);
      writer.add(reader);
      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return output.toString();
  }

  /**
   * <p>Json字符串转Xml</p>
   *
   * @param jsonStr json字符串
   */
  public static String json2Xml(String jsonStr) {
    StringReader input = new StringReader(jsonStr);
    StringWriter output = new StringWriter();
    JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false)
        .build();
    try {
      XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
      XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
      writer = new PrettyXMLEventWriter(writer);
      writer.add(reader);
      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (output.toString().length() >= 38) {//remove <?xml version="1.0" encoding="UTF-8"?>
      return output.toString().substring(39);
    }
    return output.toString();
  }
}
