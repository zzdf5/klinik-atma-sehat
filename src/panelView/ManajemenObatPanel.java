/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.ObatControl;

import exception.*;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Component;

import model.Obat;
import model.ObatHerbal;
import model.ObatPaten;

import table.TableObat;


public class ManajemenObatPanel extends javax.swing.JPanel {
    private ObatControl obatControl = new ObatControl();
    
    
    private Obat obat = null;
    String action = null;
    String selectedId = null;
    
    List<Obat> listObat;
    
    private Component rootPane;
    
    
    public ManajemenObatPanel() {
        initComponents();
        setOpaque(false); 
        
        pilihBentukSediaanDropDown.removeAllItems();
        addItemDropDown();
        
        showObat(); 
        setComponentsObat(false); 
        setEditDeleteButtonObat(false); 
        clearTextObat(); 
    }
    
    private void addItemDropDown(){
        pilihBentukSediaanDropDown.addItem("Tablet");
        pilihBentukSediaanDropDown.addItem("Kapsul");
        pilihBentukSediaanDropDown.addItem("Sirup");
        pilihBentukSediaanDropDown.addItem("Puyer");
    }
    
    public void setRadioButtonValue(){
        obatHerbalRadioButton.setActionCommand("Obat Herbal");
        obatPatenRadioButton.setActionCommand("Obat Paten");
    }
    
    public void showObat() {
        obatPatenTable.setModel(buildPatenModel(""));
        obatHerbalTable.setModel(buildHerbalModel(""));
    }

    public void searchObat(String target) {
        obatPatenTable.setModel(buildPatenModel(target));
        obatHerbalTable.setModel(buildHerbalModel(target));
    }

    private DefaultTableModel buildPatenModel(String keyword) {
        DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID Obat", "Nama Obat", "Bentuk Sediaan", "Dosis", "Merk", "Harga Satuan", "Stok"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String kw = keyword == null ? "" : keyword.toLowerCase();
        for (ObatPaten p : obatControl.showDataPaten()) {
            if (kw.isEmpty() || p.getIdObat().toLowerCase().contains(kw)
                    || p.getNamaObat().toLowerCase().contains(kw)
                    || p.getMerk().toLowerCase().contains(kw)) {
                m.addRow(new Object[]{
                    p.getIdObat(), p.getNamaObat(), p.getBentukSediaan(),
                    p.getDosis(), p.getMerk(), p.getHargaSatuan(), p.getStok()
                });
            }
        }
        return m;
    }

    private DefaultTableModel buildHerbalModel(String keyword) {
        DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID Obat", "Nama Obat", "Bentuk Sediaan", "Dosis", "Bahan Utama", "Harga Satuan", "Stok"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String kw = keyword == null ? "" : keyword.toLowerCase();
        for (ObatHerbal h : obatControl.showDataHerbal()) {
            if (kw.isEmpty() || h.getIdObat().toLowerCase().contains(kw)
                    || h.getNamaObat().toLowerCase().contains(kw)
                    || h.getBahanUtama().toLowerCase().contains(kw)) {
                m.addRow(new Object[]{
                    h.getIdObat(), h.getNamaObat(), h.getBentukSediaan(),
                    h.getDosis(), h.getBahanUtama(), h.getHargaSatuan(), h.getStok()
                });
            }
        }
        return m;
    }
    
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public void setComponentsObat(boolean value){
        inputIdObatTextField.setEnabled(false);
        inputNamaObatTextField.setEnabled(value);
        pilihBentukSediaanDropDown.setEnabled(value);
        inputDosisTextField.setEnabled(value);
        obatHerbalRadioButton.setEnabled(value);
        obatPatenRadioButton.setEnabled(value);
        inputHargaSatuanTextField.setEnabled(value);
        inputStokObatTextField.setEnabled(value);
        simpanObatButton.setEnabled(value);
        batalObatButton.setEnabled(value);
        inputSpecialAtributeTextField.setEnabled(value);
    }
    
 
    public void setEditDeleteButtonObat(boolean value){
        barukanObatButton.setEnabled(value);
        hapusObatButton.setEnabled(value);
    }
    

