/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.PembelianKendaraanControl;
import control.CustomerControlE;
import control.CustomerServiceControlE;
import control.GaransiControlE;
import control.KendaraanControlE;
import exception.InputKosongException;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.PembelianKendaraan;
import model.CustomerE;
import model.CustomerServiceE;
import model.GaransiE;
import model.KendaraanE;

import table.TablePembelianKendaraan;

public class PendaftaranPasienPanel extends javax.swing.JPanel {
    private PembelianKendaraanControl pembelianControl;
    private CustomerControlE customerControl;
    private KendaraanControlE kendaraanControl;
    private CustomerServiceControlE customerServiceControl;
    private GaransiControlE garansiControl; 
    
    private PembelianKendaraan pk = null;
    String action = null;
    int selectedId = 0;
    
    List<CustomerE> listCustomer;
    List<KendaraanE> listKendaraan;
    List<CustomerServiceE> listCustomerService;
    List<GaransiE> listGaransi;
    
    private Component rootPane;
    
    public PendaftaranPasienPanel() {
        initComponents();
        setOpaque(false);
        
        pembelianControl = new PembelianKendaraanControl();
        customerControl = new CustomerControlE();
        kendaraanControl = new KendaraanControlE();
        customerServiceControl = new CustomerServiceControlE();
        garansiControl = new GaransiControlE();
        
        setCustomerToDropdown();
        setKendaraanToDropdown();
        setCustomerServiceToDropdown();
        setGaransiToDropdown();
        setRadioButtonValue();
        
        showTableBySearch("");
        
        clearText();        
        setComponent(false);
        setRadioComponent(false);
        setEditDeleteBtn(false);
    }
    
    public void setKendaraanToDropdown(){
        listKendaraan = kendaraanControl.showListKendaraan();
        System.out.println(listKendaraan.size());
        for (int i = 0; i < listKendaraan.size(); i++) {
            namaKendaraanDropdown.addItem(listKendaraan.get(i));
        }
    }
    
    public void setCustomerToDropdown(){
        listCustomer = customerControl.showListCustomer();
        for (int i = 0; i < listCustomer.size(); i++) {
            namaCustomerDropdown.addItem(listCustomer.get(i));
        }
    }
    
    public void setCustomerServiceToDropdown(){
        listCustomerService = customerServiceControl.showListCustomerService();
        for (int i = 0; i < listCustomerService.size(); i++) {
            namaCustomerServiceDropdown.addItem(listCustomerService.get(i));
        }
    }

    public void setGaransiToDropdown(){
        listGaransi = garansiControl.showListGaransi();
        for (int i = 0; i < listGaransi.size(); i++) {
            namaGaransiDropdown.addItem(listGaransi.get(i));
        }
    }
    
    public void setRadioButtonValue(){
        bcaRadio.setActionCommand("BCA");
        briRadio.setActionCommand("BRI");
        mayRadio.setActionCommand("May Bank");
        bpdRadio.setActionCommand("BPD DIY");
        tunaiRadio.setActionCommand("Tunai");
    }
    
    public void showTableBySearch(String target){
        tablePembelian.setModel(pembelianControl.showTable(target));
    }
    
    public void inputKosongPembelianException() throws InputKosongException{
        if (namaCustomerDropdown.getSelectedIndex() == -1 || namaKendaraanDropdown.getSelectedIndex() == -1 || namaCustomerServiceDropdown.getSelectedIndex() == -1 ||
            namaGaransiDropdown.getSelectedIndex() == -1 || jumlahPembelianInput.getText().isEmpty() ||  tanggalTransaksiInput.getDate() == null || (!kursiCheckbox.isSelected() && !sistemNavigasiCheckbox.isSelected())
            || metodePembayaranGroup.getSelection().getActionCommand() == null) {
            throw new InputKosongException();
        }
    }
    
    public void clearText(){
        namaCustomerDropdown.setSelectedIndex(-1);
        namaKendaraanDropdown.setSelectedIndex(-1);
        namaCustomerServiceDropdown.setSelectedIndex(-1);
        namaGaransiDropdown.setSelectedIndex(-1);
        
        kursiCheckbox.setSelected(false);
        sistemNavigasiCheckbox.setSelected(false);
        
        metodePembayaranGroup.clearSelection();
        
        searchTransaksiInputTextField.setText("");
        jumlahPembelianInput.setText("");
        
        tanggalTransaksiInput.setDate(null);
    }
    
