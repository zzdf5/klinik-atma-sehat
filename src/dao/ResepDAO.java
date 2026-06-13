package dao;

import connection.DBConnection;
import interfaceDAO.IDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Obat;
import model.Resep;

public class ResepDAO implements IDAO<Resep, String> {
    private DBConnection dbCon = new DBConnection();
    private Connection con;

    @Override
    public void insert(Resep data) {
        con = dbCon.makeConnection();

        String sqlResep = "INSERT INTO resep (id_resep, id_dokter, nomor_rekam_medis, tanggal_resep, status) VALUES (?,?,?,?,?)";
        String sqlItem = "INSERT INTO resep_detail (id_resep, id_obat, jumlah, aturan_pakai) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlResep);
            ps1.setString(1, data.getIdResep());
            ps1.setString(2, data.getIdDokter());
            ps1.setString(3, data.getNomorRekamMedis());
            ps1.setString(4, data.getTanggalResep());
            ps1.setString(5, data.getStatus().name());
            ps1.executeUpdate();
            ps1.close();

            for (Resep.ItemResep item : data.getDaftarObat()) {
                PreparedStatement ps2 = con.prepareStatement(sqlItem);
                ps2.setString(1, data.getIdResep());
                ps2.setString(2, item.getObat().getIdObat());
                ps2.setInt(3, item.getJumlah());
                ps2.setString(4, item.getAturanPakai());
                ps2.executeUpdate();
                ps2.close();
            }

            System.out.println("Resep berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Error insert Resep: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void update(Resep data, String id) {
        con = dbCon.makeConnection();

        String sqlResep = "UPDATE resep SET id_dokter=?, nomor_rekam_medis=?, tanggal_resep=?, status=? WHERE id_resep=?";
        String sqlHapusItem = "DELETE FROM item_resep WHERE id_resep=?";
        String sqlItem = "INSERT INTO item_resep (id_resep, id_obat, jumlah, aturan_pakai) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps1 = con.prepareStatement(sqlResep);
            ps1.setString(1, data.getIdDokter());
            ps1.setString(2, data.getNomorRekamMedis());
            ps1.setString(3, data.getTanggalResep());
            ps1.setString(4, data.getStatus().name());
            ps1.setString(5, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement psHapus = con.prepareStatement(sqlHapusItem);
            psHapus.setString(1, id);
            psHapus.executeUpdate();
            psHapus.close();

            for (Resep.ItemResep item : data.getDaftarObat()) {
                PreparedStatement ps2 = con.prepareStatement(sqlItem);
                ps2.setString(1, id);
                ps2.setString(2, item.getObat().getIdObat());
                ps2.setInt(3, item.getJumlah());
                ps2.setString(4, item.getAturanPakai());
                ps2.executeUpdate();
                ps2.close();
            }

            System.out.println("Resep berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Error update Resep: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public void delete(String id) {
        con = dbCon.makeConnection();
        String sql = "DELETE FROM resep WHERE id_resep=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Resep berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error delete Resep: " + e);
        }

        dbCon.closeConnection();
    }

    @Override
    public List<Resep> showData() {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM resep";
        List<Resep> list = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Resep resep = new Resep(
                        rs.getString("id_resep"),
                        rs.getString("id_dokter"),
                        rs.getString("nomor_rekam_medis"),
                        rs.getString("tanggal_resep")
                    );
                    resep.setStatus(Resep.Status.valueOf(rs.getString("status")));
                    loadItemResep(resep);
                    list.add(resep);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error showData Resep: " + e);
        }

        dbCon.closeConnection();
        return list;
    }

    @Override
    public Resep search(String id) {
        con = dbCon.makeConnection();

        String sql = "SELECT * FROM resep WHERE id_resep=?";
        Resep resep = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                resep = new Resep(
                    rs.getString("id_resep"),
                    rs.getString("id_dokter"),
                    rs.getString("nomor_rekam_medis"),
                    rs.getString("tanggal_resep")
                );
                resep.setStatus(Resep.Status.valueOf(rs.getString("status")));
                loadItemResep(resep);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error search Resep: " + e);
        }

        dbCon.closeConnection();
        return resep;
    }

    private void loadItemResep(Resep resep) {
        String sql = "SELECT ir.jumlah, ir.aturan_pakai, o.id_obat, o.nama_obat, o.bentuk_sediaan, "
                + "o.dosis, o.kategori, o.harga_satuan, o.stok "
                + "FROM item_resep ir JOIN obat o ON ir.id_obat = o.id_obat "
                + "WHERE ir.id_resep=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, resep.getIdResep());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Obat obat = new Obat(
                    rs.getString("id_obat"),
                    rs.getString("nama_obat"),
                    rs.getString("bentuk_sediaan"),
                    rs.getString("dosis"),
                    rs.getString("kategori"),
                    rs.getDouble("harga_satuan"),
                    rs.getInt("stok")
                );
                resep.tambahObat(new Resep.ItemResep(obat, rs.getInt("jumlah"), rs.getString("aturan_pakai")));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error loading item resep: " + e);
        }
    }

    public String generateId() {
        con = dbCon.makeConnection();
        String newId = "RES001";
        String sql = "SELECT id_resep FROM resep ORDER BY id_resep DESC LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                String lastId = rs.getString("id_resep");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                newId = String.format("RES%03d", num);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error generateId Resep: " + e);
        }
        dbCon.closeConnection();
        return newId;
    }
}
