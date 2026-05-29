package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Dokter;

public class TableDokter extends AbstractTableModel {
    private List<Dokter> listDokter;

    public TableDokter(List<Dokter> listDokter) {
        this.listDokter = listDokter;
    }

    @Override
    public int getRowCount() {
        return listDokter.size();
    }

    @Override
    public int getColumnCount() {
        return 6; 
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Dokter d = listDokter.get(rowIndex);
        switch (columnIndex) {
            case 0: return d.getId();
            case 1: return d.getNama();
            case 2: return d.getNomorSTR();
            case 3: return d.getSpesialisasi();
            case 4: return d.getTarifKonsultasi();
            case 5: return d.isStatusAktif() ? "Aktif" : "Tidak Aktif";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Dokter";
            case 1: return "Nama Dokter";
            case 2: return "Nomor STR";
            case 3: return "Spesialisasi";
            case 4: return "Tarif Konsultasi";
            case 5: return "Status Aktif";
            default: return null;
        }
    }
}