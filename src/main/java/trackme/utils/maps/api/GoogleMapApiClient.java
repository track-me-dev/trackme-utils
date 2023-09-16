package trackme.utils.maps.api;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;

public class GoogleMapApiClient {

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        GoogleMapApiClient mapApiClient = new GoogleMapApiClient();
        mapApiClient.request();
    }

    public void request() throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(MapApiConfig.GOOGLE_MAP_API_KEY)
                .build();

        DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .originPlaceId("ChIJpeKhoOiffDUR58XwBLzu4qE")
                .destinationPlaceId("ChIJr6AMqqakfDURXANuMHX7RNs")
                .mode(TravelMode.DRIVING) // Specify the travel mode
                .await();

        DirectionsRoute[] routes = directionsResult.routes;

        for (DirectionsRoute route : routes) {
            DirectionsLeg leg = route.legs[0]; // Assuming one leg for simplicity
            System.out.println("Distance: " + leg.distance);
            System.out.println("Duration: " + leg.duration);
            System.out.println(route.overviewPolyline.toString());
        }
    }

}
