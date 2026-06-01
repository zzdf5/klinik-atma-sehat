package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Diagnosa;

public class DiagnosaDAO implements IDAO<Diagnosa, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Diagnosa data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO diagnosa (id_diagnosa, kode_penyakit, nama_penyakit, keterangan, tanggal_diagnosa, perlu_rujukan) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdDiagnosa());
            ps.setString(2, data.getKodePenyakit());
            ps.setString(3, data.getNamaPenyakit());
            ps.setString(4, data.getKeterangan());
            ps.setString(5, data.getTanggalDiagnosa());
            ps.setBoolean(6, data.isPerluRujukan());
            ps.executeUpdate();
            ps.close();
            System.out.println("Diagnosa berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Diagnosa: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Diagnosa data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE diagnosa SET kode_penyakit=?, nama_penyakit=?, keterangan=?, tanggal_diagnosa=?, perlu_rujukan=? WHERE id_diagnosa=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getKodePenyakit());
            ps.setString(2, data.getNamaPenyakit());
            ps.setString(3, data.getKeterangan());
            ps.setString(4, data.getTanggalDiagnosa());
            ps.setBoolean(5, data.isPerluRujukan());
            ps.setString(6, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Diagnosa berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Diagnosa: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM diagnosa WHERE id_diagnosa=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Diagnosa berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Diagnosa: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Diagnosa> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM diagnosa";
        List<Diagnosa> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new Diagnosa(
                            rs.getString("id_diagnosa"),
                            rs.getString("kode_penyakit"),
                            rs.getString("nama_penyakit"),
                            rs.getString("keterangan"),
                            rs.getString("tanggal_diagnosa"),
                            rs.getBoolean("perlu_rujukan")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Diagnosa: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Diagnosa search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM diagnosa WHERE id_diagnosa=?";
        Diagnosa diagnosa = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                diagnosa = new Diagnosa(
                        rs.getString("id_diagnosa"),
                        rs.getString("kode_penyakit"),
                        rs.getString("nama_penyakit"),
                        rs.getString("keterangan"),
                        rs.getString("tanggal_diagnosa"),
                        rs.getBoolean("perlu_rujukan"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Diagnosa: " + e);
        }

        dbCon.closeConnection();
        return diagnosa;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "DIS001";
        String sql = "SELECT id_diagnosa FROM diagnosa ORDER BY id_diagnosa DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id_diagnosa");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("DIS%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Diagnosa: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }
}
