package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ObatHerbal;

public class TableObatHerbal extends AbstractTableModel {
    private List<ObatHerbal> listObatHerbal;

    public TableObatHerbal(List<ObatHerbal> listObatHerbal) {
        this.listObatHerbal = listObatHerbal;
    }

    @Override
    public int getRowCount() {
        return listObatHerbal.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObatHerbal h = listObatHerbal.get(rowIndex);
        switch (columnIndex) {
            case 0: return h.getIdObat();
            case 1: return h.getNamaObat();
            case 2: return h.getBentukSediaan();
            case 3: return h.getDosis();
            case 4: return h.getBahanUtama();
            case 5: return h.getHargaSatuan();
            case 6: return h.getStok();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Obat";
            case 1: return "Nama Obat";
            case 2: return "Bentuk Sediaan";
            case 3: return "Dosis";
            case 4: return "Bahan Utama";
            case 5: return "Harga Satuan";
            case 6: return "Stok";
            default: return null;
        }
    }
}
