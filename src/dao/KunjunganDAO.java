package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Kunjungan;

public class KunjunganDAO implements IDAO<Kunjungan, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Kunjungan data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO kunjungan (id_kunjungan, nomor_rekam_medis, id_dokter, tanggal, jam, keluhan_utama, hasil_pemeriksaan, id_diagnosa, id_resep, biaya_konsultasi, status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdKunjungan());
            ps.setString(2, data.getNomorRekamMedis());
            ps.setString(3, data.getIdDokter());
            ps.setString(4, data.getTanggal());
            ps.setString(5, data.getJam());
            ps.setString(6, data.getKeluhanUtama());
            ps.setString(7, data.getHasilPemeriksaan());
            ps.setString(8, data.getIdDiagnosa());
            ps.setString(9, data.getIdResep());
            ps.setDouble(10, data.getBiayaKonsultasi());
            ps.setString(11, data.getStatus().name());
            ps.executeUpdate();
            ps.close();
            System.out.println("Kunjungan berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Kunjungan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Kunjungan data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE kunjungan SET nomor_rekam_medis=?, id_dokter=?, tanggal=?, jam=?, keluhan_utama=?, hasil_pemeriksaan=?, id_diagnosa=?, id_resep=?, biaya_konsultasi=?, status=? WHERE id_kunjungan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getNomorRekamMedis());
            ps.setString(2, data.getIdDokter());
            ps.setString(3, data.getTanggal());
            ps.setString(4, data.getJam());
            ps.setString(5, data.getKeluhanUtama());
            ps.setString(6, data.getHasilPemeriksaan());
            ps.setString(7, data.getIdDiagnosa());
            ps.setString(8, data.getIdResep());
            ps.setDouble(9, data.getBiayaKonsultasi());
            ps.setString(10, data.getStatus().name());
            ps.setString(11, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Kunjungan berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Kunjungan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM kunjungan WHERE id_kunjungan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Kunjungan berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Kunjungan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Kunjungan> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM kunjungan";
        List<Kunjungan> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Kunjungan k = new Kunjungan(
                            rs.getString("id_kunjungan"),
                            rs.getString("nomor_rekam_medis"),
                            rs.getString("tanggal"),
                            rs.getString("jam"),
                            rs.getString("keluhan_utama"));
                    k.setIdDokter(rs.getString("id_dokter"));
                    k.setHasilPemeriksaan(rs.getString("hasil_pemeriksaan"));
                    k.setIdDiagnosa(rs.getString("id_diagnosa"));
                    k.setIdResep(rs.getString("id_resep"));
                    k.setBiayaKonsultasi(rs.getDouble("biaya_konsultasi"));
                    k.setStatus(Kunjungan.Status.valueOf(rs.getString("status")));
                    list.add(k);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Kunjungan: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public List<Object[]> showDataWithNames() {
        con = dbCon.makeConnection();
        String sql = "SELECT k.id_kunjungan, k.nomor_rekam_medis, "
                + "COALESCE(p.nama, '-') AS nama_pasien, "
                + "COALESCE(d.nama, '-') AS nama_dokter, "
                + "k.tanggal, k.jam, k.status "
                + "FROM kunjungan k "
                + "LEFT JOIN pasien p ON k.nomor_rekam_medis = p.nomor_rekam_medis "
                + "LEFT JOIN dokter d ON k.id_dokter = d.id "
                + "ORDER BY k.tanggal DESC, k.jam DESC";
        List<Object[]> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_kunjungan"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("nama_pasien"),
                    rs.getString("nama_dokter"),
                    rs.getString("tanggal"),
                    rs.getString("jam"),
                    rs.getString("status")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showDataWithNames Kunjungan: " + e);
        }
        dbCon.closeConnection();
        return list;
    }

    public List<Object[]> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();
        String sql = "SELECT k.id_kunjungan, k.nomor_rekam_medis, "
                + "COALESCE(p.nama, '-') AS nama_pasien, "
                + "COALESCE(d.nama, '-') AS nama_dokter, "
                + "k.tanggal, k.jam, k.status "
                + "FROM kunjungan k "
                + "LEFT JOIN pasien p ON k.nomor_rekam_medis = p.nomor_rekam_medis "
                + "LEFT JOIN dokter d ON k.id_dokter = d.id "
                + "WHERE k.id_kunjungan LIKE ? OR p.nama LIKE ? OR d.nama LIKE ? "
                + "ORDER BY k.tanggal DESC, k.jam DESC";
        List<Object[]> list = new ArrayList<>();
        String param = "%" + keyword + "%";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ps.setString(2, param);
            ps.setString(3, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_kunjungan"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("nama_pasien"),
                    rs.getString("nama_dokter"),
                    rs.getString("tanggal"),
                    rs.getString("jam"),
                    rs.getString("status")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByKeyword Kunjungan: " + e);
        }
        dbCon.closeConnection();
        return list;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "KUN001";
        String sql = "SELECT id_kunjungan FROM kunjungan ORDER BY id_kunjungan DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id_kunjungan");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("KUN%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Kunjungan: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }

    @Override
    public Kunjungan search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM kunjungan WHERE id_kunjungan=?";
        Kunjungan kunjungan = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                kunjungan = new Kunjungan(
                        rs.getString("id_kunjungan"),
                        rs.getString("nomor_rekam_medis"),
                        rs.getString("tanggal"),
                        rs.getString("jam"),
                        rs.getString("keluhan_utama")
                );
                kunjungan.setIdDokter(rs.getString("id_dokter"));
                kunjungan.setHasilPemeriksaan(rs.getString("hasil_pemeriksaan"));
                kunjungan.setIdDiagnosa(rs.getString("id_diagnosa"));
                kunjungan.setIdResep(rs.getString("id_resep"));
                kunjungan.setBiayaKonsultasi(rs.getDouble("biaya_konsultasi"));
                kunjungan.setStatus(Kunjungan.Status.valueOf(rs.getString("status")));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Kunjungan: " + e);
        }

        dbCon.closeConnection();
        return kunjungan;
    }
}
