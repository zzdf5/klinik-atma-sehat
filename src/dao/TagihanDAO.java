package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Tagihan;

public class TagihanDAO implements IDAO<Tagihan, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Tagihan data) {
        con = dbCon.makeConnection();

        String sqlTagihan = "INSERT INTO tagihan (id_tagihan, id_kunjungan, tanggal_tagihan, total_tagihan, jumlah_bayar, kembalian, metode_pembayaran, status) VALUES (?,?,?,?,?,?,?,?)";
        String sqlItem = "INSERT INTO item_tagihan (id_tagihan, nama_item, jumlah, harga_satuan) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlTagihan);
            ps1.setString(1, data.getIdTagihan());
            ps1.setString(2, data.getIdKunjungan());
            ps1.setString(3, data.getTanggalTagihan());
            ps1.setDouble(4, data.getTotalTagihan());
            ps1.setDouble(5, data.getJumlahBayar());
            ps1.setDouble(6, data.getKembalian());
            ps1.setString(7, data.getMetodePembayaran() != null ? data.getMetodePembayaran().name() : null);
            ps1.setString(8, data.getStatus().name());
            ps1.executeUpdate();
            ps1.close();

            for (Tagihan.ItemTagihan item : data.getDaftarItem()) {
                PreparedStatement ps2 = con.prepareStatement(sqlItem);
                ps2.setString(1, data.getIdTagihan());
                ps2.setString(2, item.getNamaItem());
                ps2.setInt(3, item.getJumlah());
                ps2.setDouble(4, item.getHargaSatuan());
                ps2.executeUpdate();
                ps2.close();
            }

            System.out.println("Tagihan berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Tagihan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Tagihan data, String id) {
        con = dbCon.makeConnection();

        String sqlTagihan = "UPDATE tagihan SET id_kunjungan=?, tanggal_tagihan=?, total_tagihan=?, jumlah_bayar=?, kembalian=?, metode_pembayaran=?, status=? WHERE id_tagihan=?";
        String sqlHapusItem = "DELETE FROM item_tagihan WHERE id_tagihan=?";
        String sqlItem = "INSERT INTO item_tagihan (id_tagihan, nama_item, jumlah, harga_satuan) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlTagihan);
            ps1.setString(1, data.getIdKunjungan());
            ps1.setString(2, data.getTanggalTagihan());
            ps1.setDouble(3, data.getTotalTagihan());
            ps1.setDouble(4, data.getJumlahBayar());
            ps1.setDouble(5, data.getKembalian());
            ps1.setString(6, data.getMetodePembayaran() != null ? data.getMetodePembayaran().name() : null);
            ps1.setString(7, data.getStatus().name());
            ps1.setString(8, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement psHapus = con.prepareStatement(sqlHapusItem);
            psHapus.setString(1, id);
            psHapus.executeUpdate();
            psHapus.close();

            for (Tagihan.ItemTagihan item : data.getDaftarItem()) {
                PreparedStatement ps2 = con.prepareStatement(sqlItem);
                ps2.setString(1, id);
                ps2.setString(2, item.getNamaItem());
                ps2.setInt(3, item.getJumlah());
                ps2.setDouble(4, item.getHargaSatuan());
                ps2.executeUpdate();
                ps2.close();
            }

            System.out.println("Tagihan berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Tagihan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();

        String sql = "DELETE FROM tagihan WHERE id_tagihan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Tagihan berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Tagihan: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Tagihan> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM tagihan";
        List<Tagihan> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Tagihan tagihan = new Tagihan(
                            rs.getString("id_tagihan"),
                            rs.getString("id_kunjungan"),
                            rs.getString("tanggal_tagihan"));
                    tagihan.setTotalTagihan(rs.getDouble("total_tagihan"));
                    tagihan.setJumlahBayar(rs.getDouble("jumlah_bayar"));
                    tagihan.setKembalian(rs.getDouble("kembalian"));
                    String metode = rs.getString("metode_pembayaran");
                    if (metode != null) {
                        tagihan.setMetodePembayaran(Tagihan.MetodePembayaran.valueOf(metode));
                    }
                    tagihan.setStatus(Tagihan.Status.valueOf(rs.getString("status")));
                    loadItemTagihan(tagihan);
                    list.add(tagihan);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Tagihan: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Tagihan search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM tagihan WHERE id_tagihan=?";
        Tagihan tagihan = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                tagihan = new Tagihan(
                        rs.getString("id_tagihan"),
                        rs.getString("id_kunjungan"),
                        rs.getString("tanggal_tagihan"));
                tagihan.setTotalTagihan(rs.getDouble("total_tagihan"));
                tagihan.setJumlahBayar(rs.getDouble("jumlah_bayar"));
                tagihan.setKembalian(rs.getDouble("kembalian"));
                String metode = rs.getString("metode_pembayaran");
                if (metode != null) {
                    tagihan.setMetodePembayaran(Tagihan.MetodePembayaran.valueOf(metode));
                }
                tagihan.setStatus(Tagihan.Status.valueOf(rs.getString("status")));
                loadItemTagihan(tagihan);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Tagihan: " + e);
        }

        dbCon.closeConnection();
        return tagihan;
    }

    private void loadItemTagihan(Tagihan tagihan) {
        String sql = "SELECT * FROM item_tagihan WHERE id_tagihan=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tagihan.getIdTagihan());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tagihan.tambahItem(new Tagihan.ItemTagihan(
                        rs.getString("nama_item"),
                        rs.getInt("jumlah"),
                        rs.getDouble("harga_satuan")));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error loading item tagihan: " + e);
        }
    }
}
