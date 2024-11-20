package ClassDesign.hk_12306.mapper;

import ClassDesign.hk_12306.pojo.UserList;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserListMapper {
//    // 插入用户信息
//    @Insert("INSERT INTO user_list (username, password, phone, ID_number, Is_admin) " +
//            "VALUES (#{username}, #{password}, #{phone}, #{IDNumber}, #{isAdmin})")
//    int insertUser(UserList user);
//
//    // 根据ID查询用户信息
//    @Select("SELECT * FROM user_list WHERE u_id = #{uId}")
//    UserList getUserById(Integer uId);
//
//    // 根据身份证号查询用户信息
//    @Select("SELECT * FROM user_list WHERE ID_number = #{IDNumber}")
//    UserList getUserByCard(String IDNumber);
//
//    // 查询所有用户信息
//    @Select("SELECT * FROM user_list")
//    List<UserList> getAllUsers();
//
//    // 根据手机号更新用户信息（这里假设可以更新部分字段，比如密码等，可根据实际需求调整SQL语句）
//    @Update("UPDATE user_list SET password = #{password} WHERE phone = #{phone}")
//    int updateUserByPhone(UserList user);
//
//    // 根据手机号删除用户信息
//    @Delete("DELETE FROM user_list WHERE phone = #{phone}")
//    int deleteUserByPhone(String phone);
    //根据手机号查询用户信息
    @Select("SELECT * FROM user_list WHERE phone = #{phone}")
    UserList findByPhone(String phone);

    @Insert("INSERT INTO user_list (username, password, phone, ID_number, Is_admin) " +
            "VALUES (#{username}, #{password}, #{phone}, #{IDNumber}, #{isAdmin})")
    int add(String username, String password, String phone, String IDNumber, boolean isAdmin);
}
