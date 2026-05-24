package control;

import dao.JadwalDokterDAO;
import java.util.List;
import model.JadwalDokter;

public class JadwalDokterControl {
    private final JadwalDokterDAO dao = new JadwalDokterDAO();

    public void insert(JadwalDokter data) { dao.insert(data); }
    public void update(JadwalDokter data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<JadwalDokter> showData() { return dao.showData(); }
    public JadwalDokter search(String id) { return dao.search(id); }
}
