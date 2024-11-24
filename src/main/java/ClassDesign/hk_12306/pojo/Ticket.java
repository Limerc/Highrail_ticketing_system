package ClassDesign.hk_12306.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.Duration;

@Data
public class Ticket {
    @NotNull
    @JsonProperty("h_id")
    Integer h_id;

    @NotNull
    @JsonProperty("begin_station_interval")
    Integer begin_station_interval;

    @NotNull
    @JsonProperty("arrive_station_interval")
    Integer arrive_station_interval;

    @NotNull
    @JsonProperty("begin_station")
    String begin_station;

    @NotNull
    @JsonProperty("arrive_station")
    String arrive_station;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$", message = "Duration must match HH:mm:ss format")
    String duration;
}
