package control;

import dao.PasienDAO;
import java.util.ArrayList;
import java.util.List;
import model.Pasien;
import table.TablePasien;

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

    public TablePasien showTable(String target) {
        if (target == null || target.isBlank()) {
            return new TablePasien(dao.showData());
        }
        Pasien byId = dao.search(target);
        if (byId != null) {
            List<Pasien> hasil = new ArrayList<>();
            hasil.add(byId);
            return new TablePasien(hasil);
        }
        return new TablePasien(dao.searchByNama(target));
    }
}
