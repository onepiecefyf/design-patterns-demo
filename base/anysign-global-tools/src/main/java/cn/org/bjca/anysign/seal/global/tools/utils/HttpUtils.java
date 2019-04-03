package cn.org.bjca.anysign.seal.global.tools.utils;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/***************************************************************************
 * <pre>http工具类</pre>
 *
 * @文件名称: HttpUtils.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/13 15:22
 ***************************************************************************/
@Slf4j
public class HttpUtils {

  private static PoolingHttpClientConnectionManager connMgr;
  private static RequestConfig requestConfig;
  private static final int MAX_TIMEOUT = 120000000;
  private static final String ENDCODE = "UTF-8";

  static {
    // 设置连接池
    connMgr = new PoolingHttpClientConnectionManager();
    // 设置连接池大小
    connMgr.setMaxTotal(100);
    connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

    RequestConfig.Builder configBuilder = RequestConfig.custom();
    // 设置连接超时
    configBuilder.setConnectTimeout(MAX_TIMEOUT);
    // 设置读取超时
    configBuilder.setSocketTimeout(MAX_TIMEOUT);
    // 设置从连接池获取连接实例的超时
    configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
    // 在提交请求之前 测试连接是否可用
    configBuilder.setExpectContinueEnabled(true);
    requestConfig = configBuilder.build();
  }

  /**
   * 发送 GET 请求（HTTP），不带输入数据
   */
  public static String doGet(String url) {
    return doGet(url, new HashMap<String, String>(16), new HashMap<String, String>(16));
  }

  /**
   * 发送 GET 请求（HTTP），K-V形式
   */
  public static String doGet(String url, Map<String, String> params) {
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      URIBuilder builder = new URIBuilder(url);
      for (String key : params.keySet()) {
        builder.addParameter(key, params.get(key));
      }
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
          .setSocketTimeout(5000).build();
      HttpGet request = new HttpGet(builder.build());
      request.setConfig(requestConfig);
      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpStatus.SC_OK) {
        log.error("[ERROR]: {}", EntityUtils.toString(entity));
        throw new BaseRuntimeException(StatusConstants.FAIL);
      }
      return EntityUtils.toString(entity);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 发送 GET 请求（HTTP），K-V形式
   */
  public static byte[] doGetByte(String url, Map<String, String> params) {
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      URIBuilder builder = new URIBuilder(url);
      for (String key : params.keySet()) {
        builder.addParameter(key, params.get(key));
      }
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
          .setSocketTimeout(5000).build();
      HttpGet request = new HttpGet(builder.build());
      request.setConfig(requestConfig);
      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpStatus.SC_OK) {
        log.error("[ERROR]: {}", EntityUtils.toString(entity));
        throw new BaseRuntimeException(StatusConstants.FAIL, EntityUtils.toString(entity));
      }
      return EntityUtils.toByteArray(entity);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * http get 请求
   *
   * @param url 请求地址
   * @param params 请求参数
   * @param header 请求头
   * @return String
   */
  public static String doGet(String url, Map<String, String> params, Map<String, String> header) {
    String apiUrl = url;
    StringBuffer param = new StringBuffer();
    int i = 0;
    for (String key : params.keySet()) {
      if (i == 0) {
        param.append("?");
      } else {
        param.append("&");
      }
      param.append(key).append("=").append(params.get(key));
      i++;
    }
    apiUrl += param;
    String result = null;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpGet httpGet = new HttpGet(apiUrl);
      for (String key : header.keySet()) {
        httpGet.setHeader(key, header.get(key));
      }
      HttpResponse response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        result = EntityUtils.toString(entity, ENDCODE);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * http get 请求
   *
   * @param url 请求地址
   * @param token token
   */
  public static String doGet(String url, String token) {
    String apiUrl = url;
    String result = null;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpGet httpGet = new HttpGet(apiUrl);
      httpGet.setHeader("X-Auth-Token", token);
      HttpResponse response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        result = EntityUtils.toString(entity, ENDCODE);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 发送 POST 请求（HTTP），JSON形式
   *
   * @param json json对象
   */
  public static JSONObject doPost(String apiUrl, JSONObject json) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse resp = null;
    JSONObject response = null;
    try {
      httpPost.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(json.toString(), ENDCODE);
      stringEntity.setContentEncoding(ENDCODE);
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      resp = httpClient.execute(httpPost);
      HttpEntity entity = resp.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
      log.info("http post request is {}", httpStr);
      response = JSONObject.parseObject(httpStr);
    } catch (IOException e) {
      log.error("http 请求异常 ！", e);
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }

  /**
   * http post 上传文件
   *
   * @param serverUrl 服务地址
   * @param fileName 文件名称
   * @param fileBytes 文件流
   * @param params 参数
   */
  public static String post(String serverUrl, String fileName, byte[] fileBytes,
      Map<String, String> params)
      throws IOException {
    HttpPost httpPost = new HttpPost(serverUrl);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
    // 上传的文件
    builder.addBinaryBody("file", inputStream,
        ContentType.APPLICATION_OCTET_STREAM, fileName);
    // 设置其他参数
    for (Map.Entry<String, String> entry : params.entrySet()) {
      builder.addTextBody(entry.getKey(), entry.getValue(),
          ContentType.TEXT_PLAIN.withCharset("UTF-8"));
    }
    HttpEntity httpEntity = builder.build();
    httpPost.setEntity(httpEntity);
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpPost);
    if (null == response || response.getStatusLine() == null) {
      log.info("Post Request For Url[{}] is not ok. Response is null", serverUrl);
      return null;
    } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      log.info("Post Request For Url[{}] is not ok. Response Status Code is {}", serverUrl,
          response.getStatusLine().getStatusCode());
      return null;
    }
    if (null != inputStream) {
      inputStream.close();
    }
    return EntityUtils.toString(response.getEntity());
  }


