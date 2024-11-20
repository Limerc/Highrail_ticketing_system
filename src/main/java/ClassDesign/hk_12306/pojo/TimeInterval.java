package ClassDesign.hk_12306.pojo;

import lombok.Data;

import java.time.Duration;

@Data
public class TimeInterval {
    private Integer tId;
    private String beginStationId;
    private String arriveStationId;
    private String lineId;
    // 使用Java 8中的Duration类型来映射数据库中的INTERVAL DAY TO MINUTE类型
    private Duration beginTime;
    private Duration arriveTime;
}
