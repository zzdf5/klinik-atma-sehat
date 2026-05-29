package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Admin;

public class AdminDAO implements IDAO<Admin, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Admin data) {
        con = dbCon.makeConnection();

        String sqlPengguna = "INSERT INTO pengguna (id, username, password, peran) VALUES (?,?,?,'ADMIN')";
        String sqlAdmin = "INSERT INTO admin (id, nama, no_telepon, jabatan, status_aktif) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getId());
            ps1.setString(2, data.getUsername());
            ps1.setString(3, data.getPassword());
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlAdmin);
            ps2.setString(1, data.getId());
            ps2.setString(2, data.getNama());
            ps2.setString(3, data.getNoTelepon());
            ps2.setString(4, data.getJabatan().name());
            ps2.setBoolean(5, data.isStatusAktif());
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Admin berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Admin: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Admin data, String id) {
        con = dbCon.makeConnection();

        String sqlPengguna = "UPDATE pengguna SET username=?, password=? WHERE id=?";
        String sqlAdmin = "UPDATE admin SET nama=?, no_telepon=?, jabatan=?, status_aktif=? WHERE id=?";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlPengguna);
            ps1.setString(1, data.getUsername());
            ps1.setString(2, data.getPassword());
            ps1.setString(3, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlAdmin);
            ps2.setString(1, data.getNama());
            ps2.setString(2, data.getNoTelepon());
            ps2.setString(3, data.getJabatan().name());
            ps2.setBoolean(4, data.isStatusAktif());
            ps2.setString(5, id);
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Admin berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Admin: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        // CASCADE di FK akan hapus baris admin otomatis
        String sql = "DELETE FROM pengguna WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Admin berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Admin: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Admin> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, a.nama, a.no_telepon, a.jabatan, a.status_aktif "
                + "FROM pengguna p JOIN admin a ON p.id = a.id";
        List<Admin> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Admin admin = new Admin(
                            rs.getString("id"),
                            rs.getString("nama"),
                            rs.getString("no_telepon"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Admin.Jabatan.valueOf(rs.getString("jabatan")));
                    admin.setStatusAktif(rs.getBoolean("status_aktif"));
                    list.add(admin);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Admin: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public Admin searchByCredential(String username, String password) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, a.nama, a.no_telepon, a.jabatan, a.status_aktif "
                + "FROM pengguna p JOIN admin a ON p.id = a.id "
                + "WHERE p.username=? AND p.password=? AND p.peran='ADMIN'";
        Admin admin = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                admin = new Admin(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("no_telepon"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Admin.Jabatan.valueOf(rs.getString("jabatan")));
                admin.setStatusAktif(rs.getBoolean("status_aktif"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByCredential Admin: " + e);
        }

        dbCon.closeConnection();
        return admin;
    }

    @Override
    public Admin search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT p.id, p.username, p.password, a.nama, a.no_telepon, a.jabatan, a.status_aktif "
                + "FROM pengguna p JOIN admin a ON p.id = a.id WHERE p.id=?";
        Admin admin = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                admin = new Admin(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("no_telepon"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Admin.Jabatan.valueOf(rs.getString("jabatan")));
                admin.setStatusAktif(rs.getBoolean("status_aktif"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Admin: " + e);
        }

        dbCon.closeConnection();
        return admin;
    }
}
