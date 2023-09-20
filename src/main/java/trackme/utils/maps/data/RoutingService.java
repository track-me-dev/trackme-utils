package trackme.utils.maps.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import trackme.utils.maps.api.NaverMapApiResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static trackme.utils.maps.api.MapApiConfig.*;

public class RoutingService {

    private static RoutingService instance;
    private ObjectMapper objectMapper;

    public static RoutingService getInstance() {
        if (instance == null) {
            instance = new RoutingService();
            instance.objectMapper = new ObjectMapper();
        }
        return instance;
    }

    public List<RoutingData> routing(double fromLat, double fromLng, double toLat, double toLng) {
        String apiUrl = String.format(NAVER_DIRECTION_API_URL + "?start=%f,%f&goal=%f,%f&option=$s",
                fromLng, fromLat, toLng, toLat, "trafast");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-NCP-APIGW-API-KEY-ID", NAVER_MAP_API_KEY_ID)
                .header("X-NCP-APIGW-API-KEY", NAVER_MAP_API_KEY)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                NaverMapApiResponse naverMapApiResponse = objectMapper.readValue(response.body(), NaverMapApiResponse.class);
                return naverMapApiResponse.getRoute().get("traoptimal").stream()
                        .map(this::toRoutingData)
                        .toList();
            } else {
                System.err.println("Request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private RoutingData toRoutingData(NaverMapApiResponse.Route route) {
        return new RoutingData(route.getSummary().getDistance(), route.getPath());
    }


}
