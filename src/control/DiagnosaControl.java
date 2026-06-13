package control;

import dao.DiagnosaDAO;
import java.util.List;
import model.Diagnosa;

public class DiagnosaControl {
    private final DiagnosaDAO dao = new DiagnosaDAO();

    public String generateIdDiagnosa() { 
        return dao.generateId(); 
    }
    
    public void insert(Diagnosa data) { 
        dao.insert(data); 
    }
    
    public void update(Diagnosa data, String id) { 
        dao.update(data, id); 
    }
    
    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<Diagnosa> showData() { 
        return dao.showData(); 
    }
    
    public Diagnosa search(String id) { 
        return dao.search(id); 
    }
}
