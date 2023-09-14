package trackme.utils.waypoint;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;

public class MyWaypoint extends DefaultWaypoint {

    private String name;
    private JButton button;

    public MyWaypoint(String name, EventWaypoint event, GeoPosition coord) {
        super(coord);
        this.name = name;
        initButton(event);
    }

    private void initButton(EventWaypoint event) {
        button = new ButtonWaypoint();
        button.addActionListener(e -> {
            event.selected(this);
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }
}
