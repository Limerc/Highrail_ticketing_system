-- 先建立这个触发器再导入highway_list表
CREATE OR REPLACE FUNCTION CREATE_TICKET_LEFT_FUNC()
RETURNS TRIGGER AS
$$
BEGIN
        INSERT INTO train_interval(h_id, t_id, tickets_left)  -- 插入数据（车次号，时间区间号，余票量）给车次区间表（用于余票相关操作用）
        SELECT NEW.h_id, t_id, NEW.capacity   -- 新车次号，所属线路的所有时间区间号，及对应客容量
        FROM time_interval
        WHERE time_interval.line_id = NEW.line_id;   -- 车次号所属线路包括的所有时间区间
        RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

-- 
CREATE TRIGGER CREATE_TICKET_LEFT
AFTER INSERT ON highway_list   -- 对车次表进行插入操作后触发
FOR EACH ROW
EXECUTE PROCEDURE CREATE_TICKET_LEFT_FUNC();