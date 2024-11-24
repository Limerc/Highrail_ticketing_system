package ClassDesign.hk_12306.service;

import ClassDesign.hk_12306.pojo.UserList;

public interface UserListService {

    // 根据电话查询用户
    UserList findByPhone(String phone);

    // 注册用户
    void register(String username,String password,String phone,String ID_number);

    // 根据身份证号查询用户
    UserList findByIDNumber(String ID_number);

    // 更新用户信息
    void update(UserList user);

    // 更新密码
    void updatePwd(String new_pwd);
}
