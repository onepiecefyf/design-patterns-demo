package cn.org.bjca.anysign.template.controller;

import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.moulage.ICreateImage;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <pre>模板服务</pre>
 *
 * @author july_whj
 * @文件名称: TemplateController
 * @包 路   径：  cn.org.bjca.anysign.template.controller
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/11 18:23
 ***************************************************************************/
@RestController
@RequestMapping("template/v1")
public class TemplateController {

    @Autowired
    private ICreateImage createImage;

    @RequestMapping("/image")
    public ObjectRestResponse<String> createImage(@RequestBody TemplateBean templateBean) {
        System.out.println(JSON.toJSONString(templateBean));
        byte[] bytes = createImage.creatrImage(templateBean);
        return new ObjectRestResponse(Base64Utils.byte2Base64StringFun(bytes));
    }

}