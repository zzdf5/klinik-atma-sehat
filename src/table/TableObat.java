/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;

/**
 *
 * @author HP
 */
public class TableObat extends AbstractTableModel{
    private List<Obat> listObat;

    public TableObat(List<Obat> listObat) {
        this.listObat = listObat;
    }
    
    @Override
    public int getRowCount() {
        return listObat.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return listObat.get(rowIndex).getIdObat();
            case 1:
                return listObat.get(rowIndex).getNamaObat();
            case 2:
                return listObat.get(rowIndex).getBentukSediaan();
            case 3:
                return listObat.get(rowIndex).getDosis();
            case 4:
                return listObat.get(rowIndex).getKategori();
            case 5:
                return listObat.get(rowIndex).getHargaSatuan();
            case 6:
                return listObat.get(rowIndex).getStok();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:
                return "ID Obat";
            case 1:
                return "Nama Obat";
            case 2:
                return "Bentuk Sediaan";
            case 3:
                return "Dosis";
            case 4:
                return "Kategori";
            case 5:
                return "Harga Satuan";
            case 6:
                return "Stok";
            default:
                return null;
        }
    }
}
