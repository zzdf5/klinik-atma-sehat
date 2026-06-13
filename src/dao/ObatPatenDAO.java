package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ObatPaten;

public class ObatPatenDAO implements IDAO<ObatPaten, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(ObatPaten data) {
        con = dbCon.makeConnection();

        String sqlObat = "INSERT INTO obat (id_obat, nama_obat, bentuk_sediaan, dosis, kategori, harga_satuan, stok) VALUES (?,?,?,?,?,?,?)";
        String sqlPaten = "INSERT INTO obat_paten (id_obat, merk) VALUES (?,?)";

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

            PreparedStatement ps2 = con.prepareStatement(sqlPaten);
            ps2.setString(1, data.getIdObat());
            ps2.setString(2, data.getMerk());
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Obat Paten berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert ObatPaten: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(ObatPaten data, String id) {
        con = dbCon.makeConnection();

        String sqlObat = "UPDATE obat SET nama_obat=?, bentuk_sediaan=?, dosis=?, kategori=?, harga_satuan=?, stok=? WHERE id_obat=?";
        String sqlPaten = "UPDATE obat_paten SET merk=? WHERE id_obat=?";

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

            PreparedStatement ps2 = con.prepareStatement(sqlPaten);
            ps2.setString(1, data.getMerk());
            ps2.setString(2, id);
            ps2.executeUpdate();
            ps2.close();

            System.out.println("Obat Paten berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update ObatPaten: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM obat WHERE id_obat=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Obat Paten berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete ObatPaten: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<ObatPaten> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, p.merk FROM obat o JOIN obat_paten p ON o.id_obat = p.id_obat";
        List<ObatPaten> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new ObatPaten(
                            rs.getString("merk"),
                            rs.getString("id_obat"),
                            rs.getString("nama_obat"),
                            rs.getString("bentuk_sediaan"),
                            rs.getString("dosis"),
                            rs.getString("kategori"),
                            rs.getDouble("harga_satuan"),
                            rs.getInt("stok")
                        )
                    );
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData ObatPaten: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "OBTP001";
        String sql = "SELECT MAX(CAST(SUBSTRING(id_obat, 5) AS UNSIGNED)) AS max_num FROM obat WHERE id_obat LIKE 'OBTP%'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                int maxNum = rs.getInt("max_num");
                newId = String.format("OBTP%03d", maxNum + 1);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId ObatPaten: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }

    @Override
    public ObatPaten search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, p.merk FROM obat o JOIN obat_paten p ON o.id_obat = p.id_obat WHERE o.id_obat=?";
        ObatPaten obat = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                obat = new ObatPaten(
                    rs.getString("merk"),
                    rs.getString("id_obat"),
                    rs.getString("nama_obat"),
                    rs.getString("bentuk_sediaan"),
                    rs.getString("dosis"),
                    rs.getString("kategori"),
                    rs.getDouble("harga_satuan"),
                    rs.getInt("stok")
                );
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search ObatPaten: " + e);
        }

        dbCon.closeConnection();
        return obat;
    }
}