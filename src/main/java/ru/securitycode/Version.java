package ru.securitycode;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Version
{
    @SerializedName("ver")
    private String ver;

    public String getVer() { return this.ver; }

    public void setVer(String ver) { this.ver = ver; }
    @SerializedName("sum")
    private String sum;

    public String getSum() { return this.sum; }

    public void setSum(String sum) { this.sum = sum; }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    Version (String ver, String sum){
        this.ver = ver;
        this.sum = sum;
    }
}
