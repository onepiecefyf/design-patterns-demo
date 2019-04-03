package cn.org.bjca.anysign.seal.template;

import cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImage;
import cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImageFactory;
import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import cn.org.bjca.anysign.seal.moulage.tools.PngDPIProcessor;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImage.buildText;

/***************************************************************************
 * <pre></pre>
 *
 * @文件名称: ${FILE_NAME}
 * @包 路   径：  cn.org.bjca.anysign.seal.template
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/9 9:18
 ***************************************************************************/
public class SvgImage {

    public static void main(String[] args) throws IOException {
        SealImageBean sealImageBean = new SealImageBean();
        sealImageBean.setStar(true);
        sealImageBean.setShape(ShapeEnum.rectangle);
        sealImageBean.setBorderWeight(4);
        sealImageBean.setCanvasWidth(192);
        sealImageBean.setCanvasHeight(100);
        sealImageBean.setBorderColor("#ff0000");
        List<Map<String, Object>> txtDescs = new LinkedList<Map<String, Object>>();
        Map<String, Object> topTxt = buildText("北京市规划和国土资源管理委员会", "#ff0000", 16, "公章刻章字体", "bold", "M1,28 194,28");
//        Map<String, Object> topTxt = buildText(name, "#ff0000", 25, "公章刻章字体", "bold", path);
        Map<String, Object> centerTxt1 = buildText("规划国土审批文件附图专用章", "#ff0000", 18, "公章刻章字体", "bold", "M7,56 189,56");
        Map<String, Object> centerTxt2 = buildText("（XX）规土XX字xx号", "#ff0000", 17, "公章刻章字体", "bold", "M5.0,82 189,82");
        txtDescs.add(topTxt);
        txtDescs.add(centerTxt1);
        txtDescs.add(centerTxt2);
        sealImageBean.setTxtDescs(txtDescs);
        long start = System.currentTimeMillis();
        byte[] image = SealImageFactory.creatSealImage(sealImageBean);
        long end = System.currentTimeMillis();

        System.out.println((end - start));
        FileUtils.writeByteArrayToFile(new File("E:\\1803.png"), image);
        SealImage sealImage = new SealImage();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        String svg = "<svg contentScriptType=\"text/ecmascript\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "     baseProfile=\"full\" zoomAndPan=\"magnify\" contentStyleType=\"text/css\" preserveAspectRatio=\"xMidYMid meet\"\n" +
                "     width=\"945.0px\" height=\"709.0px\"\n" +
                "     version=\"1.0\">\n" +
                "    <ellipse stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" cx=\"473\" cy=\"354\" rx=\"460\" ry=\"343\" stroke=\"rgb(255,0,0)\"\n" +
                "             stroke-width=\"24\"/>\n" +
                "    <defs>\n" +
                "        <path id=\"d01ba558-e00b-4889-8c40-aa50bdc67a64\" d=\"M170,470 A355,225 0 1,1,775,470\"/>\n" +
                "    </defs>\n" +
                "    <text style=\"fill:#ff0000;\" font-size=\"85\" font-family=\"公章刻章字体\" font-weight=\"bold\">\n" +
                "        <textPath xlink:href=\"#d01ba558-e00b-4889-8c40-aa50bdc67a64\" text-anchor=\"middle\" startOffset=\"50%\"\n" +
                "                  lengthAdjust=\"spacing\">北京数字 认 证 股 份 有限公司\n" +
                "        </textPath>\n" +
                "    </text>\n" +
                "    <defs>\n" +
                "        <path id=\"c361ade4-a371-4cc9-afe0-0f223e82397c\" d=\"M155,375 L810,375\"/>\n" +
                "    </defs>\n" +
                "    <text style=\"fill:#ff0000;\" font-size=\"70\" font-family=\"arial\" >\n" +
                "        <textPath xlink:href=\"#c361ade4-a371-4cc9-afe0-0f223e82397c\" font-stretch=\"condensed\" text-anchor=\"middle\" startOffset=\"50%\"\n" +
                "                  lengthAdjust=\"spacing\">91110108722619411AX\n" +
                "        </textPath>\n" +
                "    </text>\n" +
                "    <defs>\n" +
                "        <path id=\"678bf14f-cb85-4c61-8008-2e14da97d769\" d=\"M165,550 L800,550\"/>\n" +
                "    </defs>\n" +
                "    <text style=\"fill:#ff0000;\" font-size=\"110\" font-family=\"公章刻章字体\" font-weight=\"bold\">\n" +
                "        <textPath xlink:href=\"#678bf14f-cb85-4c61-8008-2e14da97d769\" text-anchor=\"middle\" startOffset=\"50%\"\n" +
                "                  lengthAdjust=\"spacing\">发票专用章\n" +
                "        </textPath>\n" +
                "    </text>\n" +
                "</svg>";
        try {
            sealImage.creatImage(bao, svg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtils.writeByteArrayToFile(new File("E:\\test\\1804.png"), bao.toByteArray());

        File imageFile = new File("E:\\test\\1804.png");
        BufferedImage bid = null;
        bid = ImageIO.read(imageFile);

        PngDPIProcessor pngDPIProcessor = new PngDPIProcessor();
        byte[] bytes = pngDPIProcessor.process(bid, 600);
        FileUtils.writeByteArrayToFile(new File("E:\\test\\1804_600.png"), bytes);
    }

}