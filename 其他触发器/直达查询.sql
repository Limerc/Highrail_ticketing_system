-- 输入起始车站名称，目的车站名称，目的到达时间
CREATE OR REPLACE FUNCTION LIST_DIRECT_TRAIN (BEGIN_STATION_ VARCHAR ( 255 ), ARRIVE_STATION_ VARCHAR ( 255 ), DEPARTURE_TIME DATE )
RETURNS TABLE (
                H_ID INT4,   -- 车次id
                H_NAME VARCHAR(5),   -- 车次名称
                BEGIN_TIME DATE,   -- 起始时间
                ARRIVE_TIME DATE,   -- 到达时间
                DURATION INTERVAL DAY TO MINUTE,   -- 持续时间
                LEFT_TICKETS INT8,   -- 剩余票数
                BEGIN_STATION VARCHAR ( 15 ),    -- 出发车站名
                ARRIVE_STATION VARCHAR ( 15 ),   -- 目的车站名
                BEGIN_TID INT4,   -- 起始时间区间id
                ARRIVE_TID INT4   -- 终点时间区间id
        ) AS 
$$ 
DECLARE
        BEGIN_ID VARCHAR ( 15 );   -- 起点站id
        END_ID VARCHAR ( 15 );    -- 终点站id
        H_ID_T INT4;   -- 列车id
        H_NAME_T VARCHAR(5);    -- 列车名称
        LEFT_TICKET INT8;   -- 余票量
        LINE_ID_T VARCHAR ( 12 );   -- 线路id 
        B_T DATE;   -- 出发时间
        E_T DATE;   -- 到达时间
        BEGIN_TID_T INT4;   -- 起始时间区间id
        ARRIVE_TID_T INT4;   -- 到达时间区间id
        B_INTER_T INTERVAL DAY TO MINUTE;   -- 时间偏移量，用于计算运行时间和过滤余票 
        E_INTER_T INTERVAL DAY TO MINUTE;   
        
        -- 可能的列车
        -- 输入起始车站id，终止车站id，到达时间
        CURSOR POSSIBLE_TRAIN ( B_ID VARCHAR ( 15 ), E_ID VARCHAR ( 15 ), D_T DATE ) FOR 
        SELECT
                highway_list.h_id,   -- 车次id
                highway_list.h_name,   -- 车次名称
                highway_list.line_id,   -- 线路id
                highway_list.start_time + X.begin_time,   -- 
                highway_list.start_time + Y.arrive_time,
                X.t_id,
                Y.t_id,
                X.begin_time,
                Y.arrive_time
        FROM
                time_interval X,
                time_interval Y,
                highway_list 
        WHERE
                        X.line_id = Y.line_id 
        AND highway_list.line_id = X.line_id -- 连接
        AND X.begin_station_id = B_ID 
        AND Y.arrive_station_id = E_ID -- x 是起点，y 是终点
        AND X.begin_time < Y.arrive_time 
        AND EXTRACT ( YEAR FROM highway_list.start_time + X.begin_time ) = EXTRACT ( YEAR FROM D_T ) -- 年月日符合要求
        AND EXTRACT ( MONTH FROM highway_list.start_time + X.begin_time ) = EXTRACT ( MONTH FROM D_T ) 
        AND EXTRACT ( DAY FROM highway_list.start_time + X.begin_time ) = EXTRACT ( DAY FROM D_T );
BEGIN
        SELECT s_id 
        INTO BEGIN_ID 
        FROM station   -- 获取起点站id
        WHERE s_name = BEGIN_STATION_;
                                
        SELECT s_id
        INTO END_ID 
        FROM station     -- 获取终点站id
        WHERE s_name = ARRIVE_STATION_;
        
        -- 如果起始车站是不能作为起始车站的，拒绝此查询操作
        IF BEGIN_ID LIKE BEGIN_STATION_ THEN
                        RAISE EXCEPTION '% 不允许作为起点', BEGIN_STATION_;
        END IF;
        
        -- 如果终止车站是不能作为终止车站的，拒绝此查询操作
        IF END_ID LIKE ARRIVE_STATION_ THEN
                        RAISE EXCEPTION '% 不允许作为终点', ARRIVE_STATION_;
        END IF;
        
        -- 打开游标
        OPEN POSSIBLE_TRAIN ( BEGIN_ID, END_ID, DEPARTURE_TIME );
        
        DROP TABLE IF EXISTS LIST_DIRECT_TRAIN_RES;   -- 清空直达车站查询表
        
        CREATE TEMP TABLE LIST_DIRECT_TRAIN_RES (  -- 临时表
                H_ID INT4,
                H_NAME VARCHAR(5),
                BEGIN_TIME DATE,
                ARRIVE_TIME DATE,
                DURATION INTERVAL DAY TO MINUTE,
                LEFT_TICKETS INT8,
                BEGIN_STATION VARCHAR ( 15 ),
                ARRIVE_STATION VARCHAR ( 15 ),
                BEGIN_TID INT4,
                ARRIVE_TID INT4 
        );
        
        LOOP
                FETCH POSSIBLE_TRAIN    -- 推动游标
                INTO H_ID_T,
                                 H_NAME_T,
                                 LINE_ID_T,
                                 B_T,
                                 E_T,
               BEGIN_TID_T,
               ARRIVE_TID_T,
                                 B_INTER_T,
                                 E_INTER_T;
        IF NOT FOUND THEN
                        CLOSE POSSIBLE_TRAIN;
                  EXIT;
        END IF;
        
        SELECT MIN(tickets_left)
        INTO LEFT_TICKET    -- 余票数
        FROM time_interval, train_interval 
        WHERE line_id = LINE_ID_T 
                AND time_interval.begin_time >= B_INTER_T
                AND time_interval.arrive_time <= E_INTER_T
                AND train_interval.h_id = H_ID_T 
                AND time_interval.t_id = train_interval.t_id;


        INSERT INTO LIST_DIRECT_TRAIN_RES (
                H_ID,
                H_NAME,
                BEGIN_TIME,
                ARRIVE_TIME,
                DURATION,
                LEFT_TICKETS,
                BEGIN_STATION,
                ARRIVE_STATION,
                BEGIN_TID,
                ARRIVE_TID
        )
        VALUES
                (H_ID_T, H_NAME_T, B_T, E_T, E_INTER_T - B_INTER_T, LEFT_TICKET, BEGIN_ID, END_ID, BEGIN_TID_T, ARRIVE_TID_T);
        
END LOOP;
RETURN QUERY SELECT
* 
FROM
        LIST_DIRECT_TRAIN_RES;

END;
$$ LANGUAGE PLPGSQL;

-- 使用举例
SELECT highway_list.h_name, LEFT_TICKETS
FROM LIST_DIRECT_TRAIN('肇庆东', '广州南', '2024-11-15') RES, highway_list   -- res是别名
WHERE RES.H_ID = highway_list.h_id;

SELECT *
FROM LIST_DIRECT_TRAIN('肇庆东', '广州南', '2024-11-15');