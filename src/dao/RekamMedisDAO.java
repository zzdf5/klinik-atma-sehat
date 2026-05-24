package dao;

import Connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.RekamMedis;

public class RekamMedisDAO implements IDAO<RekamMedis, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(RekamMedis data) {
        con = dbCon.makeConnection();

        String sqlRekam = "INSERT INTO rekam_medis (nomor_rekam_medis, id_pasien, tanggal_buat) VALUES (?,?,?)";
        String sqlAlergi = "INSERT INTO rekam_medis_alergi (nomor_rekam_medis, alergi) VALUES (?,?)";
        String sqlRiwayat = "INSERT INTO rekam_medis_riwayat_penyakit (nomor_rekam_medis, riwayat_penyakit) VALUES (?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlRekam);
            ps1.setString(1, data.getNomorRekamMedis());
            ps1.setString(2, data.getIdPasien());
            ps1.setString(3, data.getTanggalBuat());
            ps1.executeUpdate();
            ps1.close();

            for (String alergi : data.getAlergi()) {
                PreparedStatement ps2 = con.prepareStatement(sqlAlergi);
                ps2.setString(1, data.getNomorRekamMedis());
                ps2.setString(2, alergi);
                ps2.executeUpdate();
                ps2.close();
            }

            for (String riwayat : data.getRiwayatPenyakit()) {
                PreparedStatement ps3 = con.prepareStatement(sqlRiwayat);
                ps3.setString(1, data.getNomorRekamMedis());
                ps3.setString(2, riwayat);
                ps3.executeUpdate();
                ps3.close();
            }

            System.out.println("Rekam Medis berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert RekamMedis: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(RekamMedis data, String nomorRekamMedis) {
        con = dbCon.makeConnection();

        String sqlRekam = "UPDATE rekam_medis SET id_pasien=?, tanggal_buat=? WHERE nomor_rekam_medis=?";
        String sqlHapusAlergi = "DELETE FROM rekam_medis_alergi WHERE nomor_rekam_medis=?";
        String sqlHapusRiwayat = "DELETE FROM rekam_medis_riwayat_penyakit WHERE nomor_rekam_medis=?";
        String sqlAlergi = "INSERT INTO rekam_medis_alergi (nomor_rekam_medis, alergi) VALUES (?,?)";
        String sqlRiwayat = "INSERT INTO rekam_medis_riwayat_penyakit (nomor_rekam_medis, riwayat_penyakit) VALUES (?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlRekam);
            ps1.setString(1, data.getIdPasien());
            ps1.setString(2, data.getTanggalBuat());
            ps1.setString(3, nomorRekamMedis);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement psHapusA = con.prepareStatement(sqlHapusAlergi);
            psHapusA.setString(1, nomorRekamMedis);
            psHapusA.executeUpdate();
            psHapusA.close();

            PreparedStatement psHapusR = con.prepareStatement(sqlHapusRiwayat);
            psHapusR.setString(1, nomorRekamMedis);
            psHapusR.executeUpdate();
            psHapusR.close();

            for (String alergi : data.getAlergi()) {
                PreparedStatement ps2 = con.prepareStatement(sqlAlergi);
                ps2.setString(1, nomorRekamMedis);
                ps2.setString(2, alergi);
                ps2.executeUpdate();
                ps2.close();
            }

            for (String riwayat : data.getRiwayatPenyakit()) {
                PreparedStatement ps3 = con.prepareStatement(sqlRiwayat);
                ps3.setString(1, nomorRekamMedis);
                ps3.setString(2, riwayat);
                ps3.executeUpdate();
                ps3.close();
            }

            System.out.println("Rekam Medis berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update RekamMedis: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String nomorRekamMedis) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM rekam_medis WHERE nomor_rekam_medis=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nomorRekamMedis);
            ps.executeUpdate();
            ps.close();
            System.out.println("Rekam Medis berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete RekamMedis: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<RekamMedis> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM rekam_medis";
        List<RekamMedis> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    RekamMedis rm = new RekamMedis(
                            rs.getString("nomor_rekam_medis"),
                            rs.getString("id_pasien"),
                            rs.getString("tanggal_buat"));
                    loadAlergiDanRiwayat(rm);
                    list.add(rm);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData RekamMedis: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public RekamMedis search(String nomorRekamMedis) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM rekam_medis WHERE nomor_rekam_medis=?";
        RekamMedis rm = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nomorRekamMedis);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                rm = new RekamMedis(
                        rs.getString("nomor_rekam_medis"),
                        rs.getString("id_pasien"),
                        rs.getString("tanggal_buat"));
                loadAlergiDanRiwayat(rm);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search RekamMedis: " + e);
        }

        dbCon.closeConnection();
        return rm;
    }

    private void loadAlergiDanRiwayat(RekamMedis rm) {
        String sqlAlergi = "SELECT alergi FROM rekam_medis_alergi WHERE nomor_rekam_medis=?";
        String sqlRiwayat = "SELECT riwayat_penyakit FROM rekam_medis_riwayat_penyakit WHERE nomor_rekam_medis=?";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlAlergi);
            ps1.setString(1, rm.getNomorRekamMedis());
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                rm.tambahAlergi(rs1.getString("alergi"));
            }
            rs1.close();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlRiwayat);
            ps2.setString(1, rm.getNomorRekamMedis());
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                rm.tambahRiwayatPenyakit(rs2.getString("riwayat_penyakit"));
            }
            rs2.close();
            ps2.close();
        } catch (Exception e) {
            System.out.println("Error loading alergi/riwayat: " + e);
        }
    }
}
