package trackme.utils.waypoint;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ButtonWaypoint extends JButton {

    public ButtonWaypoint() {
        setContentAreaFilled(false);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/start-point.png")));
        icon.setImage(icon.getImage().getScaledInstance(24, 32, Image.SCALE_DEFAULT));
        setIcon(icon);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(24, 32));
    }
}
