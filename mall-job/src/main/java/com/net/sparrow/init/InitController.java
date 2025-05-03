package com.net.sparrow.init;


import com.net.sparrow.service.CommonSensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.net.sparrow.util.FillUserUtil.mockCurrentUser;


/**
 * 初始化相关接口
 */
@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private CommonSensitiveWordService commonSensitiveWordService;

    /**
     * 初始化敏感词
     */
    @GetMapping("/initSensitiveWord")
    public Boolean initSensitiveWord(@RequestParam("type") Integer type, @RequestParam("filePath") String filePath) {
        return mockCurrentUser(() -> commonSensitiveWordService.initSensitiveWord(type, filePath));
    }
}
