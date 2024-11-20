package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.StationMapper;
import ClassDesign.hk_12306.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationMapper stationMapper;
}
