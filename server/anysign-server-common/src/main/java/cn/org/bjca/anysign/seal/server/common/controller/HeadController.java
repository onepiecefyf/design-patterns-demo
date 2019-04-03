package cn.org.bjca.anysign.seal.server.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 探测信息
 *
 * @author july_whj
 */
@RestController
public class HeadController {
    @RequestMapping(value = "/head")
    public String head() {
        return "200";
    }
}
