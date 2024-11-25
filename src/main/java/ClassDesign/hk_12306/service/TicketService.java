package ClassDesign.hk_12306.service;

import ClassDesign.hk_12306.pojo.OrderForm;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface TicketService {

    List<OrderForm> getTickets(Integer u_id);

    OrderForm getTicketById(Integer o_id);

    void deleteTicket(Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid);

    void updateTicket(Integer o_id, Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid, Double fee);
}
