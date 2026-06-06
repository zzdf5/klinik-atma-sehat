/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.TagihanControl;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.Tagihan;

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

        // Field ini selalu read-only — diisi otomatis dari data tagihan
        inputIdTagihanTextField.setEnabled(false);
        inputIdKunjunganTextField.setEnabled(false);
        inputTanggalTagihanDateChooser.setEnabled(false);
    }


    private void setupTables() {
        TagihanTable.setModel(new DefaultTableModel(
            new String[]{"ID Tagihan", "Nama Pasien", "ID Kunjungan", "Tanggal", "Total", "Metode", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
        TagihanTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        itemTagihanTable.setModel(new DefaultTableModel(
            new String[]{"Nama Item", "Jumlah", "Harga Satuan", "Subtotal"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
    }

    private void setupComboBox() {
        inputMetodePembayaranComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"TUNAI", "BPJS", "TRANSFER", "DEBIT"}
        ));
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(lunasRadioButton);
        statusGroup.add(belumLunasRadioButton);
        belumLunasRadioButton.setSelected(true);
    }

    private void setupListeners() {
        pencarianTagihanButton.addActionListener(e -> doSearch());
        pencarianTagihanTextField.addActionListener(e -> doSearch());

        TagihanTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = TagihanTable.getSelectedRow();
                if (row >= 0) {
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
                }
            }
        });

        inputJumlahBayarTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hitungKembalian(); }
            public void removeUpdate(DocumentEvent e) { hitungKembalian(); }
            public void changedUpdate(DocumentEvent e) { hitungKembalian(); }
        });

        lunasRadioButton.addActionListener(e -> {
            inputMetodePembayaranComboBox.setEnabled(true);
            inputJumlahBayarTextField.setEnabled(true);
        });
        belumLunasRadioButton.addActionListener(e -> {
            inputMetodePembayaranComboBox.setEnabled(false);
            inputJumlahBayarTextField.setEnabled(false);
            kembalianTextField.setText("");
        });
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
        boolean lunas = value && lunasRadioButton.isSelected();
        inputMetodePembayaranComboBox.setEnabled(lunas);
        inputJumlahBayarTextField.setEnabled(lunas);
        kembalianTextField.setEnabled(false);
        simpanButton.setEnabled(value);
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
        ((DefaultTableModel) itemTagihanTable.getModel()).setRowCount(0);
    }

    private void fillForm(Tagihan tagihan) {
        inputIdTagihanTextField.setText(tagihan.getIdTagihan());
        inputIdKunjunganTextField.setText(tagihan.getIdKunjungan());
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
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
        DefaultTableModel model = (DefaultTableModel) TagihanTable.getModel();
        model.setRowCount(0);
        for (Object[] row : data) {
            Object[] display = row.clone();
            display[4] = String.format("Rp%.0f", (Double) row[4]);
            model.addRow(display);
        }
    }

    private void loadItemTable(Tagihan tagihan) {
        DefaultTableModel model = (DefaultTableModel) itemTagihanTable.getModel();
        model.setRowCount(0);
        for (Tagihan.ItemTagihan item : tagihan.getDaftarItem()) {
            double subtotal = item.getJumlah() * item.getHargaSatuan();
            model.addRow(new Object[]{
                item.getNamaItem(),
                item.getJumlah(),
                String.format("Rp%.0f", item.getHargaSatuan()),
                String.format("Rp%.0f", subtotal)
            });
        }
    }

    private void hitungKembalian() {
        try {
            String text = inputJumlahBayarTextField.getText().trim();
            if (text.isEmpty()) { kembalianTextField.setText(""); return; }
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
        pencarianTagihanPanel = new javax.swing.JPanel();
        pencarianTagihanLabel = new javax.swing.JLabel();
        pencarianTagihanTextField = new javax.swing.JTextField();
        pencarianTagihanButton = new javax.swing.JButton();
        formInputTagihanPanel = new javax.swing.JPanel();
        formInputTagihanLabel = new javax.swing.JLabel();
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
        formRingkasanTagihanPanel = new javax.swing.JPanel();
        formRingkasanTagihanLabel = new javax.swing.JLabel();
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
        TagihanScrollPane = new javax.swing.JScrollPane();
        TagihanTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));

        pencarianTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));
        pencarianTagihanPanel.setPreferredSize(new java.awt.Dimension(800, 70));

        pencarianTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        pencarianTagihanLabel.setText("Pencarian Tagihan");

        pencarianTagihanButton.setText("Cari");

        javax.swing.GroupLayout pencarianTagihanPanelLayout = new javax.swing.GroupLayout(pencarianTagihanPanel);
        pencarianTagihanPanel.setLayout(pencarianTagihanPanelLayout);
        pencarianTagihanPanelLayout.setHorizontalGroup(
            pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pencarianTagihanLabel)
                    .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                        .addComponent(pencarianTagihanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pencarianTagihanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pencarianTagihanPanelLayout.setVerticalGroup(
            pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pencarianTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pencarianTagihanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pencarianTagihanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        formInputTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        formInputTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        formInputTagihanLabel.setText("Data Tagihan");

        inputIdTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdTagihanLabel.setText("ID Tagihan");

        javax.swing.GroupLayout inputIdTagihanPanelLayout = new javax.swing.GroupLayout(inputIdTagihanPanel);
        inputIdTagihanPanel.setLayout(inputIdTagihanPanelLayout);
        inputIdTagihanPanelLayout.setHorizontalGroup(
            inputIdTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdTagihanLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputIdTagihanTextField, javax.swing.GroupLayout.Alignment.TRAILING)
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
        inputIdKunjunganLabel.setText("ID Kunjungan");

        javax.swing.GroupLayout inputIdKunjunganPanelLayout = new javax.swing.GroupLayout(inputIdKunjunganPanel);
        inputIdKunjunganPanel.setLayout(inputIdKunjunganPanelLayout);
        inputIdKunjunganPanelLayout.setHorizontalGroup(
            inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdKunjunganLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputIdKunjunganTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        inputIdKunjunganPanelLayout.setVerticalGroup(
            inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(inputIdKunjunganTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputTanggalTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputTanggalTagihanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalTagihanLabel.setText("Tanggal Tagihan");

        javax.swing.GroupLayout inputTanggalTagihanPanelLayout = new javax.swing.GroupLayout(inputTanggalTagihanPanel);
        inputTanggalTagihanPanel.setLayout(inputTanggalTagihanPanelLayout);
        inputTanggalTagihanPanelLayout.setHorizontalGroup(
            inputTanggalTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalTagihanLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                .addComponent(inputTanggalTagihanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );
        inputTanggalTagihanPanelLayout.setVerticalGroup(
            inputTanggalTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(inputTanggalTagihanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        itemTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        itemTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
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
                .addComponent(itemTagihanLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(itemTagihanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
        );
        itemTagihanPanelLayout.setVerticalGroup(
            itemTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTagihanScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formInputTagihanPanelLayout = new javax.swing.GroupLayout(formInputTagihanPanel);
        formInputTagihanPanel.setLayout(formInputTagihanPanelLayout);
        formInputTagihanPanelLayout.setHorizontalGroup(
            formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(formInputTagihanLabel))
                    .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputIdTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputTanggalTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(itemTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formInputTagihanPanelLayout.setVerticalGroup(
            formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formInputTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(formInputTagihanPanelLayout.createSequentialGroup()
                        .addComponent(inputIdTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputTanggalTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(itemTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        formRingkasanTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        formRingkasanTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        formRingkasanTagihanLabel.setText("Ringkasan Tagihan");

        formInputRingkasanTagihanPanel.setBackground(new java.awt.Color(255, 255, 255));

        totalTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        totalTagihanLabel.setText("Total Tagihan");

        totalHargaTagihanLabel.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        totalHargaTagihanLabel.setText("Rp0");

        inputMetodePembayaranPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputMetodePembayaranLabel.setText("Metode Pembayaran");

        inputMetodePembayaranComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout inputMetodePembayaranPanelLayout = new javax.swing.GroupLayout(inputMetodePembayaranPanel);
        inputMetodePembayaranPanel.setLayout(inputMetodePembayaranPanelLayout);
        inputMetodePembayaranPanelLayout.setHorizontalGroup(
            inputMetodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputMetodePembayaranPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputMetodePembayaranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputMetodePembayaranComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputMetodePembayaranLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        inputStatusBayarLabel.setText("Status Bayar");

        lunasRadioButton.setText("LUNAS");
        lunasRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lunasRadioButtonActionPerformed(evt);
            }
        });

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
        simpanButton.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 12)); // NOI18N
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
                    .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(totalTagihanLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalHargaTagihanLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );
        formInputRingkasanTagihanPanelLayout.setVerticalGroup(
            formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(inputJumlahBayarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputMetodePembayaranPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(formInputRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalTagihanLabel)
                            .addComponent(totalHargaTagihanLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputStatusBayarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formInputRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(kembalianPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formRingkasanTagihanPanelLayout = new javax.swing.GroupLayout(formRingkasanTagihanPanel);
        formRingkasanTagihanPanel.setLayout(formRingkasanTagihanPanelLayout);
        formRingkasanTagihanPanelLayout.setHorizontalGroup(
            formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                        .addComponent(formRingkasanTagihanLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(formInputRingkasanTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        formRingkasanTagihanPanelLayout.setVerticalGroup(
            formRingkasanTagihanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRingkasanTagihanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formRingkasanTagihanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formInputRingkasanTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        TagihanScrollPane.setViewportView(TagihanTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(formRingkasanTagihanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pencarianTagihanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)
                    .addComponent(formInputTagihanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TagihanScrollPane))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(pencarianTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(formInputTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formRingkasanTagihanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TagihanScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(240, 240, 240))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lunasRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lunasRadioButtonActionPerformed
    }//GEN-LAST:event_lunasRadioButtonActionPerformed

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        if (selectedTagihan == null) {
            JOptionPane.showMessageDialog(this, "Pilih tagihan terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (lunasRadioButton.isSelected()) {
                String jumlahText = inputJumlahBayarTextField.getText().trim();
                if (jumlahText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Masukkan jumlah bayar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double jumlahBayar = Double.parseDouble(jumlahText);
                if (jumlahBayar < selectedTagihan.getTotalTagihan()) {
                    JOptionPane.showMessageDialog(this,
                        "Jumlah bayar Rp" + String.format("%.0f", jumlahBayar) +
                        " kurang dari total tagihan Rp" + String.format("%.0f", selectedTagihan.getTotalTagihan()) + ".",
                        "Pembayaran Tidak Cukup", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Tagihan.MetodePembayaran metode = Tagihan.MetodePembayaran.valueOf(
                    (String) inputMetodePembayaranComboBox.getSelectedItem()
                );
                tc.bayar(selectedTagihan, jumlahBayar, metode);
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                selectedTagihan.setStatus(Tagihan.Status.BELUM_BAYAR);
                tc.update(selectedTagihan, selectedTagihan.getIdTagihan());
                JOptionPane.showMessageDialog(this, "Status tagihan diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
            loadTagihanTable(tc.showDataWithNames());
            clearForm();
            setFormEnabled(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format jumlah bayar tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_simpanButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane TagihanScrollPane;
    private javax.swing.JTable TagihanTable;
    private javax.swing.JRadioButton belumLunasRadioButton;
    private javax.swing.JPanel formInputRingkasanTagihanPanel;
    private javax.swing.JLabel formInputTagihanLabel;
    private javax.swing.JPanel formInputTagihanPanel;
    private javax.swing.JLabel formRingkasanTagihanLabel;
    private javax.swing.JPanel formRingkasanTagihanPanel;
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
    private javax.swing.JLabel inputStatusBayarLabel;
    private javax.swing.JPanel inputStatusBayarPanel;
    private com.toedter.calendar.JDateChooser inputTanggalTagihanDateChooser;
    private javax.swing.JLabel inputTanggalTagihanLabel;
    private javax.swing.JPanel inputTanggalTagihanPanel;
    private javax.swing.JLabel itemTagihanLabel;
    private javax.swing.JPanel itemTagihanPanel;
    private javax.swing.JScrollPane itemTagihanScrollPane;
    private javax.swing.JTable itemTagihanTable;
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
    private javax.swing.JLabel totalHargaTagihanLabel;
    private javax.swing.JLabel totalTagihanLabel;
    // End of variables declaration//GEN-END:variables
}
