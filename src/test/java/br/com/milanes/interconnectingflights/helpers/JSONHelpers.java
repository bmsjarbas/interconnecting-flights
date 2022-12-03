package br.com.milanes.interconnectingflights.helpers;

import java.io.IOException;
import java.io.InputStream;

public class JSONHelpers {
    public static String getJson(String path) {
        try {
            InputStream jsonStream = JSONHelpers.class.getClassLoader().getResourceAsStream(path);
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
