package dao;

import Connection.DBConnection;
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

        String sqlPengguna = "INSERT INTO pengguna (id, nama, no_telepon, username, password, peran) VALUES (?,?,?,?,?,'DOKTER')";
        String sqlDokter = "INSERT INTO dokter (id, nomor_str, spesialisasi, tarif_konsultasi, status_aktif) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getId());
            ps1.setString(2, data.getNama());
            ps1.setString(3, data.getNoTelepon());
            ps1.setString(4, data.getUsername());
            ps1.setString(5, data.getPassword());
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlDokter);
            ps2.setString(1, data.getId());
            ps2.setString(2, data.getNomorSTR());
            ps2.setString(3, data.getSpesialisasi());
            ps2.setDouble(4, data.getTarifKonsultasi());
            ps2.setBoolean(5, data.isStatusAktif());
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

        String sqlPengguna = "UPDATE pengguna SET nama=?, no_telepon=?, username=?, password=? WHERE id=?";
        String sqlDokter = "UPDATE dokter SET nomor_str=?, spesialisasi=?, tarif_konsultasi=?, status_aktif=? WHERE id=?";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getNama());
            ps1.setString(2, data.getNoTelepon());
            ps1.setString(3, data.getUsername());
            ps1.setString(4, data.getPassword());
            ps1.setString(5, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlDokter);
            ps2.setString(1, data.getNomorSTR());
            ps2.setString(2, data.getSpesialisasi());
            ps2.setDouble(3, data.getTarifKonsultasi());
            ps2.setBoolean(4, data.isStatusAktif());
            ps2.setString(5, id);
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

        // Cascade delete akan menghapus baris di tabel dokter otomatis
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

        String sql = "SELECT p.id, p.nama, p.no_telepon, p.username, p.password, "
                + "d.nomor_str, d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
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

    @Override
    public Dokter search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.nama, p.no_telepon, p.username, p.password, "
                + "d.nomor_str, d.spesialisasi, d.tarif_konsultasi, d.status_aktif "
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
