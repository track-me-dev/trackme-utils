package trackme.utils.maps.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverMapApiResponse {

    private Integer code;
    private String message;
    private Map<String, List<Route>> route; // key: option code, value: Route

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
    public static class Route {

        private Summary summary; // 탐색된 경로의 요약 정보
        private List<double[]> path; // 경로를 구성하는 모든 좌표열 [lng, lat]
        private List<Section> section; // 해당 경로를 구성하는 주요 도로에 관한 정보열 (모든 경로를 포함하는 정보는 아님)
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        private ResponsePosition start; // 출발지
        private ResponsePosition goal; // 목적지
        private List<ResponsePosition> waypoints; // 경유지
        private Integer distance; // 전체 경로 거리 (meters)
        private Integer duration; // 전체 경로 소요 시간 (milliseconds)
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponsePosition {
        private double[] location; // [lng, lat]
        private Integer dir; // 0: 전방, 1: 왼쪽, 2: 오른쪽
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section {
        private Integer pointIndex; // 경로를 구성하는 좌표 인덱스
        private Integer pointCount; // 형상점 수
        private Integer distance; // 거리 (meters)
        private String name; // 도로명
        private Integer speed; // 평균속도 (km/h)
    }
}
