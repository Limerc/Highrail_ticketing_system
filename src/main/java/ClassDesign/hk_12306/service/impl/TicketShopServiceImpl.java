package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.TicketShopMapper;
import ClassDesign.hk_12306.service.TicketShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TicketShopServiceImpl implements TicketShopService {
    @Autowired
    private TicketShopMapper ticketShopMapper;
    @Override
    public List<Map<String, Object>> getTicketList(String begin_station_name, String arrive_station_name, Date start_date) {
        List<Map<String, Object>> ticketlist = ticketShopMapper.getTicketList(begin_station_name, arrive_station_name, start_date);
        return ticketlist;
     }

    @Override
    public void buyTicket(Integer u_id, Integer h_id, Integer begin_station_interval, Integer arrive_station_interval, String begin_station, String arrive_station, Double fee) {
        ticketShopMapper.buyTicket(u_id, h_id, begin_station_interval, arrive_station_interval, begin_station, arrive_station, fee);
    }


}
