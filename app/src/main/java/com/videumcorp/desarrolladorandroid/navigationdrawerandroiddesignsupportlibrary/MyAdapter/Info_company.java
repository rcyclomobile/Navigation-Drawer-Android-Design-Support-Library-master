package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

/**
 * Created by Roberto on 26-02-2016.
 */
public class Info_company {

    private String nameCompany;

    public Info_company(String nameCompany, String addressCompany, String lat, String lng, int cantidad_contenedores, int llenos, int medios, int vacios) {
        this.nameCompany = nameCompany;
        this.addressCompany = addressCompany;
        this.lat = lat;
        this.lng = lng;
        this.cantidad_contenedores = cantidad_contenedores;
        this.llenos = llenos;
        this.medios = medios;
        this.vacios = vacios;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getAddressCompany() {
        return addressCompany;
    }

    public void setAddressCompany(String addressCompany) {
        this.addressCompany = addressCompany;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getCantidad_contenedores() {
        return cantidad_contenedores;
    }

    public void setCantidad_contenedores(int cantidad_contenedores) {
        this.cantidad_contenedores = cantidad_contenedores;
    }

    public int getLlenos() {
        return llenos;
    }

    public void setLlenos(int llenos) {
        this.llenos = llenos;
    }

    public int getMedios() {
        return medios;
    }

    public void setMedios(int medios) {
        this.medios = medios;
    }

    public int getVacios() {
        return vacios;
    }

    public void setVacios(int vacios) {
        this.vacios = vacios;
    }

    private String addressCompany;
    private String lat;
    private String lng;
    private int cantidad_contenedores;
    private int llenos;
    private int medios;
    private int vacios;

}
