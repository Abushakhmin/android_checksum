package ru.securitycode;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
//
//        updateJSONFile();
            newJSONElementApk("/home/roman/test_chek/vpncontinent-debug-latest.apk", "/home/roman/test_chek/vpncontinent-debug-latest.apk");
//        System.out.println(createJSONFile());
//        } else System.out.printf("Ошибка ввода аргументов");


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

    private static String newJSONElementApk(String filePath, String generate) throws IOException {

        JSONObject JSONElementApk = new JSONObject();

        String package_name;
        long version;
        String checksum = checksumBufferedInputStream(filePath);

        try (ApkFile apkFile = new ApkFile(new File(filePath))) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            package_name = apkMeta.getPackageName();
            version = apkMeta.getVersionCode();
        }

        JSONElementApk.put("pack_name", package_name);
        JSONElementApk.put("ver", version);
        JSONElementApk.put("sum", checksum);

        try (FileWriter file = new FileWriter(generate)) {
            file.write(JSONElementApk.toString());

            System.out.println("Successfully Copied JSON Object to File...");
        }

       return generate;
    }

    private static void updateJSONFile() throws IOException, ParseException {

//        String content_json = readFile("/home/roman/transport/mdmapp_mdm-2018-01-25-1537.apk", UTF_8);

        Version version = new Version();
        version.setVer("3.7.5.9.154");
        version.setSum("12345R");
        System.out.println(version.getSum());
        System.out.println(version.getVer());

        ArrayList<Version> versions = new ArrayList<>();


        AppCtl ctl = new AppCtl();
        ctl.setAppName("Code Security");
        ctl.setPackName("com.securitycode");
        ctl.setVersions(versions);
        System.out.println(ctl.getVersions());

        RootObject ro = new RootObject();
        ro.setAppCtl(new ArrayList<AppCtl>());

        String s = String.valueOf(ro.getAppCtl());
        System.out.println(s);
    }




//        for(AppCtl app_ctl : ro.getAppCtl()) {
//
//
//
//            AppCtl ctl = new AppCtl();
//            ctl.setAppName("Code Security");
//            ctl.setPackName("com.securitycode");
//
//             System.out.println(ctl.getAppName());
//             System.out.println(ctl.getPackName());
//
//            ctl.setVersions(new ArrayList<Version>());
//            for(Version version : ctl.getVersions()) {
////                System.out.println(release.getVer());
////                System.out.println(release.getSumFile());
//
//                version.setVer("3.7.5.9.154");
//
//
//                version.setSum("12345R");
//
//
//
//
//                ctl.getVersions().add(version);
//
//            }
//
//            ro.getAppCtl().add(ctl);
//
//        }


//        System.out.println(ro.toString());
//        System.out.println(forUpdateJSON.toString());


    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
