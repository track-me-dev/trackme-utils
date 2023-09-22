package trackme.utils;

import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import trackme.utils.generator.GpxFileGenerator;
import trackme.utils.maps.view.JxMapViewerCustom;
import trackme.utils.maps.routing.RoutingData;
import trackme.utils.maps.routing.RoutingService;
import trackme.utils.waypoint.EventWaypoint;
import trackme.utils.waypoint.MyWaypoint;
import trackme.utils.waypoint.WaypointRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GpxGenerator extends JFrame {

    //== location point ==//
    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private List<RoutingData> routingData = new ArrayList<>();
    private Point mousePosition;

    //== Java Swing Components ==//
    private JPanel mainPanel;
    private JxMapViewerCustom mapViewer;
    private JComboBox<String> comboMapType;
    private JButton buttonAddWaypoint;
    private JButton buttonClearWaypoint;
    private JButton buttonGenerate;
    private EventWaypoint event;
    private JPopupMenu jPopupMenu1;
    private JMenuItem menuStart;
    private JMenuItem menuVia;
    private JMenuItem menuEnd;

    //== Constructor ==//
    public GpxGenerator() {
        initFrame();
        initMapView();
        initComboMapType();
        initEvent();
        initButtons();
        initPopupMenus();
    }

    //== main method ==//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GpxGenerator gpxGenerator = new GpxGenerator();
            gpxGenerator.setVisible(true);
        });
    }

    //== initialization methods ==//
    private void initFrame() {
        setContentPane(mainPanel);
        setTitle("Gpx Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void initMapView() {
        TileFactoryInfo tileFactoryInfo = new OSMTileFactoryInfo();
        TileFactory tileFactory = new DefaultTileFactory(tileFactoryInfo);
        mapViewer.setTileFactory(tileFactory);
        GeoPosition initialPosition = new GeoPosition(37.566755, 126.97);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(3);

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
    private void initEvent() {
        event = waypoint -> JOptionPane.showMessageDialog(this, waypoint.getName());
    }
    private void initButtons() {
        buttonAddWaypoint.addActionListener(e -> {
        });
        buttonClearWaypoint.addActionListener(e -> {
            clearWaypoint();
        });
        buttonGenerate.addActionListener(e -> {
            generateGpx();
        });
    }
    private void initPopupMenus() {
        jPopupMenu1 = new JPopupMenu();
        initMenuStart();
        initMenuVia();
        initMenuEnd();
        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                mapViewerMouseReleased(evt);
            }
        });
    }
    private void initWaypoint() {
        WaypointPainter<MyWaypoint> wp = new WaypointRenderer();
        wp.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(wp);
        for (MyWaypoint d : waypoints) {
            mapViewer.add(d.getButton());
        }
        //  Routing Data
        if (waypoints.size() >= 2) {
            GeoPosition start = null;
            List<GeoPosition> via = new ArrayList<>();
            GeoPosition end = null;
            for (MyWaypoint w : waypoints) {
                if (w.getPointType() == MyWaypoint.PointType.START) {
                    start = w.getPosition();
                } else if (w.getPointType() == MyWaypoint.PointType.VIA) {
                    via.add(w.getPosition());
                } else if (w.getPointType() == MyWaypoint.PointType.END) {
                    end = w.getPosition();
                }
            }
            if (start != null && end != null) { // start와 end가 동시에 있는 경우만 경로 생성
                routingData = RoutingService.getInstance()
                        .doRouting(
                                new double[]{start.getLatitude(), start.getLongitude()},
                                new double[]{end.getLatitude(), end.getLongitude()},
                                via.stream().map(g -> new double[]{g.getLatitude(), g.getLongitude()}).toList()
                        );
                mapViewer.setRoutingData(routingData);
            }
        }
    }
    private void addWaypoint(MyWaypoint waypoint) {
        for (MyWaypoint d : waypoints) {
            mapViewer.remove(d.getButton());
        }
        // START나 END가 추가될 경우 기존 START, END 삭제
        if (waypoint.getPointType() == MyWaypoint.PointType.START
                || waypoint.getPointType() == MyWaypoint.PointType.END) {
            waypoints.removeIf(myWaypoint -> myWaypoint.getPointType() == waypoint.getPointType());
        }
        waypoints.add(waypoint);
        initWaypoint();
    }
    private void clearWaypoint() {
        for (MyWaypoint d : waypoints) {
            mapViewer.remove(d.getButton());
        }
        if (!routingData.isEmpty()) {
            routingData = new ArrayList<>(routingData);
            routingData.clear();
        }
        if (!waypoints.isEmpty()) {
            waypoints.clear();
        }
        initWaypoint();
    }

    private void generateGpx() {
        if (!routingData.isEmpty()) {
            new GpxFileGenerator().generate(routingData, 30);
        }
    }
    private void initMenuStart() {
        menuStart = new JMenuItem();
        menuStart.setText("Start");
        menuStart.addActionListener(e -> {
            GeoPosition geop = mapViewer.convertPointToGeoPosition(mousePosition);
            MyWaypoint wayPoint = new MyWaypoint("Start Location", MyWaypoint.PointType.START, event,
                    new GeoPosition(geop.getLatitude(), geop.getLongitude()));
            addWaypoint(wayPoint);
        });
        jPopupMenu1.add(menuStart);
    }
    private void initMenuVia() {
        menuVia = new JMenuItem();
        menuVia.setText("Via");
        menuVia.addActionListener(e -> {
            GeoPosition geop = mapViewer.convertPointToGeoPosition(mousePosition);
            MyWaypoint wayPoint = new MyWaypoint("Via Location", MyWaypoint.PointType.VIA, event,
                    new GeoPosition(geop.getLatitude(), geop.getLongitude()));
            addWaypoint(wayPoint);
        });
        jPopupMenu1.add(menuVia);
    }
    private void initMenuEnd() {
        menuEnd = new JMenuItem();
        menuEnd.setText("End");
        menuEnd.addActionListener(e -> {
            GeoPosition geop = mapViewer.convertPointToGeoPosition(mousePosition);
            MyWaypoint wayPoint = new MyWaypoint("End Location", MyWaypoint.PointType.END, event,
                    new GeoPosition(geop.getLatitude(), geop.getLongitude()));
            addWaypoint(wayPoint);
        });
        jPopupMenu1.add(menuEnd);
    }
    private void mapViewerMouseReleased(MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            mousePosition = evt.getPoint();
            jPopupMenu1.show(mapViewer, evt.getX(), evt.getY());
        }
    }
}
