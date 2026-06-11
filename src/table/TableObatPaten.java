package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ObatPaten;

public class TableObatPaten extends AbstractTableModel {
    private List<ObatPaten> listObatPaten;

    public TableObatPaten(List<ObatPaten> listObatPaten) {
        this.listObatPaten = listObatPaten;
    }

    @Override
    public int getRowCount() {
        return listObatPaten.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObatPaten p = listObatPaten.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getIdObat();
            case 1: return p.getNamaObat();
            case 2: return p.getBentukSediaan();
            case 3: return p.getDosis();
            case 4: return p.getMerk();
            case 5: return p.getHargaSatuan();
            case 6: return p.getStok();
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
            case 4: return "Merk";
            case 5: return "Harga Satuan";
            case 6: return "Stok";
            default: return null;
        }
    }
}
