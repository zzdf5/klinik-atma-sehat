package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;

public class TableObat extends AbstractTableModel{
    private List<Obat> listObat;

    public TableObat(List<Obat> listObat) {
        this.listObat = listObat;
    }
    
    @Override
    public int getRowCount() {
        return listObat.size();
    }

    @Override
    public int getColumnCount() {
        return 8; // Sudah benar 8 kolom
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Obat o = listObat.get(rowIndex); // Ambil objek pada baris tersebut
        
        switch(columnIndex){
            case 0:
                return o.getIdObat();
            case 1:
                return o.getNamaObat();
            case 2:
                return o.getBentukSediaan();
            case 3:
                return o.getDosis();
            case 4:
                return o.getKategori();
            case 5:
                // Cek tipe objek secara dinamis untuk mengambil atribut khusus
                if (o instanceof ObatHerbal) {
                    return ((ObatHerbal) o).getBahanUtama();
                } else if (o instanceof ObatPaten) {
                    return ((ObatPaten) o).getMerk();
                }
                return "-"; // Default jika tidak ada
            case 6:
                return o.getHargaSatuan();
            case 7:
                return o.getStok();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:
                return "ID Obat";
            case 1:
                return "Nama Obat";
            case 2:
                return "Bentuk Sediaan";
            case 3:
                return "Dosis";
            case 4:
                return "Kategori";
            case 5:
                return "Atribut Khusus"; // Kolom untuk Bahan Utama / Merk
            case 6:
                return "Harga Satuan";
            case 7:
                return "Stok";
            default:
                return null;
        }
    }
}