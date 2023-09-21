package trackme.utils.waypoint;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ButtonWaypoint extends JButton {

    public ButtonWaypoint(MyWaypoint.PointType pointType) {
        setContentAreaFilled(false);
        ImageIcon icon = null;
        if (pointType == MyWaypoint.PointType.START) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/start-point.png")));
        } else if (pointType == MyWaypoint.PointType.END) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/end-point.png")));
        } else if (pointType == MyWaypoint.PointType.VIA) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/via-point.png")));
        }
        assert icon != null;
        icon.setImage(icon.getImage().getScaledInstance(24, 32, Image.SCALE_DEFAULT));
        setIcon(icon);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(24, 32));
    }
}
