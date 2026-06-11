package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableJadwalDokter extends AbstractTableModel {
    private List<Object[]> listJadwal;

    public TableJadwalDokter(List<Object[]> listJadwal) {
        this.listJadwal = listJadwal;
    }

    @Override
    public int getRowCount() {
        return listJadwal.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return listJadwal.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Jadwal";
            case 1: return "Nama Dokter";
            case 2: return "Jam Mulai";
            case 3: return "Jam Selesai";
            case 4: return "Poliklinik";
            case 5: return "Kuota";
            default: return null;
        }
    }
}
