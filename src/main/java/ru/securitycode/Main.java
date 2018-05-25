package ru.securitycode;

import org.json.JSONObject;

import java.io.*;
import java.util.Locale;
import java.util.zip.CRC32;

public class Main {

    public static void main(String[] args) throws IOException {

        String generate = "/home/roman/transport/generate.txt";
        String filepath = "/home/roman/transport/mdmapp_mdm-2018-05-23-1724.apk";
        String package_name = "mdm.secure.agent";
        String version = "1.7.1724";
        String checksum;

        checksum = String.format(Locale.US, "%08X", checksumBufferedInputStream(filepath));
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

    private static long checksumBufferedInputStream(String filepath) throws IOException {

        InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
        CRC32 crc = new CRC32();
        int cnt;
        while ((cnt = inputStream.read()) != -1) {
            crc.update(cnt);

        }

        return crc.getValue();
    }
}
