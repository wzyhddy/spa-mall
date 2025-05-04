package com.net.sparrow.integration;

import com.net.sparrow.entity.order.TradeEntity;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付
 */
@Component
public class AliPayIntegration {
    @Autowired
    private Config config;


    /**
     * 调用支付宝预下订单接口
     *
     * @param tradeEntity 订单实体
     * @return 二维码url
     * @throws Exception
     */
    public String pay(TradeEntity tradeEntity) throws Exception {
        Factory.setOptions(config);
        //调用支付宝的接口
        AlipayTradePrecreateResponse payResponse = Factory.Payment.FaceToFace()
                .preCreate("商城",
                        tradeEntity.getCode(),
                        tradeEntity.getPaymentAmount().toString());
        //参照官方文档响应示例，解析返回结果
        String httpBodyStr = payResponse.getHttpBody();
        JSONObject jsonObject = JSONObject.parseObject(httpBodyStr);
        return jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
    }

}