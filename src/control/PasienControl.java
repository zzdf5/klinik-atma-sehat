package control;

import dao.PasienDAO;
import java.util.List;
import model.Pasien;

public class PasienControl {
    private final PasienDAO dao = new PasienDAO();

    public void insert(Pasien data) { dao.insert(data); }
    public void update(Pasien data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Pasien> showData() { return dao.showData(); }
    public Pasien search(String id) { return dao.search(id); }
}
