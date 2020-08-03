package com.pflager;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class ConnectHttpsSimple {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.udacity.com/media/js/standalone/libs/jquery-1.10.2.min.js");
        URLConnection con = url.openConnection();
        Reader reader = new InputStreamReader(con.getInputStream());
        while (true) {
            int ch = reader.read();
            if (ch==-1) {
                break;
            }
            System.out.print((char)ch);
        }
    }
}