package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.widget.Button;

/**
 * Created by Roberto on 31-10-2015.
 */
public class Container {

    public String getNameContainer() {
        return nameContainer;
    }

    public void setNameContainer(String nameContainer) {
        this.nameContainer = nameContainer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getDesecho() {
        return desecho;
    }

    public void setDesecho(String desecho) {
        this.desecho = desecho;
    }

    private String nameContainer;
    private String status;
    private String latlong;
    private String establishment;
    private String desecho ;
    private String company;

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    private String activo;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Container(String nameContainer, String latlong, String establishment , String company ,String status, String desecho, String activo) {
        this.nameContainer = nameContainer;
        this.latlong = latlong;
        this.establishment = establishment;
        this.status = status;
        this.desecho = desecho;
        this.company = company;
        this.activo = activo;
    }



}
