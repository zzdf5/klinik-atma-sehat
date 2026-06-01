package control;

import dao.ResepDAO;
import java.util.List;
import model.Resep;

public class ResepControl {
    private final ResepDAO dao = new ResepDAO();

    public String generateIdResep() { return dao.generateId(); }
    public void insert(Resep data) { dao.insert(data); }
    public void update(Resep data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Resep> showData() { return dao.showData(); }
    public Resep search(String id) { return dao.search(id); }

    public void ubahStatus(Resep resep, Resep.Status statusBaru) {
        resep.setStatus(statusBaru);
        dao.update(resep, resep.getIdResep());
    }
}