    public void setEditDeleteBtn(boolean value){
        barukanTransaksiButton.setEnabled(value);
        hapusTransaksiButton.setEnabled(value);
    }
    
    public void setComponent(boolean value){
        namaCustomerDropdown.setEnabled(value);
        namaKendaraanDropdown.setEnabled(value);
        namaCustomerServiceDropdown.setEnabled(value);
        namaGaransiDropdown.setEnabled(value);
        jumlahPembelianInput.setEnabled(value);
        tanggalTransaksiInput.setEnabled(value);
        
        kursiCheckbox.setEnabled(value);
        sistemNavigasiCheckbox.setEnabled(value);
        
        simpanTransaksiButton.setEnabled(value);
        batalTransaksiButton.setEnabled(value);
    }
    
    public void setRadioComponent(boolean value){
        bcaRadio.setEnabled(value);
        briRadio.setEnabled(value);
        mayRadio.setEnabled(value);
        bpdRadio.setEnabled(value);
        tunaiRadio.setEnabled(value);
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        metodePembayaranGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        searchTransaksiInputPanel = new javax.swing.JPanel();
        searchTransaksiInputLabel = new javax.swing.JLabel();
        searchTransaksiInputTextField = new javax.swing.JTextField();
        searchTransaksiInputButton = new javax.swing.JButton();
        transaksiFormPanel = new javax.swing.JPanel();
        transaksiButtonPanel = new javax.swing.JPanel();
        barukanTransaksiButton = new javax.swing.JButton();
        hapusTransaksiButton = new javax.swing.JButton();
        tambahTransaksiButton = new javax.swing.JButton();
        namaCustomerPanel = new javax.swing.JPanel();
        namaCustomerLabel = new javax.swing.JLabel();
        namaCustomerDropdown = new javax.swing.JComboBox<>();
        namaGaransiPanel = new javax.swing.JPanel();
        namaGaransiLabel = new javax.swing.JLabel();
        namaGaransiDropdown = new javax.swing.JComboBox<>();
        jumlahPembelianPanel = new javax.swing.JPanel();
        jumlahLabel = new javax.swing.JLabel();
        jumlahPembelianInput = new javax.swing.JTextField();
        checkboxPanel = new javax.swing.JPanel();
        atributLabel = new javax.swing.JLabel();
        kursiCheckbox = new javax.swing.JCheckBox();
        sistemNavigasiCheckbox = new javax.swing.JCheckBox();
        metodePembayaranPanel = new javax.swing.JPanel();
        metodePembayaranLabel = new javax.swing.JLabel();
        bcaRadio = new javax.swing.JRadioButton();
        briRadio = new javax.swing.JRadioButton();
        mayRadio = new javax.swing.JRadioButton();
        bpdRadio = new javax.swing.JRadioButton();
        tunaiRadio = new javax.swing.JRadioButton();
        namaCustomerServicePanel = new javax.swing.JPanel();
        namaCustomerServiceLabel = new javax.swing.JLabel();
        namaCustomerServiceDropdown = new javax.swing.JComboBox<>();
        namaKendaraanPanel = new javax.swing.JPanel();
        namaKendaraanLabel = new javax.swing.JLabel();
        namaKendaraanDropdown = new javax.swing.JComboBox<>();
        tanggalTransaksiPanel = new javax.swing.JPanel();
        tanggalTransaksiLabel = new javax.swing.JLabel();
        tanggalTransaksiInput = new com.toedter.calendar.JDateChooser();
        simpanTransaksiButton = new javax.swing.JButton();
        batalTransaksiButton = new javax.swing.JButton();
        pembelianScrollPane = new javax.swing.JScrollPane();
        tablePembelian = new javax.swing.JTable();

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        searchTransaksiInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchTransaksiInputPanel.setPreferredSize(new java.awt.Dimension(687, 65));

        searchTransaksiInputLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        searchTransaksiInputLabel.setText("Pencarian Transaksi  ");

        searchTransaksiInputTextField.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        searchTransaksiInputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTransaksiInputTextFieldActionPerformed(evt);
            }
        });
        searchTransaksiInputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTransaksiInputTextFieldKeyPressed(evt);
            }
        });

        searchTransaksiInputButton.setBackground(new java.awt.Color(27, 26, 85));
        searchTransaksiInputButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        searchTransaksiInputButton.setForeground(new java.awt.Color(255, 255, 255));
        searchTransaksiInputButton.setText("Cari");
        searchTransaksiInputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTransaksiInputButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchTransaksiInputPanelLayout = new javax.swing.GroupLayout(searchTransaksiInputPanel);
        searchTransaksiInputPanel.setLayout(searchTransaksiInputPanelLayout);
        searchTransaksiInputPanelLayout.setHorizontalGroup(
            searchTransaksiInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchTransaksiInputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchTransaksiInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchTransaksiInputLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(searchTransaksiInputPanelLayout.createSequentialGroup()
                        .addComponent(searchTransaksiInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchTransaksiInputButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        searchTransaksiInputPanelLayout.setVerticalGroup(
            searchTransaksiInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchTransaksiInputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchTransaksiInputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchTransaksiInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchTransaksiInputTextField)
                    .addComponent(searchTransaksiInputButton, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        transaksiFormPanel.setBackground(new java.awt.Color(255, 255, 255));

        transaksiButtonPanel.setBackground(new java.awt.Color(255, 255, 255));

        barukanTransaksiButton.setBackground(new java.awt.Color(255, 189, 3));
        barukanTransaksiButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        barukanTransaksiButton.setForeground(new java.awt.Color(255, 255, 255));
        barukanTransaksiButton.setText("Barukan");
        barukanTransaksiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barukanTransaksiButtonActionPerformed(evt);
            }
        });

        hapusTransaksiButton.setBackground(new java.awt.Color(237, 8, 0));
        hapusTransaksiButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        hapusTransaksiButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusTransaksiButton.setText("Hapus");
        hapusTransaksiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusTransaksiButtonActionPerformed(evt);
            }
        });

        tambahTransaksiButton.setBackground(new java.awt.Color(51, 178, 73));
        tambahTransaksiButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        tambahTransaksiButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahTransaksiButton.setText("Tambah");
        tambahTransaksiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahTransaksiButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout transaksiButtonPanelLayout = new javax.swing.GroupLayout(transaksiButtonPanel);
        transaksiButtonPanel.setLayout(transaksiButtonPanelLayout);
        transaksiButtonPanelLayout.setHorizontalGroup(
            transaksiButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaksiButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahTransaksiButton)
                .addGap(18, 18, 18)
                .addComponent(barukanTransaksiButton)
                .addGap(18, 18, 18)
                .addComponent(hapusTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        transaksiButtonPanelLayout.setVerticalGroup(
            transaksiButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaksiButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transaksiButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(barukanTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahTransaksiButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hapusTransaksiButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namaCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));
        namaCustomerPanel.setPreferredSize(new java.awt.Dimension(322, 60));

        namaCustomerLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaCustomerLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        namaCustomerLabel.setText("Nama Customer ");

        namaCustomerDropdown.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaCustomerDropdown.setPreferredSize(new java.awt.Dimension(72, 21));

        javax.swing.GroupLayout namaCustomerPanelLayout = new javax.swing.GroupLayout(namaCustomerPanel);
        namaCustomerPanel.setLayout(namaCustomerPanelLayout);
        namaCustomerPanelLayout.setHorizontalGroup(
            namaCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaCustomerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namaCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(namaCustomerPanelLayout.createSequentialGroup()
                        .addComponent(namaCustomerLabel)
                        .addGap(0, 215, Short.MAX_VALUE))
                    .addComponent(namaCustomerDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        namaCustomerPanelLayout.setVerticalGroup(
            namaCustomerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaCustomerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaCustomerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaCustomerDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        namaGaransiPanel.setBackground(new java.awt.Color(255, 255, 255));
        namaGaransiPanel.setPreferredSize(new java.awt.Dimension(322, 60));

        namaGaransiLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaGaransiLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        namaGaransiLabel.setText("Nama Garansi");

        namaGaransiDropdown.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaGaransiDropdown.setPreferredSize(new java.awt.Dimension(72, 21));
        namaGaransiDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaGaransiDropdownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout namaGaransiPanelLayout = new javax.swing.GroupLayout(namaGaransiPanel);
        namaGaransiPanel.setLayout(namaGaransiPanelLayout);
        namaGaransiPanelLayout.setHorizontalGroup(
            namaGaransiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaGaransiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namaGaransiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(namaGaransiPanelLayout.createSequentialGroup()
                        .addComponent(namaGaransiLabel)
                        .addGap(0, 229, Short.MAX_VALUE))
                    .addComponent(namaGaransiDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        namaGaransiPanelLayout.setVerticalGroup(
            namaGaransiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaGaransiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaGaransiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaGaransiDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jumlahPembelianPanel.setBackground(new java.awt.Color(255, 255, 255));
        jumlahPembelianPanel.setPreferredSize(new java.awt.Dimension(322, 60));

        jumlahLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        jumlahLabel.setText("Jumlah Pembelian ");

        jumlahPembelianInput.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        jumlahPembelianInput.setPreferredSize(new java.awt.Dimension(72, 21));
        jumlahPembelianInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahPembelianInputActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jumlahPembelianPanelLayout = new javax.swing.GroupLayout(jumlahPembelianPanel);
        jumlahPembelianPanel.setLayout(jumlahPembelianPanelLayout);
        jumlahPembelianPanelLayout.setHorizontalGroup(
            jumlahPembelianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jumlahPembelianPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jumlahPembelianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jumlahPembelianPanelLayout.createSequentialGroup()
                        .addComponent(jumlahLabel)
                        .addGap(0, 203, Short.MAX_VALUE))
                    .addComponent(jumlahPembelianInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jumlahPembelianPanelLayout.setVerticalGroup(
            jumlahPembelianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jumlahPembelianPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jumlahLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jumlahPembelianInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        checkboxPanel.setBackground(new java.awt.Color(255, 255, 255));
        checkboxPanel.setPreferredSize(new java.awt.Dimension(180, 190));

        atributLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        atributLabel.setText("Atribut Tambahan Kendaraan ");

        kursiCheckbox.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        kursiCheckbox.setText("Kursi Kulit ");

        sistemNavigasiCheckbox.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        sistemNavigasiCheckbox.setText(" Sistem Navigasi GPS");

        javax.swing.GroupLayout checkboxPanelLayout = new javax.swing.GroupLayout(checkboxPanel);
        checkboxPanel.setLayout(checkboxPanelLayout);
        checkboxPanelLayout.setHorizontalGroup(
            checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkboxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(atributLabel)
                    .addComponent(kursiCheckbox)
                    .addComponent(sistemNavigasiCheckbox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        checkboxPanelLayout.setVerticalGroup(
            checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkboxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(atributLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kursiCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sistemNavigasiCheckbox)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        metodePembayaranPanel.setBackground(new java.awt.Color(255, 255, 255));
        metodePembayaranPanel.setPreferredSize(new java.awt.Dimension(500, 190));

        metodePembayaranLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        metodePembayaranLabel.setText("Metode Pembayaran ");

        metodePembayaranGroup.add(bcaRadio);
        bcaRadio.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        bcaRadio.setText("BCA");

        metodePembayaranGroup.add(briRadio);
        briRadio.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        briRadio.setText("BRI");

        metodePembayaranGroup.add(mayRadio);
        mayRadio.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        mayRadio.setText("May Bank ");

        metodePembayaranGroup.add(bpdRadio);
        bpdRadio.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        bpdRadio.setText("BPD DIY ");

        metodePembayaranGroup.add(tunaiRadio);
        tunaiRadio.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        tunaiRadio.setText("Tunai");

        javax.swing.GroupLayout metodePembayaranPanelLayout = new javax.swing.GroupLayout(metodePembayaranPanel);
        metodePembayaranPanel.setLayout(metodePembayaranPanelLayout);
        metodePembayaranPanelLayout.setHorizontalGroup(
            metodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(metodePembayaranPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(metodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(metodePembayaranLabel)
                    .addComponent(bcaRadio)
                    .addComponent(briRadio)
                    .addComponent(mayRadio)
                    .addComponent(bpdRadio)
                    .addComponent(tunaiRadio))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        metodePembayaranPanelLayout.setVerticalGroup(
            metodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(metodePembayaranPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(metodePembayaranLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bcaRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(briRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mayRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bpdRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tunaiRadio)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        namaCustomerServicePanel.setBackground(new java.awt.Color(255, 255, 255));
        namaCustomerServicePanel.setPreferredSize(new java.awt.Dimension(322, 60));

        namaCustomerServiceLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaCustomerServiceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        namaCustomerServiceLabel.setText("Nama Customer  Service");

        namaCustomerServiceDropdown.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaCustomerServiceDropdown.setPreferredSize(new java.awt.Dimension(72, 21));

        javax.swing.GroupLayout namaCustomerServicePanelLayout = new javax.swing.GroupLayout(namaCustomerServicePanel);
        namaCustomerServicePanel.setLayout(namaCustomerServicePanelLayout);
        namaCustomerServicePanelLayout.setHorizontalGroup(
            namaCustomerServicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaCustomerServicePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namaCustomerServicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(namaCustomerServicePanelLayout.createSequentialGroup()
                        .addComponent(namaCustomerServiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 149, Short.MAX_VALUE))
                    .addComponent(namaCustomerServiceDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        namaCustomerServicePanelLayout.setVerticalGroup(
            namaCustomerServicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaCustomerServicePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaCustomerServiceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaCustomerServiceDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        namaKendaraanPanel.setBackground(new java.awt.Color(255, 255, 255));
        namaKendaraanPanel.setPreferredSize(new java.awt.Dimension(322, 60));

        namaKendaraanLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaKendaraanLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        namaKendaraanLabel.setText("Nama Kendaraan");

        namaKendaraanDropdown.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        namaKendaraanDropdown.setPreferredSize(new java.awt.Dimension(72, 21));
        namaKendaraanDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaKendaraanDropdownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout namaKendaraanPanelLayout = new javax.swing.GroupLayout(namaKendaraanPanel);
        namaKendaraanPanel.setLayout(namaKendaraanPanelLayout);
        namaKendaraanPanelLayout.setHorizontalGroup(
            namaKendaraanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaKendaraanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namaKendaraanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(namaKendaraanPanelLayout.createSequentialGroup()
                        .addComponent(namaKendaraanLabel)
                        .addGap(0, 209, Short.MAX_VALUE))
                    .addComponent(namaKendaraanDropdown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        namaKendaraanPanelLayout.setVerticalGroup(
            namaKendaraanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaKendaraanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaKendaraanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaKendaraanDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tanggalTransaksiPanel.setBackground(new java.awt.Color(255, 255, 255));
        tanggalTransaksiPanel.setPreferredSize(new java.awt.Dimension(322, 60));

        tanggalTransaksiLabel.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        tanggalTransaksiLabel.setText("Tanggal Transaksi");

        tanggalTransaksiInput.setPreferredSize(new java.awt.Dimension(72, 21));

        javax.swing.GroupLayout tanggalTransaksiPanelLayout = new javax.swing.GroupLayout(tanggalTransaksiPanel);
        tanggalTransaksiPanel.setLayout(tanggalTransaksiPanelLayout);
        tanggalTransaksiPanelLayout.setHorizontalGroup(
            tanggalTransaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanggalTransaksiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tanggalTransaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tanggalTransaksiPanelLayout.createSequentialGroup()
                        .addComponent(tanggalTransaksiLabel)
                        .addGap(0, 208, Short.MAX_VALUE))
                    .addComponent(tanggalTransaksiInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        tanggalTransaksiPanelLayout.setVerticalGroup(
            tanggalTransaksiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanggalTransaksiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tanggalTransaksiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tanggalTransaksiInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout transaksiFormPanelLayout = new javax.swing.GroupLayout(transaksiFormPanel);
        transaksiFormPanel.setLayout(transaksiFormPanelLayout);
        transaksiFormPanelLayout.setHorizontalGroup(
            transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transaksiButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                        .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                                .addComponent(namaCustomerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaCustomerServicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                                .addComponent(jumlahPembelianPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tanggalTransaksiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                                .addComponent(namaKendaraanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaGaransiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkboxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(metodePembayaranPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        transaksiFormPanelLayout.setVerticalGroup(
            transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(metodePembayaranPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                        .addComponent(transaksiButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(transaksiFormPanelLayout.createSequentialGroup()
                                .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namaCustomerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(namaCustomerServicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(namaKendaraanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(namaGaransiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(transaksiFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jumlahPembelianPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tanggalTransaksiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6))
                            .addComponent(checkboxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        simpanTransaksiButton.setBackground(new java.awt.Color(51, 178, 73));
        simpanTransaksiButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        simpanTransaksiButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanTransaksiButton.setText("Simpan");
        simpanTransaksiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanTransaksiButtonActionPerformed(evt);
            }
        });

        batalTransaksiButton.setBackground(new java.awt.Color(237, 8, 0));
        batalTransaksiButton.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 12)); // NOI18N
        batalTransaksiButton.setForeground(new java.awt.Color(255, 255, 255));
        batalTransaksiButton.setText("Batalkan");
        batalTransaksiButton.setPreferredSize(new java.awt.Dimension(68, 27));
        batalTransaksiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalTransaksiButtonActionPerformed(evt);
            }
        });

        tablePembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tablePembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePembelianMouseClicked(evt);
            }
        });
        pembelianScrollPane.setViewportView(tablePembelian);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGap(0, 1131, Short.MAX_VALUE)
                                .addComponent(simpanTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(batalTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(transaksiFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(searchTransaksiInputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1363, Short.MAX_VALUE)))
                    .addComponent(pembelianScrollPane))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchTransaksiInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(transaksiFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpanTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batalTransaksiButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pembelianScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchTransaksiInputTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTransaksiInputTextFieldKeyPressed
        
    }//GEN-LAST:event_searchTransaksiInputTextFieldKeyPressed

    private void searchTransaksiInputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTransaksiInputButtonActionPerformed
        showTableBySearch(searchTransaksiInputTextField.getText());
        
        clearText();
        setComponent(false);
        setEditDeleteBtn(false);
        setRadioComponent(false);
        metodePembayaranGroup.clearSelection();
        tambahTransaksiButton.setEnabled(true);
    }//GEN-LAST:event_searchTransaksiInputButtonActionPerformed

    private void barukanTransaksiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barukanTransaksiButtonActionPerformed
        action = "baharui";
        setComponent(true);
        namaCustomerDropdown.setEnabled(false);
        setEditDeleteBtn(true);
        setRadioComponent(true);
    }//GEN-LAST:event_barukanTransaksiButtonActionPerformed

    private void hapusTransaksiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusTransaksiButtonActionPerformed
        action = "hapus";
        int opsi = JOptionPane.showConfirmDialog(rootPane, "Yakin Ingin Hapus ?", "Hapus Data", JOptionPane.YES_NO_OPTION);
        if(opsi == JOptionPane.NO_OPTION || opsi == JOptionPane.CLOSED_OPTION)
            return;
        
        pembelianControl.deletePembelianKendaraan(selectedId);
        
        clearText();
        setComponent(false);
        setEditDeleteBtn(false);
        setRadioComponent(false);
        metodePembayaranGroup.clearSelection();
        tambahTransaksiButton.setEnabled(true);
        showTableBySearch("");
    }//GEN-LAST:event_hapusTransaksiButtonActionPerformed

    private void tambahTransaksiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahTransaksiButtonActionPerformed
        action = "tambah";
        setComponent(true);
        setRadioComponent(true);
    }//GEN-LAST:event_tambahTransaksiButtonActionPerformed

    private void simpanTransaksiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanTransaksiButtonActionPerformed
         try{
            inputKosongPembelianException();
            
            String atribut = "";
            String metodePembayaran = "";
            String tanggalTransaksi = "";
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tanggalTransaksi = sdf.format(tanggalTransaksiInput.getDate());
            
            int selectedIndexCustomer = namaCustomerDropdown.getSelectedIndex();
            CustomerE selectedCustomer = listCustomer.get(selectedIndexCustomer);
            
            int selectedIndexKendaraan = namaKendaraanDropdown.getSelectedIndex();
            KendaraanE selectedKendaraan = listKendaraan.get(selectedIndexKendaraan);
            
            int selectedIndexCustomerService = namaCustomerServiceDropdown.getSelectedIndex();
            CustomerServiceE selectedCustomerService = listCustomerService.get(selectedIndexCustomerService);
            
            int selectedIndexGaransi = namaGaransiDropdown.getSelectedIndex();
            GaransiE selectedGaransi = listGaransi.get(selectedIndexGaransi);
            
            double totalHarga = Integer.parseInt(jumlahPembelianInput.getText()) * selectedKendaraan.getHarga();
            
            if (kursiCheckbox.isSelected() && sistemNavigasiCheckbox.isSelected()) {
                atribut = "Paket Lengkap";
            }else if(kursiCheckbox.isSelected()){
                atribut = "Tambahan kursi kulit";
            }else{
                atribut = "Tambahan Sistem navigasi";
            }
            
            metodePembayaran = metodePembayaranGroup.getSelection().getActionCommand();
            
            int confirm = JOptionPane.showConfirmDialog(rootPane, "Total pembayaran yang perlu Anda bayarkan adalah : Rp. " + totalHarga);

            if(confirm == JOptionPane.YES_OPTION){
                if (action.equals("tambah")) {
                    PembelianKendaraan pk = new PembelianKendaraan(
                        selectedCustomer.getId_customer(),
                        selectedCustomerService.getId_cs(),
                        Integer.parseInt(jumlahPembelianInput.getText()),
                        selectedKendaraan.getId_kendaraan(),
                        selectedGaransi.getId_garansi(),
                        atribut,
                        metodePembayaran,
                        tanggalTransaksi,
                        selectedCustomer,
                        selectedKendaraan,
                        selectedCustomerService,
                        selectedGaransi
                    );
                    pembelianControl.insertDataPembelianKendaraan(pk);
                } else {
                    PembelianKendaraan pk = new PembelianKendaraan(
                        selectedCustomer.getId_customer(),
                        selectedCustomerService.getId_cs(),
                        Integer.parseInt(jumlahPembelianInput.getText()),
                        selectedKendaraan.getId_kendaraan(),
                        selectedGaransi.getId_garansi(),
                        atribut,
                        metodePembayaran,
                        tanggalTransaksi,
                        selectedCustomer,
                        selectedKendaraan,
                        selectedCustomerService,
                        selectedGaransi
                    );

                    pembelianControl.updatePembelianKendaraan(pk, selectedId);
                    selectedId = -1;
                }
            }
        }catch (InputKosongException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
        clearText();
        setComponent(false);
        setEditDeleteBtn(false);
        setRadioComponent(false);
        metodePembayaranGroup.clearSelection();
        tambahTransaksiButton.setEnabled(true);
        showTableBySearch("");
    }//GEN-LAST:event_simpanTransaksiButtonActionPerformed

    private void searchTransaksiInputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTransaksiInputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTransaksiInputTextFieldActionPerformed

    private void jumlahPembelianInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahPembelianInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahPembelianInputActionPerformed

    private void batalTransaksiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalTransaksiButtonActionPerformed
        // TODO add your handling code here:
        clearText();
        setEditDeleteBtn(false);
        setComponent(false);
        setRadioComponent(false);
        
        tambahTransaksiButton.setEnabled(true);
    }//GEN-LAST:event_batalTransaksiButtonActionPerformed

    private void tablePembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePembelianMouseClicked
        // TODO add your handling code here:
        int indexCustomer = -1, indexKendaraan = -1, indexCustomerService = -1, indexGaransi = -1;
        action = "baharui";

        tambahTransaksiButton.setEnabled(false);
        batalTransaksiButton.setEnabled(true);
        simpanTransaksiButton.setEnabled(true);
        setEditDeleteBtn(true);

        setComponent(false);
        setRadioComponent(false);
        metodePembayaranGroup.clearSelection();

        int clickedRow = tablePembelian.getSelectedRow();
        TableModel tableModel = tablePembelian.getModel();

        selectedId = Integer.parseInt(tableModel.getValueAt(clickedRow, 0).toString());
        jumlahPembelianInput.setText(tableModel.getValueAt(clickedRow, 5).toString());

        String metodePembayaran = tableModel.getValueAt(clickedRow, 7).toString();
        String atribut = tableModel.getValueAt(clickedRow, 6).toString();
        
        String tanggalStr = tableModel.getValueAt(clickedRow, 8).toString();
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date tanggal = sdf.parse(tanggalStr);
            tanggalTransaksiInput.setDate(tanggal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (metodePembayaran) {
            case "BCA":
                bcaRadio.setSelected(true);
                break;
            case "BRI":
                briRadio.setSelected(true);
                break;
            case "May Bank":
                mayRadio.setSelected(true);
                break;
            case "BPD DIY":
                bpdRadio.setSelected(true);
                break;
            case "Tunai":
                tunaiRadio.setSelected(true);
                break;
        }

        kursiCheckbox.setSelected(false);
        sistemNavigasiCheckbox.setSelected(false);

        switch (atribut) {
            case "Paket Lengkap":
                kursiCheckbox.setSelected(true);
                sistemNavigasiCheckbox.setSelected(true);
                break;
            case "Tambahan Sistem navigasi":
                sistemNavigasiCheckbox.setSelected(true);
                break;
            case "Tambahan kursi kulit":
                kursiCheckbox.setSelected(true);
                break;
        }

        for (CustomerE customer : listCustomer) {
            if (customer.getNama().equals(tableModel.getValueAt(clickedRow, 1).toString())) {
                indexCustomer = listCustomer.indexOf(customer);
            }
        }

        for (KendaraanE kendaraan : listKendaraan) {
            if (kendaraan.getNama().equals(tableModel.getValueAt(clickedRow, 2).toString())) {
                indexKendaraan = listKendaraan.indexOf(kendaraan);
            }
        }
        
        for (CustomerServiceE cs : listCustomerService) {
            if (cs.getNama_cs().equals(tableModel.getValueAt(clickedRow, 3).toString())) {
                indexCustomerService = listCustomerService.indexOf(cs);
            }
        }

        for (GaransiE garansi : listGaransi) {
            if (garansi.getNama_garansi().equals(tableModel.getValueAt(clickedRow, 4).toString())) {
                indexGaransi = listGaransi.indexOf(garansi);
            }
        }

        namaCustomerDropdown.setSelectedIndex(indexCustomer);
        namaKendaraanDropdown.setSelectedIndex(indexKendaraan);
        namaCustomerServiceDropdown.setSelectedIndex(indexCustomerService);
        namaGaransiDropdown.setSelectedIndex(indexGaransi);

        batalTransaksiButton.setEnabled(true);
    }//GEN-LAST:event_tablePembelianMouseClicked

    private void namaGaransiDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaGaransiDropdownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaGaransiDropdownActionPerformed

    private void namaKendaraanDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaKendaraanDropdownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaKendaraanDropdownActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel atributLabel;
    private javax.swing.JButton barukanTransaksiButton;
    private javax.swing.JButton batalTransaksiButton;
    private javax.swing.JRadioButton bcaRadio;
    private javax.swing.JRadioButton bpdRadio;
    private javax.swing.JRadioButton briRadio;
    private javax.swing.JPanel checkboxPanel;
    private javax.swing.JButton hapusTransaksiButton;
    private javax.swing.JLabel jumlahLabel;
    private javax.swing.JTextField jumlahPembelianInput;
    private javax.swing.JPanel jumlahPembelianPanel;
    private javax.swing.JCheckBox kursiCheckbox;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JRadioButton mayRadio;
    private javax.swing.ButtonGroup metodePembayaranGroup;
    private javax.swing.JLabel metodePembayaranLabel;
    private javax.swing.JPanel metodePembayaranPanel;
    private javax.swing.JComboBox<CustomerE> namaCustomerDropdown;
    private javax.swing.JLabel namaCustomerLabel;
    private javax.swing.JPanel namaCustomerPanel;
    private javax.swing.JComboBox<CustomerServiceE> namaCustomerServiceDropdown;
    private javax.swing.JLabel namaCustomerServiceLabel;
    private javax.swing.JPanel namaCustomerServicePanel;
    private javax.swing.JComboBox<GaransiE> namaGaransiDropdown;
    private javax.swing.JLabel namaGaransiLabel;
    private javax.swing.JPanel namaGaransiPanel;
    private javax.swing.JComboBox<KendaraanE> namaKendaraanDropdown;
    private javax.swing.JLabel namaKendaraanLabel;
    private javax.swing.JPanel namaKendaraanPanel;
    private javax.swing.JScrollPane pembelianScrollPane;
    private javax.swing.JButton searchTransaksiInputButton;
    private javax.swing.JLabel searchTransaksiInputLabel;
    private javax.swing.JPanel searchTransaksiInputPanel;
    private javax.swing.JTextField searchTransaksiInputTextField;
    private javax.swing.JButton simpanTransaksiButton;
    private javax.swing.JCheckBox sistemNavigasiCheckbox;
    private javax.swing.JTable tablePembelian;
    private javax.swing.JButton tambahTransaksiButton;
    private com.toedter.calendar.JDateChooser tanggalTransaksiInput;
    private javax.swing.JLabel tanggalTransaksiLabel;
    private javax.swing.JPanel tanggalTransaksiPanel;
    private javax.swing.JPanel transaksiButtonPanel;
    private javax.swing.JPanel transaksiFormPanel;
    private javax.swing.JRadioButton tunaiRadio;
    // End of variables declaration//GEN-END:variables
}
