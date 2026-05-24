package control;

import dao.ObatDAO;
import dao.ObatHerbalDAO;
import dao.ObatPatenDAO;
import java.util.ArrayList;
import java.util.List;
import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;

public class ObatControl {
    private final ObatDAO dao = new ObatDAO();
    private final ObatHerbalDAO herbalDAO = new ObatHerbalDAO();
    private final ObatPatenDAO patenDAO = new ObatPatenDAO();

    public void insert(Obat data) { dao.insert(data); }
    public void insertHerbal(ObatHerbal data) { herbalDAO.insert(data); }
    public void insertPaten(ObatPaten data) { patenDAO.insert(data); }

    public void update(Obat data, String id) { dao.update(data, id); }
    public void updateHerbal(ObatHerbal data, String id) { herbalDAO.update(data, id); }
    public void updatePaten(ObatPaten data, String id) { patenDAO.update(data, id); }

    public void delete(String id) { dao.delete(id); }

    public Obat search(String id) { return dao.search(id); }
    public ObatHerbal searchHerbal(String id) { return herbalDAO.search(id); }
    public ObatPaten searchPaten(String id) { return patenDAO.search(id); }

    public List<Obat> showData() { return dao.showData(); }
    public List<ObatHerbal> showDataHerbal() { return herbalDAO.showData(); }
    public List<ObatPaten> showDataPaten() { return patenDAO.showData(); }

    public List<Obat> showSemua() {
        List<Obat> semua = new ArrayList<>();
        semua.addAll(dao.showData());
        semua.addAll(herbalDAO.showData());
        semua.addAll(patenDAO.showData());
        return semua;
    }
}
