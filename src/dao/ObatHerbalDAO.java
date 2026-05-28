package dao;

import Connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ObatHerbal;

public class ObatHerbalDAO implements IDAO<ObatHerbal, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(ObatHerbal data) {
        con = dbCon.makeConnection();

        // KOREKSI: Menghapus jenis_obat dan 'HERBAL' agar sesuai dengan tabel mysql
        String sqlObat = "INSERT INTO obat (id_obat, nama_obat, bentuk_sediaan, dosis, kategori, harga_satuan, stok) VALUES (?,?,?,?,?,?,?)";
        String sqlHerbal = "INSERT INTO obat_herbal (id_obat, bahan_utama) VALUES (?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlObat);
            ps1.setString(1, data.getIdObat());
            ps1.setString(2, data.getNamaObat());
            ps1.setString(3, data.getBentukSediaan());
            ps1.setString(4, data.getDosis());
            ps1.setString(5, data.getKategori());
            ps1.setDouble(6, data.getHargaSatuan());
            ps1.setInt(7, data.getStok());
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlHerbal);
            ps2.setString(1, data.getIdObat());
            ps2.setString(2, data.getBahanUtama());
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Obat Herbal berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert ObatHerbal: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(ObatHerbal data, String id) {
        con = dbCon.makeConnection();

        String sqlObat = "UPDATE obat SET nama_obat=?, bentuk_sediaan=?, dosis=?, kategori=?, harga_satuan=?, stok=? WHERE id_obat=?";
        String sqlHerbal = "UPDATE obat_herbal SET bahan_utama=? WHERE id_obat=?";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlObat);
            ps1.setString(1, data.getNamaObat());
            ps1.setString(2, data.getBentukSediaan());
            ps1.setString(3, data.getDosis());
            ps1.setString(4, data.getKategori());
            ps1.setDouble(5, data.getHargaSatuan());
            ps1.setInt(6, data.getStok());
            ps1.setString(7, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement(sqlHerbal);
            ps2.setString(1, data.getBahanUtama());
            ps2.setString(2, id);
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Obat Herbal berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update ObatHerbal: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        // Cukup delete dari tabel induk 'obat', karena foreign key di mysql 
        // sudah kita pasang ON DELETE CASCADE, tabel obat_herbal otomatis ikut terhapus
        String sql = "DELETE FROM obat WHERE id_obat=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Obat Herbal berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete ObatHerbal: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<ObatHerbal> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, h.bahan_utama FROM obat o JOIN obat_herbal h ON o.id_obat = h.id_obat";
        List<ObatHerbal> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new ObatHerbal(
                            rs.getString("bahan_utama"),
                            rs.getString("id_obat"),
                            rs.getString("nama_obat"),
                            rs.getString("bentuk_sediaan"),
                            rs.getString("dosis"),
                            rs.getString("kategori"),
                            rs.getDouble("harga_satuan"),
                            rs.getInt("stok")));
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData ObatHerbal: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public ObatHerbal search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, h.bahan_utama FROM obat o JOIN obat_herbal h ON o.id_obat = h.id_obat WHERE o.id_obat=?";
        ObatHerbal obat = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                obat = new ObatHerbal(
                            rs.getString("bahan_utama"),
                            rs.getString("id_obat"),
                            rs.getString("nama_obat"),
                            rs.getString("bentuk_sediaan"),
                            rs.getString("dosis"),
                            rs.getString("kategori"),
                            rs.getDouble("harga_satuan"),
                            rs.getInt("stok"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search ObatHerbal: " + e);
        }

        dbCon.closeConnection();
        return obat;
    }
}