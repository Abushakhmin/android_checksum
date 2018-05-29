//package ru.securitycode;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class ReadAndWrite {
//
//    public static void main(String[] args) throws IOException, ParseException {
//        readeJSON();
//    }
//
//    public static void readeJSON (String merge) throws IOException, ParseException {
//
//        ArrayList<String> sumfileArray = new ArrayList<String>();
//
//        FileReader fileReader = new FileReader(merge);
//        Object obj = new JSONParser().parse(fileReader);
//
//        JSONObject root = (JSONObject) obj; // полный объект
//
//        JSONArray controlled_appsArray = (JSONArray) root.get("controlled_apps"); // массив из controlled_apps
//
//
//        for (Object aControlled_appsArray : controlled_appsArray) {
//
//            JSONObject jsonObjectReleases = (JSONObject) aControlled_appsArray;
//            JSONArray releasesArray = (JSONArray) jsonObjectReleases.get("releases"); // массив из releases
//
//            for (Object aReleasesArray : releasesArray) {
//
//                JSONObject jsonObjectSumfile = (JSONObject) aReleasesArray;
//                String sjsonObjectSumfile = Main.checksumBufferedInputStream(jsonObjectSumfile.get("sum-file").toString());
//                sumfileArray.add(sjsonObjectSumfile);
//            }
//        }
//
//        System.out.println(sumfileArray);
//    }
//}
