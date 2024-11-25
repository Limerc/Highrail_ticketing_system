-- 创建普通索引
CREATE INDEX time_interval_line_id_idx ON time_interval(LINE_ID);

-- 创建聚集索引
CLUSTER time_interval USING time_interval_line_id_idx;

CREATE OR REPLACE FUNCTION TRANSIT_SCHEDULES(BEGIN_STATION VARCHAR(255), ARRIVE_STATION VARCHAR(255), DEPARTURE_TIME DATE)
RETURNS TABLE(H_ID1 INT4, TICKET_LEFT1 INT8, H_ID2 INT4, TICKET_LEFT2 INT8, TRANSIT_TRAIN_ID VARCHAR(15)) AS
$$
DECLARE
        BEGIN_ID VARCHAR(15);
        END_ID VARCHAR(15);
        H_ID_1 INT4;
        H_ID_2 INT4;
        L_1 VARCHAR(12);
        L_2 VARCHAR(12);
        BT_1 INTERVAL DAY TO MINUTE;
        ET_1 INTERVAL DAY TO MINUTE;
        BT_2 INTERVAL DAY TO MINUTE;
        ET_2 INTERVAL DAY TO MINUTE;
        MID_STATION VARCHAR(15); -- 中转站
        L_T_1 INT8; -- 余票1
        L_T_2 INT8; -- 余票2
        CURSOR POSSIBLE_TRAIN(B_ID VARCHAR(15), E_ID VARCHAR(15), D_T DATE)  FOR
        SELECT ONE.H_ID, ONE.line_id, X.begin_time, Y.arrive_time, 
               TWO.H_ID, TWO.line_id, U.begin_time, V.arrive_time, 
               Y.arrive_station_id
        FROM time_interval X, time_interval Y, time_interval U, time_interval V, highway_list ONE, highway_list TWO, station X_ID, station Y_ID, station U_ID, station V_ID
        WHERE X.begin_station_id = B_ID AND V.arrive_station_id = E_ID AND Y.arrive_station_id = U.begin_station_id
        AND X.line_id = Y.line_id AND X.line_id = ONE.line_id AND X.begin_time < Y.arrive_time-- x, y 为第一趟车的起点和终点
        AND U.line_id = V.line_id AND U.line_id = TWO.line_id AND U.begin_time < V.arrive_time -- u, v 为第一趟车的起点和终点
        AND EXTRACT(YEAR FROM ONE.start_time + X.begin_time) = EXTRACT(YEAR FROM D_T)  -- 第一趟车年月日符合要求
        AND EXTRACT(MONTH FROM ONE.start_time + X.begin_time) = EXTRACT(MONTH FROM D_T) 
        AND EXTRACT(DAY FROM ONE.start_time + X.begin_time) = EXTRACT(DAY FROM D_T)
        AND ONE.start_time + Y.arrive_time < TWO.start_time + U.begin_time -- 时间符合中转条件
        AND ONE.start_time + Y.arrive_time + (INTERVAL '1D') > TWO.start_time + U.begin_time -- 等待时间不超过 24h
        AND X_ID.s_id = X.begin_station_id AND X_ID.s_id NOT LIKE X_ID.s_name -- 该车站允许上下车
        AND Y_ID.s_id = Y.arrive_station_id AND Y_ID.s_id NOT LIKE Y_ID.s_name
        AND U_ID.s_id = U.begin_station_id AND U_ID.s_id NOT LIKE U_ID.s_name
        AND V_ID.s_id = V.arrive_station_id AND V_ID.s_id NOT LIKE V_ID.s_name
        AND NOT EXISTS
        (
                (
                    SELECT begin_station_id
                    FROM time_interval
                    WHERE time_interval.line_id = ONE.line_id
                    AND time_interval.begin_time >= X.begin_time AND time_interval.arrive_time <= Y.arrive_time
                    AND begin_station_id NOT LIKE Y.arrive_station_id
                    UNION
                    SELECT arrive_station_id
                    FROM time_interval
                    WHERE time_interval.line_id = ONE.line_id
                    AND time_interval.begin_time >= X.begin_time AND time_interval.arrive_time <= Y.arrive_time
                    AND arrive_station_id NOT LIKE Y.arrive_station_id
                )
                INTERSECT
                (
                    SELECT begin_station_id
                    FROM time_interval
                    WHERE time_interval.line_id = TWO.line_id
                    AND time_interval.begin_time >= U.begin_time AND time_interval.arrive_time <= V.arrive_time
                    AND begin_station_id NOT LIKE Y.arrive_station_id
                    UNION
                    SELECT arrive_station_id
                    FROM time_interval
                    WHERE time_interval.line_id = TWO.line_id
                    AND time_interval.begin_time >= U.begin_time AND time_interval.arrive_time <= V.arrive_time
                    AND arrive_station_id NOT LIKE Y.arrive_station_id
                )
        );
        
BEGIN
        SELECT s_id
        INTO BEGIN_ID
        FROM station
        WHERE s_name = BEGIN_STATION;
        
        SELECT s_id
        INTO END_ID
        FROM station
        WHERE s_name = ARRIVE_STATION;
        
        IF BEGIN_ID LIKE BEGIN_STATION THEN
                RAISE EXCEPTION '% 不允许作为起点', BEGIN_STATION;
        END IF;
        
        IF END_ID LIKE ARRIVE_STATION THEN
                RAISE EXCEPTION '% 不允许作为终点', ARRIVE_STATION;
        END IF;
        
        OPEN POSSIBLE_TRAIN(BEGIN_ID, END_ID, DEPARTURE_TIME);
        
        DROP TABLE IF EXISTS TRANSIT_SCHEDULES_RES;
        
        CREATE TEMP TABLE TRANSIT_SCHEDULES_RES(H_ID1 INT4, TICKET_LEFT1 INT8, H_ID2 INT4, TICKET_LEFT2 INT8, TRANSIT_TRAIN_ID VARCHAR(15));
        
        LOOP
        
                FETCH POSSIBLE_TRAIN INTO H_ID_1, L_1, BT_1, ET_1,
                                          H_ID_2, L_2, BT_2, ET_2,
                                                                                                                        MID_STATION;

                IF NOT FOUND THEN
                        CLOSE POSSIBLE_TRAIN;
                        EXIT;
                END IF;
        
                SELECT MIN(tickets_left)
                INTO L_T_1
                FROM time_interval, train_interval
                WHERE line_id = L_1 AND begin_time >= BT_1 AND arrive_time <= ET_1 AND h_id = H_ID_1 AND time_interval.t_id = train_interval.t_id;
                
                SELECT MIN(tickets_left)
                INTO L_T_2
                FROM time_interval, train_interval
                WHERE line_id = L_2 AND begin_time >= BT_2 AND arrive_time <= ET_2 AND h_id = H_ID_2 AND time_interval.t_id = train_interval.t_id;
        
                INSERT INTO TRANSIT_SCHEDULES_RES(H_ID1, TICKET_LEFT1, H_ID2, TICKET_LEFT2, TRANSIT_TRAIN_ID)
                VALUES(H_ID_1, L_T_1, H_ID_2, L_T_2, MID_STATION);
        END LOOP;
        
        RETURN QUERY
        SELECT * FROM TRANSIT_SCHEDULES_RES;
END;
$$ LANGUAGE PLPGSQL;

-- 使用示例
SELECT *
FROM TRANSIT_SCHEDULES('湛江西', '北京西', '2024-11-15');