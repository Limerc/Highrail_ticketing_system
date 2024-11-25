package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.StationMapper;
import ClassDesign.hk_12306.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationMapper stationMapper;
    @Override
    public String getStationName(String s_id) {
        return stationMapper.getStationName(s_id);
    }

    @Override
    public String getBeginTime(String t_id) {
        return stationMapper.getBeginTime(t_id);
    }

    @Override
    public String getArrivalTime(String t_id) {
        return stationMapper.getArrivalTime(t_id);
    }
}
