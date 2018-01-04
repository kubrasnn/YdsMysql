package com.example.kubra_pc.ydsmysql;
/**
 * Created by Kerim on 17.3.2017.
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventorModel implements Serializable {


    private String ad;
    private String sifre;
    private String email;

    private String anahtar;

    private String tarih;
    private Integer dogruSayisi;
    private Integer yanlisSayisi ;
    private Integer sonuc;
    private String keysonuc;


    public InventorModel() {
    }


    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getAd() {
        return ad;
    }

    public String getSifre() {
        return sifre;
    }




    public String getEmail() {
        return email;
    }

    public String join(List<String> list, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("ad", ad);
        result.put("sifre", sifre);

        result.put("anahtar",anahtar);
        result.put("email", email);
        result.put("Puan", sonuc);
        result.put("dogruSayisi", dogruSayisi);
        result.put("tarih", tarih);
        result.put("yanlisSayisi", yanlisSayisi);

        return result;
    }

    public String getAnahtar() {
        return anahtar;
    }

    public void setAnahtar(String anahtar) {
        this.anahtar = anahtar;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Integer getDogruSayisi() {
        return dogruSayisi;
    }

    public void setDogruSayisi(Integer dogruSayisi) {
        this.dogruSayisi = dogruSayisi;
    }

    public Integer getYanlisSayisi() {
        return yanlisSayisi;
    }

    public void setYanlisSayisi(Integer yanlisSayisi) {
        this.yanlisSayisi = yanlisSayisi;
    }

    public Integer getSonuc() {
        return sonuc;
    }

    public void setSonuc(Integer sonuc) {
        this.sonuc = sonuc;
    }

    public String getKeysonuc() {
        return keysonuc;
    }

    public void setKeysonuc(String keysonuc) {
        this.keysonuc = keysonuc;
    }
}
