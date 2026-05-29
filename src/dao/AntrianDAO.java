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

        // id_antrian tidak disertakan karena AUTO_INCREMENT
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
                            Antrian.JenisKunjungan.valueOf(rs.getString("jenis_kunjungan")));
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
                        Antrian.JenisKunjungan.valueOf(rs.getString("jenis_kunjungan")));
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
