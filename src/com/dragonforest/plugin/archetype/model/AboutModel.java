package com.dragonforest.plugin.archetype.model;

public class AboutModel {
    String companyName;
    String companyPhone;
    String companyUrl;
    String support;
    String supportPhone;

    public String getCompanyName() {
        return companyName;
    }

    public AboutModel(String companyName, String companyPhone, String companyUrl, String support, String supportPhone) {
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.companyUrl = companyUrl;
        this.support = support;
        this.supportPhone = supportPhone;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }
}