  /**
   * http post 上传文件
   *
   * @param serverUrl 服务地址
   * @param fileParamName 文件名称
   * @param file 文件路径
   * @param params 参数
   */
  public static String post(String serverUrl, String fileParamName, File file,
      Map<String, String> params)
      throws IOException {
    HttpPost httpPost = new HttpPost(serverUrl);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    // 上传的文件
    builder.addBinaryBody(fileParamName, new FileInputStream(file),
        ContentType.APPLICATION_OCTET_STREAM, file.getName());
    // 设置其他参数
    for (Map.Entry<String, String> entry : params.entrySet()) {
      builder.addTextBody(entry.getKey(), entry.getValue(),
          ContentType.TEXT_PLAIN.withCharset("UTF-8"));
    }
    HttpEntity httpEntity = builder.build();
    httpPost.setEntity(httpEntity);
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpPost);
    if (null == response || response.getStatusLine() == null) {
      log.info("Post Request For Url[{}] is not ok. Response is null", serverUrl);
      return null;
    } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      log.info("Post Request For Url[{}] is not ok. Response Status Code is {}", serverUrl,
          response.getStatusLine().getStatusCode());
      return null;
    }
    return EntityUtils.toString(response.getEntity());
  }

  /**
   * http post 上传文件
   *
   * @param serverUrl 服务地址
   * @param fileParamName 文件名称
   * @param file 文件路径
   */
  public static String post(String serverUrl, String fileParamName, File file)
      throws IOException {
    HttpPost httpPost = new HttpPost(serverUrl);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    // 上传的文件
    builder.addBinaryBody(fileParamName, file);
    HttpEntity httpEntity = builder.build();
    httpPost.setEntity(httpEntity);
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpPost);
    if (null == response || response.getStatusLine() == null) {
      log.info("Post Request For Url[{}] is not ok. Response is null", serverUrl);
      return null;
    } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      log.info("Post Request For Url[{}] is not ok. Response Status Code is {}", serverUrl,
          response.getStatusLine().getStatusCode());
      return null;
    }
    return EntityUtils.toString(response.getEntity());
  }


  /**
   * http post 上传文件
   *
   * @param serverUrl 服务地址
   * @param fileParamName 文件名称
   * @param fileBytes 文件字节数组
   */
  public static String post(String serverUrl, String fileParamName, byte[] fileBytes)
      throws IOException {
    HttpPost httpPost = new HttpPost(serverUrl);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    // 上传的文件
    builder.addBinaryBody(fileParamName, fileBytes);
    HttpEntity httpEntity = builder.build();
    httpPost.setEntity(httpEntity);
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpPost);
    if (null == response || response.getStatusLine() == null) {
      log.info("Post Request For Url[{}] is not ok. Response is null", serverUrl);
      return null;
    } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      log.info("Post Request For Url[{}] is not ok. Response Status Code is {}", serverUrl,
          response.getStatusLine().getStatusCode());
      return null;
    }
    return EntityUtils.toString(response.getEntity());
  }

  public static String doPost2(String apiUrl, List<BasicNameValuePair> listParams) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse response = null;
    try {
      httpPost.setConfig(requestConfig);
      UrlEncodedFormEntity urlFormEntity = new UrlEncodedFormEntity(listParams);
      httpPost.setEntity(urlFormEntity);
      response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return httpStr;
  }

  /**
   * 发送 POST 请求（HTTP），JSON形式 ，
   */
  public static JSONObject doPost(String apiUrl, JSONObject json, Map<String, String> header) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse resp = null;
    JSONObject response = null;
    try {
      httpPost.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(json.toString(), ENDCODE);
      stringEntity.setContentEncoding(ENDCODE);
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      for (String key : header.keySet()) {
        httpPost.setHeader(key, header.get(key));
      }
      resp = httpClient.execute(httpPost);
      log.info("resp: {}", resp);
      HttpEntity entity = resp.getEntity();
      if (entity == null || entity.getContentLength() <= 0) {
        response = new JSONObject();
      } else {
        httpStr = EntityUtils.toString(entity, ENDCODE);
        response = JSONObject.parseObject(httpStr);
        log.info("httpStr : {}", httpStr);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }


  public static void doPost(String apiUrl, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse response = null;
    try {
      httpPost.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(token, ENDCODE);
      httpPost.setEntity(stringEntity);
      response = httpClient.execute(httpPost);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static String doPost2(String apiUrl, JSONObject json, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse resp = null;
    try {
      httpPost.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(json.toString(), ENDCODE);
      stringEntity.setContentEncoding(ENDCODE);
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      httpPost.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpPost);
      HttpEntity entity = resp.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return httpStr;
  }

  public static String doPost2(String apiUrl, String xml) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPost httpPost = new HttpPost(apiUrl);
    CloseableHttpResponse response = null;
    try {
      httpPost.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(xml, ENDCODE);
      httpPost.setEntity(stringEntity);
      response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return httpStr;
  }

  /**
   * 发送 PUT 请求（HTTP），JSON形式 ，带身份令牌的head
   */
  public static JSONObject doPut(String apiUrl, String token, JSONObject json) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPut httpPut = new HttpPut(apiUrl);
    CloseableHttpResponse resp = null;
    JSONObject response = null;
    try {
      httpPut.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(json.toString(), ENDCODE);
      stringEntity.setContentEncoding(ENDCODE);
      stringEntity.setContentType("application/json");
      httpPut.setEntity(stringEntity);
      httpPut.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpPut);
      HttpEntity entity = resp.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
      response = JSONObject.parseObject(httpStr);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }

  public static JSONObject doPut(String apiUrl, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String httpStr = null;
    HttpPut httpPut = new HttpPut(apiUrl);
    CloseableHttpResponse resp = null;
    JSONObject response = null;
    try {
      httpPut.setConfig(requestConfig);
      // 解决中文乱码问题
      StringEntity stringEntity = new StringEntity(token.toString(), ENDCODE);
      stringEntity.setContentEncoding(ENDCODE);
      stringEntity.setContentType("application/json");
      httpPut.setEntity(stringEntity);
      httpPut.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpPut);
      HttpEntity entity = resp.getEntity();
      httpStr = EntityUtils.toString(entity, ENDCODE);
      response = JSONObject.parseObject(httpStr);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }

  /**
   * 发送 DELETE 请求（HTTP），JSON形式 ，带身份令牌的head
   */
  public static void deleteCloud(String url, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpDelete httpDelete = new HttpDelete(url);
    CloseableHttpResponse resp = null;
    httpDelete.setConfig(requestConfig);
    try {
      httpDelete.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpDelete);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 发送 DELETE 请求（HTTP），JSON形式 ，带身份令牌的head
   */
  public static JSONObject doDelete(String url, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpDelete httpDelete = new HttpDelete(url);
    CloseableHttpResponse resp = null;
    httpDelete.setConfig(requestConfig);
    String httpStr = null;
    JSONObject jsonObject = null;
    try {
      httpDelete.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpDelete);
      HttpEntity entity = resp.getEntity();
      if (entity == null || entity.getContentLength() == 0) {
        jsonObject = new JSONObject();
        jsonObject.put("state", "删除成功！");
      } else {
        httpStr = EntityUtils.toString(entity, ENDCODE);
        jsonObject = JSONObject.parseObject(httpStr);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
          resp.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return jsonObject;
  }

  /**
   * 发送 DELETE 请求（HTTP），
   */
  public static JSONObject delete(String url, String token) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpDelete httpDelete = new HttpDelete(url);
    CloseableHttpResponse resp = null;
    httpDelete.setConfig(requestConfig);
    String httpStr = null;
    JSONObject jsonObject = null;
    try {
      httpDelete.setHeader("X-Auth-Token", token);
      resp = httpClient.execute(httpDelete);
      HttpEntity entity = resp.getEntity();
      if (entity == null) {
        jsonObject = null;
      } else {
        httpStr = EntityUtils.toString(entity, ENDCODE);
        jsonObject = JSONObject.parseObject(httpStr);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (resp != null) {
        try {
          EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return jsonObject;
  }
}