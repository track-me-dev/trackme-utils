package trackme.utils;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import trackme.utils.maps.NaverMapsTileFactory;
import trackme.utils.maps.NaverMapsTileFactoryInfo;
import trackme.utils.waypoint.EventWaypoint;
import trackme.utils.waypoint.MyWaypoint;
import trackme.utils.waypoint.WaypointRender;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class GpxGenerator extends JFrame {

    private final Set<MyWaypoint> waypoints = new HashSet<>();

    private JPanel mainPanel;
    private JXMapViewer mapViewer;
    private JComboBox<String> comboMapType;
    private JButton buttonAddWaypoint;
    private JButton buttonClearWaypoint;
    private EventWaypoint event;

    public GpxGenerator() {
        initFrame();
        initMapView();
        initComboMapType();
        initButtons();
        event = waypoint -> JOptionPane.showMessageDialog(this, waypoint.getName());
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
        NaverMapsTileFactoryInfo tileFactoryInfo = new NaverMapsTileFactoryInfo();
        TileFactory tileFactory = new NaverMapsTileFactory(tileFactoryInfo);
        mapViewer.setTileFactory(tileFactory);
        GeoPosition initialPosition = new GeoPosition(37.7749, -122.4194);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(12);

        PanMouseInputListener mouseMove = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseMove);
        mapViewer.addMouseMotionListener(mouseMove);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

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
            mapViewer.setTileFactory(tileFactory);
        });
    }

    private void initButtons() {
        buttonAddWaypoint.addActionListener(e -> {
            addWaypoint(new MyWaypoint("Test001", event, new GeoPosition(37.2961211, -121.980907)));
            initWaypoint();
        });
        buttonClearWaypoint.addActionListener(event -> {
            clearWaypoint();
        });
    }

    private void initWaypoint() {
        WaypointPainter<MyWaypoint> wp = new WaypointRender();
        wp.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(wp);
        for (MyWaypoint d : waypoints) {
            mapViewer.add(d.getButton());
        }
    }

    private void addWaypoint(MyWaypoint waypoint) {
        for (MyWaypoint d : waypoints) {
            mapViewer.remove(d.getButton());
        }
        waypoints.add(waypoint);
        initWaypoint();
    }

    private void clearWaypoint() {
        for (MyWaypoint d : waypoints) {
            mapViewer.remove(d.getButton());
        }
        waypoints.clear();
        initWaypoint();
    }
}
