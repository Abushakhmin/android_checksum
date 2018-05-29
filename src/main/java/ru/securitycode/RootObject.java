package ru.securitycode;

import java.util.ArrayList;

public class RootObject {

    private ArrayList<AppCtl> app_ctl;

    public ArrayList<AppCtl> getAppCtl() {
        return this.app_ctl;
    }

    public void setAppCtl(ArrayList<AppCtl> app_ctl) {
        this.app_ctl = app_ctl;
    }
}