package ru.securitycode;

import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.zip.CRC32;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        int n = args.length;

        if (n == 4) {

            String generate = args[0];
            String filepath = args[1];
            String package_name = args[2];
            String version = args[3];

            System.out.println("Режим 1");
            firstMode(generate, filepath, package_name, version);

        } else if (n == 2) {

            String merge = args[0];
            String output = args[1];

            System.out.println("Режим 2");


//            readeJSON(merge, output);

        } else System.out.printf("Ошибка ввода аргументов");


    }

    private static void firstMode(String generate, String filepath, String package_name, String version) throws IOException {

        String checksum = checksumBufferedInputStream(filepath);
        System.out.println(checksum);

        JSONObject resultJson = new JSONObject();

        resultJson.put("package_name", package_name);
        resultJson.put("version", version);
        resultJson.put("checksum", checksum);

        try (FileWriter file = new FileWriter(generate)) {
            file.write(resultJson.toString());
            System.out.println("Successfully Copied JSON Object to File...");
        }
    }

    public static String checksumBufferedInputStream(String filepath) throws IOException {

        String checksum;

        InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
        CRC32 crc = new CRC32();
        int cnt;
        while ((cnt = inputStream.read()) != -1) {
            crc.update(cnt);

        }
        checksum = String.format(Locale.US, "%08X", crc.getValue());
        return checksum;
    }

//    public static void secondMode(String merge,String output){
//
//        JSONObject obj = new JSONObject(merge);
//
//        obj.
//
//        String pageName = obj.getJSONObject("controlled_apps").getString("pageName");
//
//        JSONArray arr = obj.getJSONArray("posts");
//
//        for (int i = 0; i < arr.length(); i++)
//        {
//            String post_id = arr.getJSONObject(i).getString("post_id");
//        }
//    }
//
//    private static void writeJSON (JSONObject versions){
//
//        JSONArray ja = new JSONArray();
//
//        ja.put("pack_name", );
//        m.put("city", "New York");
//        m.put("state", "NY");
//        m.put("postalCode", 10021);
//
//    }



}
