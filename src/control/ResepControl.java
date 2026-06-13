package control;

import dao.ResepDAO;
import exception.StokTidakCukupException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Obat;
import model.Resep;

public class ResepControl {
    private final ResepDAO dao = new ResepDAO();
    private final ObatControl obatControl = new ObatControl();

    public String generateIdResep() {
        return dao.generateId();
    }

    public void insert(Resep data) {
        dao.insert(data);
    }

    /**
     * Menjumlahkan kebutuhan tiap obat (menggabungkan item dengan obat yang
     * sama) lalu memvalidasi terhadap stok terkini di database. Melempar
     * {@link StokTidakCukupException} pada obat pertama yang stoknya kurang.
     */
    public void validasiStok(List<Resep.ItemResep> items) throws StokTidakCukupException {
        Map<String, Integer> diminta = new HashMap<>();
        Map<String, String> namaObat = new HashMap<>();
        for (Resep.ItemResep item : items) {
            String id = item.getObat().getIdObat();
            diminta.merge(id, item.getJumlah(), Integer::sum);
            namaObat.put(id, item.getObat().getNamaObat());
        }
        for (Map.Entry<String, Integer> e : diminta.entrySet()) {
            Obat obat = obatControl.search(e.getKey());
            int tersedia = obat != null ? obat.getStok() : 0;
            if (tersedia < e.getValue()) {
                throw new StokTidakCukupException(namaObat.get(e.getKey()), e.getValue(), tersedia);
            }
        }
    }

    /**
     * Membuat resep sekaligus mengurangi stok obat. Stok divalidasi lebih dulu;
     * bila ada yang tidak cukup, resep tidak disimpan dan stok tidak berubah.
     */
    public void buatResep(Resep data) throws StokTidakCukupException {
        validasiStok(data.getDaftarObat());
        dao.insert(data);

        Map<String, Integer> diminta = new HashMap<>();
        for (Resep.ItemResep item : data.getDaftarObat()) {
            diminta.merge(item.getObat().getIdObat(), item.getJumlah(), Integer::sum);
        }
        for (Map.Entry<String, Integer> e : diminta.entrySet()) {
            obatControl.kurangiStok(e.getKey(), e.getValue());
        }
    }
    
    public void update(Resep data, String id) { 
        dao.update(data, id); 
    }
    
    public void delete(String id) { 
        dao.delete(id); 
    }
    
    public List<Resep> showData() { 
        return dao.showData(); 
    }
    
    public Resep search(String id) { 
        return dao.search(id); 
    }

    public void ubahStatus(Resep resep, Resep.Status statusBaru) {
        resep.setStatus(statusBaru);
        dao.update(resep, resep.getIdResep());
    }
}
