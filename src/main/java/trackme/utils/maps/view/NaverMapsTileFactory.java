package trackme.utils.maps.view;

import org.jxmapviewer.viewer.AbstractTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import trackme.utils.maps.api.MapApiConfig;

import java.net.URLConnection;

public class NaverMapsTileFactory extends AbstractTileFactory {
    /**
     * Creates a new instance of DefaultTileFactory using the specified TileFactoryInfo
     *
     * @param info a TileFactoryInfo to configure this TileFactory
     */
    public NaverMapsTileFactory(TileFactoryInfo info) {
        super(info);
    }

    @Override
    protected void addCustomRequestProperties(URLConnection connection) {
        connection.addRequestProperty("X-NCP-APIGW-API-KEY-ID", MapApiConfig.NAVER_MAP_API_KEY_ID);
        connection.addRequestProperty("X-NCP-APIGW-API-KEY", MapApiConfig.NAVER_MAP_API_KEY);
    }
}
