package cn.org.bjca.anysign.cloud.document.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/***************************************************************************
 * <pre>图片处理工具</pre>
 * @文件名称: ImageHandleUtil
 * @包路径: cn.org.bjca.anysign.cloud.document.utils
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/10/16 14:50
 ***************************************************************************/
public class ImageHandleUtil {

    /**
     * <p>图片拼接 （注意：必须两张图片长宽一致）</p>
     * @param imageList  要拼接的图片列表
     * @param mergeType  1 横向拼接， 2 纵向拼接
     * @return 缓冲图像
     */
    public static BufferedImage mergeImage(List<byte[]> imageList, int mergeType) {
        int len = imageList.size();
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
        }
        BufferedImage[] images = new BufferedImage[len];
        int[][] imageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                images[i] = ImageIO.read(new ByteArrayInputStream(imageList.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            imageArrays[i] = new int[width * height];
            imageArrays[i] = images[i].getRGB(0, 0, width, height, imageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (BufferedImage image : images) {
            if (mergeType == 1) { // 横向
                newHeight = newHeight > image.getHeight() ? newHeight : image.getHeight();
                newWidth += image.getWidth();
            } else if (mergeType == 2) { // 纵向
                newWidth = newWidth > image.getWidth() ? newWidth : image.getWidth();
                newHeight += image.getHeight();
            }
        }
        if (mergeType == 1 && newWidth < 1) {
            return null;
        }
        if (mergeType == 2 && newHeight < 1) {
            return null;
        }
        // 生成新图片
        BufferedImage imageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        int height_i = 0;
        int width_i = 0;
        for (int i = 0; i < images.length; i++) {
            if (mergeType == 1) {
                imageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, imageArrays[i], 0,
                        images[i].getWidth());
                width_i += images[i].getWidth();
            } else if (mergeType == 2) {
                imageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), imageArrays[i], 0,
                        newWidth);
                height_i += images[i].getHeight();
            }

        }
        return imageNew;
    }

    /**
     * <p>写入图片文件</p>
     * @param bufferedImage 缓冲图像
     * @param format 输出图片后缀
     * @param outputStream 输出图片字节流
     * @throws IOException 写异常
     */
    public static void writeBufferedImage2File(BufferedImage bufferedImage, String format, OutputStream outputStream) throws IOException {
        ImageIO.write(bufferedImage, format, outputStream);
    }

    /**
     * <p>变更图片尺寸</p>
     * @param srcImg 源图片文件
     * @param scaledWidth 宽
     * @param scaledHeight 高
     * @return 缓冲图片
     * @throws IOException 读异常
     */
    public static BufferedImage resize(byte[] srcImg, int scaledWidth, int scaledHeight) throws IOException {
        // 读取源图片
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(srcImg));

        // 创建输出图片
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // 变更源图片尺寸为输出图片尺寸
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    /**
     * <p>缩放图像（按比例缩放）</p>
     * @param srcImageFile 源图像文件地址
     * @param scale 缩放比例
     * @param scaleSelection 缩放选择:true 放大; false 缩小;
     */
    public static BufferedImage scaleByProportion(BufferedImage srcImageFile, int scale, boolean scaleSelection) {
        int width = srcImageFile.getWidth(); // 得到源图宽
        int height = srcImageFile.getHeight(); // 得到源图长
        if (scaleSelection) {// 放大
            width = width * scale;
            height = height * scale;
        } else {// 缩小
            width = width / scale;
            height = height / scale;
        }
        Image image = srcImageFile.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        return tag;
    }

    /**
     * <p>缩放图像（按高度和宽度缩放）</p>
     * @param srcImageFile 源图像文件
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param fillWhite 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static BufferedImage scaleByWidthAndHeight(BufferedImage srcImageFile, int height, int width, boolean fillWhite) {
        double ratio = 0.0; // 缩放比例
        Image itemp = srcImageFile.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // 计算比例
        if ((srcImageFile.getHeight() > height) || (srcImageFile.getWidth() > width)) {
            if (srcImageFile.getHeight() > srcImageFile.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / srcImageFile.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / srcImageFile.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            itemp = op.filter(srcImageFile, null);
        }
        if (fillWhite) {// 补白
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            if (width == itemp.getWidth(null))
                g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                        itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            else
                g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                        itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            g.dispose();
            itemp = image;
        }
        return (BufferedImage) itemp;
    }

    public static double calcProportion(int targetSize, int inputSize){
        double ratio = 0.0;
        ratio = (new Integer(targetSize)).doubleValue() / inputSize;
        return ratio;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage targetImg = ImageIO.read(new File("D:/sf/2.jpg"));
        BufferedImage inputImg = ImageIO.read(new File("D:/sf/1.jpg"));
        double ratio = ImageHandleUtil.calcProportion(targetImg.getWidth(), inputImg.getWidth());
        /*inputImg = ImageHandleUtil.scaleByProportion(inputImg, (int)ratio, true);*/
        ImageHandleUtil.scaleByWidthAndHeight(inputImg, (int) (inputImg.getHeight() * ratio), (int) (inputImg.getWidth() * ratio), true);
        ImageIO.write(inputImg, "jpg", new File("D:/sf/333.jpg"));
    }
}

