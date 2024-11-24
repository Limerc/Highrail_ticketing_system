package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.pojo.UserList;
import ClassDesign.hk_12306.service.UserListService;
import ClassDesign.hk_12306.utils.JwtUtil;
import ClassDesign.hk_12306.utils.Md5Util;
import ClassDesign.hk_12306.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserListService userService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{3,16}$") String username, @Pattern(regexp = "^\\S{3,16}$") String password,
                           @Pattern(regexp = "^1[3-9]\\d{9}$") String phone, @Pattern(regexp = "^\\d{17}[0-9Xx]$") String ID_number) {
        UserList user = userService.findByPhone(phone);
        if(user == null){
            userService.register(username, password, phone, ID_number);
            return Result.success("注册成功");
        }
        else{
            return Result.error("手机号已注册");
        }
    }
    @PostMapping("/loginByPhone")
    public Result loginByPhone(@Pattern(regexp = "^1[3-9]\\d{9}$") String phone, @Pattern(regexp = "^\\S{3,16}$") String password){
        UserList user = userService.findByPhone(phone);
        if(user == null){
            return Result.error("手机号未注册");
        }
        if(Md5Util.getMD5String(password).equals(user.getPassword().trim())) {
            Map<String,Object> claims = new HashMap<>();
            claims.put("u_id",user.getUId());
            claims.put("username",user.getUsername());
            claims.put("password",user.getPassword().trim());
            claims.put("phone",user.getPhone());
            claims.put("ID_number",user.getIDNumber());
            claims.put("is_admin",user.getIsAdmin());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @PostMapping("/loginByID")
    public Result loginByID(@Pattern(regexp = "^\\d{17}[0-9Xx]$") String ID_number, @Pattern(regexp = "^\\S{3,16}$") String password){
        UserList user = userService.findByIDNumber(ID_number);
        if(user == null){
            return Result.error("身份证号未注册");
        }
        if(Md5Util.getMD5String(password).equals(user.getPassword().trim())) {
            Map<String,Object> claims = new HashMap<>();
            claims.put("u_id",user.getUId());
            claims.put("username",user.getUsername());
            claims.put("password",user.getPassword().trim());
            claims.put("phone",user.getPhone());
            claims.put("ID_number",user.getIDNumber());
            claims.put("is_admin",user.getIsAdmin());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);

        }
        return Result.success("密码错误");
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated UserList user){
        userService.update(user);
        return Result.success("更新成功");
    }

    @PatchMapping("updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params){
        //参数校验
        String old_pwd = params.get("old_pwd");
        String new_pwd = params.get("new_pwd");
        String re_pwd = params.get("re_pwd");
        if(!StringUtils.hasLength(old_pwd) || !StringUtils.hasLength(new_pwd) || !StringUtils.hasLength(re_pwd)){
            return Result.error("参数不能为空");
        }

        // 验证密码
        Map<String, Object> map = ThreadLocalUtil.get();
        if(!map.get("password").equals(Md5Util.getMD5String(old_pwd))){
            return Result.error("原密码错误");
        }

        // 验证两次新密码是否一致
        if(!new_pwd.equals(re_pwd)){
            return Result.error("两次密码不一致");
        }

        // 更新密码
        userService.updatePwd(new_pwd);

        return Result.success("密码修改成功");
    }

    @GetMapping("/userInfo")
    public Result<UserList> userInfo(@RequestHeader(name = "Authorization") String token){
        // 解码用户名
        Map<String, Object> map = JwtUtil.parseToken(token);
        String phone = (String) map.get("phone");
        UserList user = userService.findByPhone(phone);
        return Result.success(user);
    }
}
