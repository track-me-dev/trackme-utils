import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;

public class GpxGenerator extends JFrame {

    private JPanel mainPanel;
    private JXMapViewer jxMapViewer;

    public GpxGenerator() {
        initComponents(); // Initialize components created by your GUI builder or manually
        init();
    }

    private void initComponents() {
        setContentPane(mainPanel);
        setTitle("Gpx Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init() {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GpxGenerator form = new GpxGenerator();
            form.setVisible(true);
        });
    }
}
