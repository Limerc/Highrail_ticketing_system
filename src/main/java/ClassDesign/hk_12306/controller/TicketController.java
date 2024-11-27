package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.OrderForm;
import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.service.TicketService;
import ClassDesign.hk_12306.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @GetMapping
    public Result<List<OrderForm>> getTickets(@RequestParam Integer u_id){
        List<OrderForm> orderForms = ticketService.getTickets(u_id);
        return Result.success(orderForms);
    }

    @DeleteMapping
    public Result deleteTicket(@RequestBody Map<String,Object> map){
        Integer o_id = (Integer) map.get("o_id");
        OrderForm orderForm = ticketService.getTicketById(o_id);
        if(orderForm == null){
            return Result.error("订单不存在");
        }
        else{
            Integer u_id = orderForm.getUId();
            Integer h_id = orderForm.getHId();
            Integer begin_tid = orderForm.getBeginTid();
            Integer arrive_tid = orderForm.getArriveTid();
            Double fee = orderForm.getFee();
            ticketService.deleteTicket(u_id, h_id, begin_tid, arrive_tid);
            return Result.success(fee);
        }
    }

    @PutMapping
    public Result updateTicket(@RequestHeader(name = "Authorization") String token, @RequestBody Map<String,Object> data){
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer u_id = (Integer) map.get("u_id");
        Integer o_id = (Integer) data.get("o_id");
        Integer h_id = (Integer) data.get("h_id");
        Integer begin_tid = (Integer) data.get("begin_tid");
        Integer arrive_tid = (Integer) data.get("arrive_tid");
        String durationStr = (String) data.get("duration");

        // 查找原订单价格
        OrderForm originalOrderForm = ticketService.getTicketById(o_id);
        if (originalOrderForm == null){
            return Result.error("订单不存在");
        }
        Double originalFee = originalOrderForm.getFee();

        // 将前端的 HH:mm:ss 转换为 Duration
        String[] parts = durationStr.split(":");
        Duration duration = Duration.ofHours(Long.parseLong(parts[0]))
                .plusMinutes(Long.parseLong(parts[1]))
                .plusSeconds(Long.parseLong(parts[2]));

        // 转换为小时
        double hours = duration.toMinutes() / 60.0;

        // 计算价格
        double price = hours * 100;
        double fee = BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        ticketService.updateTicket(o_id, u_id, h_id, begin_tid, arrive_tid, fee);
        return Result.success(fee - originalFee);
    }
}
