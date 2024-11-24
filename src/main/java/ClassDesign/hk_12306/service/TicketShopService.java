package ClassDesign.hk_12306.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TicketShopService {

    List<Map<String, Object>> getTicketList(String begin_station_name, String arrive_station_name, Date start_date);

    void buyTicket(Integer u_id, Integer h_id,Integer begin_station_interval,
                   Integer arrive_station_interval, String begin_station,
                   String arrive_station, Double fee);

}
