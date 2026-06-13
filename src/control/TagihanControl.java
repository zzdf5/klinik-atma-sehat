package control;

import dao.TagihanDAO;
import java.util.List;
import model.Kunjungan;
import model.Resep;
import model.Tagihan;
import model.Tagihan.ItemTagihan;
import model.Tagihan.MetodePembayaran;
import model.Tagihan.Status;
import table.TableTagihan;

public class TagihanControl {
    private final TagihanDAO dao = new TagihanDAO();

    public String generateId() { 
        return dao.generateId(); 
    }
    
    public List<Object[]> showDataWithNames() { 
        return dao.showDataWithNames(); 
    }
    
    public List<Object[]> searchByKeyword(String keyword) { 
        return dao.searchByKeyword(keyword); 
    }

    public TableTagihan showTable(String target) {
        List<Object[]> data = (target == null || target.isBlank()) ? dao.showDataWithNames() : dao.searchByKeyword(target);
        return new TableTagihan(data);
    }

    public void insert(Tagihan data) {
        hitungTotal(data);
        dao.insert(data);
    }

    public void update(Tagihan data, String id) {
        hitungTotal(data);
        dao.update(data, id);
    }

    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<Tagihan> showData() { 
        return dao.showData(); 
    }
    
    public Tagihan search(String id) { 
        return dao.search(id); 
    }
    
    public Tagihan searchByIdKunjungan(String idKunjungan) { 
        return dao.searchByIdKunjungan(idKunjungan); 
    }

    public void buatDariKunjungan(Kunjungan kunjungan, Resep resep) {
        if (dao.searchByIdKunjungan(kunjungan.getIdKunjungan()) != null) {
            return;
        }
            

        String idTagihan = dao.generateId();
        Tagihan tagihan = new Tagihan(idTagihan, kunjungan.getIdKunjungan(), kunjungan.getTanggal());

        if (kunjungan.getBiayaKonsultasi() > 0) {
            tagihan.tambahItem(new ItemTagihan("Biaya Konsultasi", 1, kunjungan.getBiayaKonsultasi()));
        }

        if (resep != null) {
            for (Resep.ItemResep item : resep.getDaftarObat()) {
                tagihan.tambahItem(new ItemTagihan(item.getNamaObat(), item.getJumlah(), item.getHarga()));
            }
        }
        insert(tagihan);
    }

    public void bayar(Tagihan tagihan, double jumlahBayar, MetodePembayaran metode) {
        hitungTotal(tagihan);
        tagihan.setJumlahBayar(jumlahBayar);
        tagihan.setKembalian(jumlahBayar - tagihan.getTotalTagihan());
        tagihan.setMetodePembayaran(metode);
        tagihan.setStatus(Status.LUNAS);
        dao.update(tagihan, tagihan.getIdTagihan());
    }

    public double hitungTotal(Tagihan tagihan) {
        double total = 0;
        for (ItemTagihan item : tagihan.getDaftarItem()) {
            total += item.getJumlah() * item.getHargaSatuan();
        }
        tagihan.setTotalTagihan(total);
        return total;
    }
}
