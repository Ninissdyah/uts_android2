package com.example.uts_android2;

import java.io.Serializable;

public class ModelMain implements Serializable {
    String strName;
    String strVicinity;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrVicinity() {
        return strVicinity;
    }

    public void setStrVicinity(String strVicinity) {
        this.strVicinity = strVicinity;
    }

    public String getStrPhoto() {
        return strPhoto;
    }

    public void setStrPhoto(String strPhoto) {
        this.strPhoto = strPhoto;
    }

    public double getLatloc() {
        return latloc;
    }

    public void setLatloc(double latloc) {
        this.latloc = latloc;
    }

    public double getLongloc() {
        return longloc;
    }

    public void setLongloc(double longloc) {
        this.longloc = longloc;
    }

    String strPhoto;
    double latloc;
    double longloc;
}
