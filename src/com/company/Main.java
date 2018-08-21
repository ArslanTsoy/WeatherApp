package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static String urlBegin = "http://api.openweathermap.org/data/2.5/weather?q=";
    final static String urlEnd = "&units=metric&APPID=6bab4d6713adbf3a428b1f2a7454395d";

    private static String getCity(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Write city: ");
        return sc.next().toLowerCase();
    }

    private static URLConnection getConnect(URL url){
        URLConnection connect = null;
        try {
            connect = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    private static String getWeather(URLConnection urlConnect){
        InputStream iS = null;
        String line = "";
        try {
            iS = urlConnect.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iS));
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    private static String[] weatherMain(String weatherLine){
        String[] main = new String[2];
        Pattern weatMain = Pattern.compile("main\\\"\\:\\\"(\\w*)\\W");
        Pattern weatMainDescription = Pattern.compile("description\\\"\\:\\\"(.*)\\\"\\,\\\"icon");
        Matcher m = weatMain.matcher(weatherLine);
        if(m.find()) {
            main[0] = m.group(1);
        }
        m = weatMainDescription.matcher(weatherLine);
        if(m.find()) {
            main[1] = m.group(1);
        }

        return main;
    }

    private static float[] weatherTemp(String weatherLine){
        float[] temp = new float[5];
        Pattern tempPattern = Pattern.compile("temp\\\"\\:(.*)\\,\\\"pressure");
        Pattern pressurePattern = Pattern.compile("pressure\\\"\\:(.*)\\,\\\"humidity");
        Pattern tempMinPattern = Pattern.compile("temp_min\\\"\\:(.*)\\,\\\"temp_max");
        Pattern tempMaxPattern = Pattern.compile("temp_max\\\"\\:(.*)\\}\\,\\\"visibility");


        Matcher m = tempPattern.matcher(weatherLine);
        if (m.find()) temp[0] = Float.valueOf(m.group(1));

        m = pressurePattern.matcher(weatherLine);
        if (m.find()) temp[1] = Float.valueOf(m.group(1));

        m = tempMinPattern.matcher(weatherLine);
        if (m.find()) temp[3] = Float.valueOf(m.group(1));

        m = tempMaxPattern.matcher(weatherLine);
        if (m.find()) temp[4] = Float.valueOf(m.group(1));

        return temp;
    }
    public static void main(String[] args) throws MalformedURLException {

	    String city = getCity();
	    URL urlFinal = new URL(urlBegin + city + urlEnd);
        URLConnection connect = getConnect(urlFinal);
        String weatherLine = getWeather(connect);
        System.out.println(weatherLine);
        String[] main = weatherMain(weatherLine);
        System.out.println(main[1]);
        float [] temperature = weatherTemp(weatherLine);
        System.out.println(temperature[0]);
    }
}
