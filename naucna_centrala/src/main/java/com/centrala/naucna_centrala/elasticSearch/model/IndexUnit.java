package com.centrala.naucna_centrala.elasticSearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName = IndexUnit.INDEX_NAME, type = IndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class IndexUnit {

	public static final String INDEX_NAME = "libraryserbian";
	public static final String TYPE_NAME = "radovi";
	
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String sadrzaj;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String naslovRada;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String kljucniPojmovi;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String apstrakt;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String nazivCasopisa;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String nazivNaucneOblasti;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String imeAutora;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer", index = true, store = true)
	private String prezimeAutora;
	@Id
	@Field(type = FieldType.text, index = false, store = true)
	private String filename;
	@Field(type = FieldType.text, analyzer= "serbian-analyzer",  index = true, store = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
	private String filedate;
	@GeoPointField
	@JsonProperty
	private GeoPoint geo_point;

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getNaslovRada() {
		return naslovRada;
	}

	public void setNaslovRada(String naslovRada) {
		this.naslovRada = naslovRada;
	}

	public String getKljucniPojmovi() {
		return kljucniPojmovi;
	}

	public void setKljucniPojmovi(String kljucniPojmovi) {
		this.kljucniPojmovi = kljucniPojmovi;
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

	public String getNazivNaucneOblasti() {
		return nazivNaucneOblasti;
	}

	public void setNazivNaucneOblasti(String nazivNaucneOblasti) {
		this.nazivNaucneOblasti = nazivNaucneOblasti;
	}

	public String getImeAutora() {
		return imeAutora;
	}

	public void setImeAutora(String imeAutora) {
		this.imeAutora = imeAutora;
	}

	public String getPrezimeAutora() {
		return prezimeAutora;
	}

	public void setPrezimeAutora(String prezimeAutora) {
		this.prezimeAutora = prezimeAutora;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiledate() {
		return filedate;
	}

	public void setFiledate(String filedate) {
		this.filedate = filedate;
	}

	public GeoPoint getGeo_point() {
		return geo_point;
	}
	public void setGeo_point(GeoPoint geo_point) {
		this.geo_point = geo_point;
	}
	
	
	
}
