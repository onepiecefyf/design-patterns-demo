package cn.org.bjca.anysign.seal.moulage.initialization;

import cn.org.bjca.anysign.seal.moulage.convert.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <pre>系统启动加载数据模板数据到内存</pre>
 *
 * @author july_whj
 * @文件名称: TemplateRunner.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.template
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/8 11:50
 ***************************************************************************/
@Component
@Order(1)
@Slf4j
public class TemplateRunner implements CommandLineRunner {

    @Autowired
    private ITemplateService templateService;

    @Override
    public void run(String... strings) throws Exception {
        templateService.templateRefresh();
        log.info("Template data loaded successfully...");
    }
}