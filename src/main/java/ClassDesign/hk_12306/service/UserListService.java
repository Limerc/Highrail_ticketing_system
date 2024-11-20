package ClassDesign.hk_12306.service;

import ClassDesign.hk_12306.pojo.UserList;

public interface UserListService {

    // 根据电话查询用户
    UserList findByPhone(String phone);

    void register(String username,String password,String phone,String ID_number);
}
