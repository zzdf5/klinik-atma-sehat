/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sistem Klinik Kesehatan
 */
public class Pasien {
    private String id, nomorRekamMedis, nama, tanggalLahir, jenisKelamin;
    private String noTelepon, alamat, golonganDarah;
    private List<String> alergi;
    private List<String> riwayatPenyakit;

    public Pasien(String id, String nomorRekamMedis, String nama, String tanggalLahir,
                  String jenisKelamin, String noTelepon, String alamat, String golonganDarah) {
        this.id = id;
        this.nomorRekamMedis = nomorRekamMedis;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
        this.golonganDarah = golonganDarah;
        this.alergi = new ArrayList<>();
        this.riwayatPenyakit = new ArrayList<>();
    }

    public Pasien(String nomorRekamMedis, String nama, String tanggalLahir,
                  String jenisKelamin, String noTelepon, String alamat, String golonganDarah) {
        this.nomorRekamMedis = nomorRekamMedis;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
        this.golonganDarah = golonganDarah;
        this.alergi = new ArrayList<>();
        this.riwayatPenyakit = new ArrayList<>();
    }

    public void setId(String id) { this.id = id; }
    public void setNomorRekamMedis(String nomorRekamMedis) { this.nomorRekamMedis = nomorRekamMedis; }
    public void setNama(String nama) { this.nama = nama; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setGolonganDarah(String golonganDarah) { this.golonganDarah = golonganDarah; }

    public String getId() { return id; }
    public String getNomorRekamMedis() { return nomorRekamMedis; }
    public String getNama() { return nama; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getNoTelepon() { return noTelepon; }
    public String getAlamat() { return alamat; }
    public String getGolonganDarah() { return golonganDarah; }
    public List<String> getAlergi() { return alergi; }
    public List<String> getRiwayatPenyakit() { return riwayatPenyakit; }

    public void tambahAlergi(String alergiItem) {
        this.alergi.add(alergiItem);
    }

    public void tambahRiwayatPenyakit(String penyakit) {
        this.riwayatPenyakit.add(penyakit);
    }

    public void daftarAntrian() {
        System.out.println("Pasien " + nama + " berhasil didaftarkan ke antrian.");
    }

    public void bayarTagihan() {
        System.out.println("Pasien " + nama + " melakukan pembayaran tagihan.");
    }

    public String getInfo() {
        return id + " | " + nomorRekamMedis + " | " + nama + " | " + golonganDarah;
    }

    public String getString() {
        return id + " | " + nomorRekamMedis + " | " + nama + " | " + jenisKelamin
                + " | " + noTelepon;
    }
}