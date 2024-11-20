package ClassDesign.hk_12306.pojo;

import lombok.Data;

@Data
public class TrainInterval {
    private Integer hId;
    private Integer tId;
    private Long ticketsLeft;
}
