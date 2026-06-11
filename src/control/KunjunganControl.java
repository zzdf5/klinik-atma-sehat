package control;

import dao.KunjunganDAO;
import java.util.List;
import model.Kunjungan;
import table.TableKunjungan;

public class KunjunganControl {
    private final KunjunganDAO dao = new KunjunganDAO();

    public String generateId() { return dao.generateId(); }
    public List<Object[]> showDataWithNames() { return dao.showDataWithNames(); }
    public List<Object[]> searchByKeyword(String keyword) { return dao.searchByKeyword(keyword); }

    public TableKunjungan showTable(String target) {
        List<Object[]> data = (target == null || target.isBlank())
                ? dao.showDataWithNames()
                : dao.searchByKeyword(target);
        return new TableKunjungan(data);
    }
    public void insert(Kunjungan data) { dao.insert(data); }
    public void update(Kunjungan data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Kunjungan> showData() { return dao.showData(); }
    public Kunjungan search(String id) { return dao.search(id); }

    public void selesaikan(Kunjungan kunjungan, String hasilPemeriksaan, String idDiagnosa, String idResep) {
        kunjungan.setHasilPemeriksaan(hasilPemeriksaan);
        kunjungan.setIdDiagnosa(idDiagnosa);
        kunjungan.setIdResep(idResep);
        kunjungan.setStatus(Kunjungan.Status.SELESAI);
        dao.update(kunjungan, kunjungan.getIdKunjungan());
    }
}
