package com.centrala.naucna_centrala.elasticSearch.model;

public final class ResultData {

	private String apstrakt;
	private String nazivCasopisa;
	private String naslovRada;
	private String autor;
	private String kljucniPojmovi;
	private String sadrzaj;
	private String nazivnaucneOblasti;
	private String location;
	private String highlight;

	public ResultData() {
	}

	public ResultData(String apstrakt, String nazivCasopisa, String naslovRada, String autor, String kljucniPojmovi, String sadrzaj, String nazivnaucneOblasti, String location, String highlight) {
		this.apstrakt = apstrakt;
		this.nazivCasopisa = nazivCasopisa;
		this.naslovRada = naslovRada;
		this.autor = autor;
		this.kljucniPojmovi = kljucniPojmovi;
		this.sadrzaj = sadrzaj;
		this.nazivnaucneOblasti = nazivnaucneOblasti;
		this.location = location;
		this.highlight = highlight;
	}

	public String getApstrakt() {
		return apstrakt;
	}

	public void setApstrakt(String apstrakt) {
		this.apstrakt = apstrakt;
	}

	public String getNazivCasopisa() {
		return nazivCasopisa;
	}

	public void setNazivCasopisa(String nazivCasopisa) {
		this.nazivCasopisa = nazivCasopisa;
	}

	public String getNaslovRada() {
		return naslovRada;
	}

	public void setNaslovRada(String naslovRada) {
		this.naslovRada = naslovRada;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getKljucniPojmovi() {
		return kljucniPojmovi;
	}

	public void setKljucniPojmovi(String kljucniPojmovi) {
		this.kljucniPojmovi = kljucniPojmovi;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getNazivnaucneOblasti() {
		return nazivnaucneOblasti;
	}

	public void setNazivnaucneOblasti(String nazivnaucneOblasti) {
		this.nazivnaucneOblasti = nazivnaucneOblasti;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
}
