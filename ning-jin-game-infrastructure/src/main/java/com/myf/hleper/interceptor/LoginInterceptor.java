package com.myf.hleper.interceptor;

import com.google.common.collect.Lists;
import com.myf.common.model.RequestLogDTO;
import com.myf.common.util.RequestLogUtils;
import com.myf.zouding.database.driver.RequestLogRepo;
import com.myf.zouding.database.entity.RequestLogDO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: myf
 * @CreateTime: 2023-12-17  16:48
 * @Description: LoginInterceptor
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    RequestLogRepo requestLogRepo;



    static ExecutorService requestLogThreadPoolExecutor;
    static {
        AtomicInteger requestLogThreadNumber = new AtomicInteger(1);
        requestLogThreadPoolExecutor =
                new ThreadPoolExecutor(5, 40, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(60), task -> {
                    Thread thread = new Thread(task);
                    thread.setName(
                            "SaveRequestLogThread-" + requestLogThreadNumber.incrementAndGet() + "-" + thread.getName());
                    return thread;
                });
    }

    private static final List<String> NEED_TO_LOGIN_URL_LIST = Lists.newArrayList();
    private static final List<String> NEED_NO_LOGIN_URL_LIST = Lists.newArrayList();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        requestLogThreadPoolExecutor.submit(new SaveRequestLog(request));

        return true;
    }

    private Boolean needToLogin(String url) {
        //return NEED_TO_LOGIN_URL_LIST.stream().filter(x -> url.contains(x)).findAny().isPresent();
        //todo 暂时不做登录拦截
        return false;
    }

    private Boolean needNoLogin(String url) {
        //return NEED_NO_LOGIN_URL_LIST.stream().filter(x -> url.contains(x)).findAny().isPresent();
        return true;
    }

    @AllArgsConstructor
    class SaveRequestLog implements Callable<Void> {

        private HttpServletRequest request;

        @Override
        public Void call() {
            try{
                RequestLogDTO requestLogDTO = RequestLogUtils.buildRequestLogByHttpRequest(request);
                RequestLogDO requestLogDO = new RequestLogDO();
                BeanUtils.copyProperties(requestLogDTO, requestLogDO);
                requestLogRepo.save(requestLogDO);
            }catch (Exception e){
                log.error("LoginInterceptor.call.error",e);
            }
            return null;
        }
    }
}
