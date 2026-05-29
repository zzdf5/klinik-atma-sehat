package control;

import dao.DokterDAO;
import java.util.List;
import model.Dokter;
import table.TableDokter;

public class DokterControl {
    private final DokterDAO dao = new DokterDAO();

    public void insert(Dokter data) { 
        dao.insert(data); 
    }
    
    public void update(Dokter data, String id) { 
        dao.update(data, id); 
    }
    
    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<Dokter> showData() { 
        return dao.showData(); 
    }
    
    public Dokter search(String id) { 
        return dao.search(id); 
    }
    
    public TableDokter showTable(String target) {
        List<Dokter> dataDokter = dao.showData();
        TableDokter tableDokter = new TableDokter(dataDokter);
        
        for (Dokter d : dataDokter) {
            System.out.println(d.getNama());
        }

        return tableDokter;
    }
}
