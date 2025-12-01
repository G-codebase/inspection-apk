package com.example.myapss.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class apiClient {

    private static final String BASE_URL = "http://192.168.1.100:3000/api"; // Update with your backend IP

    public static String login(String email, String password) throws Exception {
        URL url = new URL(BASE_URL + "/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public static String register(String email, String password, String fullName) throws Exception {
        URL url = new URL(BASE_URL + "/auth/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"fullName\":\"" + fullName + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public static String createInspection(String token, String jsonData) throws Exception {
        URL url = new URL(BASE_URL + "/inspections/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public static String getInspections(String token) throws Exception {
        URL url = new URL(BASE_URL + "/inspections/list");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        return readResponse(conn);
    }

    public static String savePage2(String token, String inspectionId, String jsonData) throws Exception {
        URL url = new URL(BASE_URL + "/inspections/page2/" + inspectionId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public static String savePage3(String token, String inspectionId, String jsonData) throws Exception {
        URL url = new URL(BASE_URL + "/inspections/page3/" + inspectionId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(conn);
    }

    public static String submitInspection(String token, String inspectionId) throws Exception {
        URL url = new URL(BASE_URL + "/inspections/submit/" + inspectionId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        return readResponse(conn);
    }

    private static String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
