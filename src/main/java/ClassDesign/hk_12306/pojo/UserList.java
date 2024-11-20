package ClassDesign.hk_12306.pojo;

import lombok.Data;

@Data
public class UserList {
    private Integer uId; // 对应自增ID，使用Integer类型接收自增的整数值
    private String username; // 客户姓名
    private String password;
    private String phone;
    private String IDNumber; // 按照Java命名规范，属性名修改为驼峰命名法
    private Boolean isAdmin; // 是否为管理员

}
