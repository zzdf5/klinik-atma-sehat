package control;

import dao.AdminDAO;
import java.util.List;
import model.Admin;

public class AdminControl {
    private final AdminDAO dao = new AdminDAO();

    public void insert(Admin data) { dao.insert(data); }
    public void update(Admin data, String id) { dao.update(data, id); }
    public void delete(String id) { dao.delete(id); }
    public List<Admin> showData() { return dao.showData(); }
    public Admin search(String id) { return dao.search(id); }
}
