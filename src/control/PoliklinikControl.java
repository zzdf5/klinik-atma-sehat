package control;

import dao.PoliklinikDAO;
import java.util.List;
import model.Poliklinik;
import table.TablePoliKlinik;

public class PoliklinikControl {
    private final PoliklinikDAO dao = new PoliklinikDAO();

    public String generateId() { 
        return dao.generateId(); 
    }

    public void insert(Poliklinik data) {
        dao.insert(data);
    }
    
    public void update(Poliklinik data, String id) { 
        dao.update(data, id); 
    }
    
    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<Poliklinik> showData() { 
        return dao.showData(); 
    }
    
    public Poliklinik search(String id) { 
        return dao.search(id); 
    }
    
    public TablePoliKlinik showTable(String target) {
        List<Poliklinik> dataPoliKlinik = (target == null || target.isBlank()) ? dao.showData() : dao.searchByKeyword(target);
        return new TablePoliKlinik(dataPoliKlinik);
    }
}
