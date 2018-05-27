package ru.securitycode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ReadAndWrite {

    public static void main(String[] args) throws IOException, ParseException {
        readeJSON();
    }

    public static void readeJSON () throws IOException, ParseException {

        FileReader fileReader = new FileReader("/home/roman/transport/old.txt");
        Object obj = new JSONParser().parse(fileReader);

        JSONObject root = (JSONObject) obj; // полный объект

        JSONArray controlled_appsArray = (JSONArray) root.get("controlled_apps"); // массив из controlled_apps


        for (Object aControlled_appsArray : controlled_appsArray) {

            JSONObject jsonObjectReleases = (JSONObject) aControlled_appsArray;
            JSONArray releasesArray = (JSONArray) jsonObjectReleases.get("releases"); // массив из releases

            for (Object aReleasesArray : releasesArray) {

                JSONObject jsonObjectSum_file = (JSONObject) aReleasesArray;
                String sjsonObjectSum_file = Main.checksumBufferedInputStream(jsonObjectSum_file.get("sum-file").toString());
                System.out.println(sjsonObjectSum_file);
            }
        }
    }
}
