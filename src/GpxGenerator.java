import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;

public class GpxGenerator extends JFrame {

    private JPanel mainPanel;
    private JXMapViewer jxMapViewer;
    private JComboBox<String> comboMapType;

    public GpxGenerator() {
        initFrame();
        initMapView();
        initComboMapType();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GpxGenerator gpxGenerator = new GpxGenerator();
            gpxGenerator.setVisible(true);
        });
    }

    private void initFrame() {
        setContentPane(mainPanel);
        setTitle("Gpx Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initMapView() {
        OSMTileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jxMapViewer.setTileFactory(tileFactory);
        GeoPosition geoPosition = new GeoPosition(37.7749, -122.4194);
        jxMapViewer.setAddressLocation(geoPosition);
        jxMapViewer.setZoom(12);

        PanMouseInputListener mouseMove = new PanMouseInputListener(jxMapViewer);
        jxMapViewer.addMouseListener(mouseMove);
        jxMapViewer.addMouseMotionListener(mouseMove);
        jxMapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jxMapViewer));

    }

    private void initComboMapType() {
        comboMapType.addActionListener(event -> {
            TileFactoryInfo info;
            int index = comboMapType.getSelectedIndex();
            info = switch (index) {
                case 1 -> new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
                case 2 -> new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
                case 3 -> new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
                default -> new OSMTileFactoryInfo();
            };
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            jxMapViewer.setTileFactory(tileFactory);
        });
    }
}
