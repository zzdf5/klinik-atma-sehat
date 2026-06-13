package model;

public class Poliklinik {
    private String idPoliklinik, namaPoliklinik, lokasiRuangan, jamOperasional;

    public Poliklinik(String idPoliklinik, String namaPoliklinik, String lokasiRuangan, String jamOperasional) {
        this.idPoliklinik = idPoliklinik;
        this.namaPoliklinik = namaPoliklinik;
        this.lokasiRuangan = lokasiRuangan;
        this.jamOperasional = jamOperasional;
    }

    public void setIdPoliklinik(String idPoliklinik) { 
        this.idPoliklinik = idPoliklinik; 
    }
    
    public void setNamaPoliklinik(String namaPoliklinik) { 
        this.namaPoliklinik = namaPoliklinik; 
    }
    
    public void setLokasiRuangan(String lokasiRuangan) { 
        this.lokasiRuangan = lokasiRuangan; 
    }
    
    public void setJamOperasional(String jamOperasional) { 
        this.jamOperasional = jamOperasional; 
    }

    public String getIdPoliklinik() { 
        return idPoliklinik; 
    }
    
    public String getNamaPoliklinik() { 
        return namaPoliklinik; 
    }
    
    public String getLokasiRuangan() { 
        return lokasiRuangan; 
    }
    
    public String getJamOperasional() { 
        return jamOperasional; 
    }

    public String getInfo() {
        return idPoliklinik + " | " + namaPoliklinik + " | " + lokasiRuangan;
    }
}