    public void clearTextObat(){
        inputIdObatTextField.setText("");
        inputNamaObatTextField.setText("");
        pilihBentukSediaanDropDown.setSelectedIndex(-1);
        inputDosisTextField.setText("");
        kategoriObatRadioGroup.clearSelection();
        inputHargaSatuanTextField.setText("");
        inputStokObatTextField.setText("");
        pencarianObatTextField.setText("");
        inputSpecialAtributeTextField.setText("");
    }
    
    public void inputKosongException() throws InputKosongException {
        if(inputNamaObatTextField.getText().isEmpty() || inputDosisTextField.getText().isEmpty() ||
           inputHargaSatuanTextField.getText().isEmpty() || inputStokObatTextField.getText().isEmpty() ||
           pilihBentukSediaanDropDown.getSelectedIndex() == -1 || kategoriObatRadioGroup.getSelection() == null ||
           inputSpecialAtributeTextField.getText().isEmpty()) { 
            throw new InputKosongException();
        }
    }
    
    public void doSearchObat(){
        if(pencarianObatTextField.getText().isEmpty()) return;
    
        Obat o = obatControl.search(pencarianObatTextField.getText());
        if(o == null ){
            JOptionPane.showMessageDialog(rootPane, "NOT FOUND !!!");
            return;
        }

        setEditDeleteButtonObat(true);
        clearTextObat();

        setComponentsObat(true);

        inputIdObatTextField.setText(o.getIdObat()); 
        inputNamaObatTextField.setText(o.getNamaObat());
        pilihBentukSediaanDropDown.setSelectedItem(o.getBentukSediaan());
        inputDosisTextField.setText(o.getDosis());

        if (o instanceof model.ObatHerbal) {
            obatHerbalRadioButton.setSelected(true);
            inputSpecialAtributeLabel.setText("Bahan Utama");

            model.ObatHerbal oh = (model.ObatHerbal) o;
            inputSpecialAtributeTextField.setText(oh.getBahanUtama());
        } else {
            obatPatenRadioButton.setSelected(true);
            inputSpecialAtributeLabel.setText("Merk");

            model.ObatPaten op = (model.ObatPaten) o;
            inputSpecialAtributeTextField.setText(op.getMerk());
        }

        inputHargaSatuanTextField.setText(String.valueOf(o.getHargaSatuan()));
        inputStokObatTextField.setText(String.valueOf(o.getStok()));

        selectedId = o.getIdObat();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kategoriObatRadioGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        pencarianObatPanel = new javax.swing.JPanel();
        pencarianObatLabel = new javax.swing.JLabel();
        pencarianObatTextField = new javax.swing.JTextField();
        pencarianObatButton = new javax.swing.JButton();
        formInputDataObatPanel = new javax.swing.JPanel();
        inputDataObatLabel = new javax.swing.JLabel();
        pilihBentukSediaanPanel = new javax.swing.JPanel();
        pilihBentukSediaanLabel = new javax.swing.JLabel();
        pilihBentukSediaanDropDown = new javax.swing.JComboBox<>();
        inputNamaObatPanel = new javax.swing.JPanel();
        inputNamaObatLabel = new javax.swing.JLabel();
        inputNamaObatTextField = new javax.swing.JTextField();
        simpanObatButton = new javax.swing.JButton();
        batalObatButton = new javax.swing.JButton();
        inputIdObatPanel = new javax.swing.JPanel();
        inputIdObatLabel = new javax.swing.JLabel();
        inputIdObatTextField = new javax.swing.JTextField();
        inputDosisPanel = new javax.swing.JPanel();
        inputDosisLabel = new javax.swing.JLabel();
        inputDosisTextField = new javax.swing.JTextField();
        pilihKategoriObatPanel = new javax.swing.JPanel();
        pilihKategoriObatLabel = new javax.swing.JLabel();
        obatHerbalRadioButton = new javax.swing.JRadioButton();
        obatPatenRadioButton = new javax.swing.JRadioButton();
        inputHargaSatuanPanel = new javax.swing.JPanel();
        inputHargaSatuanLabel = new javax.swing.JLabel();
        inputHargaSatuanTextField = new javax.swing.JTextField();
        inputStokObatPanel = new javax.swing.JPanel();
        inputStokObatLabel = new javax.swing.JLabel();
        inputStokObatTextField = new javax.swing.JTextField();
        inputSpecialAtributePanel = new javax.swing.JPanel();
        inputSpecialAtributeLabel = new javax.swing.JLabel();
        inputSpecialAtributeTextField = new javax.swing.JTextField();
        obatPatenScrollPane = new javax.swing.JScrollPane();
        obatPatenTable = new javax.swing.JTable();
        obatButtonPanel = new javax.swing.JPanel();
        tambahObatButton = new javax.swing.JButton();
        barukanObatButton = new javax.swing.JButton();
        hapusObatButton = new javax.swing.JButton();
        obatPatenLabel = new javax.swing.JLabel();
        obatHerbalScrollPane = new javax.swing.JScrollPane();
        obatHerbalTable = new javax.swing.JTable();
        obatHerbalLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));

        pencarianObatPanel.setBackground(new java.awt.Color(18, 32, 86));
        pencarianObatPanel.setForeground(new java.awt.Color(255, 255, 255));
        pencarianObatPanel.setPreferredSize(new java.awt.Dimension(800, 70));

        pencarianObatLabel.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 18)); // NOI18N
        pencarianObatLabel.setForeground(new java.awt.Color(255, 255, 255));
        pencarianObatLabel.setText("Pencarian Obat");

        pencarianObatTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pencarianObatTextFieldKeyPressed(evt);
            }
        });

        pencarianObatButton.setBackground(new java.awt.Color(204, 204, 255));
        pencarianObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        pencarianObatButton.setText("Cari");
        pencarianObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencarianObatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pencarianObatPanelLayout = new javax.swing.GroupLayout(pencarianObatPanel);
        pencarianObatPanel.setLayout(pencarianObatPanelLayout);
        pencarianObatPanelLayout.setHorizontalGroup(
            pencarianObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pencarianObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pencarianObatPanelLayout.createSequentialGroup()
                        .addComponent(pencarianObatLabel)
                        .addGap(0, 994, Short.MAX_VALUE))
                    .addGroup(pencarianObatPanelLayout.createSequentialGroup()
                        .addComponent(pencarianObatTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pencarianObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pencarianObatPanelLayout.setVerticalGroup(
            pencarianObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pencarianObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pencarianObatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pencarianObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        formInputDataObatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputDataObatLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        inputDataObatLabel.setText("Data Obat");

        pilihBentukSediaanPanel.setBackground(new java.awt.Color(255, 255, 255));

        pilihBentukSediaanLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        pilihBentukSediaanLabel.setText("Bentuk sediaan");

        pilihBentukSediaanDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pilihBentukSediaanPanelLayout = new javax.swing.GroupLayout(pilihBentukSediaanPanel);
        pilihBentukSediaanPanel.setLayout(pilihBentukSediaanPanelLayout);
        pilihBentukSediaanPanelLayout.setHorizontalGroup(
            pilihBentukSediaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihBentukSediaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pilihBentukSediaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pilihBentukSediaanPanelLayout.createSequentialGroup()
                        .addComponent(pilihBentukSediaanLabel)
                        .addGap(0, 111, Short.MAX_VALUE))
                    .addComponent(pilihBentukSediaanDropDown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pilihBentukSediaanPanelLayout.setVerticalGroup(
            pilihBentukSediaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihBentukSediaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pilihBentukSediaanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihBentukSediaanDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        inputNamaObatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNamaObatLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputNamaObatLabel.setText("Nama Obat");

        javax.swing.GroupLayout inputNamaObatPanelLayout = new javax.swing.GroupLayout(inputNamaObatPanel);
        inputNamaObatPanel.setLayout(inputNamaObatPanelLayout);
        inputNamaObatPanelLayout.setHorizontalGroup(
            inputNamaObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNamaObatLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(inputNamaObatPanelLayout.createSequentialGroup()
                .addComponent(inputNamaObatTextField)
                .addContainerGap())
        );
        inputNamaObatPanelLayout.setVerticalGroup(
            inputNamaObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaObatPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNamaObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaObatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        simpanObatButton.setBackground(new java.awt.Color(51, 178, 73));
        simpanObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        simpanObatButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanObatButton.setText("Simpan");
        simpanObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanObatButtonActionPerformed(evt);
            }
        });

        batalObatButton.setBackground(new java.awt.Color(237, 8, 0));
        batalObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        batalObatButton.setForeground(new java.awt.Color(255, 255, 255));
        batalObatButton.setText("Batal");
        batalObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalObatButtonActionPerformed(evt);
            }
        });

        inputIdObatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdObatLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputIdObatLabel.setText("ID-Obat");

        javax.swing.GroupLayout inputIdObatPanelLayout = new javax.swing.GroupLayout(inputIdObatPanel);
        inputIdObatPanel.setLayout(inputIdObatPanelLayout);
        inputIdObatPanelLayout.setHorizontalGroup(
            inputIdObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdObatLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputIdObatTextField)
        );
        inputIdObatPanelLayout.setVerticalGroup(
            inputIdObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdObatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputDosisPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputDosisLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputDosisLabel.setText("Dosis");

        inputDosisTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputDosisTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputDosisPanelLayout = new javax.swing.GroupLayout(inputDosisPanel);
        inputDosisPanel.setLayout(inputDosisPanelLayout);
        inputDosisPanelLayout.setHorizontalGroup(
            inputDosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputDosisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputDosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputDosisTextField)
                    .addGroup(inputDosisPanelLayout.createSequentialGroup()
                        .addComponent(inputDosisLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inputDosisPanelLayout.setVerticalGroup(
            inputDosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputDosisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputDosisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputDosisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pilihKategoriObatPanel.setBackground(new java.awt.Color(255, 255, 255));

        pilihKategoriObatLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        pilihKategoriObatLabel.setText("Kategori");

        kategoriObatRadioGroup.add(obatHerbalRadioButton);
        obatHerbalRadioButton.setText("Obat Herbal");
        obatHerbalRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obatHerbalRadioButtonActionPerformed(evt);
            }
        });

        kategoriObatRadioGroup.add(obatPatenRadioButton);
        obatPatenRadioButton.setText("Obat Paten");
        obatPatenRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obatPatenRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pilihKategoriObatPanelLayout = new javax.swing.GroupLayout(pilihKategoriObatPanel);
        pilihKategoriObatPanel.setLayout(pilihKategoriObatPanelLayout);
        pilihKategoriObatPanelLayout.setHorizontalGroup(
            pilihKategoriObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihKategoriObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pilihKategoriObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pilihKategoriObatLabel)
                    .addGroup(pilihKategoriObatPanelLayout.createSequentialGroup()
                        .addComponent(obatHerbalRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(obatPatenRadioButton)))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        pilihKategoriObatPanelLayout.setVerticalGroup(
            pilihKategoriObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihKategoriObatPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pilihKategoriObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pilihKategoriObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(obatHerbalRadioButton)
                    .addComponent(obatPatenRadioButton))
                .addGap(8, 8, 8))
        );

        inputHargaSatuanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputHargaSatuanLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputHargaSatuanLabel.setText("Harga Satuan");

        inputHargaSatuanTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHargaSatuanTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputHargaSatuanPanelLayout = new javax.swing.GroupLayout(inputHargaSatuanPanel);
        inputHargaSatuanPanel.setLayout(inputHargaSatuanPanelLayout);
        inputHargaSatuanPanelLayout.setHorizontalGroup(
            inputHargaSatuanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputHargaSatuanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputHargaSatuanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputHargaSatuanTextField)
                    .addGroup(inputHargaSatuanPanelLayout.createSequentialGroup()
                        .addComponent(inputHargaSatuanLabel)
                        .addGap(0, 114, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inputHargaSatuanPanelLayout.setVerticalGroup(
            inputHargaSatuanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputHargaSatuanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputHargaSatuanLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputHargaSatuanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputStokObatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputStokObatLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputStokObatLabel.setText("Stok Obat");

        inputStokObatTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputStokObatTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputStokObatPanelLayout = new javax.swing.GroupLayout(inputStokObatPanel);
        inputStokObatPanel.setLayout(inputStokObatPanelLayout);
        inputStokObatPanelLayout.setHorizontalGroup(
            inputStokObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStokObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputStokObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputStokObatPanelLayout.createSequentialGroup()
                        .addComponent(inputStokObatLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(inputStokObatTextField)))
        );
        inputStokObatPanelLayout.setVerticalGroup(
            inputStokObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStokObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputStokObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputStokObatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputSpecialAtributePanel.setBackground(new java.awt.Color(255, 255, 255));

        inputSpecialAtributeLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        inputSpecialAtributeLabel.setText("Bahan Utama");

        inputSpecialAtributeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSpecialAtributeTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputSpecialAtributePanelLayout = new javax.swing.GroupLayout(inputSpecialAtributePanel);
        inputSpecialAtributePanel.setLayout(inputSpecialAtributePanelLayout);
        inputSpecialAtributePanelLayout.setHorizontalGroup(
            inputSpecialAtributePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputSpecialAtributePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputSpecialAtributePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputSpecialAtributeTextField)
                    .addGroup(inputSpecialAtributePanelLayout.createSequentialGroup()
                        .addComponent(inputSpecialAtributeLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inputSpecialAtributePanelLayout.setVerticalGroup(
            inputSpecialAtributePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputSpecialAtributePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputSpecialAtributeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputSpecialAtributeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formInputDataObatPanelLayout = new javax.swing.GroupLayout(formInputDataObatPanel);
        formInputDataObatPanel.setLayout(formInputDataObatPanelLayout);
        formInputDataObatPanelLayout.setHorizontalGroup(
            formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formInputDataObatPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(inputDataObatLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, formInputDataObatPanelLayout.createSequentialGroup()
                                .addComponent(pilihBentukSediaanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputDosisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(pilihKategoriObatPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputSpecialAtributePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputNamaObatPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(formInputDataObatPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputIdObatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(formInputDataObatPanelLayout.createSequentialGroup()
                                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(simpanObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inputHargaSatuanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputStokObatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(batalObatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())))
        );
        formInputDataObatPanelLayout.setVerticalGroup(
            formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataObatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputDataObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputIdObatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaObatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputDosisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pilihBentukSediaanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihKategoriObatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputSpecialAtributePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputStokObatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputHargaSatuanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataObatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpanObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batalObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        obatPatenScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        obatPatenTable.setAutoCreateRowSorter(true);
        obatPatenTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        obatPatenTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        obatPatenTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                obatPatenTableMouseClicked(evt);
            }
        });
        obatPatenScrollPane.setViewportView(obatPatenTable);

        obatButtonPanel.setBackground(new java.awt.Color(255, 255, 255));

        tambahObatButton.setBackground(new java.awt.Color(51, 178, 73));
        tambahObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        tambahObatButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahObatButton.setText("Tambah");
        tambahObatButton.setPreferredSize(new java.awt.Dimension(124, 24));
        tambahObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahObatButtonActionPerformed(evt);
            }
        });

        barukanObatButton.setBackground(new java.awt.Color(255, 189, 3));
        barukanObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        barukanObatButton.setForeground(new java.awt.Color(255, 255, 255));
        barukanObatButton.setText("Barukan");
        barukanObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barukanObatButtonActionPerformed(evt);
            }
        });

        hapusObatButton.setBackground(new java.awt.Color(237, 8, 0));
        hapusObatButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
        hapusObatButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusObatButton.setText("Hapus");
        hapusObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusObatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout obatButtonPanelLayout = new javax.swing.GroupLayout(obatButtonPanel);
        obatButtonPanel.setLayout(obatButtonPanelLayout);
        obatButtonPanelLayout.setHorizontalGroup(
            obatButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(obatButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(barukanObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(hapusObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        obatButtonPanelLayout.setVerticalGroup(
            obatButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(obatButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(obatButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tambahObatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(obatButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(barukanObatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(hapusObatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
                .addContainerGap())
        );

        obatPatenLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        obatPatenLabel.setText("Obat Paten");

        obatHerbalScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        obatHerbalTable.setAutoCreateRowSorter(true);
        obatHerbalTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        obatHerbalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        obatHerbalTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                obatHerbalTableMouseClicked(evt);
            }
        });
        obatHerbalScrollPane.setViewportView(obatHerbalTable);

        obatHerbalLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        obatHerbalLabel.setText("Obat Herbal");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pencarianObatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(formInputDataObatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(obatButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(obatPatenScrollPane)
                            .addComponent(obatHerbalScrollPane)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(obatPatenLabel)
                                    .addComponent(obatHerbalLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pencarianObatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(obatButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formInputDataObatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(obatPatenLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(obatPatenScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(obatHerbalLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(obatHerbalScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void batalObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalObatButtonActionPerformed
        clearTextObat();
        action = null;
        selectedId = null;
        setComponentsObat(false);
        setEditDeleteButtonObat(false);
        tambahObatButton.setEnabled(true);
    }//GEN-LAST:event_batalObatButtonActionPerformed

    private void inputDosisTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputDosisTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputDosisTextFieldActionPerformed

    private void inputHargaSatuanTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHargaSatuanTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputHargaSatuanTextFieldActionPerformed

    private void inputStokObatTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputStokObatTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputStokObatTextFieldActionPerformed

    private void tambahObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahObatButtonActionPerformed
        action = "tambah";
        clearTextObat();
        setComponentsObat(true);
        setEditDeleteButtonObat(false);
    }//GEN-LAST:event_tambahObatButtonActionPerformed

    private void barukanObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barukanObatButtonActionPerformed
        action = "ubah";
        setComponentsObat(true);
        setEditDeleteButtonObat(true);
        inputNamaObatTextField.setEnabled(false);
        obatHerbalRadioButton.setEnabled(false);
        obatPatenRadioButton.setEnabled(false);
    }//GEN-LAST:event_barukanObatButtonActionPerformed

    private void hapusObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusObatButtonActionPerformed
        action = "hapus";
        int confirm = JOptionPane.showConfirmDialog(rootPane, "Yakin ingin menghapus data Obat ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            obatControl.delete(inputIdObatTextField.getText()); 
            clearTextObat();
            setComponentsObat(false);
            setEditDeleteButtonObat(false);
            showObat();
            tambahObatButton.setEnabled(true);
        }
    }//GEN-LAST:event_hapusObatButtonActionPerformed

    private void simpanObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanObatButtonActionPerformed
        try {
            
            inputKosongException();

            
            if(!isDouble(inputHargaSatuanTextField.getText()) || !isInteger(inputStokObatTextField.getText())){
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka/desimal, dan Stok harus berupa angka bulat!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Apakah Anda yakin ingin melakukan aksi " + action + " pada data Obat ini?", 
                "Konfirmasi Simpan", 
                JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return; 
            }
            String idObat = inputIdObatTextField.getText();
            String namaObat = inputNamaObatTextField.getText();
            String bentukSediaan = pilihBentukSediaanDropDown.getSelectedItem().toString();
            String dosis = inputDosisTextField.getText();
            double harga = Double.parseDouble(inputHargaSatuanTextField.getText());
            int stok = Integer.parseInt(inputStokObatTextField.getText());
            String specialAttr = inputSpecialAtributeTextField.getText();
            
            if(obatHerbalRadioButton.isSelected()){
                ObatHerbal herbal = new ObatHerbal(specialAttr, idObat, namaObat, bentukSediaan, dosis, "Obat Herbal", harga, stok);
                
                if(action.equals("tambah")){
                    obatControl.insertHerbal(herbal);   
                } else if(action.equals("ubah")){
                    obatControl.updateHerbal(herbal, selectedId);
                }
            } else {
                ObatPaten paten = new ObatPaten(specialAttr, idObat, namaObat, bentukSediaan, dosis, "Obat Paten", harga, stok);
                
                if(action.equals("tambah")){
                    obatControl.insertPaten(paten); 
                } else if(action.equals("ubah")){
                    obatControl.updatePaten(paten, selectedId); 
                }
            }
            
            clearTextObat();
            setComponentsObat(false);
            setEditDeleteButtonObat(false);
            tambahObatButton.setEnabled(true);
            showObat();
            
            selectedId = null; 
            action = null;

        } catch(InputKosongException e) {
            JOptionPane.showMessageDialog(this, "Data Input Tidak Boleh Kosong!");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }//GEN-LAST:event_simpanObatButtonActionPerformed

    private void inputSpecialAtributeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSpecialAtributeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputSpecialAtributeTextFieldActionPerformed

    private void obatHerbalRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obatHerbalRadioButtonActionPerformed
        inputSpecialAtributeLabel.setText("Bahan Utama");
        if ("tambah".equals(action)) {
            inputIdObatTextField.setText(obatControl.generateIdHerbal());
        }
    }//GEN-LAST:event_obatHerbalRadioButtonActionPerformed

    private void obatPatenRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obatPatenRadioButtonActionPerformed
        inputSpecialAtributeLabel.setText("Merk");
        if ("tambah".equals(action)) {
            inputIdObatTextField.setText(obatControl.generateIdPaten());
        }
    }//GEN-LAST:event_obatPatenRadioButtonActionPerformed

    private void pencarianObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencarianObatButtonActionPerformed
        doSearchObat();
    }//GEN-LAST:event_pencarianObatButtonActionPerformed

    private void pencarianObatTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pencarianObatTextFieldKeyPressed
        if(evt.getKeyChar() == '\n'){
            doSearchObat();
        }
    }//GEN-LAST:event_pencarianObatTextFieldKeyPressed

    private void obatPatenTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_obatPatenTableMouseClicked
        int clickedRow = obatPatenTable.getSelectedRow();
        if(clickedRow < 0) return;

        TableModel tableModel = obatPatenTable.getModel();
        setComponentsObat(false);
        setEditDeleteButtonObat(true);
        tambahObatButton.setEnabled(true);
        
        // Pengecekan aman untuk mencegah NullPointerException
        Object idVal = tableModel.getValueAt(clickedRow, 0);
        selectedId = idVal != null ? idVal.toString() : "";
        inputIdObatTextField.setText(selectedId);
        
        Object namaVal = tableModel.getValueAt(clickedRow, 1);
        inputNamaObatTextField.setText(namaVal != null ? namaVal.toString() : "");
        
        Object bentukVal = tableModel.getValueAt(clickedRow, 2);
        if(bentukVal != null) pilihBentukSediaanDropDown.setSelectedItem(bentukVal.toString());
        
        Object dosisVal = tableModel.getValueAt(clickedRow, 3);
        inputDosisTextField.setText(dosisVal != null ? dosisVal.toString() : "");
        
        // Tabel paten: col 4=Merk, 5=Harga, 6=Stok (Kategori tidak ditampilkan)
        obatPatenRadioButton.setSelected(true);
        inputSpecialAtributeLabel.setText("Merk");

        Object merkVal = tableModel.getValueAt(clickedRow, 4);
        inputSpecialAtributeTextField.setText(merkVal != null ? merkVal.toString() : "");

        Object hargaVal = tableModel.getValueAt(clickedRow, 5);
        inputHargaSatuanTextField.setText(hargaVal != null ? hargaVal.toString() : "");

        Object stokVal = tableModel.getValueAt(clickedRow, 6);
        inputStokObatTextField.setText(stokVal != null ? stokVal.toString() : "");
    }//GEN-LAST:event_obatPatenTableMouseClicked

    private void obatHerbalTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_obatHerbalTableMouseClicked
        int clickedRow = obatHerbalTable.getSelectedRow();
        if (clickedRow < 0) return;

        TableModel tableModel = obatHerbalTable.getModel();
        setComponentsObat(false);
        setEditDeleteButtonObat(true);
        tambahObatButton.setEnabled(true);

        Object idVal = tableModel.getValueAt(clickedRow, 0);
        selectedId = idVal != null ? idVal.toString() : "";
        inputIdObatTextField.setText(selectedId);

        Object namaVal = tableModel.getValueAt(clickedRow, 1);
        inputNamaObatTextField.setText(namaVal != null ? namaVal.toString() : "");

        Object bentukVal = tableModel.getValueAt(clickedRow, 2);
        if (bentukVal != null) pilihBentukSediaanDropDown.setSelectedItem(bentukVal.toString());

        Object dosisVal = tableModel.getValueAt(clickedRow, 3);
        inputDosisTextField.setText(dosisVal != null ? dosisVal.toString() : "");

        // Tabel herbal: col 4=Bahan Utama, 5=Harga, 6=Stok
        obatHerbalRadioButton.setSelected(true);
        inputSpecialAtributeLabel.setText("Bahan Utama");

        Object bahanVal = tableModel.getValueAt(clickedRow, 4);
        inputSpecialAtributeTextField.setText(bahanVal != null ? bahanVal.toString() : "");

        Object hargaVal = tableModel.getValueAt(clickedRow, 5);
        inputHargaSatuanTextField.setText(hargaVal != null ? hargaVal.toString() : "");

        Object stokVal = tableModel.getValueAt(clickedRow, 6);
        inputStokObatTextField.setText(stokVal != null ? stokVal.toString() : "");
    }//GEN-LAST:event_obatHerbalTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barukanObatButton;
    private javax.swing.JButton batalObatButton;
    private javax.swing.JPanel formInputDataObatPanel;
    private javax.swing.JButton hapusObatButton;
    private javax.swing.JLabel inputDataObatLabel;
    private javax.swing.JLabel inputDosisLabel;
    private javax.swing.JPanel inputDosisPanel;
    private javax.swing.JTextField inputDosisTextField;
    private javax.swing.JLabel inputHargaSatuanLabel;
    private javax.swing.JPanel inputHargaSatuanPanel;
    private javax.swing.JTextField inputHargaSatuanTextField;
    private javax.swing.JLabel inputIdObatLabel;
    private javax.swing.JPanel inputIdObatPanel;
    private javax.swing.JTextField inputIdObatTextField;
    private javax.swing.JLabel inputNamaObatLabel;
    private javax.swing.JPanel inputNamaObatPanel;
    private javax.swing.JTextField inputNamaObatTextField;
    private javax.swing.JLabel inputSpecialAtributeLabel;
    private javax.swing.JPanel inputSpecialAtributePanel;
    private javax.swing.JTextField inputSpecialAtributeTextField;
    private javax.swing.JLabel inputStokObatLabel;
    private javax.swing.JPanel inputStokObatPanel;
    private javax.swing.JTextField inputStokObatTextField;
    private javax.swing.ButtonGroup kategoriObatRadioGroup;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel obatButtonPanel;
    private javax.swing.JLabel obatHerbalLabel;
    private javax.swing.JRadioButton obatHerbalRadioButton;
    private javax.swing.JScrollPane obatHerbalScrollPane;
    private javax.swing.JTable obatHerbalTable;
    private javax.swing.JLabel obatPatenLabel;
    private javax.swing.JRadioButton obatPatenRadioButton;
    private javax.swing.JScrollPane obatPatenScrollPane;
    private javax.swing.JTable obatPatenTable;
    private javax.swing.JButton pencarianObatButton;
    private javax.swing.JLabel pencarianObatLabel;
    private javax.swing.JPanel pencarianObatPanel;
    private javax.swing.JTextField pencarianObatTextField;
    private javax.swing.JComboBox<String> pilihBentukSediaanDropDown;
    private javax.swing.JLabel pilihBentukSediaanLabel;
    private javax.swing.JPanel pilihBentukSediaanPanel;
    private javax.swing.JLabel pilihKategoriObatLabel;
    private javax.swing.JPanel pilihKategoriObatPanel;
    private javax.swing.JButton simpanObatButton;
    private javax.swing.JButton tambahObatButton;
    // End of variables declaration//GEN-END:variables
}
