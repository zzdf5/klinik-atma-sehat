package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Dokter;

public class DokterDAO implements IDAO<Dokter, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Dokter data) {
        con = dbCon.makeConnection();

        String sqlPengguna = "INSERT INTO pengguna (id, username, password, peran) VALUES (?,?,?,'DOKTER')";
        String sqlDokter = "INSERT INTO dokter (id, nomor_str, nama, no_telepon, spesialisasi, tarif_konsultasi, status_aktif) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getId());
            ps1.setString(2, data.getUsername());
            ps1.setString(3, data.getPassword());
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlDokter);
            ps2.setString(1, data.getId());
            ps2.setString(2, data.getNomorSTR());
            ps2.setString(3, data.getNama());
            ps2.setString(4, data.getNoTelepon());
            ps2.setString(5, data.getSpesialisasi());
            ps2.setDouble(6, data.getTarifKonsultasi());
            ps2.setBoolean(7, data.isStatusAktif());
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Dokter berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Dokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Dokter data, String id) {
        con = dbCon.makeConnection();

        String sqlPengguna = "UPDATE pengguna SET username=?, password=? WHERE id=?";
        String sqlDokter = "UPDATE dokter SET nomor_str=?, nama=?, no_telepon=?, spesialisasi=?, tarif_konsultasi=?, status_aktif=? WHERE id=?";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getUsername());
            ps1.setString(2, data.getPassword());
            ps1.setString(3, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlDokter);
            ps2.setString(1, data.getNomorSTR());
            ps2.setString(2, data.getNama());
            ps2.setString(3, data.getNoTelepon());
            ps2.setString(4, data.getSpesialisasi());
            ps2.setDouble(5, data.getTarifKonsultasi());
            ps2.setBoolean(6, data.isStatusAktif());
            ps2.setString(7, id);
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Dokter berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Dokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        // CASCADE di FK akan hapus baris dokter otomatis
        String sql = "DELETE FROM pengguna WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Dokter berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Dokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Dokter> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, d.nomor_str, d.nama, d.no_telepon, "
                + "d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
                + "FROM pengguna p JOIN dokter d ON p.id = d.id";
        List<Dokter> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Dokter dokter = new Dokter(
                            rs.getString("id"),
                            rs.getString("nomor_str"),
                            rs.getString("nama"),
                            rs.getString("no_telepon"),
                            rs.getString("spesialisasi"),
                            rs.getDouble("tarif_konsultasi"),
                            rs.getString("username"),
                            rs.getString("password"));
                    dokter.setStatusAktif(rs.getBoolean("status_aktif"));
                    list.add(dokter);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Dokter: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public List<Dokter> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, d.nomor_str, d.nama, d.no_telepon, "
                + "d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
                + "FROM pengguna p JOIN dokter d ON p.id = d.id "
                + "WHERE p.id LIKE ? OR d.nama LIKE ?";
        List<Dokter> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String param = "%" + keyword + "%";
            ps.setString(1, param);
            ps.setString(2, param);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Dokter dokter = new Dokter(
                            rs.getString("id"),
                            rs.getString("nomor_str"),
                            rs.getString("nama"),
                            rs.getString("no_telepon"),
                            rs.getString("spesialisasi"),
                            rs.getDouble("tarif_konsultasi"),
                            rs.getString("username"),
                            rs.getString("password"));
                    dokter.setStatusAktif(rs.getBoolean("status_aktif"));
                    list.add(dokter);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByKeyword Dokter: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "DOK001";
        String sql = "SELECT id FROM pengguna WHERE peran='DOKTER' ORDER BY id DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("DOK%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Dokter: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }

    public Dokter searchByCredential(String username, String password) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, d.nomor_str, d.nama, d.no_telepon, "
                + "d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
                + "FROM pengguna p JOIN dokter d ON p.id = d.id "
                + "WHERE p.username=? AND p.password=? AND p.peran='DOKTER'";
        Dokter dokter = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                dokter = new Dokter(
                        rs.getString("id"),
                        rs.getString("nomor_str"),
                        rs.getString("nama"),
                        rs.getString("no_telepon"),
                        rs.getString("spesialisasi"),
                        rs.getDouble("tarif_konsultasi"),
                        rs.getString("username"),
                        rs.getString("password"));
                dokter.setStatusAktif(rs.getBoolean("status_aktif"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByCredential Dokter: " + e);
        }

        dbCon.closeConnection();
        return dokter;
    }

    @Override
    public Dokter search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, d.nomor_str, d.nama, d.no_telepon, "
                + "d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
                + "FROM pengguna p JOIN dokter d ON p.id = d.id WHERE p.id=?";
        Dokter dokter = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                dokter = new Dokter(
                        rs.getString("id"),
                        rs.getString("nomor_str"),
                        rs.getString("nama"),
                        rs.getString("no_telepon"),
                        rs.getString("spesialisasi"),
                        rs.getDouble("tarif_konsultasi"),
                        rs.getString("username"),
                        rs.getString("password"));
                dokter.setStatusAktif(rs.getBoolean("status_aktif"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Dokter: " + e);
        }

        dbCon.closeConnection();
        return dokter;
    }
}
