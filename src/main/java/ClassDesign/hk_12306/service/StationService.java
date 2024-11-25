package ClassDesign.hk_12306.service;

import java.util.Date;

public interface StationService {

    String getStationName(String s_id);

    String getBeginTime(String t_id);

    String getArrivalTime(String t_id);
}
