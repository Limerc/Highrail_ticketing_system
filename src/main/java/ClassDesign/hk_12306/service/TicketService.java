package ClassDesign.hk_12306.service;

import ClassDesign.hk_12306.pojo.OrderForm;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface TicketService {

    List<OrderForm> getTickets(Integer u_id);
}
