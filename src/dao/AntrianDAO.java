package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Antrian;

public class AntrianDAO implements IDAO<Antrian, Integer> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Antrian data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO antrian (nomor_urut, id_pasien, id_dokter, id_poliklinik, tanggal, status, jenis_kunjungan) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, data.getNomorUrut());
            ps.setString(2, data.getIdPasien());
            ps.setString(3, data.getIdDokter());
            ps.setString(4, data.getIdPoliklinik());
            ps.setString(5, data.getTanggal());
            ps.setString(6, data.getStatus().name());
            ps.setString(7, data.getJenisKunjungan().name());
            ps.executeUpdate();
            ps.close();
            System.out.println("Antrian berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Antrian: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Antrian data, Integer id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE antrian SET nomor_urut=?, id_pasien=?, id_dokter=?, id_poliklinik=?, tanggal=?, status=?, jenis_kunjungan=? WHERE id_antrian=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, data.getNomorUrut());
            ps.setString(2, data.getIdPasien());
            ps.setString(3, data.getIdDokter());
            ps.setString(4, data.getIdPoliklinik());
            ps.setString(5, data.getTanggal());
            ps.setString(6, data.getStatus().name());
            ps.setString(7, data.getJenisKunjungan().name());
            ps.setInt(8, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Antrian berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Antrian: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(Integer id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM antrian WHERE id_antrian=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Antrian berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Antrian: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Antrian> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM antrian";
        List<Antrian> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Antrian antrian = new Antrian(
                            rs.getInt("id_antrian"),
                            rs.getInt("nomor_urut"),
                            rs.getString("id_pasien"),
                            rs.getString("id_dokter"),
                            rs.getString("id_poliklinik"),
                            rs.getString("tanggal"),
                            Antrian.JenisKunjungan.valueOf(rs.getString("jenis_kunjungan"))
                    );
                    antrian.setStatus(Antrian.Status.valueOf(rs.getString("status")));
                    list.add(antrian);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Antrian: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    /**
     * Mencari antrian milik pasien pada tanggal tertentu (antrian terbaru jika
     * ada lebih dari satu). Dipakai untuk menyinkronkan antrian saat data
     * kunjungan terkait diubah, karena antrian tidak menyimpan id_kunjungan.
     */
    public Antrian searchByPasienTanggal(String idPasien, String tanggal) {
        con = dbCon.makeConnection();
        Antrian antrian = null;

        String sql = "SELECT * FROM antrian WHERE id_pasien=? AND tanggal=? ORDER BY id_antrian DESC LIMIT 1";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPasien);
            ps.setString(2, tanggal);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                antrian = new Antrian(
                        rs.getInt("id_antrian"),
                        rs.getInt("nomor_urut"),
                        rs.getString("id_pasien"),
                        rs.getString("id_dokter"),
                        rs.getString("id_poliklinik"),
                        rs.getString("tanggal"),
                        Antrian.JenisKunjungan.valueOf(rs.getString("jenis_kunjungan"))
                );
                antrian.setStatus(Antrian.Status.valueOf(rs.getString("status")));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error searchByPasienTanggal Antrian: " + e);
        }

        dbCon.closeConnection();
        return antrian;
    }

    public int generateNomorUrut(String tanggal) {
        con = dbCon.makeConnection();
        int next = 1;
        String sql = "SELECT MAX(nomor_urut) FROM antrian WHERE tanggal = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tanggal);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                int max = rs.getInt(1);
                if (max > 0) {
                    next = max + 1;
                }
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error generateNomorUrut: " + e); 
        }
        
        dbCon.closeConnection();
        return next;
    }

    public List<Object[]> showDataWithNames() {
        con = dbCon.makeConnection();
        String sql = "SELECT a.id_antrian, a.nomor_urut, "
                + "COALESCE(p.nama, a.id_pasien) AS nama_pasien, "
                + "COALESCE(d.nama, a.id_dokter) AS nama_dokter, "
                + "COALESCE(pk.nama_poliklinik, a.id_poliklinik) AS nama_poliklinik, "
                + "a.tanggal, a.jenis_kunjungan, a.status "
                + "FROM antrian a "
                + "LEFT JOIN pasien p ON a.id_pasien = p.id_pasien "
                + "LEFT JOIN dokter d ON a.id_dokter = d.id "
                + "LEFT JOIN poliklinik pk ON a.id_poliklinik = pk.id_poliklinik "
                + "ORDER BY a.tanggal DESC, a.nomor_urut ASC";
        List<Object[]> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id_antrian"), rs.getInt("nomor_urut"),
                    rs.getString("nama_pasien"), rs.getString("nama_dokter"),
                    rs.getString("nama_poliklinik"), rs.getString("tanggal"),
                    rs.getString("jenis_kunjungan"), rs.getString("status")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error showDataWithNames Antrian: " + e); 
        }
        
        dbCon.closeConnection();
        return list;
    }

    public List<Object[]> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();
        String sql = "SELECT a.id_antrian, a.nomor_urut, "
                + "COALESCE(p.nama, a.id_pasien) AS nama_pasien, "
                + "COALESCE(d.nama, a.id_dokter) AS nama_dokter, "
                + "COALESCE(pk.nama_poliklinik, a.id_poliklinik) AS nama_poliklinik, "
                + "a.tanggal, a.jenis_kunjungan, a.status "
                + "FROM antrian a "
                + "LEFT JOIN pasien p ON a.id_pasien = p.id_pasien "
                + "LEFT JOIN dokter d ON a.id_dokter = d.id "
                + "LEFT JOIN poliklinik pk ON a.id_poliklinik = pk.id_poliklinik "
                + "WHERE p.nama LIKE ? OR d.nama LIKE ? OR pk.nama_poliklinik LIKE ? "
                + "ORDER BY a.tanggal DESC, a.nomor_urut ASC";
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
                    rs.getInt("id_antrian"), rs.getInt("nomor_urut"),
                    rs.getString("nama_pasien"), rs.getString("nama_dokter"),
                    rs.getString("nama_poliklinik"), rs.getString("tanggal"),
                    rs.getString("jenis_kunjungan"), rs.getString("status")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error searchByKeyword Antrian: " + e); 
        }
        
        dbCon.closeConnection();
        return list;
    }

    @Override
    public Antrian search(Integer id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM antrian WHERE id_antrian=?";
        Antrian antrian = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                antrian = new Antrian(
                        rs.getInt("id_antrian"),
                        rs.getInt("nomor_urut"),
                        rs.getString("id_pasien"),
                        rs.getString("id_dokter"),
                        rs.getString("id_poliklinik"),
                        rs.getString("tanggal"),
                        Antrian.JenisKunjungan.valueOf(rs.getString("jenis_kunjungan"))
                );
                antrian.setStatus(Antrian.Status.valueOf(rs.getString("status")));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Antrian: " + e);
        }

        dbCon.closeConnection();
        return antrian;
    }
}
