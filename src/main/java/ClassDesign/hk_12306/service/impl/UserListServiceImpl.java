package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.UserListMapper;
import ClassDesign.hk_12306.pojo.UserList;
import ClassDesign.hk_12306.service.UserListService;
import ClassDesign.hk_12306.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserListServiceImpl implements UserListService {
    @Autowired
    private UserListMapper userListMapper;
    //Ctrol+O重写方法快捷键
    @Override
    public UserList findByPhone(String phone) {
        UserList user = userListMapper.findByPhone(phone);
        return user;
    }

    @Override
    public void register(String username, String password, String phone, String ID_number) {
        String md5Password = Md5Util.getMD5String(password);
        userListMapper.add(username, md5Password, phone, ID_number,false);

    }
}
