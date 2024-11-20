package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.TrainIntervalMapper;
import ClassDesign.hk_12306.service.TrainIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainIntervalServiceImpl implements TrainIntervalService {
    @Autowired
    private TrainIntervalMapper trainIntervalMapper;
}
