package ClassDesign.hk_12306.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderForm {
    // 对应表中的o_id字段，使用与数据库类型对应的Java类型
    // （这里假设数据库自增类型对应Java的Integer类型，可根据实际情况调整）
    private Integer oId;
    private Integer uId;
    private Integer hId;
    private Integer beginTid;
    private Integer arriveTid;
    private String beginStation;
    private String arriveStation;
    private Date orderTime;
    // 对应数据库中的布尔类型字段，使用Java的Boolean类型
    private Boolean ticketChange;
    private Boolean isRefund;
    private Double fee;
}
