package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class AmadeusAuth {
    private static final String CLIENT_ID = ""; //API ID de Amadeus
    private static final String CLIENT_SECRET = ""; //API Secret de Amadeus
    private static final String TOKEN_URL = "https://test.api.amadeus.com/v1/security/oauth2/token";

    public static String getAccessToken() {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // Parámetros del body
            String params = "grant_type=client_credentials&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;

            // Escribir en la petición
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = params.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("❌ Error al obtener el token. Código HTTP: " + responseCode);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Convertir la respuesta en JSON
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Verificar si el campo "access_token" está presente
            if (!jsonResponse.has("access_token")) {
                System.out.println("❌ Respuesta de API no contiene 'access_token': " + response.toString());
                return null;
            }

            return jsonResponse.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
