package trackme.utils.maps;

import org.jxmapviewer.google.GoogleMapsTileFactoryInfo;
import trackme.utils.maps.api.MapApiConfig;

public class CustomGoogleMapsTileFactoryInfo extends GoogleMapsTileFactoryInfo {
    private static final String key = MapApiConfig.GOOGLE_MAP_API_KEY;

    public CustomGoogleMapsTileFactoryInfo() {
        super(key);
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        zoom = this.getTotalMapZoom() - zoom;
        double xtile = (double)x + 0.5D;
        double ytile = (double)y + 0.5D;
        double n = Math.pow(2.0D, zoom);
        double lon_deg = xtile / n * 360.0D - 180.0D;
        double lat_rad = Math.atan(Math.sinh(3.141592653589793D * (1.0D - 2.0D * ytile / n)));
        double lat_deg = lat_rad * 180.0D / 3.141592653589793D;
        return baseURL
                + "?center=" + lat_deg + "," + lon_deg
                + "&zoom=" + zoom
                + "&key=" + key
                + "&maptype=roadmap"
                + "&size=" + 256 + "x" + 256;
    }


}
