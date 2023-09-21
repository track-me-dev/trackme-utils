package trackme.utils.maps.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import trackme.utils.maps.api.NaverMapApiResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

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

    public List<RoutingData> doRouting(double[] from, double[] to, List<double[]> via) {
        StringBuilder apiUrl = new StringBuilder(
                String.format(NAVER_DIRECTION_API_URL + "?start=%f,%f&goal=%f,%f&option=%s",
                from[1], from[0], to[1], to[0], "trafast"));
        if (!via.isEmpty()) {
            StringJoiner sj = new StringJoiner(URLEncoder.encode("|", StandardCharsets.UTF_8));
            via.forEach(v -> {
                sj.add(v[1] + "," + v[0]);
            });
            apiUrl.append("&waypoints=");
            apiUrl.append(sj);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl.toString());
        request.setHeader("X-NCP-APIGW-API-KEY-ID", NAVER_MAP_API_KEY_ID);
        request.setHeader("X-NCP-APIGW-API-KEY", NAVER_MAP_API_KEY);
        try {
            NaverMapApiResponse naverMapApiResponse = httpClient.execute(request, httpResponse ->
                    objectMapper.readValue(httpResponse.getEntity().getContent(), NaverMapApiResponse.class)
            );
            return naverMapApiResponse.getRoute().get("trafast").stream()
                    .map(this::toRoutingData)
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RoutingData toRoutingData(NaverMapApiResponse.Route route) {
        return new RoutingData(route.getSummary().getDistance(), route.getPath());
    }


}
