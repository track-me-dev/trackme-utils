package trackme.utils.maps.routing;

import java.util.List;

public class RoutingData {

    private double distance;
    private List<double[]> path; // [lat, lng]

    public RoutingData() {
    }

    public RoutingData(double distance, List<double[]> path) {
        this.distance = distance;
        this.path = path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<double[]> getPath() {
        return path;
    }

    public void setPath(List<double[]> path) {
        this.path = path;
    }
}
