package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Menampilkan subset kolom dari baris Antrian 8-kolom
 * (id, noUrut, namaPasien, namaDokter, poliklinik, tanggal, jenis, status).
 */
public class TableAntrianHarian extends AbstractTableModel {
    private static final int[] SOURCE_COLUMN = {1, 2, 3, 4, 6, 7};

    private List<Object[]> listAntrian;

    public TableAntrianHarian(List<Object[]> listAntrian) {
        this.listAntrian = listAntrian;
    }

    @Override
    public int getRowCount() {
        return listAntrian.size();
    }

    @Override
    public int getColumnCount() {
        return SOURCE_COLUMN.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return listAntrian.get(rowIndex)[SOURCE_COLUMN[columnIndex]];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "No. Urut";
            case 1: return "Nama Pasien";
            case 2: return "Nama Dokter";
            case 3: return "Poliklinik";
            case 4: return "Jenis";
            case 5: return "Status";
            default: return null;
        }
    }
}
