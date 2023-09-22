package trackme.utils.maps.view;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import trackme.utils.maps.routing.RoutingData;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class JxMapViewerCustom extends JXMapViewer {

    private List<RoutingData> routingData;
    private boolean first = true;

    public List<RoutingData> getRoutingData() {
        return routingData;
    }

    public void setRoutingData(List<RoutingData> routingData) {
        this.routingData = routingData;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (routingData != null && !routingData.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Path2D p2 = new Path2D.Double();
            first = true;
            for (RoutingData d : routingData) {
                draw(p2, d);
            }
            g2.setColor(new Color(28, 23, 255));
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(p2);
            g2.dispose();
        }
    }

    private void draw(Path2D p2, RoutingData d) {
        d.getPath().forEach(p -> {
                    Point2D point = convertGeoPositionToPoint(new GeoPosition(p[0], p[1]));
                    if (first) {
                        first = false;
                        p2.moveTo(point.getX(), point.getY());
                    } else {
                        p2.lineTo(point.getX(), point.getY());
                    }
                });
    }
}
