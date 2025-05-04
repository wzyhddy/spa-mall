package com.net.sparrow.controller.web;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.entity.order.TradeEntity;
import com.net.sparrow.integration.AliPayIntegration;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 支付操作
 */
@Api(tags = "web支付操作", description = "web支付操作")
@RestController
@RequestMapping("/v1/web/pay")
@Validated
public class WebPayController {

    @Autowired
    private AliPayIntegration aliPayIntegration;

    /**
     * 支付接口
     *
     * @param tradeEntity 订单实体
     * @param response    响应
     * @throws Exception
     */
    @NoLogin
    @PostMapping("/doPay")
    public void doPay(@RequestBody TradeEntity tradeEntity, HttpServletResponse response) throws Exception {
        String qrUrl = aliPayIntegration.pay(tradeEntity);
        QrCodeUtil.generate(qrUrl, 300, 300, "png", response.getOutputStream());
    }
}