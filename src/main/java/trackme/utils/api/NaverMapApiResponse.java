package trackme.utils.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverMapApiResponse {

    private Integer code;
    private String message;
    private Map<String, Route[]> route;

    enum OptionCode {

        TRAFAST("trafast"),
        TRACOMFORT("tracomfort"),
        TRAOPTIMAL("traoptimal"),
        TRAAVOIDTOLL("traavoidtoll"),
        TRAAVOIDCARONLY("traavoidcaronly");

        private final String value;

        OptionCode(String value) {
            this.value = value;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Route {

        private Summary summary;
        private Double[][] path;
        private Section[] section;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Summary {
        private ResponsePosition start;
        private ResponsePosition goal;
        private ResponsePosition[] waypoints;
        private Integer distance;
        private Integer duration;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ResponsePosition {
        private Double[] location; // [lng, lat]
        private Integer dir; // 0: 전방, 1: 왼쪽, 2: 오른쪽
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Section {
        private Integer pointIndex;
        private Integer pointCount;
        private Integer distance;
        private String name;
        private Integer speed;
    }
}
