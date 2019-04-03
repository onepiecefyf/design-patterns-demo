package cn.org.bjca.anysign.seal.moulage;

import java.io.OutputStream;
import java.util.List;

public interface ISealImage {

  public void createSeal(OutputStream outputStream, List<String> imageTexts, String centerFlag,
      Float width,
      Float height, boolean equalProportion) throws Exception;

  public String genSvg(List<String> imageTexts, String centerFlag) throws Exception;

}
