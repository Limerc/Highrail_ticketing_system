package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.UserListMapper;
import ClassDesign.hk_12306.pojo.UserList;
import ClassDesign.hk_12306.service.UserListService;
import ClassDesign.hk_12306.utils.Md5Util;
import ClassDesign.hk_12306.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public UserList findByIDNumber(String ID_number) {
        UserList user = userListMapper.findByIDNumber(ID_number);
        return user;
    }

    @Override
    public void update(UserList user) {
        userListMapper.update(user);
    }

    @Override
    public void updatePwd(String new_pwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("u_id");
        userListMapper.updatePwd(Md5Util.getMD5String(new_pwd),id);
    }
}
