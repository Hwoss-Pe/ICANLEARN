package com.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.pojo.Result;
import com.utils.Code;
import com.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private boolean debugModeEnabled;


    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    @Override    //目标资源方法运行前运行，返回true，放行，返回false，不放行
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        //若为调试模式则直接放行
        if (debugModeEnabled) {
            return true;
        }

        //1.获取请求url
        String url = req.getRequestURL().toString();
        log.info("请求的url:{}", url);

        //2.判断请求url是否包含login,若包含,说明是登录操作,放行
        if (url.contains("login") || url.contains("register") || url.contains("resetPassword") || url.contains("api")) {
            log.info("登录操作,放行...");
            return true;
        }

        //3.获取请求头中的令牌(token)
        String jwt = req.getHeader("token");

        //4.判断令牌是否存在,若不存在,返回错误结果(未登录)
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空,返回未登录的信息");
            Result result = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(result);
            resp.getWriter().write(notLogin);
            return false;
        }

        //5.解析token,若解析失败,返回错误结果(未登录)
        try {
            JwtUtils.parseJwt(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败,返回未登录错误信息");
            Result error = Result.error(Code.LOGIN_ERR, "NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行
        log.info("令牌合法,放行");
        return true;
    }

    @Override   //目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override  //视图渲染完毕后运行，最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
