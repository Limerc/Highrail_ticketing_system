package ClassDesign.hk_12306.controller;

import ClassDesign.hk_12306.pojo.Result;
import ClassDesign.hk_12306.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/name")
    public Result getStationName(@RequestParam String s_id) {
        String sname = stationService.getStationName(s_id);
        if(sname == null){
            return Result.error("No such station");
        }
        return Result.success(sname);
    }

    @GetMapping("/beginTime")
    public Result getBeginTime(@RequestParam String t_id){
        String beginTime = stationService.getBeginTime(t_id);
        if(beginTime == null){
            return Result.error("No such train");
        }
        return Result.success(beginTime);
    }

    @GetMapping("/arrivalTime")
    public Result getArrivalTime(@RequestParam String t_id){
        String arrivalTime = stationService.getArrivalTime(t_id);
        if(arrivalTime == null){
            return Result.error("No such train");
        }
        return Result.success(arrivalTime);
    }
}
