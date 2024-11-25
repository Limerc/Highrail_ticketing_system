CREATE OR REPLACE FUNCTION handle_ticket_delete()
RETURNS TRIGGER AS
$$
DECLARE
    begin_tid_ref INT4; -- 起点时间区间 ID
    arrive_tid_ref INT4; -- 终点时间区间 ID
    train_id_ref INT4; -- 列车编号
BEGIN
    -- 获取需要退票记录的起点区间、终点区间和列车编号
    SELECT begin_tid, arrive_tid, h_id
    INTO begin_tid_ref, arrive_tid_ref, train_id_ref
    FROM order_form   -- 购票订单
    WHERE o_id = OLD.order_id;    -- delete为触发条件

    -- 如果订单已经退票，则拒绝操作
    IF OLD.is_refund THEN
        RAISE NOTICE 'Ticket for this order has already been refunded.';
        RETURN NULL; -- 存在多条退票记录，不抛出异常
    END IF;

    -- 标记订单为退票
    UPDATE order_form
    SET is_refund = TRUE
    WHERE o_id = OLD.order_id;

    -- 将车次相关区间的余票加回
    UPDATE train_interval
    SET tickets_left = tickets_left + 1
    WHERE h_id = train_id_ref AND t_id BETWEEN begin_tid_ref AND arrive_tid_ref;    -- 直达车票情况下，区间号有序

    -- 阻止实际删除
    RETURN NULL; -- INSTEAD OF DELETE 触发器不允许删除记录
END;
$$ LANGUAGE PLPGSQL;


CREATE TRIGGER ticket_delete_trigger
INSTEAD OF DELETE ON purchase_ticket_view
FOR EACH ROW
EXECUTE procedure handle_ticket_delete();

-- 使用user_id、h_id、begin_tid、arrive_tid定位订单
delete from purchase_ticket_view
where user_id = 0
        and h_id = 3913
        and begin_tid = 83800
        and arrive_tid = 83801;

-- 也可以直接用order_id定位订单
delete from purchase_ticket_view
where order_id = 0;