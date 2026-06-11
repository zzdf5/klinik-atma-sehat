package control;

import dao.AntrianDAO;
import java.util.List;
import model.Antrian;
import table.TableAntrian;

public class AntrianControl {
    private final AntrianDAO dao = new AntrianDAO();

    public int generateNomorUrut(String tanggal) { return dao.generateNomorUrut(tanggal); }
    public List<Object[]> showDataWithNames() { return dao.showDataWithNames(); }
    public List<Object[]> searchByKeyword(String keyword) { return dao.searchByKeyword(keyword); }

    public TableAntrian showTable(String target) {
        List<Object[]> data = (target == null || target.isBlank())
                ? dao.showDataWithNames()
                : dao.searchByKeyword(target);
        return new TableAntrian(data);
    }

    public void insert(Antrian data) { dao.insert(data); }
    public void update(Antrian data, Integer id) { dao.update(data, id); }
    public void delete(Integer id) { dao.delete(id); }
    public List<Antrian> showData() { return dao.showData(); }
    public Antrian search(Integer id) { return dao.search(id); }

    public void ubahStatus(Antrian antrian, Antrian.Status statusBaru) {
        antrian.setStatus(statusBaru);
        dao.update(antrian, antrian.getIdAntrian());
    }
}
