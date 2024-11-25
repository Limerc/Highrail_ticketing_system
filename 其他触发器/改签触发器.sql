-- 基于update触发时机的改签触发器
CREATE OR REPLACE FUNCTION handle_ticket_update()
RETURNS TRIGGER AS
$$
DECLARE
    -- 旧票相关信息
    old_begin_tid INT4;
    old_arrive_tid INT4;
    old_train_id INT4;

    -- 新票相关信息
    new_begin_tid INT4 := NEW.begin_tid;
    new_arrive_tid INT4 := NEW.arrive_tid;
    new_train_id INT4 := NEW.h_id;

    new_order_id INT4; -- 新订单编号
    new_fee DECIMAL(5,2) := NEW.fee; -- 新票费用
BEGIN
    -- 获取旧票信息
    SELECT begin_tid, arrive_tid, h_id
    INTO old_begin_tid, old_arrive_tid, old_train_id
    FROM order_form
    WHERE o_id = OLD.order_id;

    -- 如果已改签，拒绝操作
    IF OLD.ticket_change THEN
        RAISE NOTICE 'This ticket has already been changed.';
        RETURN NULL; -- 存在多个已经退票的记录，不抛出异常
    END IF;
                
    -- 如果订单已经退票，则拒绝操作
    IF OLD.is_refund THEN
        RAISE NOTICE 'Ticket for this order has already been refunded.';
        RETURN NULL;
    END IF;
                
    IF EXISTS(
        SELECT *
        FROM order_form Y, time_interval X1_T,  time_interval Y1_T, time_interval X2_T,  time_interval Y2_T, highway_list X_H, highway_list Y_H
        WHERE Y.u_id = NEW.user_id AND Y.is_refund = FALSE
        AND X_H.h_id = NEW.h_id AND X1_T.t_id = NEW.begin_tid AND X2_T.t_id = NEW.arrive_tid
        AND Y_H.h_id = Y.h_id AND Y1_T.t_id = Y.begin_tid AND Y2_T.t_id = Y.arrive_tid
        AND (Y_H.start_time + Y1_T.begin_time) < (X_H.start_time + X2_T.arrive_time)
        AND (Y_H.start_time + Y2_T.arrive_time) > (X_H.start_time + X1_T.begin_time)
        AND Y.o_id <> OLD.order_id
    ) THEN
        RAISE NOTICE '当前订单的乘车时间和已购车票重叠';
        RETURN NULL;
    END IF;
                
    IF OLD.USER_ID = NEW.USER_ID AND OLD.h_id = NEW.h_id AND OLD.begin_tid = NEW.begin_tid AND OLD.arrive_tid = NEW.arrive_tid THEN
        RAISE NOTICE '新的车票和原来车票一样';
        RETURN NULL;
    END IF;
                
    -- 标记旧票为已改签
    UPDATE order_form
    SET ticket_change = TRUE, is_refund = TRUE
    WHERE o_id = OLD.order_id;

    -- 将旧票对应区间的余票加回
    UPDATE train_interval
    SET tickets_left = tickets_left + 1
    WHERE h_id = old_train_id AND t_id BETWEEN old_begin_tid AND old_arrive_tid;    -- 直达类型车票有效
                
    INSERT INTO order_form (o_id, u_id, h_id, begin_tid, arrive_tid, begin_station, arrive_station, ticket_change, is_refund, fee, order_time)
    VALUES (
        DEFAULT,              -- 自动生成订单编号,数据series类型
        NEW.user_id, 
        NEW.h_id, 
        NEW.begin_tid, 
        NEW.arrive_tid, 
        NEW.begin_station, 
        NEW.arrive_station, 
        TRUE,                -- 不允许再次改签
        False,                -- 默认
        NEW.fee,             -- 用户提供票价（后端凯实现）
        NOW()                -- 当前时间
    );

    -- 减少新票对应区间的余票
    UPDATE train_interval
    SET tickets_left = tickets_left - 1
    WHERE h_id = new_train_id AND t_id BETWEEN new_begin_tid AND new_arrive_tid;

    -- 返回 NULL 阻止原始更新
    RETURN NULL;
END;
$$ LANGUAGE PLPGSQL;


CREATE TRIGGER ticket_update_trigger
INSTEAD OF UPDATE ON purchase_ticket_view
FOR EACH ROW
EXECUTE procedure handle_ticket_update();

-- 改签操作,指定车次,出发时间区间,终点时间区间,费用  (必要)
update purchase_ticket_view
set user_id = 0, h_id = 3923, begin_tid = 69781, arrive_tid = 69782, fee = 600
where user_id = 0 and h_id = 2756;