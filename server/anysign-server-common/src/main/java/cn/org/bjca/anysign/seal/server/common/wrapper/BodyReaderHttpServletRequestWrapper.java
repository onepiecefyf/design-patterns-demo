package cn.org.bjca.anysign.seal.server.common.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/***************************************************************************
 * <pre>获取body流信息</pre>
 *
 * @author july_whj
 * @文件名称: BodyReaderHttpServletRequestWrapper.class
 * @包 路   径：  cn.org.bjca.anysign.seal.server.common.wrapper
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/12 17:41
 ***************************************************************************/
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] body;

  public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);
    String sessionStream = getBodyString(request);
    body = sessionStream.getBytes(Charset.forName("UTF-8"));
  }

  /**
   * 获取请求Body
   */
  public String getBodyString(final ServletRequest request) {
    StringBuilder sb = new StringBuilder();
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      inputStream = cloneInputStream(request.getInputStream());
      reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
      String line = "";
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }

  /**
   * Description: 复制输入流</br>
   *
   * @return</br>
   */
  public InputStream cloneInputStream(ServletInputStream inputStream) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len;
    try {
      while ((len = inputStream.read(buffer)) > -1) {
        byteArrayOutputStream.write(buffer, 0, len);
      }
      byteArrayOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    InputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    return byteArrayInputStream;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {

    final ByteArrayInputStream bais = new ByteArrayInputStream(body);

    return new ServletInputStream() {

      @Override
      public int read() throws IOException {
        return bais.read();
      }

      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
      }
    };
  }
}