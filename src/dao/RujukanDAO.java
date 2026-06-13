package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Rujukan;

public class RujukanDAO implements IDAO<Rujukan, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Rujukan data) {
        con = dbCon.makeConnection();

        String sql = "INSERT INTO rujukan (id_rujukan, id_kunjungan, nomor_rekam_medis, id_dokter_pengirim, tujuan_rujukan, alasan_rujukan, tanggal_rujukan, tanggal_berlaku, status) VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdRujukan());
            ps.setString(2, data.getIdKunjungan());
            ps.setString(3, data.getNomorRekamMedis());
            ps.setString(4, data.getIdDokterPengirim());
            ps.setString(5, data.getTujuanRujukan());
            ps.setString(6, data.getAlasanRujukan());
            ps.setString(7, data.getTanggalRujukan());
            ps.setString(8, data.getTanggalBerlaku());
            ps.setString(9, data.getStatus().name());
            ps.executeUpdate();
            ps.close();
            System.out.println("Rujukan berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Rujukan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Rujukan data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE rujukan SET id_kunjungan=?, nomor_rekam_medis=?, id_dokter_pengirim=?, tujuan_rujukan=?, alasan_rujukan=?, tanggal_rujukan=?, tanggal_berlaku=?, status=? WHERE id_rujukan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdKunjungan());
            ps.setString(2, data.getNomorRekamMedis());
            ps.setString(3, data.getIdDokterPengirim());
            ps.setString(4, data.getTujuanRujukan());
            ps.setString(5, data.getAlasanRujukan());
            ps.setString(6, data.getTanggalRujukan());
            ps.setString(7, data.getTanggalBerlaku());
            ps.setString(8, data.getStatus().name());
            ps.setString(9, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Rujukan berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Rujukan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM rujukan WHERE id_rujukan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Rujukan berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Rujukan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Rujukan> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM rujukan";
        List<Rujukan> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Rujukan rujukan = new Rujukan(
                        rs.getString("id_rujukan"),
                        rs.getString("id_kunjungan"),
                        rs.getString("nomor_rekam_medis"),
                        rs.getString("id_dokter_pengirim"),
                        rs.getString("tujuan_rujukan"),
                        rs.getString("alasan_rujukan"),
                        rs.getString("tanggal_rujukan"),
                        rs.getString("tanggal_berlaku")
                    );
                    rujukan.setStatus(Rujukan.Status.valueOf(rs.getString("status")));
                    list.add(rujukan);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Rujukan: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Rujukan search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM rujukan WHERE id_rujukan=?";
        Rujukan rujukan = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                rujukan = new Rujukan(
                    rs.getString("id_rujukan"),
                    rs.getString("id_kunjungan"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("id_dokter_pengirim"),
                    rs.getString("tujuan_rujukan"),
                    rs.getString("alasan_rujukan"),
                    rs.getString("tanggal_rujukan"),
                    rs.getString("tanggal_berlaku")
                );
                rujukan.setStatus(Rujukan.Status.valueOf(rs.getString("status")));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Rujukan: " + e);
        }

        dbCon.closeConnection();
        return rujukan;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "RUJ001";
        String sql = "SELECT id_rujukan FROM rujukan ORDER BY id_rujukan DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id_rujukan");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("RUJ%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Rujukan: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }
}
