package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableAntrian extends AbstractTableModel {
    private List<Object[]> listAntrian;

    public TableAntrian(List<Object[]> listAntrian) {
        this.listAntrian = listAntrian;
    }

    @Override
    public int getRowCount() {
        return listAntrian.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return listAntrian.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "No. Urut";
            case 2: return "Nama Pasien";
            case 3: return "Nama Dokter";
            case 4: return "Poliklinik";
            case 5: return "Tanggal";
            case 6: return "Jenis";
            case 7: return "Status";
            default: return null;
        }
    }
}
