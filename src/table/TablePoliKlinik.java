package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Poliklinik;

public class TablePoliKlinik extends AbstractTableModel {
    private List<Poliklinik> listPoliklinik;

    public TablePoliKlinik(List<Poliklinik> listPoliklinik) {
        this.listPoliklinik = listPoliklinik;
    }

    @Override
    public int getRowCount() {
        return listPoliklinik.size();
    }

    @Override
    public int getColumnCount() {
        return 4; 
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return listPoliklinik.get(rowIndex).getIdPoliklinik();
            case 1:
                return listPoliklinik.get(rowIndex).getNamaPoliklinik();
            case 2:
                return listPoliklinik.get(rowIndex).getLokasiRuangan();
            case 3:
                return listPoliklinik.get(rowIndex).getJamOperasional();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID PoliKlinik";
            case 1: return "Nama PoliKlinik";
            case 2: return "Lokasi Ruangan";
            case 3: return "Jam Operasional";
            default: return null;
        }
    }
}