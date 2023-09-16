package trackme.utils.maps;

import org.jxmapviewer.viewer.TileFactoryInfo;
import trackme.utils.maps.api.MapApiConfig;

public class NaverMapsTileFactoryInfo extends TileFactoryInfo {
    private static final int max = 20;
    private static final int TILE_SIZE = 256;

    public NaverMapsTileFactoryInfo() {
        super("Naver Map",
                1, max - 2, max,
                TILE_SIZE, true, true,
                MapApiConfig.NAVER_STATIC_MAP_API_URL,
                "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        return MapApiConfig.NAVER_STATIC_MAP_API_URL
                + "?center=" + x + "," + y
                + "&level=" + zoom
                + "&maptype=basic"
                + "&w=" + TILE_SIZE
                + "&h=" + TILE_SIZE;
    }
}
