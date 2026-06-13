package model;

public abstract class Pengguna {
    private String id, nama, noTelepon, username, password;

    public Pengguna(String id, String nama, String noTelepon, String username, String password) {
        this.id = id;
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id; 
    }
    
    public void setNama(String nama) {
        this.nama = nama; 
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
    
    public void setUsername(String username) {
        this.username = username; 
    }
    
    public void setPassword(String password) {
        this.password = password; 
    }

    public String getId() {
        return id;
    }
    
    public String getNama() { 
        return nama; 
    }
    
    public String getNoTelepon() { 
        return noTelepon; 
    }
    
    public String getUsername() {
        return username; 
    }
    
    public String getPassword() { 
        return password; 
    }

    public String getInfo() {
        return id + " | " + nama + " | " + noTelepon;
    }

    public abstract String getPeran();
}
