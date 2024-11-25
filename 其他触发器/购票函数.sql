CREATE OR REPLACE VIEW purchase_ticket_view AS
SELECT 
    o_id as order_id,          -- 订单编号
    u_id as user_id,           -- 用户编号
    h_id,              -- 列车编号
    begin_tid,         -- 起点时间区间 ID
    arrive_tid,        -- 终点时间区间 ID
    begin_station,     -- 起始站
    arrive_station,    -- 到达站
    ticket_change,     -- 是否改签
    is_refund,         -- 是否退票
    fee,               -- 票价
    order_time         -- 下单时间
FROM  order_form;

-- 购票视图上的触发器函数
CREATE OR REPLACE FUNCTION handle_ticket_purchase()
RETURNS TRIGGER AS
$$
DECLARE
    ticket_exists INT8; -- 当前余票数量
BEGIN
    IF EXISTS(
        SELECT *
        FROM order_form Y, time_interval X1_T,  time_interval Y1_T, time_interval X2_T,  time_interval Y2_T, highway_list X_H, highway_list Y_H
        WHERE Y.u_id = NEW.user_id AND Y.is_refund = FALSE
        AND X_H.h_id = NEW.h_id AND X1_T.t_id = NEW.begin_tid AND X2_T.t_id = NEW.arrive_tid
        AND Y_H.h_id = Y.h_id AND Y1_T.t_id = Y.begin_tid AND Y2_T.t_id = Y.arrive_tid
        AND (Y_H.start_time + Y1_T.begin_time) < (X_H.start_time + X2_T.arrive_time)
        AND (Y_H.start_time + Y2_T.arrive_time) > (X_H.start_time + X1_T.begin_time)
    ) THEN
        RAISE NOTICE '当前订单的乘车时间和已购车票重叠';
        RETURN NULL;  -- 为满足集体购票需求，不抛出异常
    END IF;
    -- 减少对应车次区间的余票数量
    UPDATE train_interval  
    SET tickets_left = tickets_left - 1
    WHERE h_id = NEW.h_id AND t_id BETWEEN NEW.begin_tid AND NEW.arrive_tid;   -- 该车次，该线路对应的区间号， 所有余票减1

    -- 插入购票记录到 order_form 表
    INSERT INTO order_form (o_id, u_id, h_id, begin_tid, arrive_tid, begin_station, arrive_station, ticket_change, is_refund, fee, order_time)
    VALUES (
        DEFAULT,              -- 自动生成订单编号,数据series类型
        NEW.user_id, 
        NEW.h_id, 
        NEW.begin_tid, 
        NEW.arrive_tid, 
        NEW.begin_station, 
        NEW.arrive_station, 
        False,                                                                 -- 默认
        False,                -- 默认
        NEW.fee,                     -- 用户提供票价（后端凯实现）
        NOW()                        -- 当前时间
    );

    RETURN NULL; -- INSTEAD OF INSERT 触发器不返回值
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER ticket_purchase_trigger
INSTEAD OF INSERT ON purchase_ticket_view   -- 代替在购票视图上的插入操作
FOR EACH ROW
EXECUTE procedure handle_ticket_purchase();

-- 使用样例
INSERT INTO purchase_ticket_view (user_id, h_id, begin_tid, arrive_tid, begin_station, arrive_station, fee)
VALUES (
    0,              -- 用户编号
    3913,                  -- 列车编号
    83800,                -- 起始区间编号
    83801,                -- 终点区间编号
    'FCQ',          -- 起始站
    'IZQ',         -- 到达站
    300.00              -- 票价（后端凯需要给出票价具体数值）
);