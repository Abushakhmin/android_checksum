package ru.securitycode;

import com.google.gson.Gson;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.CRC32;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(String[] args) throws IOException {

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
            File file = new File(output);

            if (file.exists()){ // Проверка, существует ли файл
                withTxtInJson(output); // перобразовываем имеющийся файл, удаляем пробелы и чексумму в конце файла
                updateJSONFile(newJSONElementApk(merge), output); // обновляем
            } else {
                firstFormatJSONFile(newJSONElementApk(merge), output); // создаем новый
            }
        } else {
            System.out.println("Ошибка кол-ва аргументов");
        }
    }

    private static void firstMode(String generate, String filepath, String package_name, String version) throws IOException {

        JSONObject resultJson = new JSONObject();

        resultJson.put("package_name", package_name);
        resultJson.put("version", version);

        String checksum = checksumBufferedInputStream(filepath);
        resultJson.put("checksum", checksum);
        System.out.println(checksum);

        try (FileWriter file = new FileWriter(generate)) {
            file.write(resultJson.toString());
            System.out.println("Successfully Copied JSON Object to File...");
        }
    }

    private static String checksumBufferedInputStream(String filepath) throws IOException {

        String checksum;

        File file = new File(filepath);

        if (file.exists()) {

            InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
            CRC32 crc = new CRC32();
            int cnt;
            while ((cnt = inputStream.read()) != -1) {
                crc.update(cnt);

            }
            checksum = String.format(Locale.US, "%08X", crc.getValue());
        } else {

            byte bytes[] = filepath.getBytes();

            CRC32 crc = new CRC32();
            crc.update(bytes,0,bytes.length);

            checksum = String.format(Locale.US, "%08X", crc.getValue());
        }
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

//        String textAndSum = newJSONElementApk.toString() + "\n" + "\n" + "\n" + checksumBufferedInputStream(newJSONElementApk.toString());
//        String textAndSum = checksumBufferedInputStream(newJSONElementApk.toString());
//        System.out.println(textAndSum);

        return newJSONElementApk;
    }

    // Работает, создает новый JSON на основании первого считывания apk //
    private static void firstFormatJSONFile(JSONObject JsonElementApk, String fileName) throws IOException {

        JSONObject mainObject = new JSONObject();

        JSONObject objectVersion = new JSONObject();
        JSONObject objectAppCtl = new JSONObject();

        JSONArray jsonArrayAppCtl = new JSONArray();
        JSONArray jsonArrayVersions = new JSONArray();

        objectVersion.put("ver", JsonElementApk.get("ver"));
        objectVersion.put("sum", JsonElementApk.get("sum"));

        jsonArrayVersions.put(objectVersion);

        objectAppCtl.put("pack_name", JsonElementApk.get("pack_name"));
        objectAppCtl.put("app_name", JsonElementApk.get("app_name"));
        objectAppCtl.put("versions", jsonArrayVersions);

        jsonArrayAppCtl.put(objectAppCtl);

        mainObject.put("app_ctl", jsonArrayAppCtl);

        System.out.println("Обычный вид:");
        System.out.println(mainObject.toString());
        System.out.println("Чек_сумма:" + checksumBufferedInputStream(mainObject.toString()));

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(mainObject.toString() + "\n" + "\n" + "\n" + checksumBufferedInputStream(mainObject.toString()));
            System.out.println("Successfully Copied JSON Object to File...");
        }
    }


    private static void updateJSONFile(JSONObject newJSONElementApk, String path) throws IOException {

        // читем из нового объекта параметры
        String newPack_name = newJSONElementApk.get("pack_name").toString();
        String newApp_name = newJSONElementApk.get("app_name").toString();
        String newVersions = newJSONElementApk.get("ver").toString();
        String newSum = newJSONElementApk.get("sum").toString();

        Version newVerAndSum = new Version(newVersions, newSum);

//        System.out.println(newPack_name);
//        System.out.println(newApp_name);
//        System.out.println(newVersions);
//        System.out.println(newSum);

        // читем из главного объекта параметры

        String content_json = readFile(path, UTF_8);
//        System.out.println("content_jso:" + content_json);

        RootObject ro = new Gson().fromJson(content_json, RootObject.class);
//        System.out.println("ro:" + ro.toString());
        ArrayList<AppCtl> oldAppArray = ro.getAppCtl();
//        System.out.println("oldAppArray:" + oldAppArray);

        int countApp = oldAppArray.size();
        boolean isContainsAppCtl = false;

        for(int i = 0 ; i < countApp; i++) {
            AppCtl jsonObjectApp = oldAppArray.get(i);
//            System.out.println("jsonObjectApp " + i + ": " + jsonObjectApp);

            if (jsonObjectApp.getPackName().equals(newPack_name)) {
                ArrayList<Version> oldVerArray = jsonObjectApp.getVersions();
                int countVer = oldVerArray.size();

                boolean isContainsVer = false;

                for (int j = 0; j < countVer; j++) {
                    Version jsonObjectVer = oldVerArray.get(j);
                    String ver = jsonObjectVer.getVer();
                    String sum = jsonObjectVer.getSum();
//                    System.out.println("jsonObjectVer " + j + ": " + ver);
//                    System.out.println("jsonObjectSum " + j + ": " + sum);

                    if (jsonObjectVer.getVer().equals(newVersions) &&  jsonObjectVer.getSum().equals(newSum)){
                        isContainsVer = true;
                    }
                }

                if (!isContainsVer){
                    oldVerArray.add(newVerAndSum);
                }

                isContainsAppCtl = true;
            }
        }

        if(!isContainsAppCtl){
            AppCtl appCtl = new AppCtl(newPack_name,newApp_name, newVerAndSum);
            oldAppArray.add(appCtl);
            ro.setAppCtl(oldAppArray);
        }

        System.out.println("Обычный вид:");
        System.out.println(ro.toString());
        System.out.println("Чек_сумма:" + checksumBufferedInputStream(ro.toString()));

        try (FileWriter file = new FileWriter(path)) {
            file.write(ro.toString() + "\n" + "\n" + "\n" + checksumBufferedInputStream(ro.toString()));
            System.out.println("Successfully Copied JSON Object to File...");
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    //Преобразование текстового файла в JSON
    private static void withTxtInJson (String path) throws IOException {

        RandomAccessFile f = new RandomAccessFile(path, "rw");
        long length = f.length() - 1;
        byte b;
        do {
            length -= 1;
            f.seek(length);
            b = f.readByte();
        } while(b != 10);

        f.setLength(length - 1);
        f.close();
    }
}