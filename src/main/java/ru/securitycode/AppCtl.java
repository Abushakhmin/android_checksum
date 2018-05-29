package ru.securitycode;


import java.util.ArrayList;

public class AppCtl
{
    private String pack_name;

    public String getPackName() { return this.pack_name; }

    public void setPackName(String pack_name) { this.pack_name = pack_name; }

    private String app_name;

    public String getAppName() { return this.app_name; }

    public void setAppName(String app_name) { this.app_name = app_name; }

    private ArrayList<Version> versions;

    public ArrayList<Version> getVersions() { return this.versions; }

    public void setVersions(ArrayList<Version> versions) { this.versions = versions; }
}
