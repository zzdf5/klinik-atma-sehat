/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import util.DialogUtil;

import control.TagihanControl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Tagihan;
import table.TableItemTagihan;
import table.TableTagihan;

public class ManajemenTagihanPanel extends javax.swing.JPanel {

    private final TagihanControl tc = new TagihanControl();
    private Tagihan selectedTagihan = null;

    public ManajemenTagihanPanel() {
        initComponents();
        setOpaque(false);
        setupTables();
        setupComboBox();
        setupListeners();
        loadTagihanTable(tc.showDataWithNames());
        setFormEnabled(false);

        inputIdTagihanTextField.setEnabled(false);
        inputIdKunjunganTextField.setEnabled(false);
        inputTanggalTagihanDateChooser.setEnabled(false);
    }


    private void setupTables() {
        TagihanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTagihanTable.setModel(new TableItemTagihan(new ArrayList<>()));
    }

    private void setupComboBox() {
        inputMetodePembayaranComboBox.setModel(new DefaultComboBoxModel<>(
            new String[]{"TUNAI", "BPJS", "TRANSFER", "DEBIT"}
        ));
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(lunasRadioButton);
        statusGroup.add(belumLunasRadioButton);
        belumLunasRadioButton.setSelected(true);
    }

    private void setupListeners() {
        inputJumlahBayarTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                hitungKembalian();
            }
            public void removeUpdate(DocumentEvent e) {
                hitungKembalian();
            }
            public void changedUpdate(DocumentEvent e) {
                hitungKembalian();
            }
        });

        lunasRadioButton.addActionListener(e -> updatePembayaranEnabled());
        belumLunasRadioButton.addActionListener(e -> updatePembayaranEnabled());
    }

    private void doSearch() {
        String keyword = pencarianTagihanTextField.getText().trim();
        if (keyword.isEmpty()) {
            loadTagihanTable(tc.showDataWithNames());
        } else {
            loadTagihanTable(tc.searchByKeyword(keyword));
        }
        clearForm();
        setFormEnabled(false);
    }

    private void setFormEnabled(boolean value) {
        lunasRadioButton.setEnabled(value);
        belumLunasRadioButton.setEnabled(value);
        kembalianTextField.setEnabled(false);
        simpanButton.setEnabled(value);
        // Jumlah bayar & metode hanya aktif saat status LUNAS dipilih.
        updatePembayaranEnabled();
    }

    private void updatePembayaranEnabled() {
        boolean aktif = lunasRadioButton.isEnabled() && lunasRadioButton.isSelected();
        inputMetodePembayaranComboBox.setEnabled(aktif);
        inputJumlahBayarTextField.setEnabled(aktif);
        if (!aktif) {
            inputJumlahBayarTextField.setText("");
            kembalianTextField.setText("");
        }
    }

    private void clearForm() {
        inputIdTagihanTextField.setText("");
        inputIdKunjunganTextField.setText("");
        inputTanggalTagihanDateChooser.setDate(null);
        totalHargaTagihanLabel.setText("Rp0");
        inputJumlahBayarTextField.setText("");
        kembalianTextField.setText("");
        inputMetodePembayaranComboBox.setSelectedIndex(0);
        belumLunasRadioButton.setSelected(true);
        selectedTagihan = null;
        itemTagihanTable.setModel(new TableItemTagihan(new ArrayList<>()));
    }

    private void fillForm(Tagihan tagihan) {
        inputIdTagihanTextField.setText(tagihan.getIdTagihan());
        inputIdKunjunganTextField.setText(tagihan.getIdKunjungan());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            inputTanggalTagihanDateChooser.setDate(sdf.parse(tagihan.getTanggalTagihan()));
        } catch (Exception ex) {
            inputTanggalTagihanDateChooser.setDate(null);
        }
        
        totalHargaTagihanLabel.setText(String.format("Rp%.0f", tagihan.getTotalTagihan()));
        if (tagihan.getJumlahBayar() > 0) {
            inputJumlahBayarTextField.setText(String.format("%.0f", tagihan.getJumlahBayar()));
        } else {
            inputJumlahBayarTextField.setText("");
        }
        
        if (tagihan.getKembalian() > 0) {
            kembalianTextField.setText(String.format("Rp%.0f", tagihan.getKembalian()));
        } else {
            kembalianTextField.setText("");
        }
        
        if (tagihan.getMetodePembayaran() != null) {
            inputMetodePembayaranComboBox.setSelectedItem(tagihan.getMetodePembayaran().name());
        } else {
            inputMetodePembayaranComboBox.setSelectedIndex(0);
        }
        
        if (tagihan.getStatus() == Tagihan.Status.LUNAS) {
            lunasRadioButton.setSelected(true);
        } else {
            belumLunasRadioButton.setSelected(true);
        }
    }

    private void loadTagihanTable(List<Object[]> data) {
        TagihanTable.setModel(new TableTagihan(data));
    }

    private void loadItemTable(Tagihan tagihan) {
        itemTagihanTable.setModel(new TableItemTagihan(tagihan.getDaftarItem()));
    }

    private void hitungKembalian() {
        try {
            String text = inputJumlahBayarTextField.getText().trim();
            if (text.isEmpty()) { 
                kembalianTextField.setText(""); 
                return; 
            }
            double jumlahBayar = Double.parseDouble(text);
            String totalText = totalHargaTagihanLabel.getText().replace("Rp", "").replace(",", "");
            double total = Double.parseDouble(totalText);
            kembalianTextField.setText(String.format("Rp%.0f", jumlahBayar - total));
        } catch (NumberFormatException ex) {
            kembalianTextField.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        formInputTagihanPanel = new javax.swing.JPanel();
        inputIdTagihanPanel = new javax.swing.JPanel();
        inputIdTagihanLabel = new javax.swing.JLabel();
        inputIdTagihanTextField = new javax.swing.JTextField();
        inputIdKunjunganPanel = new javax.swing.JPanel();
        inputIdKunjunganLabel = new javax.swing.JLabel();
        inputIdKunjunganTextField = new javax.swing.JTextField();
        inputTanggalTagihanPanel = new javax.swing.JPanel();
        inputTanggalTagihanLabel = new javax.swing.JLabel();
        inputTanggalTagihanDateChooser = new com.toedter.calendar.JDateChooser();
        itemTagihanPanel = new javax.swing.JPanel();
        itemTagihanLabel = new javax.swing.JLabel();
        itemTagihanScrollPane = new javax.swing.JScrollPane();
        itemTagihanTable = new javax.swing.JTable();
        inputDataTagihanLabel = new javax.swing.JLabel();
        formRingkasanTagihanPanel = new javax.swing.JPanel();
        formInputRingkasanTagihanPanel = new javax.swing.JPanel();
        totalTagihanLabel = new javax.swing.JLabel();
        totalHargaTagihanLabel = new javax.swing.JLabel();
        inputMetodePembayaranPanel = new javax.swing.JPanel();
        inputMetodePembayaranLabel = new javax.swing.JLabel();
        inputMetodePembayaranComboBox = new javax.swing.JComboBox<>();
        inputJumlahBayarPanel = new javax.swing.JPanel();
        inputJumlahBayarLabel = new javax.swing.JLabel();
        inputJumlahBayarTextField = new javax.swing.JTextField();
        inputStatusBayarPanel = new javax.swing.JPanel();
        inputStatusBayarLabel = new javax.swing.JLabel();
        lunasRadioButton = new javax.swing.JRadioButton();
        belumLunasRadioButton = new javax.swing.JRadioButton();
        kembalianPanel = new javax.swing.JPanel();
        kembalianLabel = new javax.swing.JLabel();
        kembalianTextField = new javax.swing.JTextField();
        simpanButton = new javax.swing.JButton();
        inputRingkasanTagihanLabel = new javax.swing.JLabel();
        TagihanScrollPane = new javax.swing.JScrollPane();
        TagihanTable = new javax.swing.JTable();
        pencarianTagihanPanel = new javax.swing.JPanel();
        judulTagihanLabel = new javax.swing.JLabel();
        subJudulTagihanLabel = new javax.swing.JLabel();
        pencarianTagihanTextField = new javax.swing.JTextField();
        pencarianTagihanButton = new javax.swing.JButton();
        pencarianTagihanLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));
        mainPanel.setPreferredSize(new java.awt.Dimension(1155, 799));

        formInputTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/invoiceId.png"))); // NOI18N
        inputIdTagihanLabel.setText("ID Tagihan");

        javax.swing.GroupLayout inputIdTagihanPanelLayout = new javax.swing.GroupLayout(inputIdTagihanPanel);
        inputIdTagihanPanel.setLayout(inputIdTagihanPanelLayout);
        inputIdTagihanPanelLayout.setHorizontalGroup(
            inputIdTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputIdTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputIdTagihanPanelLayout.createSequentialGroup()
                        .addComponent(inputIdTagihanLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(inputIdTagihanTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        inputIdTagihanPanelLayout.setVerticalGroup(
            inputIdTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdTagihanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputIdKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/doctor-consultation.png"))); // NOI18N
        inputIdKunjunganLabel.setText("ID Kunjungan");

        javax.swing.GroupLayout inputIdKunjunganPanelLayout = new javax.swing.GroupLayout(inputIdKunjunganPanel);
        inputIdKunjunganPanel.setLayout(inputIdKunjunganPanelLayout);
        inputIdKunjunganPanelLayout.setHorizontalGroup(
            inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(inputIdKunjunganLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(inputIdKunjunganTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        inputIdKunjunganPanelLayout.setVerticalGroup(
            inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdKunjunganTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputTanggalTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputTanggalTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rujukan.png"))); // NOI18N
        inputTanggalTagihanLabel.setText("Tanggal Tagihan");

        javax.swing.GroupLayout inputTanggalTagihanPanelLayout = new javax.swing.GroupLayout(inputTanggalTagihanPanel);
        inputTanggalTagihanPanel.setLayout(inputTanggalTagihanPanelLayout);
        inputTanggalTagihanPanelLayout.setHorizontalGroup(
            inputTanggalTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputTanggalTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                        .addComponent(inputTanggalTagihanLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(inputTanggalTagihanDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)))
        );
        inputTanggalTagihanPanelLayout.setVerticalGroup(
            inputTanggalTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputTanggalTagihanLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTanggalTagihanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        itemTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        itemTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        itemTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ListItem.png"))); // NOI18N
        itemTagihanLabel.setText("Item Tagihan");

        itemTagihanTable.setModel(new javax.swing.table.DefaultTableModel(
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
        itemTagihanScrollPane.setViewportView(itemTagihanTable);

        javax.swing.GroupLayout itemTagihanPanelLayout = new javax.swing.GroupLayout(itemTagihanPanel);
        itemTagihanPanel.setLayout(itemTagihanPanelLayout);
        itemTagihanPanelLayout.setHorizontalGroup(
            itemTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemTagihanScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 857, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemTagihanLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        itemTagihanPanelLayout.setVerticalGroup(
            itemTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTagihanScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        inputDataTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inputDataTagihanLabel.setText("Data Tagihan");

        javax.swing.GroupLayout formInputTagihanPanelLayout = new javax.swing.GroupLayout(formInputTagihanPanel);
        formInputTagihanPanel.setLayout(formInputTagihanPanelLayout);
        formInputTagihanPanelLayout.setHorizontalGroup(
            formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                        .addGroup(formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputIdTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputTanggalTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(inputDataTagihanLabel))
                .addGap(0, 0, 0))
        );
        formInputTagihanPanelLayout.setVerticalGroup(
            formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputDataTagihanLabel)
                .addGap(22, 22, 22)
                .addGroup(formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                        .addComponent(inputIdTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputTanggalTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(itemTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        formRingkasanTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        formInputRingkasanTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        totalTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        totalTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/online-payment.png"))); // NOI18N
        totalTagihanLabel.setText("Total Tagihan");

        totalHargaTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        totalHargaTagihanLabel.setText("Rp0");

        inputMetodePembayaranPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputMetodePembayaranLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputMetodePembayaranLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/wallet.png"))); // NOI18N
        inputMetodePembayaranLabel.setText("Metode Pembayaran");

        inputMetodePembayaranComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout inputMetodePembayaranPanelLayout = new javax.swing.GroupLayout(inputMetodePembayaranPanel);
        inputMetodePembayaranPanel.setLayout(inputMetodePembayaranPanelLayout);
        inputMetodePembayaranPanelLayout.setHorizontalGroup(
            inputMetodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputMetodePembayaranPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputMetodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputMetodePembayaranPanelLayout.createSequentialGroup()
                        .addComponent(inputMetodePembayaranLabel)
                        .addContainerGap(108, Short.MAX_VALUE))
                    .addComponent(inputMetodePembayaranComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        inputMetodePembayaranPanelLayout.setVerticalGroup(
            inputMetodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputMetodePembayaranPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputMetodePembayaranLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputMetodePembayaranComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputJumlahBayarPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputJumlahBayarLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputJumlahBayarLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/credit-card.png"))); // NOI18N
        inputJumlahBayarLabel.setText("Jumlah Bayar");

        javax.swing.GroupLayout inputJumlahBayarPanelLayout = new javax.swing.GroupLayout(inputJumlahBayarPanel);
        inputJumlahBayarPanel.setLayout(inputJumlahBayarPanelLayout);
        inputJumlahBayarPanelLayout.setHorizontalGroup(
            inputJumlahBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJumlahBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputJumlahBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputJumlahBayarPanelLayout.createSequentialGroup()
                        .addComponent(inputJumlahBayarLabel)
                        .addGap(0, 151, Short.MAX_VALUE))
                    .addComponent(inputJumlahBayarTextField))
                .addContainerGap())
        );
        inputJumlahBayarPanelLayout.setVerticalGroup(
            inputJumlahBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJumlahBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputJumlahBayarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputJumlahBayarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputStatusBayarPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputStatusBayarLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputStatusBayarLabel.setText("Status Bayar");

        lunasRadioButton.setText("LUNAS");

        belumLunasRadioButton.setText("BELUM LUNAS");

        javax.swing.GroupLayout inputStatusBayarPanelLayout = new javax.swing.GroupLayout(inputStatusBayarPanel);
        inputStatusBayarPanel.setLayout(inputStatusBayarPanelLayout);
        inputStatusBayarPanelLayout.setHorizontalGroup(
            inputStatusBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStatusBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputStatusBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputStatusBayarLabel)
                    .addComponent(lunasRadioButton)
                    .addComponent(belumLunasRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inputStatusBayarPanelLayout.setVerticalGroup(
            inputStatusBayarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStatusBayarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputStatusBayarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lunasRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(belumLunasRadioButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        kembalianPanel.setBackground(new java.awt.Color(255, 255, 255));

        kembalianLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        kembalianLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/return-on-investment.png"))); // NOI18N
        kembalianLabel.setText("Kembalian");

        javax.swing.GroupLayout kembalianPanelLayout = new javax.swing.GroupLayout(kembalianPanel);
        kembalianPanel.setLayout(kembalianPanelLayout);
        kembalianPanelLayout.setHorizontalGroup(
            kembalianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kembalianPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kembalianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kembalianPanelLayout.createSequentialGroup()
                        .addComponent(kembalianLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(kembalianTextField))
                .addContainerGap())
        );
        kembalianPanelLayout.setVerticalGroup(
            kembalianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kembalianPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kembalianLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kembalianTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        simpanButton.setBackground(new java.awt.Color(51, 178, 73));
        simpanButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        simpanButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanButton.setText("Simpan");
        simpanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formInputRingkasanTagihanPanelLayout = new javax.swing.GroupLayout(formInputRingkasanTagihanPanel);
        formInputRingkasanTagihanPanel.setLayout(formInputRingkasanTagihanPanelLayout);
        formInputRingkasanTagihanPanelLayout.setHorizontalGroup(
            formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputMetodePembayaranPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputStatusBayarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kembalianPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputJumlahBayarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalHargaTagihanLabel)
                            .addComponent(totalTagihanLabel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        formInputRingkasanTagihanPanelLayout.setVerticalGroup(
            formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addComponent(totalTagihanLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalHargaTagihanLabel))
                    .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(inputJumlahBayarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputMetodePembayaranPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputStatusBayarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(kembalianPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        inputRingkasanTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inputRingkasanTagihanLabel.setText("Ringkasan Tagihan");

        javax.swing.GroupLayout formRingkasanTagihanPanelLayout = new javax.swing.GroupLayout(formRingkasanTagihanPanel);
        formRingkasanTagihanPanel.setLayout(formRingkasanTagihanPanelLayout);
        formRingkasanTagihanPanelLayout.setHorizontalGroup(
            formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formInputRingkasanTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addComponent(inputRingkasanTagihanLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        formRingkasanTagihanPanelLayout.setVerticalGroup(
            formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputRingkasanTagihanLabel)
                .addGap(1, 1, 1)
                .addComponent(formInputRingkasanTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addContainerGap())
        );

        TagihanTable.setModel(new javax.swing.table.DefaultTableModel(
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
        TagihanTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TagihanTableMouseClicked(evt);
            }
        });
        TagihanScrollPane.setViewportView(TagihanTable);

        pencarianTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));
        pencarianTagihanPanel.setPreferredSize(new java.awt.Dimension(778, 74));

        judulTagihanLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        judulTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/invoiceUtama.png"))); // NOI18N
        judulTagihanLabel.setText("Data Master Manajemen Tagihan");

        subJudulTagihanLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        subJudulTagihanLabel.setText("Tagihan");

        pencarianTagihanTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pencarianTagihanTextFieldKeyPressed(evt);
            }
        });

        pencarianTagihanButton.setBackground(new java.awt.Color(0, 0, 153));
        pencarianTagihanButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        pencarianTagihanButton.setForeground(new java.awt.Color(255, 255, 255));
        pencarianTagihanButton.setText("Cari");
        pencarianTagihanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencarianTagihanButtonActionPerformed(evt);
            }
        });

        pencarianTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        pencarianTagihanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        pencarianTagihanLabel.setText("Pencarian Tagihan");

        javax.swing.GroupLayout pencarianTagihanPanelLayout = new javax.swing.GroupLayout(pencarianTagihanPanel);
        pencarianTagihanPanel.setLayout(pencarianTagihanPanelLayout);
        pencarianTagihanPanelLayout.setHorizontalGroup(
            pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subJudulTagihanLabel)
                    .addComponent(judulTagihanLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 372, Short.MAX_VALUE)
                .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                        .addComponent(pencarianTagihanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pencarianTagihanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pencarianTagihanLabel))
                .addGap(20, 20, 20))
        );
        pencarianTagihanPanelLayout.setVerticalGroup(
            pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                        .addComponent(judulTagihanLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subJudulTagihanLabel))
                    .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                        .addComponent(pencarianTagihanLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pencarianTagihanButton)
                            .addComponent(pencarianTagihanTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pencarianTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1143, Short.MAX_VALUE)
                    .addComponent(formInputTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(formRingkasanTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TagihanScrollPane))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formInputTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formRingkasanTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TagihanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TagihanTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TagihanTableMouseClicked
        int row = TagihanTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        String id = (String) TagihanTable.getValueAt(row, 0);
        selectedTagihan = tc.search(id);
        if (selectedTagihan != null) {
            fillForm(selectedTagihan);
            loadItemTable(selectedTagihan);
            setFormEnabled(true);
            if (selectedTagihan.getStatus() == Tagihan.Status.LUNAS) {
                lunasRadioButton.setEnabled(false);
                belumLunasRadioButton.setEnabled(false);
                inputMetodePembayaranComboBox.setEnabled(false);
                inputJumlahBayarTextField.setEnabled(false);
                simpanButton.setEnabled(false);
            }
        }
    }//GEN-LAST:event_TagihanTableMouseClicked

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        if (selectedTagihan == null) {
            DialogUtil.showMessageDialog(this, "Pilih tagihan terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (lunasRadioButton.isSelected()) {
                String jumlahText = inputJumlahBayarTextField.getText().trim();
                if (jumlahText.isEmpty()) {
                    DialogUtil.showMessageDialog(this, "Masukkan jumlah bayar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double jumlahBayar = Double.parseDouble(jumlahText);
                if (jumlahBayar < selectedTagihan.getTotalTagihan()) {
                    DialogUtil.showMessageDialog(this, "Jumlah bayar Rp" + String.format("%.0f", jumlahBayar) +
                        " kurang dari total tagihan Rp" + String.format("%.0f", selectedTagihan.getTotalTagihan()) + ".",
                        "Pembayaran Tidak Cukup", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Tagihan.MetodePembayaran metode = Tagihan.MetodePembayaran.valueOf((String) inputMetodePembayaranComboBox.getSelectedItem());
                tc.bayar(selectedTagihan, jumlahBayar, metode);
                DialogUtil.showMessageDialog(this, "Pembayaran berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                selectedTagihan.setStatus(Tagihan.Status.BELUM_BAYAR);
                tc.update(selectedTagihan, selectedTagihan.getIdTagihan());
                DialogUtil.showMessageDialog(this, "Status tagihan diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
            loadTagihanTable(tc.showDataWithNames());
            clearForm();
            setFormEnabled(false);
        } catch (NumberFormatException ex) {
            DialogUtil.showMessageDialog(this, "Format jumlah bayar tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_simpanButtonActionPerformed

    private void pencarianTagihanTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pencarianTagihanTextFieldKeyPressed
        if(evt.getKeyChar() == '\n') {
            doSearch();
        }
    }//GEN-LAST:event_pencarianTagihanTextFieldKeyPressed

    private void pencarianTagihanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencarianTagihanButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_pencarianTagihanButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane TagihanScrollPane;
    private javax.swing.JTable TagihanTable;
    private javax.swing.JRadioButton belumLunasRadioButton;
    private javax.swing.JPanel formInputRingkasanTagihanPanel;
    private javax.swing.JPanel formInputTagihanPanel;
    private javax.swing.JPanel formRingkasanTagihanPanel;
    private javax.swing.JLabel inputDataTagihanLabel;
    private javax.swing.JLabel inputIdKunjunganLabel;
    private javax.swing.JPanel inputIdKunjunganPanel;
    private javax.swing.JTextField inputIdKunjunganTextField;
    private javax.swing.JLabel inputIdTagihanLabel;
    private javax.swing.JPanel inputIdTagihanPanel;
    private javax.swing.JTextField inputIdTagihanTextField;
    private javax.swing.JLabel inputJumlahBayarLabel;
    private javax.swing.JPanel inputJumlahBayarPanel;
    private javax.swing.JTextField inputJumlahBayarTextField;
    private javax.swing.JComboBox<String> inputMetodePembayaranComboBox;
    private javax.swing.JLabel inputMetodePembayaranLabel;
    private javax.swing.JPanel inputMetodePembayaranPanel;
    private javax.swing.JLabel inputRingkasanTagihanLabel;
    private javax.swing.JLabel inputStatusBayarLabel;
    private javax.swing.JPanel inputStatusBayarPanel;
    private com.toedter.calendar.JDateChooser inputTanggalTagihanDateChooser;
    private javax.swing.JLabel inputTanggalTagihanLabel;
    private javax.swing.JPanel inputTanggalTagihanPanel;
    private javax.swing.JLabel itemTagihanLabel;
    private javax.swing.JPanel itemTagihanPanel;
    private javax.swing.JScrollPane itemTagihanScrollPane;
    private javax.swing.JTable itemTagihanTable;
    private javax.swing.JLabel judulTagihanLabel;
    private javax.swing.JLabel kembalianLabel;
    private javax.swing.JPanel kembalianPanel;
    private javax.swing.JTextField kembalianTextField;
    private javax.swing.JRadioButton lunasRadioButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pencarianTagihanButton;
    private javax.swing.JLabel pencarianTagihanLabel;
    private javax.swing.JPanel pencarianTagihanPanel;
    private javax.swing.JTextField pencarianTagihanTextField;
    private javax.swing.JButton simpanButton;
    private javax.swing.JLabel subJudulTagihanLabel;
    private javax.swing.JLabel totalHargaTagihanLabel;
    private javax.swing.JLabel totalTagihanLabel;
    // End of variables declaration//GEN-END:variables
}
