package ClassDesign.hk_12306.mapper;

import ClassDesign.hk_12306.pojo.OrderForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Select("SELECT * FROM order_form WHERE u_id = #{u_id}")
    List<OrderForm> getTickets(Integer u_id);

}
