package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.pojo.UserList;
import ClassDesign.hk_12306.service.UserListService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserListService userService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password,
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
}
