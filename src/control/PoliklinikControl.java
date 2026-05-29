package control;

import dao.PoliklinikDAO;
import java.util.List;
import model.Poliklinik;
import table.TablePoliKlinik;

public class PoliklinikControl {
    private final PoliklinikDAO dao = new PoliklinikDAO();

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
        List<Poliklinik> dataPoliKlinik = dao.showData();
        TablePoliKlinik tablePoliKlinik = new TablePoliKlinik(dataPoliKlinik);

      
        for (Poliklinik p : dataPoliKlinik) {
            System.out.println(p.getNamaPoliklinik());
        }

        return tablePoliKlinik;
    }
}
