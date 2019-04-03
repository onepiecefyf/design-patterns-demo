package cn.org.bjca.anysign.seal.moulage.template.impl;

import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.moulage.tools.PngDPIProcessor;
import cn.org.bjca.anysign.seal.moulage.template.base.BaseImpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/***************************************************************************
 * <pre>模板实现</pre>
 *
 * @author july_whj
 * @文件名称: TemplateImpression.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 17:48
 ***************************************************************************/
@Slf4j
@Service
public class TemplateImpression extends BaseImpression {

    @Override
    public byte[] creatrImage(TemplateBean templateBean) {
        return creatrImage(templateBean, DPI);
    }

    @Override
    public byte[] creatrImage(TemplateBean templateBean, int dpi) {
        verification(templateBean);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        String svg = getSvg(templateBean);
        ByteArrayInputStream bin = null;
        PngDPIProcessor process = new PngDPIProcessor();
        byte[] bytes;
        try {
            creatImage(bao, svg);
            bin = new ByteArrayInputStream(bao.toByteArray());
            BufferedImage bi = ImageIO.read(bin);
            bytes = process.process(bi, dpi);
        } catch (Exception e) {
            log.error("印模生成失败！", e);
            throw new BaseRuntimeException(StatusConstants.IMAGE_ERROR, null, "生成印章");
        } finally {
            try {
                bin.close();
            } catch (IOException ignored) {
            }
            try {
                bao.close();
            } catch (IOException ignored) {
            }
        }
        return bytes;
    }

}