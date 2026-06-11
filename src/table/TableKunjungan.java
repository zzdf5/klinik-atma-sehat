package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableKunjungan extends AbstractTableModel {
    private List<Object[]> listKunjungan;

    public TableKunjungan(List<Object[]> listKunjungan) {
        this.listKunjungan = listKunjungan;
    }

    @Override
    public int getRowCount() {
        return listKunjungan.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return listKunjungan.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Kunjungan";
            case 1: return "No. Rekam Medis";
            case 2: return "Nama Pasien";
            case 3: return "Nama Dokter";
            case 4: return "Tanggal";
            case 5: return "Jam";
            case 6: return "Status";
            default: return null;
        }
    }
}
