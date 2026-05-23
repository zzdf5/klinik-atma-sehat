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
public class Dokter extends Pengguna {
    private String nomorSTR, spesialisasi;
    private List<String> jadwalPraktek;
    private float tarifKonsultasi;
    private boolean statusAktif;

    public Dokter(String id, String nomorSTR, String nama, String tanggalLahir,
                  String jenisKelamin, String noTelepon, String spesialisasi,
                  float tarifKonsultasi, String username, String password) {
        super(id, nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.nomorSTR = nomorSTR;
        this.spesialisasi = spesialisasi;
        this.tarifKonsultasi = tarifKonsultasi;
        this.jadwalPraktek = new ArrayList<>();
        this.statusAktif = true;
    }

    public Dokter(String nomorSTR, String nama, String tanggalLahir,
                  String jenisKelamin, String noTelepon, String spesialisasi,
                  float tarifKonsultasi, String username, String password) {
        super(nama, tanggalLahir, jenisKelamin, noTelepon, username, password);
        this.nomorSTR = nomorSTR;
        this.spesialisasi = spesialisasi;
        this.tarifKonsultasi = tarifKonsultasi;
        this.jadwalPraktek = new ArrayList<>();
        this.statusAktif = true;
    }

    public void setNomorSTR(String nomorSTR) { this.nomorSTR = nomorSTR; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    public void setTarifKonsultasi(float tarifKonsultasi) { this.tarifKonsultasi = tarifKonsultasi; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    public String getNomorSTR() { return nomorSTR; }
    public String getSpesialisasi() { return spesialisasi; }
    public float getTarifKonsultasi() { return tarifKonsultasi; }
    public boolean isStatusAktif() { return statusAktif; }
    public List<String> getJadwalPraktek() { return jadwalPraktek; }

    public void tambahJadwal(String jadwal) {
        this.jadwalPraktek.add(jadwal);
    }

    public void periksaPasien(Pasien pasien) {
        System.out.println("Dr. " + getNama() + " memeriksa pasien: " + pasien.getNama());
    }

    public Resep tulisResep(Pasien pasien) {
        System.out.println("Dr. " + getNama() + " menulis resep untuk: " + pasien.getNama());
        return new Resep(getId(), pasien.getNomorRekamMedis());
    }

    public Diagnosa buatDiagnosa(String kodePenyakit, String keterangan) {
        System.out.println("Dr. " + getNama() + " membuat diagnosa: " + keterangan);
        return new Diagnosa(kodePenyakit, keterangan, "Ringan");
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | dr. " + spesialisasi + " | STR: " + nomorSTR;
    }

    @Override
    public String getPeran() {
        return "Dokter";
    }

    public String getString() {
        return getId() + " | " + getNama() + " | " + spesialisasi
                + " | Rp" + tarifKonsultasi;
    }
}