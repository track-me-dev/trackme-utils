package trackme.utils;

import trackme.utils.maps.api.MapApiConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StaticMap extends JFrame {
    private JPanel mapPanel;

    public StaticMap() throws IOException, InterruptedException {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        String apiUrl = MapApiConfig.NAVER_STATIC_MAP_API_URL
                + "?center=" + -122.4194 + "," + 37.7749
                + "&level=" + 6
                + "&maptype=basic"
                + "&w=" + 256
                + "&h=" + 256
                + "&scale=" + 2;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-NCP-APIGW-API-KEY-ID", MapApiConfig.NAVER_MAP_API_KEY_ID)
                .header("X-NCP-APIGW-API-KEY", MapApiConfig.NAVER_MAP_API_KEY)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                byte[] data = readAllBytes(response.body());
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
                JLabel picLabel = new JLabel(new ImageIcon(img.getScaledInstance(600, 600, Image.SCALE_SMOOTH)));
                add(picLabel);
            } else {
                System.err.println("Request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StaticMap staticMap = null;
            try {
                staticMap = new StaticMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
            staticMap.setVisible(true);
        });
    }

    private byte[] readAllBytes(InputStream ins) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        while (true)
        {
            int n = ins.read(buf);
            if (n == -1)
                break;
            bout.write(buf, 0, n);
        }
        return bout.toByteArray();
    }
}
