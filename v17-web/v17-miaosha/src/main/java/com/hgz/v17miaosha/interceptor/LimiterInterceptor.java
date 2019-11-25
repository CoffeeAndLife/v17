package com.hgz.v17miaosha.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 * 限流拦截器
 */
@Component
@Slf4j
public class LimiterInterceptor implements HandlerInterceptor{

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        rateLimiter.setRate(10);

        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            log.info("获取令牌失败，限流开启....");
            return false;
        }
        log.info("获取令牌成功，进行正常的业务处理");
        return true;
    }
}
