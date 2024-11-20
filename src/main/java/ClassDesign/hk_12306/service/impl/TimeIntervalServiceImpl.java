package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.TimeIntervalMapper;
import ClassDesign.hk_12306.service.TimeIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeIntervalServiceImpl implements TimeIntervalService {
    @Autowired
    private TimeIntervalMapper timeIntervalMapper;
}
