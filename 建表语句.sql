
-- 用户表
create table user_list(
        u_id serial primary key,   -- 自增ID
        username varchar(255) not null,   -- 客户姓名，不必唯一
        password varchar(255) not null,
        phone varchar(255) not null unique,
        ID_number varchar(255) not null unique,  --身份证号
        Is_admin bool default False -- 是否为管理员
);

CREATE INDEX user_list_idx
ON user_list(u_id);    -- 用户ID为聚簇索引

cluster user_list using user_list_idx;

-- 车站表
create table station(
        s_id VARCHAR(15) primary key,
        s_name VARCHAR(255)
);

-- 对车次id，时间区间id的约束：指定的时间区间是否在该车次对应的线路里
CREATE OR REPLACE FUNCTION CHECK_TID(HID_CH INT4, TID_CH INT4)
RETURNS BOOL AS
$$
DECLARE
        LID_CH CHAR(12);
BEGIN
        SELECT line_id   -- 从给出的车次id，找到它所对应的线路id
        INTO LID_CH
        FROM highway_list   -- 车次表
        WHERE h_id = HID_CH;  
        
        -- 指定的时间区间在这个车次所在的线路里出现
        RETURN EXISTS(
            SELECT *
            FROM time_interval   -- 时间区间表
            WHERE line_id = LID_CH AND t_id = TID_CH   -- 该时间区间在指定线路下
         );
END;
$$LANGUAGE PLPGSQL;


-- 对车站号进行约束： 指定的车站id是否可以作为 合法的（即能够作为起点）的车站
CREATE OR REPLACE FUNCTION OPTIONAL_STATION_CHECK(S_ID_CH VARCHAR(15))
RETURNS BOOL AS
$$
BEGIN
        -- 指定的车站id是否可以作为 合法的（即能够作为起点）的车站
        RETURN EXISTS(
                SELECT *
                FROM optional_train_list   --可选作为起点的车站id
                WHERE s_id = S_ID_CH
        );
END;
$$ LANGUAGE PLPGSQL;


-- 对起始区间, 车站id的约束：指定的车站号是给定的起始时间区间起始车站
CREATE OR REPLACE FUNCTION CHECK_BEGIN_ID(BEGIN_INTERVAL INT4, STATION_ID VARCHAR(15))
RETURNS BOOL AS
$$
BEGIN
        RETURN EXISTS(
                SELECT *
                FROM time_interval   -- 时间区间表
                WHERE t_id = BEGIN_INTERVAL AND begin_station_id LIKE STATION_ID -- 指定的车站号是给定的起始时间区间起始车站
        );
END;
$$ LANGUAGE PLPGSQL;


-- 检查指定的车站号是终止区间的终止车站
CREATE OR REPLACE FUNCTION CHECK_ARRIVE_ID(ARRIVE_INTERVAL INT4, STATION_ID VARCHAR(15))
RETURNS BOOL AS
$$
BEGIN
        RETURN EXISTS(
                SELECT *
                FROM time_interval
                WHERE t_id = ARRIVE_INTERVAL AND arrive_station_id LIKE STATION_ID
        );
END;
$$ LANGUAGE PLPGSQL;


-- 判断两个时间区间id对应的时间区间，其起始时间和到达时间是否合法
CREATE OR REPLACE FUNCTION CHECK_INTERVAL_VALID(BEGIN_ID INT4, ARRIVE_ID INT4)
RETURNS BOOL AS
$$
DECLARE
        BEGIN_TIME INTERVAL DAY TO MINUTE;
        ARRIVE_TIME INTERVAL DAY TO MINUTE;
BEGIN
        SELECT time_interval.begin_time
        INTO BEGIN_TIME
        FROM time_interval
        WHERE t_id = BEGIN_ID; -- 所有以指定的起始时间区间id为标准的起始时间
        
        SELECT time_interval.arrive_time
        INTO ARRIVE_TIME
        FROM time_interval
        WHERE t_id = ARRIVE_ID;  -- 所有以指定的终止时间区间id为标准的到达时间
        
        RETURN BEGIN_TIME < ARRIVE_TIME; -- 所有以指定的终止时间区间id为标准的到达时间
END;
$$ LANGUAGE PLPGSQL;


-- 线路表
CREATE TABLE LINE_LIST(
        LINE_ID CHAR(12) PRIMARY KEY    -- 线路ID
);

-- 车次表
create table highway_list(
        h_id serial primary key,   -- 自增主码， 车次id
        h_name VARCHAR(5) NOT NULL,   -- 名称
        line_id CHAR(12) not null,   -- 线路id， 外码
        capacity int8 not null,   -- 客容量
        start_time date not null,   -- 发车时间
        FOREIGN KEY(LINE_ID) REFERENCES LINE_LIST(LINE_ID)
);

create index highway_list_idx
on highway_list(line_id);   -- 为车次表的路线ID数据项创建索引，方便路线的查询

