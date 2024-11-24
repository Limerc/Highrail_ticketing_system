package ClassDesign.hk_12306.mapper;

import ClassDesign.hk_12306.pojo.UserList;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserListMapper {
    //根据手机号查询用户信息
    @Select("SELECT * FROM user_list WHERE phone = #{phone}")
    UserList findByPhone(String phone);

    @Insert("INSERT INTO user_list (username, password, phone, ID_number, Is_admin) " +
            "VALUES (#{username}, #{password}, #{phone}, #{IDNumber}, #{isAdmin})")
    int add(String username, String password, String phone, String IDNumber, boolean isAdmin);

    @Select("SELECT * FROM user_list WHERE ID_number = #{IDNumber}")
    UserList findByIDNumber(String IDNumber);

    @Update("UPDATE user_list SET username = #{username}, phone = #{phone}, " +
            "ID_number = #{IDNumber} WHERE u_id = #{uId}")
    void update(UserList user);

    @Update("UPDATE user_list SET password = #{password} WHERE u_id = #{id}")
    void updatePwd(String password, Integer id);
}
