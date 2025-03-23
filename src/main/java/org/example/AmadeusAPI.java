package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class AmadeusAPI {
    private static final String API_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";

    public static String buscarVuelos(String origen, String destino, String fecha) {
        try {
            // Obtener un nuevo token antes de cada consulta (duran media hora)
            String accessToken = AmadeusAuth.getAccessToken();
            if (accessToken == null) {
                return "❌ Error: No se pudo obtener el token de acceso.";
            }

            String urlString = API_URL + "?originLocationCode=" + origen +
                    "&destinationLocationCode=" + destino +
                    "&departureDate=" + fecha +
                    "&adults=1&nonStop=false&max=10";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // OK
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return procesarRespuesta(response.toString());
            } else {
                return "❌ Error al consultar la API: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Hubo un error al obtener los vuelos.";
        }
    }

    private static String procesarRespuesta(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray vuelos = obj.getJSONArray("data");

        if (vuelos.length() == 0) {
            return "No se encontraron vuelos disponibles.";
        }

        JSONObject vuelo = vuelos.getJSONObject(0); // Tomamos el primer resultado
        JSONObject itinerario = vuelo.getJSONArray("itineraries").getJSONObject(0);
        JSONObject segmento = itinerario.getJSONArray("segments").getJSONObject(0);

        String aerolinea = vuelo.getJSONArray("validatingAirlineCodes").getString(0);
        String precio = vuelo.getJSONObject("price").getString("total");
        String moneda = vuelo.getJSONObject("price").getString("currency");
        String salida = segmento.getJSONObject("departure").getString("at");
        String llegada = segmento.getJSONObject("arrival").getString("at");

        return "✈️ Vuelo encontrado:\n"
                + "🔹 Aerolínea: " + aerolinea + " \n"
                + "💰 Precio: " + precio + " " + moneda + " \n"
                + "🛫 Salida: " + salida + " \n"
                + "🛬 Llegada: " + llegada;
    }

}
