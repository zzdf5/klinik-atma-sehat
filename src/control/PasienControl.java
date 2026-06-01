package control;

import dao.PasienDAO;
import java.util.List;
import model.Pasien;

public class PasienControl {
    private final PasienDAO dao = new PasienDAO();

    public String generateId() { return dao.generateId(); }
    public String generateNomorRekamMedis() { return dao.generateNomorRekamMedis(); }
    public List<Pasien> searchByNama(String nama) { return dao.searchByNama(nama); }
    public void insert(Pasien data) { dao.insert(data); }
    public void update(Pasien data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Pasien> showData() { return dao.showData(); }
    public Pasien search(String id) { return dao.search(id); }
    
    public Pasien searchByNomorRM(String nomorRM){
        return dao.searchByNomorRM(nomorRM);
    }
    
}
