package dao;

import connection.DBConnection;
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

        String sql = "INSERT INTO pasien (id_pasien, nomor_rekam_medis, nama, tanggal_lahir, jenis_kelamin, no_telepon, alamat) VALUES (?,?,?,?,?,?,?)";

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

        String sql = "UPDATE pasien SET nomor_rekam_medis=?, nama=?, tanggal_lahir=?, jenis_kelamin=?, no_telepon=?, alamat=? WHERE id_pasien=?";

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
        try {
            // 1. Ambil nomor_rekam_medis milik pasien ini
            PreparedStatement psGetRm = con.prepareStatement(
                "SELECT nomor_rekam_medis FROM pasien WHERE id_pasien=?");
            psGetRm.setString(1, id);
            ResultSet rsRm = psGetRm.executeQuery();
            String noRm = rsRm.next() ? rsRm.getString("nomor_rekam_medis") : null;
            rsRm.close(); psGetRm.close();

            if (noRm != null) {
                // 2. Hapus tagihan (lewat kunjungan)
                exec("DELETE tg FROM tagihan tg " +
                     "JOIN kunjungan k ON tg.id_kunjungan = k.id_kunjungan " +
                     "WHERE k.nomor_rekam_medis=?", noRm);

                // 3. Hapus rujukan (referensi kunjungan & rekam_medis)
                exec("DELETE FROM rujukan WHERE nomor_rekam_medis=?", noRm);

                // 4. Hapus kunjungan
                exec("DELETE FROM kunjungan WHERE nomor_rekam_medis=?", noRm);

                // 5. Hapus resep (resep_detail cascade otomatis)
                exec("DELETE FROM resep WHERE nomor_rekam_medis=?", noRm);

                // 6. Hapus rekam_medis (alergi & riwayat cascade otomatis)
                exec("DELETE FROM rekam_medis WHERE nomor_rekam_medis=?", noRm);
            }

            // 7. Hapus antrian
            exec("DELETE FROM antrian WHERE id_pasien=?", id);

            // 8. Hapus pasien
            exec("DELETE FROM pasien WHERE id_pasien=?", id);

            System.out.println("Pasien berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Pasien: " + e);
        }
        dbCon.closeConnection();
    }

    private void exec(String sql, String param) throws Exception {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, param);
        ps.executeUpdate();
        ps.close();
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
                            rs.getString("id_pasien"),
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

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "PAS001";
        String sql = "SELECT id_pasien FROM pasien ORDER BY id_pasien DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id_pasien");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("PAS%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Pasien: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }

    public String generateNomorRekamMedis() {
        con = dbCon.makeConnection();
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String newNorm = String.format("RM-%d-001", year);
        String sql = "SELECT MAX(CAST(SUBSTRING_INDEX(nomor_rekam_medis, '-', -1) AS UNSIGNED)) AS max_num FROM rekam_medis";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                int maxNum = rs.getInt("max_num");
                newNorm = String.format("RM-%d-%03d", year, maxNum + 1);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateNomorRekamMedis: " + e);
        }
        dbCon.closeConnection();
        return newNorm;
    }

    @Override
    public Pasien search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM pasien WHERE id_pasien=?";
        Pasien pasien = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                pasien = new Pasien(
                        rs.getString("id_pasien"),
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

    public List<Pasien> searchByNama(String nama) {
        con = dbCon.makeConnection();
        String sql = "SELECT * FROM pasien WHERE nama LIKE ?";
        List<Pasien> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nama + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Pasien(
                    rs.getString("id_pasien"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("nama"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByNama Pasien: " + e);
        }
        dbCon.closeConnection();
        return list;
    }
}
