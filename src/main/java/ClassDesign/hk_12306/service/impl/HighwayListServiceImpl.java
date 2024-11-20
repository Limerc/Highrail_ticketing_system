package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.HighwayListMapper;
import ClassDesign.hk_12306.service.HighwayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HighwayListServiceImpl implements HighwayListService {
    @Autowired
    private HighwayListMapper highwayListMapper;
}
