package ClassDesign.hk_12306.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TicketShopMapper {

    @Select("SELECT * FROM LIST_DIRECT_TRAIN(#{begin_station_name}, #{arrive_station_name}, #{start_date});")
    List<Map<String, Object>> getTicketList(String begin_station_name, String arrive_station_name, Date start_date);

    @Insert("INSERT INTO purchase_ticket_view (user_id, h_id, begin_tid, arrive_tid, begin_station, arrive_station, fee) " +
            "VALUES (#{u_id}, #{h_id}, #{begin_station_interval}, #{arrive_station_interval}, #{begin_station}, #{arrive_station}, #{fee});")
    void buyTicket(Integer u_id, Integer h_id, Integer begin_station_interval, Integer arrive_station_interval,
                   String begin_station, String arrive_station, Double fee);
}
