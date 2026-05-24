package dao;

import Connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Pasien;

public class PasienDAO implements IDAO<Pasien, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Pasien data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO pasien (id, nomor_rekam_medis, nama, tanggal_lahir, jenis_kelamin, no_telepon, alamat) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getId());
            ps.setString(2, data.getNomorRekamMedis());
            ps.setString(3, data.getNama());
            ps.setString(4, data.getTanggalLahir());
            ps.setString(5, data.getJenisKelamin());
            ps.setString(6, data.getNoTelepon());
            ps.setString(7, data.getAlamat());
            ps.executeUpdate();
            ps.close();
            System.out.println("Pasien berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Pasien: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Pasien data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE pasien SET nomor_rekam_medis=?, nama=?, tanggal_lahir=?, jenis_kelamin=?, no_telepon=?, alamat=? WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getNomorRekamMedis());
            ps.setString(2, data.getNama());
            ps.setString(3, data.getTanggalLahir());
            ps.setString(4, data.getJenisKelamin());
            ps.setString(5, data.getNoTelepon());
            ps.setString(6, data.getAlamat());
            ps.setString(7, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Pasien berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Pasien: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM pasien WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Pasien berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Pasien: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Pasien> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM pasien";
        List<Pasien> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new Pasien(
                            rs.getString("id"),
                            rs.getString("nomor_rekam_medis"),
                            rs.getString("nama"),
                            rs.getString("tanggal_lahir"),
                            rs.getString("jenis_kelamin"),
                            rs.getString("no_telepon"),
                            rs.getString("alamat")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Pasien: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Pasien search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM pasien WHERE id=?";
        Pasien pasien = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                pasien = new Pasien(
                        rs.getString("id"),
                        rs.getString("nomor_rekam_medis"),
                        rs.getString("nama"),
                        rs.getString("tanggal_lahir"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("no_telepon"),
                        rs.getString("alamat"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Pasien: " + e);
        }

        dbCon.closeConnection();
        return pasien;
    }
}
