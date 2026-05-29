package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Poliklinik;

public class PoliklinikDAO implements IDAO<Poliklinik, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Poliklinik data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO poliklinik (id_poliklinik, nama_poliklinik, lokasi_ruangan, jam_operasional) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdPoliklinik());
            ps.setString(2, data.getNamaPoliklinik());
            ps.setString(3, data.getLokasiRuangan());
            ps.setString(4, data.getJamOperasional());
            ps.executeUpdate();
            ps.close();
            System.out.println("Poliklinik berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Poliklinik: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Poliklinik data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE poliklinik SET nama_poliklinik=?, lokasi_ruangan=?, jam_operasional=? WHERE id_poliklinik=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getNamaPoliklinik());
            ps.setString(2, data.getLokasiRuangan());
            ps.setString(3, data.getJamOperasional());
            ps.setString(4, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Poliklinik berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Poliklinik: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM poliklinik WHERE id_poliklinik=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Poliklinik berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Poliklinik: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Poliklinik> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM poliklinik";
        List<Poliklinik> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new Poliklinik(
                            rs.getString("id_poliklinik"),
                            rs.getString("nama_poliklinik"),
                            rs.getString("lokasi_ruangan"),
                            rs.getString("jam_operasional")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Poliklinik: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public List<Poliklinik> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM poliklinik WHERE id_poliklinik LIKE ? OR nama_poliklinik LIKE ?";
        List<Poliklinik> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String param = "%" + keyword + "%";
            ps.setString(1, param);
            ps.setString(2, param);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new Poliklinik(
                            rs.getString("id_poliklinik"),
                            rs.getString("nama_poliklinik"),
                            rs.getString("lokasi_ruangan"),
                            rs.getString("jam_operasional")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByKeyword Poliklinik: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "POL001";
        String sql = "SELECT MAX(CAST(SUBSTRING(id_poliklinik, 4) AS UNSIGNED)) AS max_num FROM poliklinik WHERE id_poliklinik LIKE 'POL%'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                int maxNum = rs.getInt("max_num");
                newId = String.format("POL%03d", maxNum + 1);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Poliklinik: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }

    @Override
    public Poliklinik search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM poliklinik WHERE id_poliklinik=?";
        Poliklinik poliklinik = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                poliklinik = new Poliklinik(
                        rs.getString("id_poliklinik"),
                        rs.getString("nama_poliklinik"),
                        rs.getString("lokasi_ruangan"),
                        rs.getString("jam_operasional"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Poliklinik: " + e);
        }

        dbCon.closeConnection();
        return poliklinik;
    }
}
