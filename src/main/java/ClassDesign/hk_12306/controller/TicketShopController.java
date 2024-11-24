package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.pojo.Ticket;
import ClassDesign.hk_12306.service.TicketShopService;
import ClassDesign.hk_12306.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticketshop")
public class TicketShopController {
    @Autowired
    private TicketShopService ticketShopService;
    @GetMapping
    public Result<List<Map<String, Object>>> getTickets(@RequestBody Map<String,String> params){
        // 直达查票
        String dateStr = params.get("start_date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start_date = sdf.parse(dateStr);
            List<Map<String, Object>> ticketlist = ticketShopService.getTicketList(params.get("begin_station_name"),
                    params.get("arrive_station_name"),start_date);
            return Result.success(ticketlist);
        }catch (Exception e){
            return Result.error("日期格式错误");
        }
    }


    @PostMapping
    public Result purchaseTicket(@RequestHeader(name = "Authorization") String token, @RequestBody @Validated Ticket ticket){
        // 直达购票
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer u_id = (Integer) map.get("u_id");

//        // 将Duration转换为小时，支持小数
//        double hours = ticket.getDuration().toMinutes() / 60.0;
//        // 计算价格(每小时100元)
//        double price = hours * 100;
//        // 保留两位小数
//        double fee =  BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 将前端的 HH:mm:ss 转换为 Duration
        String durationStr = ticket.getDuration();
        String[] parts = durationStr.split(":");
        Duration duration = Duration.ofHours(Long.parseLong(parts[0]))
                .plusMinutes(Long.parseLong(parts[1]))
                .plusSeconds(Long.parseLong(parts[2]));

        // 转换为小时
        double hours = duration.toMinutes() / 60.0;

        // 计算价格
        double price = hours * 100;
        double fee = BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 购票
        ticketShopService.buyTicket(u_id, ticket.getH_id(), ticket.getBegin_station_interval(),
                ticket.getArrive_station_interval(), ticket.getBegin_station(), ticket.getArrive_station(), fee);
        return Result.success(fee);
    }

}
