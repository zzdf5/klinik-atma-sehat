package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Pasien;

public class TablePasien extends AbstractTableModel {
    private List<Pasien> listPasien;

    public TablePasien(List<Pasien> listPasien) {
        this.listPasien = listPasien;
    }

    @Override
    public int getRowCount() {
        return listPasien.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pasien p = listPasien.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getNomorRekamMedis();
            case 2: return p.getNama();
            case 3: return p.getTanggalLahir();
            case 4: return p.getJenisKelamin();
            case 5: return p.getNoTelepon();
            case 6: return p.getAlamat();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Pasien";
            case 1: return "No. Rekam Medis";
            case 2: return "Nama";
            case 3: return "Tgl Lahir";
            case 4: return "J/K";
            case 5: return "No. Telepon";
            case 6: return "Alamat";
            default: return null;
        }
    }
}
