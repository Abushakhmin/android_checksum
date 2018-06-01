package ru.securitycode;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppCtl
{
    @SerializedName("pack_name")
    private String pack_name;

    public String getPackName() { return this.pack_name; }

    public void setPackName(String pack_name) { this.pack_name = pack_name; }

    @SerializedName("app_name")
    private String app_name;

    public String getAppName() { return this.app_name; }

    public void setAppName(String app_name) { this.app_name = app_name; }
    @SerializedName("versions")
    private ArrayList<Version> versions;

    public ArrayList<Version> getVersions() { return this.versions; }

    public void setVersions(ArrayList<Version> versions) { this.versions = versions; }

    public String toString(){
        return new Gson().toJson(this);
    }

    AppCtl (String pack_name, String app_name, Version ver){
        this.pack_name = pack_name;
        this.app_name = app_name;
        this.versions = new ArrayList<Version>();
        this.versions.add(ver);
    }
}
