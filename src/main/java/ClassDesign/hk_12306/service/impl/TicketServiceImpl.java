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

    @Override
    public OrderForm getTicketById(Integer o_id) {
        OrderForm orderForm = ticketMapper.getTicketById(o_id);
        return orderForm;
    }

    @Override
    public void deleteTicket(Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid) {
        ticketMapper.deleteTicket(u_id, h_id, begin_tid, arrive_tid);
    }

    @Override
    public void updateTicket(Integer o_id, Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid, Double fee) {
        ticketMapper.updateTicket(o_id, u_id, h_id, begin_tid, arrive_tid, fee);
    }
}
