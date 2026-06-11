package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableTagihan extends AbstractTableModel {
    private List<Object[]> listTagihan;

    public TableTagihan(List<Object[]> listTagihan) {
        this.listTagihan = listTagihan;
    }

    @Override
    public int getRowCount() {
        return listTagihan.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = listTagihan.get(rowIndex);
        if (columnIndex == 4) {
            return String.format("Rp%.0f", (Double) row[4]);
        }
        return row[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Tagihan";
            case 1: return "Nama Pasien";
            case 2: return "ID Kunjungan";
            case 3: return "Tanggal";
            case 4: return "Total";
            case 5: return "Metode";
            case 6: return "Status";
            default: return null;
        }
    }
}
