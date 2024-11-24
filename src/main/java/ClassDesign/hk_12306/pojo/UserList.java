package ClassDesign.hk_12306.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class UserList {
    @NotNull
    @JsonProperty("u_id")
    private Integer uId; // 对应自增ID，使用Integer类型接收自增的整数值
    private String username; // 客户姓名
    @JsonIgnore //把对象序列化时(转换为JSON)，忽略该属性
    private String password;

    @NotEmpty
    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;

    @NotEmpty
    @Pattern(regexp = "^\\d{17}[0-9Xx]$")
    @JsonProperty("IDNumber")   // 自定义json传参属性名
    private String IDNumber; // 按照Java命名规范，属性名修改为驼峰命名法
    private Boolean isAdmin; // 是否为管理员

}
