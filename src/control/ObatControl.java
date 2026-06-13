package control;

import dao.ObatDAO;
import dao.ObatHerbalDAO;
import dao.ObatPatenDAO;
import java.util.ArrayList;
import java.util.List;
import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;
import table.TableObat;
import table.TableObatHerbal;
import table.TableObatPaten;

public class ObatControl {
    private final ObatDAO dao = new ObatDAO();
    private final ObatHerbalDAO herbalDAO = new ObatHerbalDAO();
    private final ObatPatenDAO patenDAO = new ObatPatenDAO();
   
    public void insert(Obat data) { 
        dao.insert(data); 
    }
    
    public void insertHerbal(ObatHerbal data) { 
        herbalDAO.insert(data); 
    }
    
    public void insertPaten(ObatPaten data) { 
        patenDAO.insert(data); 
    }

    public void update(Obat data, String id) { 
        dao.update(data, id); 
    }
    
    public void updateHerbal(ObatHerbal data, String id) { 
        herbalDAO.update(data, id); 
    }
    
    public void updatePaten(ObatPaten data, String id) { 
        patenDAO.update(data, id); 
    }

    public String generateIdHerbal() { 
        return herbalDAO.generateId(); 
    }
    
    public String generateIdPaten() { 
        return patenDAO.generateId(); 
    }

    public void delete(String id) {
        dao.delete(id);
    }

    public void kurangiStok(String idObat, int jumlah) {
        dao.kurangiStok(idObat, jumlah);
    }

    public Obat search(String id) {
        return dao.search(id); 
    }
    
    public ObatHerbal searchHerbal(String id) { 
        return herbalDAO.search(id); 
    }
    
    public ObatPaten searchPaten(String id) { 
        return patenDAO.search(id); 
    }

    public List<Obat> showData() { 
        return dao.showData(); 
    }
    
    public List<ObatHerbal> showDataHerbal() { 
        return herbalDAO.showData(); 
    }
    
    public List<ObatPaten> showDataPaten() { 
        return patenDAO.showData(); 
    }
    
    public TableObat showTable(String target){
        List<Obat> dataObat = dao.showData();
        TableObat tableObat = new TableObat(dataObat);

        for(Obat o : dataObat){
            System.out.println(o.getNamaObat());
        }

        return tableObat;
    }

    public TableObatPaten showTablePaten(String keyword) {
        List<ObatPaten> hasil = new ArrayList<>();
        String kw = keyword == null ? "" : keyword.toLowerCase();
        for (ObatPaten p : patenDAO.showData()) {
            if (kw.isEmpty() || p.getIdObat().toLowerCase().contains(kw) || p.getNamaObat().toLowerCase().contains(kw) || p.getMerk().toLowerCase().contains(kw)) {
                hasil.add(p);
            }
        }
        return new TableObatPaten(hasil);
    }

    public TableObatHerbal showTableHerbal(String keyword) {
        List<ObatHerbal> hasil = new ArrayList<>();
        String kw = keyword == null ? "" : keyword.toLowerCase();
        for (ObatHerbal h : herbalDAO.showData()) {
            if (kw.isEmpty() || h.getIdObat().toLowerCase().contains(kw) || h.getNamaObat().toLowerCase().contains(kw) || h.getBahanUtama().toLowerCase().contains(kw)) {
                hasil.add(h);
            }
        }
        return new TableObatHerbal(hasil);
    }

    public List<Obat> showSemua() {
        List<Obat> semua = new ArrayList<>();
        semua.addAll(dao.showData());
        semua.addAll(herbalDAO.showData());
        semua.addAll(patenDAO.showData());
        return semua;
    }
}
