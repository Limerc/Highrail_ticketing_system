package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.TicketMapper;
import ClassDesign.hk_12306.pojo.OrderForm;
import ClassDesign.hk_12306.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;
    @Override
    public List<OrderForm> getTickets(Integer u_id) {
        List<OrderForm> orderForms = ticketMapper.getTickets(u_id);
        return orderForms;
    }
}
