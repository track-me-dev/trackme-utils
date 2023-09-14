package trackme.utils.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NaverMapApiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        NaverMapApiClient naverMapApiClient = new NaverMapApiClient();
        naverMapApiClient.request();
    }

    public void request() throws IOException {
        String apiKey = "YOUR_GOOGLE_MAPS_API_KEY";

        String apiUrl = String.format("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving" +
                        "?start=%f,%f" +
                        "&goal=%f,%f" +
                        "&option=$s",
                127.1058342, 37.359708,
                129.075986, 35.179470,
                "trafast");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-NCP-APIGW-API-KEY-ID", MapApi.NAVER_MAP_API_KEY_ID)
                .header("X-NCP-APIGW-API-KEY", MapApi.NAVER_MAP_API_KEY)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                NaverMapApiResponse naverMapApiResponse = objectMapper.readValue(response.body(), NaverMapApiResponse.class);
                System.out.println(naverMapApiResponse.getRoute().get("traoptimal")[0].getSummary().getStart().getLocation()[0]);
            } else {
                System.err.println("Request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
