package ClassDesign.hk_12306.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class HighwayList {
    // 对应数据库中自增类型的主键，通常用Integer类型来映射（可根据实际数据库类型对应情况调整）
    private Integer hId;
    private String hName;
    private String lineId;
    private Long capacity;
    private Date startTime;
}
