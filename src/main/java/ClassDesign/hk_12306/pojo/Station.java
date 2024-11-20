package ClassDesign.hk_12306.pojo;

import lombok.Data;

@Data
public class Station {
    // 对应表中的s_id字段，使用与表字段匹配的类型，这里是VARCHAR类型在Java中常用String来映射
    private String sId;
    // 对应表中的s_name字段
    private String sName;

}
