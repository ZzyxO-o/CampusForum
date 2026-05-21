package cn.zuo.interceptor;

import cn.zuo.constant.JwtClaimsConstant;
import cn.zuo.constant.RedisConstants;
import cn.zuo.properties.JwtProperties;
import cn.zuo.utils.JwtUtil;
import cn.zuo.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取jwt令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        if (token == null) {
            //token为空，拒绝访问
            log.warn("token为空，拒绝访问");
            //设置响应状态码为401，拒绝访问
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录请先登录\"}");
            return false;
        }

        //2、校验令牌
        try {
            log.debug("jwt校验: token前缀={}...", token.substring(0, Math.min(20, token.length())));
            // 去除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            //3.判断token是否在黑名单中
            String blackToken = (String) redisTemplate.opsForValue().get(RedisConstants.TOKEN_BLACKLIST_PREFIX + token);
            if (blackToken != null) {
                //token在黑名单中，拒绝访问
                log.warn("token在黑名单中，拒绝访问");
                //设置响应状态码为401，拒绝访问
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"未登录请先登录\"}");
                return false;
            }
            //4.将用户id设置到ThreadLocal中
            ThreadLocalUtil.setCurrentId(userId);
            log.debug("当前登录用户id：{}", userId);
        }catch (Exception e) {
            log.error("jwt校验失败", e);
            //设置响应状态码为401，拒绝访问
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录请先登录\"}");
            return false;
        }
        //5、通过，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalUtil.removeCurrentId();
    }
}