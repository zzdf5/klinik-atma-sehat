package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
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
        String sqlGetRm = "SELECT nomor_rekam_medis FROM pasien WHERE id_pasien=?";
        String[] sqlRmDeletes = {
            "DELETE tg FROM tagihan tg "
                + "JOIN kunjungan k ON tg.id_kunjungan = k.id_kunjungan "
                + "WHERE k.nomor_rekam_medis=?",
            "DELETE FROM rujukan WHERE nomor_rekam_medis=?",
            "DELETE FROM kunjungan WHERE nomor_rekam_medis=?",
            "DELETE FROM resep WHERE nomor_rekam_medis=?",
            "DELETE FROM rekam_medis WHERE nomor_rekam_medis=?"
        };

        String sqlDeleteAntrian = "DELETE FROM antrian WHERE id_pasien=?";

        String sqlDeletePasien = "DELETE FROM pasien WHERE id_pasien=?";

        try {
            String noRm = null;

            PreparedStatement ps = con.prepareStatement(sqlGetRm);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                noRm = rs.getString("nomor_rekam_medis");
            }

            rs.close();
            ps.close();

            if (noRm != null) {
                for (String sql : sqlRmDeletes) {
                    ps = con.prepareStatement(sql);
                    ps.setString(1, noRm);
                    ps.executeUpdate();
                    ps.close();
                }
            }

            ps = con.prepareStatement(sqlDeleteAntrian);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();

            ps = con.prepareStatement(sqlDeletePasien);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();

            System.out.println("Pasien berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Pasien: " + e.getMessage());
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
                            rs.getString("id_pasien"),
                            rs.getString("nomor_rekam_medis"),
                            rs.getString("nama"),
                            rs.getString("tanggal_lahir"),
                            rs.getString("jenis_kelamin"),
                            rs.getString("no_telepon"),
                            rs.getString("alamat")
                        )
                    );
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
        int year = Calendar.getInstance().get(Calendar.YEAR);
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
                    rs.getString("alamat")
                );
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
                        rs.getString("alamat")
                    )
                );
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByNama Pasien: " + e);
        }
        dbCon.closeConnection();
        return list;
    }
    
    public Pasien searchByNomorRM(String nomorRM){
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM pasien WHERE nomor_rekam_medis=?";
        Pasien pasien = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nomorRM);

            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                pasien = new Pasien(
                    rs.getString("id_pasien"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("nama"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")
                );
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByNomorRM Pasien: " + e);
        }

        dbCon.closeConnection();
        return pasien;
    }
}
