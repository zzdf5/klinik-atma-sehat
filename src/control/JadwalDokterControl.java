package control;

import dao.JadwalDokterDAO;
import java.util.List;
import model.JadwalDokter;
import table.TableJadwalDokter;

public class JadwalDokterControl {
    private final JadwalDokterDAO dao = new JadwalDokterDAO();

    public String generateId() { 
        return dao.generateId(); 
    }
    
    public void insert(JadwalDokter data) { 
        dao.insert(data); 
    }
    
    public void update(JadwalDokter data, String id) { 
        dao.update(data, id); 
    }
    
    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<JadwalDokter> showData() { 
        return dao.showData(); 
    }
    
    public JadwalDokter search(String id) { 
        return dao.search(id); 
    }
    
    public List<Object[]> showDataWithNames() { 
        return dao.showDataWithNames(); 
    }
    
    public List<Object[]> searchByKeyword(String keyword) { 
        return dao.searchByKeyword(keyword); 
    }
    
    public List<Object[]> searchByDokterWithNames(String idDokter) { 
        return dao.searchByDokterWithNames(idDokter); 
    }

    public TableJadwalDokter showTable(String target) {
        List<Object[]> data = (target == null || target.isBlank()) ? dao.showDataWithNames() : dao.searchByKeyword(target);
        return new TableJadwalDokter(data);
    }
}
