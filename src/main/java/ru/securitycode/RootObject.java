package ru.securitycode;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootObject {
    @SerializedName("app_ctl")
    private ArrayList<AppCtl> app_ctl;

    public ArrayList<AppCtl> getAppCtl() {
        if(app_ctl == null){
            app_ctl = new ArrayList<AppCtl>();
        }
        return this.app_ctl;
    }

    public void setAppCtl(ArrayList<AppCtl> app_ctl) {
        this.app_ctl = app_ctl;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}