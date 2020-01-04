package com.centrala.naucna_centrala.DTO;

public class PretragaDTO {

    private String kriterijum;
    private String upit;

    public PretragaDTO() {

    }

    public PretragaDTO(String kriterijum, String upit) {
        this.kriterijum = kriterijum;
        this.upit = upit;
    }

    public String getKriterijum() {
        return kriterijum;
    }

    public void setKriterijum(String kriterijum) {
        this.kriterijum = kriterijum;
    }

    public String getUpit() {
        return upit;
    }

    public void setUpit(String upit) {
        this.upit = upit;
    }
}
