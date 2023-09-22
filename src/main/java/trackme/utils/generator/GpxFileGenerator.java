package trackme.utils.generator;

import trackme.utils.maps.routing.RoutingData;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GpxFileGenerator {

    public void generate(List<RoutingData> routingData, double averageSpeed) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>")
                .append("\n")
                .append("<gpx version=\"1.1\" creator=\"trackme-utils\">")
                .append("\n");

        double totalDistance = routingData.stream()
                .mapToDouble(RoutingData::getDistance)
                .sum();
        averageSpeed *= 1000;
        averageSpeed /= 3600;
        double[] prevLocation = routingData.get(0).getPath().get(0);
        LocalDateTime now = LocalDateTime.now();
        for (RoutingData data : routingData) {
            for (double[] location : data.getPath()) {
                now = now.plus(calculateTimeInterval(prevLocation, location, averageSpeed), ChronoUnit.MILLIS);
                int endIndex = now.toString().indexOf('.');
                // wpt 태그 열기
                sb.append("<wpt lat=");
                // 위치 정보
                sb.append("\"")
                        .append(location[0])
                        .append("\" lon=")
                        .append("\"")
                        .append(location[1])
                        .append("\">")
                        .append("\n");
                // 시간 정보
                sb.append("\t")
                        .append("<time>")
                        .append(now.toString(), 0, endIndex).append("Z")
                        .append("</time>")
                        .append("\n");
                // wpt 태그 닫기
                sb.append("</wpt>").append("\n");

                prevLocation = location;
            }
        }
        sb.append("</gpx>");
        write(sb.toString());
    }

    private void write(String content) {
        String filePath = "example.gpx";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(content);
            System.out.println("GPX file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculateTimeInterval(double[] prevLocation, double[] curLocation, double averageSpeed) {
        double distance = calculateDistance(prevLocation[0], prevLocation[1], curLocation[0], curLocation[1]);
        // return milliseconds
        return (long) (distance / averageSpeed * 1000);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        final double R = 6371.0;

        // Convert latitude and longitude from degrees to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance (meters)
        return R * c * 1000;
    }
}
