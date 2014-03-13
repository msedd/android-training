package com.buschmais.xpl;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * The Weather Client retrieves weather information from the Yahoo weather API
 */
public class WeatherClient {

    private static final String yahooPlaceApisBase = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=";
    private static final String yahooapisFormat = "&format=xml";
    private static String yahooPlaceAPIsQuery;

    /**
     * Creates a Weather element and insert the weather information in it
     *
     * @param location The location, for which the waeather should be retrieved
     * @return a Weather element, containing all weather information, can be null if request failed
     */

    public static Weather getWeather(String location) {
        String uriPlace = Uri.encode(location);
        yahooPlaceAPIsQuery = yahooPlaceApisBase
                + "%22" + uriPlace + "%22"
                + yahooapisFormat;
        String woeidString = WeatherClient.getLocation(yahooPlaceAPIsQuery);

        Weather weather = new Weather();

        if (woeidString != null) {

            weather.setCityCode(woeidString);
            weather = WeatherClient.getWeatherInformation(weather);


        } else {

            return null;
        }

        return weather;
    }

    /**
     * Gets Weatherinformation for a city, containing in a weatherobject
     *
     * @param weather contains the weoid from a location
     * @return a weather object with all weatherinformation
     */
    public static Weather getWeatherInformation(Weather weather) {

        String qResult = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?w=" + weather.getCityCode() + "&u=c");
        BufferedReader bufferedreader = null;
        Reader in = null;
        InputStream inputStream = null;


        try {
            HttpResponse response = httpClient.execute(httpGet,
                    localContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                inputStream = entity.getContent();
                in = new InputStreamReader(inputStream);
                bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String stringReadLine = null;
                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }
                qResult = stringBuilder.toString();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Error in the HTTP protocol");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Input or Output interrupted");
        } finally {
            try {
                bufferedreader.close();
                in.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(WeatherClient.class.getSimpleName(),"Could not close Streams");
            }

        }

        Document woeidDoc = convertStringToDocument(qResult);
        weather = extractWeatherFromXml(woeidDoc, weather);

        return weather;


    }

    /**
     * Retrieves the weather from the xml-file
     *
     * @param dest    the xml file
     * @param weather the weather to be updated
     * @return weather with retrieved information
     */
    private static Weather extractWeatherFromXml(Document dest, Weather weather) {


        Node temperatureNode = dest.getElementsByTagName(
                "yweather:condition").item(0);
        weather.setTemperature(temperatureNode.getAttributes()
                .getNamedItem("temp").getNodeValue().toString());
        Node tempUnitNode = dest.getElementsByTagName("yweather:units").item(0);
        weather.setTemperature(weather.getTemperature());

        Node cityNode = dest.getElementsByTagName("yweather:location")
                .item(0);
        weather.setCity(cityNode.getAttributes().getNamedItem("city")
                .getNodeValue().toString());

        Node dateNode = dest.getElementsByTagName("yweather:forecast")
                .item(0);
        weather.setDate(dateNode.getAttributes().getNamedItem("date")
                .getNodeValue().toString());

        Node conditionNode = dest
                .getElementsByTagName("yweather:condition").item(0);
        weather.setCondition(conditionNode.getAttributes()
                .getNamedItem("text").getNodeValue().toString());
        weather.setConditionCode(conditionNode.getAttributes()
                .getNamedItem("code").getNodeValue().toString());

        for(int i = 1;i<=3;i++){

            Weather forecast = new Weather();
            forecast.setCity(cityNode.getAttributes().getNamedItem("city")
                    .getNodeValue().toString());
            forecast.setDate(dest.getElementsByTagName("yweather:forecast").item(i).getAttributes().getNamedItem("date").getNodeValue().toString());
            forecast.setTemperature(dest.getElementsByTagName("yweather:forecast").item(i).getAttributes().getNamedItem("high").getNodeValue().toString());
            forecast.setConditionCode(dest.getElementsByTagName("yweather:forecast").item(i).getAttributes().getNamedItem("code").getNodeValue().toString());
            forecast.setCondition(dest.getElementsByTagName("yweather:forecast").item(i).getAttributes().getNamedItem("text").getNodeValue().toString());

            weather.addForecast(forecast);

        }

        return weather;
    }

    /**
     * Gets the Location id from the entered Location
     *
     * @param url the Urla request, containing the location
     * @return the first weoid from a list of locations
     */
    public static String getLocation(String url) {

        String qResult = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url + "&u=c");
        InputStream inputStream = null;
        Reader in = null;
        BufferedReader bufferedreader = null;

        try {
            HttpResponse response = httpClient.execute(httpGet,
                    localContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                inputStream = entity.getContent();
                in = new InputStreamReader(inputStream);
                bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String stringReadLine = null;
                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }
                qResult = stringBuilder.toString();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Error in the HTTP protocol");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Input or Output interrupted");
        }finally {
            try {
                bufferedreader.close();
                in.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(WeatherClient.class.getSimpleName(),"Could not close Streams");
            }

        }

        Document woeidDoc = convertStringToDocument(qResult);
        if (woeidDoc == null || parseWOEID(woeidDoc).isEmpty()) {
            return null;
        }
        String woeid = parseWOEID(woeidDoc).get(0);

        return woeid;
    }


    /**
     * creates a list of weoids from a given document
     *
     * @param srcDoc the Document containing the weoids
     * @return
     */
    private static ArrayList<String> parseWOEID(Document srcDoc) {
        ArrayList<String> listWOEID = new ArrayList<String>();

        NodeList nodeListDescription = srcDoc.getElementsByTagName("woeid");
        if (nodeListDescription.getLength() >= 0) {
            for (int i = 0; i < nodeListDescription.getLength(); i++) {
                listWOEID.add(nodeListDescription.item(i).getTextContent());
            }
        } else {
            listWOEID.clear();
        }
        return listWOEID;
    }


    /**
     * Converts a String to a document
     *
     * @param src The String, which should be converted to a document
     * @return a document
     */
    private static Document convertStringToDocument(String src) {
        Document dest = null;

        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;

        try {
            parser = dbFactory.newDocumentBuilder();
            dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Could not parse Input Stream");
        } catch (SAXException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Error parsing Input Stream");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(WeatherClient.class.getSimpleName(), "Input or Output interrupted");
        }

        return dest;
    }


}
