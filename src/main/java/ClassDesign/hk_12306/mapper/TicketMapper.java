package ClassDesign.hk_12306.mapper;

import ClassDesign.hk_12306.pojo.OrderForm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Select("SELECT * FROM order_form WHERE u_id = #{u_id}")
    List<OrderForm> getTickets(Integer u_id);

    @Select("SELECT * FROM order_form WHERE o_id = #{o_id}")
    OrderForm getTicketById(Integer o_id);

    @Delete("DELETE FROM purchase_ticket_view WHERE user_id = #{u_id} AND h_id = #{h_id}" +
            " AND begin_tid = #{begin_tid} AND arrive_tid = #{arrive_tid}")
    void deleteTicket(Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid);

    @Update("UPDATE purchase_ticket_view SET user_id = #{u_id}, h_id = #{h_id}, " +
            "begin_tid = #{begin_tid}, arrive_tid = #{arrive_tid}, fee = #{fee} WHERE order_id = #{o_id}")
    void updateTicket(Integer o_id, Integer u_id, Integer h_id, Integer begin_tid, Integer arrive_tid, Double fee);
}
