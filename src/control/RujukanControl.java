package control;

import dao.RujukanDAO;
import java.util.List;
import model.Rujukan;

public class RujukanControl {
    private final RujukanDAO dao = new RujukanDAO();

    public String generateIdRujukan() { return dao.generateId(); }
    public void insert(Rujukan data) { dao.insert(data); }
    public void update(Rujukan data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Rujukan> showData() { return dao.showData(); }
    public Rujukan search(String id) { return dao.search(id); }

    public void ubahStatus(Rujukan rujukan, Rujukan.Status statusBaru) {
        rujukan.setStatus(statusBaru);
        dao.update(rujukan, rujukan.getIdRujukan());
    }
}
