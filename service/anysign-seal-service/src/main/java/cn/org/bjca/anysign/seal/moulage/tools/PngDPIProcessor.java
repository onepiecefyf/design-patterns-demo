package cn.org.bjca.anysign.seal.moulage.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: PngDPIProcessor.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/27 14:48
 ***************************************************************************/
public class PngDPIProcessor {

  /**
   * 1英寸是2.54CM
   */
  private static final double INCH_2_CM = 2.54d;

  public boolean canHandle(String fileName) {
    assert fileName != null : "fileName should not be null";
    return fileName.endsWith("png") || fileName.endsWith("PNG");
  }

  public byte[] process(BufferedImage image, int dpi) throws IOException {
    for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext(); ) {
      ImageWriter writer = iw.next();
      ImageWriteParam writeParam = writer.getDefaultWriteParam();
      ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier
          .createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
      IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
      if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
        continue;
      }
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      ImageOutputStream stream = null;
      try {
        setDPI(metadata, dpi);
        stream = ImageIO.createImageOutputStream(output);
        writer.setOutput(stream);
        writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
      } finally {
        try {
          stream.close();
        } catch (IOException e) {
        }
      }
      return output.toByteArray();
    }
    return null;
  }

  private void setDPI(IIOMetadata metadata, int dpi) throws IIOInvalidTreeException {
    // for PMG, it's dots per millimeter
    double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;
    IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
    horiz.setAttribute("value", Double.toString(dotsPerMilli));
    IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
    vert.setAttribute("value", Double.toString(dotsPerMilli));
    IIOMetadataNode dim = new IIOMetadataNode("Dimension");
    dim.appendChild(horiz);
    dim.appendChild(vert);
    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
    root.appendChild(dim);
    metadata.mergeTree("javax_imageio_1.0", root);
  }

}