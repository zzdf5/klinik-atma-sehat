package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Resep;

public class TableItemResep extends AbstractTableModel {
    private List<Resep.ItemResep> listItemResep;

    public TableItemResep(List<Resep.ItemResep> listItemResep) {
        this.listItemResep = listItemResep;
    }

    @Override
    public int getRowCount() {
        return listItemResep.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Resep.ItemResep item = listItemResep.get(rowIndex);
        switch (columnIndex) {
            case 0: return item.getObat().getIdObat();
            case 1: return item.getObat().getNamaObat();
            case 2: return item.getJumlah();
            case 3: return item.getAturanPakai();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Obat";
            case 1: return "Nama Obat";
            case 2: return "Jumlah";
            case 3: return "Aturan Pakai";
            default: return null;
        }
    }
}
