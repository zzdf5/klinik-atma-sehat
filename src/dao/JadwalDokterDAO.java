package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.JadwalDokter;

public class JadwalDokterDAO implements IDAO<JadwalDokter, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(JadwalDokter data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO jadwal_dokter (id_jadwal, id_dokter, id_poliklinik, jam_mulai, jam_selesai, kuota_pasien) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdJadwal());
            ps.setString(2, data.getIdDokter());
            ps.setString(3, data.getIdPoliklinik());
            ps.setString(4, data.getJamMulai());
            ps.setString(5, data.getJamSelesai());
            ps.setInt(6, data.getKuotaPasien());
            ps.executeUpdate();
            ps.close();
            System.out.println("Jadwal Dokter berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert JadwalDokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(JadwalDokter data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE jadwal_dokter SET id_dokter=?, id_poliklinik=?, jam_mulai=?, jam_selesai=?, kuota_pasien=? WHERE id_jadwal=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdDokter());
            ps.setString(2, data.getIdPoliklinik());
            ps.setString(3, data.getJamMulai());
            ps.setString(4, data.getJamSelesai());
            ps.setInt(5, data.getKuotaPasien());
            ps.setString(6, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Jadwal Dokter berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update JadwalDokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM jadwal_dokter WHERE id_jadwal=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Jadwal Dokter berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete JadwalDokter: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<JadwalDokter> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM jadwal_dokter";
        List<JadwalDokter> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new JadwalDokter(
                            rs.getString("id_jadwal"),
                            rs.getString("id_dokter"),
                            rs.getString("id_poliklinik"),
                            rs.getString("jam_mulai"),
                            rs.getString("jam_selesai"),
                            rs.getInt("kuota_pasien")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData JadwalDokter: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "JDW001";
        String sql = "SELECT id_jadwal FROM jadwal_dokter ORDER BY id_jadwal DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String last = rs.getString("id_jadwal");
                int num = Integer.parseInt(last.substring(3)) + 1;
                newId = String.format("JDW%03d", num);
            }
            rs.close(); ps.close();
        } catch (Exception e) { System.out.println("Error generateId JadwalDokter: " + e); }
        dbCon.closeConnection();
        return newId;
    }

    public List<Object[]> showDataWithNames() {
        con = dbCon.makeConnection();
        String sql = "SELECT j.id_jadwal, COALESCE(d.nama,'-') AS nama_dokter, "
                + "j.jam_mulai, j.jam_selesai, "
                + "COALESCE(p.nama_poliklinik,'-') AS nama_poliklinik, j.kuota_pasien "
                + "FROM jadwal_dokter j "
                + "LEFT JOIN dokter d ON j.id_dokter = d.id "
                + "LEFT JOIN poliklinik p ON j.id_poliklinik = p.id_poliklinik "
                + "ORDER BY d.nama, j.jam_mulai";
        List<Object[]> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_jadwal"), rs.getString("nama_dokter"),
                    rs.getString("jam_mulai"), rs.getString("jam_selesai"),
                    rs.getString("nama_poliklinik"), rs.getInt("kuota_pasien")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { System.out.println("Error showDataWithNames JadwalDokter: " + e); }
        dbCon.closeConnection();
        return list;
    }

    public List<Object[]> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();
        String sql = "SELECT j.id_jadwal, COALESCE(d.nama,'-') AS nama_dokter, "
                + "j.jam_mulai, j.jam_selesai, "
                + "COALESCE(p.nama_poliklinik,'-') AS nama_poliklinik, j.kuota_pasien "
                + "FROM jadwal_dokter j "
                + "LEFT JOIN dokter d ON j.id_dokter = d.id "
                + "LEFT JOIN poliklinik p ON j.id_poliklinik = p.id_poliklinik "
                + "WHERE j.id_jadwal LIKE ? OR d.nama LIKE ? OR p.nama_poliklinik LIKE ? "
                + "ORDER BY d.nama, j.jam_mulai";
        List<Object[]> list = new ArrayList<>();
        String param = "%" + keyword + "%";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param); ps.setString(2, param); ps.setString(3, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_jadwal"), rs.getString("nama_dokter"),
                    rs.getString("jam_mulai"), rs.getString("jam_selesai"),
                    rs.getString("nama_poliklinik"), rs.getInt("kuota_pasien")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { System.out.println("Error searchByKeyword JadwalDokter: " + e); }
        dbCon.closeConnection();
        return list;
    }

    // Dipakai di panel kunjungan untuk isi dropdown jam berdasarkan dokter yang dipilih
    public List<Object[]> searchByDokterWithNames(String idDokter) {
        con = dbCon.makeConnection();
        String sql = "SELECT j.id_jadwal, j.jam_mulai, j.jam_selesai, "
                + "COALESCE(p.nama_poliklinik,'-') AS nama_poliklinik "
                + "FROM jadwal_dokter j "
                + "LEFT JOIN poliklinik p ON j.id_poliklinik = p.id_poliklinik "
                + "WHERE j.id_dokter = ? ORDER BY j.jam_mulai";
        List<Object[]> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idDokter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_jadwal"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai"),
                    rs.getString("nama_poliklinik")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { System.out.println("Error searchByDokterWithNames: " + e); }
        dbCon.closeConnection();
        return list;
    }

    @Override
    public JadwalDokter search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM jadwal_dokter WHERE id_jadwal=?";
        JadwalDokter jadwal = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                jadwal = new JadwalDokter(
                        rs.getString("id_jadwal"),
                        rs.getString("id_dokter"),
                        rs.getString("id_poliklinik"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selesai"),
                        rs.getInt("kuota_pasien"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search JadwalDokter: " + e);
        }

        dbCon.closeConnection();
        return jadwal;
    }
}
