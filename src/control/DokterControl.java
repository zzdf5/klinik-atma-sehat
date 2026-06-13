package control;

import dao.DokterDAO;
import exception.DataTidakDitemukanException;
import exception.InputKosongException;
import java.util.List;
import model.Dokter;
import table.TableDokter;

public class DokterControl {
    private final DokterDAO dao = new DokterDAO();

    public Dokter masuk(String username, String password) throws InputKosongException, DataTidakDitemukanException {
        if (username == null || username.isBlank()) {
            throw new InputKosongException();
        }
        if (password == null || password.isBlank()) {
            throw new InputKosongException();
        }

        Dokter dokter = dao.searchByCredential(username, password);
        if (dokter == null) {
            throw new DataTidakDitemukanException("Dokter", username);
        }

        return dokter;
    }

    public String generateId() { 
        return dao.generateId(); 
    }

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
        List<Dokter> dataDokter = (target == null || target.isBlank()) ? dao.showData() : dao.searchByKeyword(target);
        return new TableDokter(dataDokter);
    }
}
