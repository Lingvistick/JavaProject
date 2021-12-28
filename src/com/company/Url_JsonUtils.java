package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Url_JsonUtils extends Human {
    Url_JsonUtils() {
        super();
    }

    public static String parseUrl(URL url) {
        if (url == null) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String inputLine;
                try {
                    while((inputLine = in.readLine()) != null) {
                        stringBuilder.append(inputLine);
                        stringBuilder.append("\n");
                    }
                } catch (Throwable k) {
                    try {
                        in.close();
                    } catch (Throwable l) {
                        k.addSuppressed(l);
                    }

                    throw k;
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }
    }

    public static List<JSONObject> parseCurrentAPIJson(String resultJson) {
        JSONObject apiJsonObject = new JSONObject(resultJson);
        JSONObject apiArray = (JSONObject)apiJsonObject.get("response");
        JSONArray apiArrays = (JSONArray)apiArray.get("items");
        List<JSONObject> s = new ArrayList();

        for(int i = 0; i < apiArrays.length(); ++i) {
            JSONObject apiData = (JSONObject)apiArrays.get(i);
            s.add(apiData);
        }

        return s;
    }

    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
