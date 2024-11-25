package ClassDesign.hk_12306.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface StationMapper {
    @Select("SELECT s_name FROM station WHERE s_id = #{s_id}")
    String getStationName(String s_id);

    @Select("SELECT begin_time FROM time_interval WHERE t_id = #{t_id}")
    String getBeginTime(String t_id);

    @Select("SELECT arrive_time FROM time_interval WHERE t_id = #{t_id}")
    String getArrivalTime(String t_id);
}
