package ClassDesign.hk_12306.service.impl;

import ClassDesign.hk_12306.mapper.OrderFormMapper;
import ClassDesign.hk_12306.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFormServiceImpl implements OrderFormService {
    @Autowired
    private OrderFormMapper orderFormMapper;
}
