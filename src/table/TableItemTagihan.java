package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Tagihan;

public class TableItemTagihan extends AbstractTableModel {
    private List<Tagihan.ItemTagihan> listItem;

    public TableItemTagihan(List<Tagihan.ItemTagihan> listItem) {
        this.listItem = listItem;
    }

    @Override
    public int getRowCount() {
        return listItem.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tagihan.ItemTagihan item = listItem.get(rowIndex);
        switch (columnIndex) {
            case 0: return item.getNamaItem();
            case 1: return item.getJumlah();
            case 2: return String.format("Rp%.0f", item.getHargaSatuan());
            case 3: return String.format("Rp%.0f", item.getJumlah() * item.getHargaSatuan());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Nama Item";
            case 1: return "Jumlah";
            case 2: return "Harga Satuan";
            case 3: return "Subtotal";
            default: return null;
        }
    }
}
