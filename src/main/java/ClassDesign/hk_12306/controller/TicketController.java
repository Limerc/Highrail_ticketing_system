package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.OrderForm;
import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.service.TicketService;
import ClassDesign.hk_12306.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @GetMapping
    public Result<List<OrderForm>> getTickets(@RequestBody Map<String,Object> map){
//        Map<String, Object> map = JwtUtil.parseToken(token);
//        Integer u_id = (Integer) map.get("u_id");
        System.out.println("u_id: " + map.get("u_id"));
        Integer u_id = (Integer) map.get("u_id");
        List<OrderForm> orderForms = ticketService.getTickets(u_id);
        return Result.success(orderForms);
    }
}
