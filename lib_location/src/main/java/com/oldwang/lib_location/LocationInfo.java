package com.oldwang.lib_location;

public class LocationInfo {

    private double mLatitude;
    private double mLongitude;
    private String mCountry;
    private String mCountryCode;
    private String mCity;
    private String mCityCode;
    private String mProvince;
    private String mDistrict;
    private String mDesc;
    private String mStreet;
    private String mStreetNum;

    private String aoiName;//

    //获取地址全名
    public String getAddr() {
        return mCountry + mProvince + mCity + mDistrict + mStreet + mStreetNum + aoiName;
    }

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmCountryCode() {
        return mCountryCode;
    }

    public void setmCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCityCode() {
        return mCityCode;
    }

    public void setmCityCode(String mCityCode) {
        this.mCityCode = mCityCode;
    }

    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public void setmDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmStreet() {
        return mStreet;
    }

    public void setmStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public String getmStreetNum() {
        return mStreetNum;
    }

    public void setmStreetNum(String mStreetNum) {
        this.mStreetNum = mStreetNum;
    }
}
