package com.hgz.v17order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("pay")
public class PayController {

    //AlipayClient
    //支付接口的开发
    @RequestMapping("generate")
    public void generatePay(String orderNo,
                            HttpServletResponse response) throws IOException {
        //1.创建一个AlipayClient对象
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016091100489896",
                "AASCBKcwggSjAgEAAoIBAQCESGpLcYK0MCFglgvkKq4LbKS+0bYao2Ha1RkqmoTMNWDfQ13/k7/771GKzL9SBHevwGsMPD9Zw92FHuPybWJjWl2iZJuvt+VywBmzqWdR5Dd0BVSfMM/ar/etSyawMd0EGOjupwCu6ANH1NfkiO8XyBSQYWkLgsLCup6P1zg/23wrHE+mDpr5f4R0a0ZGl8YKOZFqDqkJj/rY3jIyp7zoRWpnQbSpcEMclAteyuZQ6QBkVu5Y6jabucEh2DzyLdkLfpPPDphskvBxilT8MRIqNTMXCEPteSilN4Gbfn/JPpj90jrtL2IlG+y5bh6HBXpIgQSupSVarMHzlLTeNIdNAgMBAAECggEABXGZdjtYnqOwa/Ig0SX0faHd8cBdlyEMIN5OJ9OuDV1Q/D8geikRaIPDvkuIwA5RApmPjjRYgxAtaJIJ3Wsk9mO8oLc1NPkDZ0KCjEM3bkZPBeZGQjrXAu/k6GvETtHuf4Gq7E9RFuGTaPxYWh5MgJUKS5rGoOOjsjHN3quDtBT7ibqfeGJNuVlxyDEg2RYCaGx0s9JlAW6le3lR3/tNcEMidhPXAaycFD/XhBP0/oVu34/+Zr9okqftCfFtGQzSJbY3jO5nK32oIMDRnu06/gnySU6M3fcJbNTxT7M3lvXREVQUZERagCM7UGPrSwVPfToSRAKFSpLtXjZqshHLIQKBgQDwVascgbWdyLiCQWLfykUs2v71Jk059vhktl218LQpDxJUNJV4/P44tJoPkSxL4BTzCOIu2RMExbEbrGDCRtk4jk8ZP2+Uiy0HWi7SdP2iCE+XICn+5t48QHRcNfoGJuyx3hZYdO0rF8cw3Pqo0bFGiwhBLtzLALdv+PhyyaspyQKBgQCM578cQ/o/kp/LRmeyTn1cTKrLUZrRVOEoZT/oDvZioEm0OFbkTFPhIRQb5NSC2uicyD9PtTQvpZO7xw3UOpKvoC0s/0B3B2H1xYqOCaKxON+ZMAaYIIcMTJ4dE9X78VmWtnQOeth+9Hy/UGvlb2kJQn8r51xmZbUo3X2DPoozZQKBgF/RuAwAhvlmenGsQhB5Y4UTtwzkfWu71KLKtqgAVMP990/NOz7mlzDiiH3mYmPJ7nBLPWpyL4v6ibc/zcAHYTmUO2MUkJcHuicxRHOyIyFD8P+O2k9/mmEpTXsqJgnn4Py1T/FqhNhmfYs0v1cpa2V1rw/V8D+bhIdHGz8gunZJAoGAXa0MfwwqaO1f0tKhygcojJ5jxfwgwx58+lAl5m9cGkGZ/nieO/UKptdI+SpupfhHQURX4vQDF9Iqn2I8rWWrc5s3rXydLQT5eV21OazqPowjC0LEf4rQfbrKO2aeknroJqxyB9Zf7Dc6YC5lLQmeeQt+ZH/SDDl98QcflIuVcYkCgYEAnCQ95aFAPtIKjw2wZpaD1UjGKj/63nL1PteZfHiNOYVU2zXIyVcTcowtgTm1Yg7D92HxQ6bHnDCiYvnL48z647ZTa0jx3bll8WcuNoIKHwmHxcVBcFUe5ShWldF+mcZiFQ/5jULlbvrcnw/c7MC+jNIc4wNEYL6hYZQTu1V6NeU=",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "RSA2");
        //2.创建交易请求对象，用于封装业务相关的参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl("https://www.baidu.com");//TODO 頁面的跳轉，給用戶看的
        request.setNotifyUrl("http://localhost:9098/order/notifyPayResult");//异步調用，我們來判斷是否支付成功依據
        // 设置业务相关的参数
        //关键参数：out_trade_no 订单编号
        //total_amount 订单总金额
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+orderNo+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":9888," +
                "    \"subject\":\"IphoneX 256G\"," +
                "    \"body\":\"IphoneX 256G\"" +
                "  }");//填充业务参数
        //3.通过client发送请求
        String form = "";

        try {
            form = alipayClient.pageExecute(request).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    //异步回调的接口,由支付宝负责回调这个接口
    //说明：因为此时我们是在内网做实验，所以需要内网穿透工具来进行映射
    @PostMapping("notifyPayResult")
    @ResponseBody
    public void notifyPayResult(HttpServletRequest request) throws AlipayApiException {

        System.out.println("支付寶異步回調我們了！！！！！");

        //1，将异步通知中收到的所有参数都存放到map中
        Map<String, String> paramsMap = new HashMap<>();
        //所有的請求參數都會封裝到request對象中
        Map<String, String[]> sourceMap = request.getParameterMap();
        //sourceMap->paramsMap
        //value不同，String[]->String
        Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            String[] values = entry.getValue();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < values.length - 1; i++) {
                value.append(values[i]+",");
            }
            value.append(values[values.length-1]);
            //將結果賦值給paramsMap
            paramsMap.put(entry.getKey(),value.toString());
        }
        //2，采用AlipaySignature签名工具类，实现验签
        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "utf-8",
                "RSA2") ;//调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("驗簽成功！表明是支付寶發送過來的請求");

        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("驗簽失敗，不是支付寶發送過來的！");
        }
    }
}
