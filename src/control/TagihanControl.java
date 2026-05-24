package control;

import dao.TagihanDAO;
import java.util.List;
import model.Tagihan;
import model.Tagihan.ItemTagihan;
import model.Tagihan.MetodePembayaran;
import model.Tagihan.Status;

public class TagihanControl {
    private final TagihanDAO dao = new TagihanDAO();

    public void insert(Tagihan data) {
        hitungTotal(data);
        dao.insert(data);
    }

    public void update(Tagihan data, String id) {
        hitungTotal(data);
        dao.update(data, id);
    }

    public void delete(String id) { dao.delete(id); }
    public List<Tagihan> showData() { return dao.showData(); }
    public Tagihan search(String id) { return dao.search(id); }

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
