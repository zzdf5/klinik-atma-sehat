package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;
import java.sql.SQLIntegrityConstraintViolationException;

public class ObatDAO implements IDAO<Obat, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Obat data) {
        con = dbCon.makeConnection();
        
        String sql = "INSERT INTO obat (id_obat, nama_obat, bentuk_sediaan, dosis, kategori, harga_satuan, stok) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getIdObat());
            ps.setString(2, data.getNamaObat());
            ps.setString(3, data.getBentukSediaan());
            ps.setString(4, data.getDosis());
            ps.setString(5, data.getKategori());
            ps.setDouble(6, data.getHargaSatuan());
            ps.setInt(7, data.getStok());
            ps.executeUpdate();
            ps.close();
            System.out.println("Obat berhasil ditambahkan ke tabel induk.");
        } catch (Exception e) {
            System.out.println("Error insert Obat: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Obat data, String id) {
        con = dbCon.makeConnection();

        String sql = "UPDATE obat SET nama_obat=?, bentuk_sediaan=?, dosis=?, kategori=?, harga_satuan=?, stok=? WHERE id_obat=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data.getNamaObat());
            ps.setString(2, data.getBentukSediaan());
            ps.setString(3, data.getDosis());
            ps.setString(4, data.getKategori());
            ps.setDouble(5, data.getHargaSatuan());
            ps.setInt(6, data.getStok());
            ps.setString(7, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Obat berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Obat: " + e);
        }

        dbCon.closeConnection();
    }

    /**
     * Mengurangi stok obat sebanyak {@code jumlah}. Dipakai saat obat
     * diresepkan. Validasi kecukupan stok dilakukan di lapisan control
     * sebelum method ini dipanggil.
     */
    public void kurangiStok(String idObat, int jumlah) {
        con = dbCon.makeConnection();

        String sql = "UPDATE obat SET stok = stok - ? WHERE id_obat = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, jumlah);
            ps.setString(2, idObat);
            ps.executeUpdate();
            ps.close();
            System.out.println("Stok obat " + idObat + " dikurangi " + jumlah + ".");
        } catch (Exception e) {
            System.out.println("Error kurangiStok Obat: " + e);
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
            System.out.println("Obat berhasil dihapus.");
        } catch (SQLIntegrityConstraintViolationException e) {
            dbCon.closeConnection();
            throw new exception.DataMasihDigunakanException("Obat ini tidak dapat dihapus karena masih digunakan pada data resep.");
        } catch (Exception e) {
            System.out.println("Error delete Obat: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Obat> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, h.bahan_utama, p.merk FROM obat o "
                   + "LEFT JOIN obat_herbal h ON o.id_obat = h.id_obat "
                   + "LEFT JOIN obat_paten p ON o.id_obat = p.id_obat";
                   
        List<Obat> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    String kategori = rs.getString("kategori");
                    
                    if (kategori.equalsIgnoreCase("Obat Herbal")) {
                        list.add(new ObatHerbal(
                                rs.getString("bahan_utama"),
                                rs.getString("id_obat"),
                                rs.getString("nama_obat"),
                                rs.getString("bentuk_sediaan"),
                                rs.getString("dosis"),
                                kategori,
                                rs.getDouble("harga_satuan"),
                                rs.getInt("stok")
                            )
                        );
                    } else {
                        list.add(new ObatPaten(
                                rs.getString("merk"),
                                rs.getString("id_obat"),
                                rs.getString("nama_obat"),
                                rs.getString("bentuk_sediaan"),
                                rs.getString("dosis"),
                                kategori,
                                rs.getDouble("harga_satuan"),
                                rs.getInt("stok")
                            )
                        );
                    }
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Obat: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Obat search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT o.*, h.bahan_utama, p.merk FROM obat o "
                   + "LEFT JOIN obat_herbal h ON o.id_obat = h.id_obat "
                   + "LEFT JOIN obat_paten p ON o.id_obat = p.id_obat "
                   + "WHERE o.id_obat = ?";
                   
        Obat obat = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                String kategori = rs.getString("kategori");
                
                if (kategori.equalsIgnoreCase("Obat Herbal")) {
                    obat = new ObatHerbal(
                            rs.getString("bahan_utama"),
                            rs.getString("id_obat"),
                            rs.getString("nama_obat"),
                            rs.getString("bentuk_sediaan"),
                            rs.getString("dosis"),
                            kategori,
                            rs.getDouble("harga_satuan"),
                            rs.getInt("stok")
                    );
                } else {
                    obat = new ObatPaten(
                            rs.getString("merk"),
                            rs.getString("id_obat"),
                            rs.getString("nama_obat"),
                            rs.getString("bentuk_sediaan"),
                            rs.getString("dosis"),
                            kategori,
                            rs.getDouble("harga_satuan"),
                            rs.getInt("stok")
                    );
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Obat: " + e);
        }

        dbCon.closeConnection();
        return obat;
    }
}