package ClassDesign.hk_12306.interceptors;

import ClassDesign.hk_12306.utils.JwtUtil;
import ClassDesign.hk_12306.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 令牌验证
        String token = request.getHeader("Authorization");
        // 验证token
        try{
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把业务数据存储到当前线程ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true; // 验证通过
        }catch (Exception e){
            //http状态码401，未登录
            response.setStatus(401);
            return false; // 验证失败
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 业务处理完成后，清除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
