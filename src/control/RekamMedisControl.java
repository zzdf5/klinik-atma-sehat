package control;

import dao.RekamMedisDAO;
import java.util.List;
import model.RekamMedis;

public class RekamMedisControl {
    private final RekamMedisDAO dao = new RekamMedisDAO();

    public void insert(RekamMedis data) { dao.insert(data); }
    public void update(RekamMedis data, String nomorRekamMedis) { dao.update(data, nomorRekamMedis); }
    public void delete(String nomorRekamMedis) { dao.delete(nomorRekamMedis); }
    public List<RekamMedis> showData() { return dao.showData(); }
    public RekamMedis search(String nomorRekamMedis) { return dao.search(nomorRekamMedis); }
}
