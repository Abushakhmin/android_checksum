package ru.securitycode;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.json.JSONObject;
import org.json.JSONArray;
//import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.CRC32;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

//        int n = args.length;
//
//        if (n == 4) {
//
//            String generate = args[0];
//            String filepath = args[1];
//            String package_name = args[2];
//            String version = args[3];
//
//            System.out.println("Режим 1");
//            firstMode(generate, filepath, package_name, version);
//
//        } else if (n == 2) {
//
//            String merge = args[0];
//            String output = args[1];
//
//            System.out.println("Режим 2");
//    formatJSONFile();
// newJSONElementApk("/home/roman/test_chek/vpncontinent-debug-latest.apk", "/home/roman/test_chek/vpncontinent-debug-latest");
//        System.out.println(createJSONFile());
//        } else System.out.printf("Ошибка ввода аргументов");
        updateJSONFile(
                newJSONElementApk("/home/roman/test_chek/tls-mobile-gui-release-signed-latest.apk"),
                formatJSONFile("qqqqqq", "wwwwwww", "1", "FDCDE", "/home/roman/test_chek/test"));
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

    private static String checksumBufferedInputStream(String filepath) throws IOException {

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

    private static JSONObject newJSONElementApk(String filePath) throws IOException {

        JSONObject newJSONElementApk = new JSONObject();

        String package_name;
        String app_name;
        String version;
        String checksum = checksumBufferedInputStream(filePath);

        try (ApkFile apkFile = new ApkFile(new File(filePath))) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            package_name = apkMeta.getPackageName();
            app_name = apkMeta.getName();
            version = String.valueOf(apkMeta.getVersionCode());
        }

        newJSONElementApk.put("pack_name", package_name);
        newJSONElementApk.put("app_name", app_name);
        newJSONElementApk.put("ver", version);
        newJSONElementApk.put("sum", checksum);

        return newJSONElementApk;

    }

    private static JSONObject formatJSONFile(String package_name, String app_name, String version, String checksum, String generate) throws IOException {

        JSONObject mainObject = new JSONObject();

        JSONObject objectVersion = new JSONObject();
        JSONObject objectAppCtl = new JSONObject();

        JSONArray jsonArrayAppCtl = new JSONArray();
        JSONArray jsonArrayVersions = new JSONArray();

        objectVersion.put("ver",version);
        objectVersion.put("sum",checksum);

        jsonArrayVersions.put(objectVersion);

        objectAppCtl.put("pack_name", package_name);
        objectAppCtl.put("app_name", app_name);
        objectAppCtl.put("versions", jsonArrayVersions);

        jsonArrayAppCtl.put(objectAppCtl);

        mainObject.put("app_ctl", jsonArrayAppCtl);

        try (FileWriter file = new FileWriter(generate + "_all" + ".txt")) {
            file.write(mainObject.toString());

            System.out.println("Successfully Copied JSON Object to File...");
        }

        return mainObject;
    }

    private static void updateJSONFile(JSONObject newJSONElementApk, JSONObject mainObject) throws IOException, ParseException {
        // читем из нового объекта параметры
        String newPack_name = newJSONElementApk.get("pack_name").toString();
        String newApp_name = newJSONElementApk.get("app_name").toString();
        String newVersions = newJSONElementApk.get("ver").toString();
        String newSum = newJSONElementApk.get("sum").toString();

        System.out.println(newPack_name);
        System.out.println(newApp_name);
        System.out.println(newVersions);
        System.out.println(newSum);

        // читем из главного объекта параметры

        JSONArray jsonArrayAppCtl = mainObject.getJSONArray("app_ctl");
        System.out.println(jsonArrayAppCtl);

        for (Object aControlled_appsArray : jsonArrayAppCtl) {
            JSONObject jsonObjectVersion = (JSONObject) aControlled_appsArray;
            JSONArray releasesArray = (JSONArray) jsonObjectVersion.get("versions"); // массив из releases

            System.out.println(releasesArray.toString());

            for (Object aReleasesArray : releasesArray) {

                JSONObject jsonObjectSumfile = (JSONObject) aReleasesArray;
                String sjsonObjectSumfile = jsonObjectSumfile.get("sum").toString();
                System.out.println(sjsonObjectSumfile);
                if (newSum.equals(sjsonObjectSumfile)){
                    System.out.println("Совпадают");
                } else {

                    System.out.println("НЕ Совпадают");
//                    jsonObjectSumfile.put(jsonObjectSumfile.keySet())
                    jsonObjectSumfile.put("ver", newVersions);
                    jsonObjectSumfile.put("sum", newSum);
                }
            }
        }
        System.out.println(mainObject.toString());
    }
}
