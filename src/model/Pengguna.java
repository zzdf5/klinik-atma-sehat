/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Sistem Klinik Kesehatan
 */
public abstract class Pengguna {
    private String id, nama, tanggalLahir, jenisKelamin, noTelepon;
    private String username, password;

    public Pengguna(String id, String nama, String tanggalLahir, String jenisKelamin,
                    String noTelepon, String username, String password) {
        this.id = id;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
    }

    public Pengguna(String nama, String tanggalLahir, String jenisKelamin,
                    String noTelepon, String username, String password) {
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
    }

    public void setId(String id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getNoTelepon() { return noTelepon; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public boolean cekPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    public String getInfo() {
        return id + " | " + nama + " | " + jenisKelamin + " | " + noTelepon;
    }

    public abstract String getPeran();
}