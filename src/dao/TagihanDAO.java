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
        String sqlItem = "INSERT INTO tagihan_detail (id_tagihan, nama_item, jumlah, harga_satuan) VALUES (?,?,?,?)";

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
        String sqlHapusItem = "DELETE FROM tagihan_detail WHERE id_tagihan=?";
        String sqlItem = "INSERT INTO tagihan_detail (id_tagihan, nama_item, jumlah, harga_satuan) VALUES (?,?,?,?)";

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

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "TAG001";
        String sql = "SELECT id_tagihan FROM tagihan ORDER BY id_tagihan DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String last = rs.getString("id_tagihan");
                int num = Integer.parseInt(last.substring(3)) + 1;
                newId = String.format("TAG%03d", num);
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error generateId Tagihan: " + e); 
        }
        dbCon.closeConnection();
        return newId;
    }

    public List<Object[]> showDataWithNames() {
        con = dbCon.makeConnection();
        String sql = "SELECT t.id_tagihan, COALESCE(p.nama, k.nomor_rekam_medis) AS nama_pasien, "
                + "t.id_kunjungan, t.tanggal_tagihan, t.total_tagihan, "
                + "COALESCE(t.metode_pembayaran, '-') AS metode, t.status "
                + "FROM tagihan t "
                + "LEFT JOIN kunjungan k ON t.id_kunjungan = k.id_kunjungan "
                + "LEFT JOIN pasien p ON k.nomor_rekam_medis = p.nomor_rekam_medis "
                + "ORDER BY t.tanggal_tagihan DESC, t.id_tagihan DESC";
        List<Object[]> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_tagihan"), rs.getString("nama_pasien"),
                    rs.getString("id_kunjungan"), rs.getString("tanggal_tagihan"),
                    rs.getDouble("total_tagihan"), rs.getString("metode"),
                    rs.getString("status")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error showDataWithNames Tagihan: " + e); 
        }
        dbCon.closeConnection();
        return list;
    }

    public List<Object[]> searchByKeyword(String keyword) {
        con = dbCon.makeConnection();
        String sql = "SELECT t.id_tagihan, COALESCE(p.nama, k.nomor_rekam_medis) AS nama_pasien, "
                + "t.id_kunjungan, t.tanggal_tagihan, t.total_tagihan, "
                + "COALESCE(t.metode_pembayaran, '-') AS metode, t.status "
                + "FROM tagihan t "
                + "LEFT JOIN kunjungan k ON t.id_kunjungan = k.id_kunjungan "
                + "LEFT JOIN pasien p ON k.nomor_rekam_medis = p.nomor_rekam_medis "
                + "WHERE t.id_tagihan LIKE ? OR p.nama LIKE ? OR t.id_kunjungan LIKE ? "
                + "ORDER BY t.tanggal_tagihan DESC, t.id_tagihan DESC";
        List<Object[]> list = new ArrayList<>();
        String param = "%" + keyword + "%";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param); ps.setString(2, param); ps.setString(3, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("id_tagihan"), rs.getString("nama_pasien"),
                    rs.getString("id_kunjungan"), rs.getString("tanggal_tagihan"),
                    rs.getDouble("total_tagihan"), rs.getString("metode"),
                    rs.getString("status")
                });
            }
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error searchByKeyword Tagihan: " + e); 
        }
        dbCon.closeConnection();
        return list;
    }

    public Tagihan searchByIdKunjungan(String idKunjungan) {
        con = dbCon.makeConnection();
        String sql = "SELECT * FROM tagihan WHERE id_kunjungan = ? LIMIT 1";
        Tagihan tagihan = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idKunjungan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tagihan = new Tagihan(rs.getString("id_tagihan"), rs.getString("id_kunjungan"), rs.getString("tanggal_tagihan"));
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
            rs.close(); ps.close();
        } catch (Exception e) { 
            System.out.println("Error searchByIdKunjungan: " + e); 
        }
        dbCon.closeConnection();
        return tagihan;
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM tagihan WHERE id_tagihan=?");
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
        List<Tagihan> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tagihan");
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Tagihan tagihan = new Tagihan(
                        rs.getString("id_tagihan"),
                        rs.getString("id_kunjungan"),
                        rs.getString("tanggal_tagihan")
                    );
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
        Tagihan tagihan = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tagihan WHERE id_tagihan=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                tagihan = new Tagihan(
                    rs.getString("id_tagihan"),
                    rs.getString("id_kunjungan"),
                    rs.getString("tanggal_tagihan")
                );
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
        try {
            PreparedStatement ps = con.prepareStatement("SELECT nama_item, jumlah, harga_satuan FROM tagihan_detail WHERE id_tagihan=?");
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

            if (tagihan.getDaftarItem().isEmpty()) {
                String sqlFallback =
                    "SELECT 'Biaya Konsultasi' AS nama_item, 1 AS jumlah, k.biaya_konsultasi AS harga_satuan " +
                    "FROM tagihan t JOIN kunjungan k ON t.id_kunjungan = k.id_kunjungan " +
                    "WHERE t.id_tagihan = ? AND k.biaya_konsultasi > 0 " +
                    "UNION ALL " +
                    "SELECT o.nama_obat, rd.jumlah, o.harga_satuan " +
                    "FROM tagihan t " +
                    "JOIN kunjungan k ON t.id_kunjungan = k.id_kunjungan " +
                    "JOIN resep_detail rd ON k.id_resep = rd.id_resep " +
                    "JOIN obat o ON rd.id_obat = o.id_obat " +
                    "WHERE t.id_tagihan = ?";
                PreparedStatement ps2 = con.prepareStatement(sqlFallback);
                ps2.setString(1, tagihan.getIdTagihan());
                ps2.setString(2, tagihan.getIdTagihan());
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    tagihan.tambahItem(new Tagihan.ItemTagihan(
                            rs2.getString("nama_item"),
                            rs2.getInt("jumlah"),
                            rs2.getDouble("harga_satuan")
                        )
                    );
                }
                rs2.close(); ps2.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading item tagihan: " + e);
        }
    }
}
