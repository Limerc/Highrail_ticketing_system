package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.LineListMapper;
import ClassDesign.hk_12306.service.LineListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineListServiceImpl implements LineListService {
    @Autowired
    private LineListMapper lineListMapper;
}
