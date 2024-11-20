package ClassDesign.hk_12306.mapper;

import ClassDesign.hk_12306.pojo.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StationMapper {
    //根据车站名查询车站信息
    @Select("SELECT * FROM station WHERE s_name = #{sName}")
    public Station getStationByName(String sName);

}