-- 购票订单表
create table order_form(
        o_id serial primary key,    -- 自增主码，订单号ID
        u_id int4 not null,   --user id
        h_id int4 not null,   --车次 ID，  highway_list id 
        begin_tid int4 not null, -- begin 时间区间id   time_interval
        arrive_tid int4 not null, -- arrive 时间区间id   time_interval
        begin_station VARCHAR(15) not null,  -- begin 车站id   车站名
        arrive_station VARCHAR(15) not null,  -- arrive 车站id  车站名
        order_time date not null,  -- 订单时间
        ticket_change bool default false, -- 是否改签过
        is_refund bool default False,   -- 该订单是否退款过
        fee DECIMAL(5,2) not null,    -- 费用
                
        constraint c1 foreign key (u_id) references user_list(u_id)  -- 用户id外码
        ON DELETE CASCADE
        ON UPDATE CASCADE,
        FOREIGN KEY(H_ID) REFERENCES highway_list(H_ID),   -- 车站id外码
        CHECK(check_tid(H_ID, begin_tid)),    -- 指定的时间区间是否在该车次对应的线路里
        CHECK(check_tid(H_ID, arrive_tid)),
        -- 指定的车站id是否可以作为合法的（即能够作为起点）的车站
        CHECK(OPTIONAL_STATION_CHECK(begin_station)),           
        CHECK(OPTIONAL_STATION_CHECK(arrive_station)),
        -- 判断两个时间区间id对应的时间区间，其起始时间和到达时间是否合法        
        CHECK(CHECK_INTERVAL_VALID(begin_tid, arrive_tid)),    
         -- 对起始区间, 车站id的约束：指定的车站号是给定的起始时间区间起始车站       
        CHECK(CHECK_BEGIN_ID(begin_tid, begin_station)),     
         -- 检查指定的车站号是终止区间的终止车站       
        CHECK(CHECK_ARRIVE_ID(arrive_tid, arrive_station))   
);

create index order_form_idx
on order_form(u_id);   -- 为购票记录表的用户号数据项创建索引



-- 时间区间表
create table time_interval(
        t_id int4 PRIMARY KEY,
        begin_station_id VARCHAR(15),    -- 时间区间开始车站id
        arrive_station_id VARCHAR(15),
        line_id CHAR(12),    -- 所属线路id
        begin_time INTERVAL DAY TO MINUTE,   -- 开始时间
        arrive_time INTERVAL DAY TO MINUTE,
        FOREIGN KEY(begin_station_id) REFERENCES station(s_id),   -- 车站id外码
        FOREIGN KEY(arrive_station_id) REFERENCES station(s_id),
        FOREIGN key(line_id) REFERENCES LINE_LIST(line_id),
        CHECK(BEGIN_TIME < arrive_time)   -- 开始时间小于到达时间
);
CREATE INDEX time_interval_idx
ON time_interval(line_id, begin_station_id, arrive_station_id);   -- 路线，起始车站，终止车站三元组索引


-- 检查余票数是否正确
-- 传入车次id， 时间区间id， 余票数
CREATE OR REPLACE FUNCTION CHECK_train_interval_KEY(H_ID_CH INT4, TID_CH INT4, TICKETS_LEFT INT8)
RETURNS BOOL AS
$$
DECLARE
        SUM_OF_TICKETS INT8;
BEGIN
        -- 指定的时间区间是否在该车次对应的线路里
        IF NOT CHECK_TID(H_ID_CH, TID_CH) THEN
                RETURN FALSE;
        END IF;
        
        SELECT capacity
        INTO SUM_OF_TICKETS
        FROM highway_list   -- 指定车次的客容量
        WHERE h_id = H_ID_CH;
        
        -- 存在余票，并且余票数不大于车次的客容量，则正确
        IF TICKETS_LEFT >= 0 AND TICKETS_LEFT <= SUM_OF_TICKETS THEN
                RETURN TRUE;
        ELSE
                RETURN FALSE;
        END IF;
END;
$$ LANGUAGE PLPGSQL;


-- 车次区间表(余票)
create table train_interval(      -- 主要用于和余票有关的操作
        H_ID int4,   -- highway_list id 车次id
        T_ID int4,    -- time_interval  时间区间id
        tickets_left int8,   -- 余票
        FOREIGN KEY(H_ID) REFERENCES highway_list(H_ID)  -- 车次id外码
        ON DELETE CASCADE,
        FOREIGN KEY(T_ID) REFERENCES time_interval(T_ID)  -- 时间区间id外码
        ON DELETE CASCADE
        ON UPDATE CASCADE,
        PRIMARY KEY(H_ID, T_ID),    -- 车次id，时间区间id联合主码
        CHECK(CHECK_train_interval_KEY(H_ID, T_ID, tickets_left))   -- 检查余票数是否正确：是否小于该车次的客容量，同时该时间区间应当在该车次所属路线里
);


-- 可选起点终点列表
CREATE VIEW OPTIONAL_TRAIN_LIST
AS
SELECT *
FROM station
WHERE s_id NOT LIKE s_name;


-- 拒绝此非法操作
CREATE OR REPLACE FUNCTION DENY_OPERATION_FUNC()
RETURNS TRIGGER AS
$$
BEGIN
        -- 不执行非法操作
        RAISE EXCEPTION 'such operation is not allow on this table';
        RETURN NULL;
END;
$$ LANGUAGE PLPGSQL;

-- 禁止highway_list 更新， 车次表不允许修改
CREATE TRIGGER DENY_highway_list_UPDATE
BEFORE UPDATE ON highway_list
FOR EACH STATEMENT
EXECUTE PROCEDURE deny_operation_func();

-- drop table highway_list cascade;
-- drop table line_list cascade;
-- drop table order_form cascade;
-- drop table station cascade;
-- drop table time_interval cascade;
-- drop table train_interval CASCADE;
-- drop table user_list cascade;

