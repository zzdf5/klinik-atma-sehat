/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.AntrianControl;
import control.PoliklinikControl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Poliklinik;

public class ManajemenAntrianPanel extends javax.swing.JPanel {

    private final AntrianControl ac = new AntrianControl();
    private final PoliklinikControl pkc = new PoliklinikControl();
    private List<Poliklinik> poliklinikList;
    private List<Object[]> todayAntrian;
    private final String TODAY = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    public ManajemenAntrianPanel() {
        initComponents();
        setOpaque(false);

        // Ukuran kartu dan kotak biru konstan, tidak ikut teks
        java.awt.Dimension cardSize = new java.awt.Dimension(371, 354);
        java.awt.Dimension blueSize = new java.awt.Dimension(344, 93);
        for (javax.swing.JPanel p : new javax.swing.JPanel[]{panelAntrian1, panelAntrian2, panelAntrian3}) {
            p.setPreferredSize(cardSize); p.setMinimumSize(cardSize); p.setMaximumSize(cardSize);
        }
        for (javax.swing.JPanel p : new javax.swing.JPanel[]{kodeAntrianPanel1, kodeAntrianPanel2, kodeAntrianPanel3}) {
            p.setPreferredSize(blueSize); p.setMinimumSize(blueSize); p.setMaximumSize(blueSize);
        }

        // Kotak kecil kiri & kanan tiap kartu
        java.awt.Dimension smallSize = new java.awt.Dimension(167, 71);
        for (javax.swing.JPanel p : new javax.swing.JPanel[]{
                namaDokterPanel1, statusAntrianPanel1, namaDokterPanel2, statusAntrianPanel2, namaDokterPanel3, statusAntrianPanel3}) {
            p.setPreferredSize(smallSize); p.setMinimumSize(smallSize); p.setMaximumSize(smallSize);
        }

        // Ganti layout kotak biru & kotak kecil ke BorderLayout agar label tepat di tengah
        centerLabelInPanel(kodeAntrianPanel1,  kodeAntrianLabel1);
        centerLabelInPanel(kodeAntrianPanel2,  kodeAntrianLabel2);
        centerLabelInPanel(kodeAntrianPanel3, kodeAntrianLabel3);
        centerLabelInPanel(namaDokterPanel1,  namaDokterLabel1);
        centerLabelInPanel(statusAntrianPanel1,  statusAntrianLabel1);
        centerLabelInPanel(namaDokterPanel2,  namaDokterLabel2);
        centerLabelInPanel(statusAntrianPanel2,  jLabel11);
        centerLabelInPanel(namaDokterPanel3, namaDokterLabel3);
        centerLabelInPanel(statusAntrianPanel3, statusAntrianLabel3);

        loadPoliklinikCombo();
        refreshAll();

        // Listener filter poliklinik
        pilihPoliklinikComboBox.addActionListener(e -> loadTable2ByPoliklinik());

        // jTextField1 enter → search
        searchAntrianPasienTextField.addActionListener(e -> doSearch());

        // Listener klik tabel antrian (jTable2)
        antrianPasienTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = antrianPasienTable.getSelectedRow();
            if (row >= 0) fillDetailCard(row);
        });

        // jButton1/2/5 dan jButton4 ditangani di GEN handler masing-masing
    }

    private void loadPoliklinikCombo() {
        poliklinikList = pkc.showData();
        javax.swing.DefaultComboBoxModel<String> m = new javax.swing.DefaultComboBoxModel<>();
        m.addElement("-- Semua Poliklinik --");
        for (Poliklinik p : poliklinikList) m.addElement(p.getIdPoliklinik() + " - " + p.getNamaPoliklinik());
        pilihPoliklinikComboBox.setModel(m);
    }

    private void refreshAll() {
        todayAntrian = ac.showDataWithNames();
        loadCards();
        loadTable2ByPoliklinik();
        loadTable1("");
    }

    private void loadCards() {
        // Urutkan semua antrian: tanggal terbaru dulu, lalu nomor urut terkecil
        List<Object[]> all = new ArrayList<>(todayAntrian);
        all.sort((a, b) -> {
            int d = String.valueOf(b[5]).compareTo(String.valueOf(a[5]));
            return d != 0 ? d : Integer.compare((int) a[1], (int) b[1]);
        });

        // Kiri: sedang diperiksa (DALAM_PEMERIKSAAN)
        Object[] sedang = null;
        for (Object[] row : all) {
            if ("DALAM_PEMERIKSAAN".equals(String.valueOf(row[7]))) { sedang = row; break; }
        }

        // Tengah & kanan: menunggu (MENUNGGU), urut nomor terkecil
        List<Object[]> menunggu = new ArrayList<>();
        for (Object[] row : all) {
            if ("MENUNGGU".equals(String.valueOf(row[7]))) menunggu.add(row);
        }

        fillCardByStatus(0, sedang);
        fillCardByStatus(1, menunggu.size() > 0 ? menunggu.get(0) : null);
        fillCardByStatus(2, menunggu.size() > 1 ? menunggu.get(1) : null);
    }

    private void centerLabelInPanel(javax.swing.JPanel panel, javax.swing.JLabel label) {
        panel.removeAll();
        panel.setLayout(new java.awt.GridBagLayout());
        label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        panel.add(label);
    }

    private String trunc(String text, int max) {
        if (text == null || text.length() <= max) return text;
        return text.substring(0, max - 2) + "..";
    }

    // row: [id_antrian, nomor_urut, nama_pasien, nama_dokter, nama_poliklinik, tanggal, jenis, status]
    private void fillCardByStatus(int idx, Object[] row) {
        String namaPasien = trunc(row != null ? String.valueOf(row[2]) : "-", 20);
        String namaPoli   = trunc(row != null ? String.valueOf(row[4]) : "-", 22);
        String noUrut     = row != null ? String.valueOf(row[1]) : "-";
        String namaDokter = trunc(row != null ? String.valueOf(row[3]) : "-", 18);
        String status     = row != null ? String.valueOf(row[7]) : "-";

        switch (idx) {
            case 0:
                namaPemeriksaanLabel1.setText(namaPasien);
                namaPoliklinik1.setText(namaPoli);
                kodeAntrianLabel1.setText(noUrut);
                namaDokterLabel1.setText(namaDokter);
                statusAntrianLabel1.setText(status);
                break;
            case 1:
                namaPemeriksaanLabel2.setText(namaPasien);
                namaPoliklinik2.setText(namaPoli);
                kodeAntrianLabel2.setText(noUrut);
                namaDokterLabel2.setText(namaDokter);
                jLabel11.setText(status);
                break;
            case 2:
                namaPemeriksaanLabel3.setText(namaPasien);
                namaPoliklinik3.setText(namaPoli);
                kodeAntrianLabel3.setText(noUrut);
                namaDokterLabel3.setText(namaDokter);
                statusAntrianLabel3.setText(status);
                break;
        }
    }

    private void loadTable2ByPoliklinik() {
        String selected = (String) pilihPoliklinikComboBox.getSelectedItem();
        String filterPoli = (selected == null || selected.startsWith("--")) ? null
                : selected.split(" - ")[1].trim();

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"No. Urut", "Nama Pasien", "Nama Dokter", "Poliklinik", "Jenis", "Status"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        for (Object[] row : todayAntrian) {
            if (!TODAY.equals(String.valueOf(row[5]))) continue;
            if (filterPoli != null && !filterPoli.equals(String.valueOf(row[4]))) continue;
            m.addRow(new Object[]{row[1], row[2], row[3], row[4], row[6], row[7]});
        }
        antrianPasienTable.setModel(m);
    }

    private void loadTable1(String keyword) {
        DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID", "No. Urut", "Nama Pasien", "Nama Dokter", "Poliklinik", "Tanggal", "Jenis", "Status"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        List<Object[]> data = keyword.isEmpty() ? todayAntrian : ac.searchByKeyword(keyword);
        for (Object[] row : data) m.addRow(row);
        tableRahasia.setModel(m);
    }

    private void fillDetailCard(int row) {
        DefaultTableModel m = (DefaultTableModel) antrianPasienTable.getModel();
        String namaPasien = String.valueOf(m.getValueAt(row, 1));
        String namaPoli   = String.valueOf(m.getValueAt(row, 3));
        String noUrut     = String.valueOf(m.getValueAt(row, 0));
    }

    

    private void filterByCardPoliklinik(int cardIdx) {
        String[] names = {namaPoliklinik1.getText(), namaPoliklinik2.getText(), namaPoliklinik3.getText()};
        String namaPoli = names[cardIdx];
        if ("-".equals(namaPoli)) return;
        // Pilih di combo
        for (int i = 0; i < pilihPoliklinikComboBox.getItemCount(); i++) {
            String item = (String) pilihPoliklinikComboBox.getItemAt(i);
            if (item.contains(namaPoli)) { pilihPoliklinikComboBox.setSelectedIndex(i); return; }
        }
    }

    private void doSearch() {
        String keyword = searchAntrianPasienTextField.getText().trim();
        if (keyword.isEmpty()) { refreshAll(); return; }
        filterTableByNamaPasien(keyword);
    }

    private void filterTableByNamaPasien(String keyword) {
        String kw = keyword.toLowerCase();
        DefaultTableModel m = new DefaultTableModel(
            new String[]{"No. Urut", "Nama Pasien", "Nama Dokter", "Poliklinik", "Jenis", "Status"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        for (Object[] row : todayAntrian) {
            if (String.valueOf(row[2]).toLowerCase().contains(kw)) {
                m.addRow(new Object[]{row[1], row[2], row[3], row[4], row[6], row[7]});
            }
        }
        antrianPasienTable.setModel(m);
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
        searchAntrianPasienPanel = new javax.swing.JPanel();
        searchAntrianPasienLabel = new javax.swing.JLabel();
        searchAntrianPasienTextField = new javax.swing.JTextField();
        searchAntrianPasienButton = new javax.swing.JButton();
        scrollPaneRahasia = new javax.swing.JScrollPane();
        tableRahasia = new javax.swing.JTable();
        panelAntrian1 = new javax.swing.JPanel();
        namaPemeriksaanLabel1 = new javax.swing.JLabel();
        namaPoliklinik1 = new javax.swing.JLabel();
        kodeAntrianPanel1 = new javax.swing.JPanel();
        kodeAntrianLabel1 = new javax.swing.JLabel();
        namaDokterPanel1 = new javax.swing.JPanel();
        namaDokterLabel1 = new javax.swing.JLabel();
        statusAntrianPanel1 = new javax.swing.JPanel();
        statusAntrianLabel1 = new javax.swing.JLabel();
        liihatButton1 = new javax.swing.JButton();
        panelAntrian2 = new javax.swing.JPanel();
        namaPemeriksaanLabel2 = new javax.swing.JLabel();
        namaPoliklinik2 = new javax.swing.JLabel();
        kodeAntrianPanel2 = new javax.swing.JPanel();
        kodeAntrianLabel2 = new javax.swing.JLabel();
        namaDokterPanel2 = new javax.swing.JPanel();
        namaDokterLabel2 = new javax.swing.JLabel();
        statusAntrianPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        liihatButton2 = new javax.swing.JButton();
        antrianPasienHeaderPanel = new javax.swing.JPanel();
        pilihPoliklinikPanel = new javax.swing.JPanel();
        pilihPoliklinikLabel = new javax.swing.JLabel();
        pilihPoliklinikComboBox = new javax.swing.JComboBox<>();
        antrianPasienHeaderLabel = new javax.swing.JLabel();
        subJudulAntrianLabel = new javax.swing.JLabel();
        antrianPasienScrollPane = new javax.swing.JScrollPane();
        antrianPasienTable = new javax.swing.JTable();
        panelAntrian3 = new javax.swing.JPanel();
        namaPemeriksaanLabel3 = new javax.swing.JLabel();
        namaPoliklinik3 = new javax.swing.JLabel();
        kodeAntrianPanel3 = new javax.swing.JPanel();
        kodeAntrianLabel3 = new javax.swing.JLabel();
        namaDokterPanel3 = new javax.swing.JPanel();
        namaDokterLabel3 = new javax.swing.JLabel();
        statusAntrianPanel3 = new javax.swing.JPanel();
        statusAntrianLabel3 = new javax.swing.JLabel();
        liihatButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));
        mainPanel.setPreferredSize(new java.awt.Dimension(1161, 799));

        searchAntrianPasienPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchAntrianPasienPanel.setPreferredSize(new java.awt.Dimension(600, 70));

        searchAntrianPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        searchAntrianPasienLabel.setText("Cari Pasien");

        searchAntrianPasienTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAntrianPasienTextFieldActionPerformed(evt);
            }
        });

        searchAntrianPasienButton.setBackground(new java.awt.Color(0, 0, 153));
        searchAntrianPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        searchAntrianPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        searchAntrianPasienButton.setText("Cari");
        searchAntrianPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAntrianPasienButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchAntrianPasienPanelLayout = new javax.swing.GroupLayout(searchAntrianPasienPanel);
        searchAntrianPasienPanel.setLayout(searchAntrianPasienPanelLayout);
        searchAntrianPasienPanelLayout.setHorizontalGroup(
            searchAntrianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchAntrianPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchAntrianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchAntrianPasienPanelLayout.createSequentialGroup()
                        .addComponent(searchAntrianPasienLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(searchAntrianPasienPanelLayout.createSequentialGroup()
                        .addComponent(searchAntrianPasienTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchAntrianPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        searchAntrianPasienPanelLayout.setVerticalGroup(
            searchAntrianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchAntrianPasienPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(searchAntrianPasienLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchAntrianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchAntrianPasienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchAntrianPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tableRahasia.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollPaneRahasia.setViewportView(tableRahasia);

        panelAntrian1.setBackground(new java.awt.Color(255, 255, 255));
        panelAntrian1.setPreferredSize(new java.awt.Dimension(371, 354));

        namaPemeriksaanLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        namaPemeriksaanLabel1.setText("Nama Pemeriksaan");

        namaPoliklinik1.setText("Poliklinik A");

        kodeAntrianPanel1.setBackground(new java.awt.Color(51, 204, 0));

        kodeAntrianLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        kodeAntrianLabel1.setForeground(new java.awt.Color(255, 255, 255));
        kodeAntrianLabel1.setText("P-105");

        javax.swing.GroupLayout kodeAntrianPanel1Layout = new javax.swing.GroupLayout(kodeAntrianPanel1);
        kodeAntrianPanel1.setLayout(kodeAntrianPanel1Layout);
        kodeAntrianPanel1Layout.setHorizontalGroup(
            kodeAntrianPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel1Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(kodeAntrianLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kodeAntrianPanel1Layout.setVerticalGroup(
            kodeAntrianPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(kodeAntrianLabel1)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        namaDokterPanel1.setBackground(new java.awt.Color(204, 255, 204));

        namaDokterLabel1.setText("jLabel5");

        javax.swing.GroupLayout namaDokterPanel1Layout = new javax.swing.GroupLayout(namaDokterPanel1);
        namaDokterPanel1.setLayout(namaDokterPanel1Layout);
        namaDokterPanel1Layout.setHorizontalGroup(
            namaDokterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(namaDokterLabel1)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        namaDokterPanel1Layout.setVerticalGroup(
            namaDokterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(namaDokterLabel1)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        statusAntrianPanel1.setBackground(new java.awt.Color(204, 255, 204));

        statusAntrianLabel1.setText("jLabel6");

        javax.swing.GroupLayout statusAntrianPanel1Layout = new javax.swing.GroupLayout(statusAntrianPanel1);
        statusAntrianPanel1.setLayout(statusAntrianPanel1Layout);
        statusAntrianPanel1Layout.setHorizontalGroup(
            statusAntrianPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusAntrianPanel1Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(statusAntrianLabel1)
                .addGap(61, 61, 61))
        );
        statusAntrianPanel1Layout.setVerticalGroup(
            statusAntrianPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusAntrianPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(statusAntrianLabel1)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        liihatButton1.setBackground(new java.awt.Color(51, 153, 0));
        liihatButton1.setForeground(new java.awt.Color(255, 255, 255));
        liihatButton1.setText("Lihat ");
        liihatButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liihatButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAntrian1Layout = new javax.swing.GroupLayout(panelAntrian1);
        panelAntrian1.setLayout(panelAntrian1Layout);
        panelAntrian1Layout.setHorizontalGroup(
            panelAntrian1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelAntrian1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(liihatButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelAntrian1Layout.createSequentialGroup()
                        .addComponent(namaDokterPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusAntrianPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(namaPoliklinik1)
                    .addComponent(namaPemeriksaanLabel1)
                    .addComponent(kodeAntrianPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelAntrian1Layout.setVerticalGroup(
            panelAntrian1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(namaPemeriksaanLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaPoliklinik1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kodeAntrianPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(panelAntrian1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusAntrianPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaDokterPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(liihatButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelAntrian2.setBackground(new java.awt.Color(255, 255, 255));
        panelAntrian2.setPreferredSize(new java.awt.Dimension(371, 354));

        namaPemeriksaanLabel2.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        namaPemeriksaanLabel2.setText("Nama Pemeriksaan");

        namaPoliklinik2.setText("Poliklinik A");

        kodeAntrianPanel2.setBackground(new java.awt.Color(91, 101, 220));

        kodeAntrianLabel2.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        kodeAntrianLabel2.setForeground(new java.awt.Color(255, 255, 255));
        kodeAntrianLabel2.setText("P-105");

        javax.swing.GroupLayout kodeAntrianPanel2Layout = new javax.swing.GroupLayout(kodeAntrianPanel2);
        kodeAntrianPanel2.setLayout(kodeAntrianPanel2Layout);
        kodeAntrianPanel2Layout.setHorizontalGroup(
            kodeAntrianPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel2Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(kodeAntrianLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kodeAntrianPanel2Layout.setVerticalGroup(
            kodeAntrianPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(kodeAntrianLabel2)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        namaDokterPanel2.setBackground(new java.awt.Color(238, 239, 253));

        namaDokterLabel2.setText("jLabel5");

        javax.swing.GroupLayout namaDokterPanel2Layout = new javax.swing.GroupLayout(namaDokterPanel2);
        namaDokterPanel2.setLayout(namaDokterPanel2Layout);
        namaDokterPanel2Layout.setHorizontalGroup(
            namaDokterPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(namaDokterLabel2)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        namaDokterPanel2Layout.setVerticalGroup(
            namaDokterPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(namaDokterLabel2)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        statusAntrianPanel2.setBackground(new java.awt.Color(238, 239, 253));

        jLabel11.setText("jLabel6");

        javax.swing.GroupLayout statusAntrianPanel2Layout = new javax.swing.GroupLayout(statusAntrianPanel2);
        statusAntrianPanel2.setLayout(statusAntrianPanel2Layout);
        statusAntrianPanel2Layout.setHorizontalGroup(
            statusAntrianPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusAntrianPanel2Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(61, 61, 61))
        );
        statusAntrianPanel2Layout.setVerticalGroup(
            statusAntrianPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusAntrianPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel11)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        liihatButton2.setBackground(new java.awt.Color(91, 101, 220));
        liihatButton2.setForeground(new java.awt.Color(255, 255, 255));
        liihatButton2.setText("Lihat ");
        liihatButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liihatButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAntrian2Layout = new javax.swing.GroupLayout(panelAntrian2);
        panelAntrian2.setLayout(panelAntrian2Layout);
        panelAntrian2Layout.setHorizontalGroup(
            panelAntrian2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelAntrian2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(liihatButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelAntrian2Layout.createSequentialGroup()
                        .addComponent(namaDokterPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusAntrianPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(namaPoliklinik2)
                    .addComponent(namaPemeriksaanLabel2)
                    .addComponent(kodeAntrianPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelAntrian2Layout.setVerticalGroup(
            panelAntrian2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(namaPemeriksaanLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaPoliklinik2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kodeAntrianPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(panelAntrian2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusAntrianPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaDokterPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(liihatButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        antrianPasienHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        antrianPasienHeaderPanel.setPreferredSize(new java.awt.Dimension(800, 70));

        pilihPoliklinikPanel.setBackground(new java.awt.Color(91, 101, 220));

        pilihPoliklinikLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        pilihPoliklinikLabel.setForeground(new java.awt.Color(255, 255, 255));
        pilihPoliklinikLabel.setText("Pilih Poliklinik");

        pilihPoliklinikComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pilihPoliklinikPanelLayout = new javax.swing.GroupLayout(pilihPoliklinikPanel);
        pilihPoliklinikPanel.setLayout(pilihPoliklinikPanelLayout);
        pilihPoliklinikPanelLayout.setHorizontalGroup(
            pilihPoliklinikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihPoliklinikPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pilihPoliklinikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pilihPoliklinikLabel)
                    .addComponent(pilihPoliklinikComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pilihPoliklinikPanelLayout.setVerticalGroup(
            pilihPoliklinikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pilihPoliklinikPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pilihPoliklinikLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihPoliklinikComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        antrianPasienHeaderLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        antrianPasienHeaderLabel.setText("Antrian Pasien");

        subJudulAntrianLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        subJudulAntrianLabel.setText("Antrian");

        javax.swing.GroupLayout antrianPasienHeaderPanelLayout = new javax.swing.GroupLayout(antrianPasienHeaderPanel);
        antrianPasienHeaderPanel.setLayout(antrianPasienHeaderPanelLayout);
        antrianPasienHeaderPanelLayout.setHorizontalGroup(
            antrianPasienHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(antrianPasienHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(antrianPasienHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subJudulAntrianLabel)
                    .addComponent(antrianPasienHeaderLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pilihPoliklinikPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        antrianPasienHeaderPanelLayout.setVerticalGroup(
            antrianPasienHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pilihPoliklinikPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(antrianPasienHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(antrianPasienHeaderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subJudulAntrianLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        antrianPasienTable.setModel(new javax.swing.table.DefaultTableModel(
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
        antrianPasienScrollPane.setViewportView(antrianPasienTable);

        panelAntrian3.setBackground(new java.awt.Color(255, 255, 255));
        panelAntrian3.setPreferredSize(new java.awt.Dimension(371, 354));

        namaPemeriksaanLabel3.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        namaPemeriksaanLabel3.setText("Nama Pemeriksaan");

        namaPoliklinik3.setText("Poliklinik A");

        kodeAntrianPanel3.setBackground(new java.awt.Color(91, 101, 220));

        kodeAntrianLabel3.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        kodeAntrianLabel3.setForeground(new java.awt.Color(255, 255, 255));
        kodeAntrianLabel3.setText("P-105");

        javax.swing.GroupLayout kodeAntrianPanel3Layout = new javax.swing.GroupLayout(kodeAntrianPanel3);
        kodeAntrianPanel3.setLayout(kodeAntrianPanel3Layout);
        kodeAntrianPanel3Layout.setHorizontalGroup(
            kodeAntrianPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel3Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(kodeAntrianLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kodeAntrianPanel3Layout.setVerticalGroup(
            kodeAntrianPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodeAntrianPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(kodeAntrianLabel3)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        namaDokterPanel3.setBackground(new java.awt.Color(238, 239, 253));

        namaDokterLabel3.setText("jLabel5");

        javax.swing.GroupLayout namaDokterPanel3Layout = new javax.swing.GroupLayout(namaDokterPanel3);
        namaDokterPanel3.setLayout(namaDokterPanel3Layout);
        namaDokterPanel3Layout.setHorizontalGroup(
            namaDokterPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(namaDokterLabel3)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        namaDokterPanel3Layout.setVerticalGroup(
            namaDokterPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaDokterPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(namaDokterLabel3)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        statusAntrianPanel3.setBackground(new java.awt.Color(238, 239, 253));

        statusAntrianLabel3.setText("jLabel6");

        javax.swing.GroupLayout statusAntrianPanel3Layout = new javax.swing.GroupLayout(statusAntrianPanel3);
        statusAntrianPanel3.setLayout(statusAntrianPanel3Layout);
        statusAntrianPanel3Layout.setHorizontalGroup(
            statusAntrianPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusAntrianPanel3Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(statusAntrianLabel3)
                .addGap(61, 61, 61))
        );
        statusAntrianPanel3Layout.setVerticalGroup(
            statusAntrianPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusAntrianPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(statusAntrianLabel3)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        liihatButton3.setBackground(new java.awt.Color(91, 101, 220));
        liihatButton3.setForeground(new java.awt.Color(255, 255, 255));
        liihatButton3.setText("Lihat ");
        liihatButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liihatButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAntrian3Layout = new javax.swing.GroupLayout(panelAntrian3);
        panelAntrian3.setLayout(panelAntrian3Layout);
        panelAntrian3Layout.setHorizontalGroup(
            panelAntrian3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelAntrian3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(liihatButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelAntrian3Layout.createSequentialGroup()
                        .addComponent(namaDokterPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusAntrianPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(namaPoliklinik3)
                    .addComponent(namaPemeriksaanLabel3)
                    .addComponent(kodeAntrianPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        panelAntrian3Layout.setVerticalGroup(
            panelAntrian3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntrian3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(namaPemeriksaanLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaPoliklinik3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kodeAntrianPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(panelAntrian3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusAntrianPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaDokterPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(liihatButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPaneRahasia, javax.swing.GroupLayout.PREFERRED_SIZE, 1149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(antrianPasienScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(searchAntrianPasienPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addComponent(panelAntrian1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(panelAntrian2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(panelAntrian3, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                        .addComponent(antrianPasienHeaderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(antrianPasienHeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAntrian1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAntrian2, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAntrian3, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchAntrianPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(antrianPasienScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(scrollPaneRahasia, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void liihatButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liihatButton1ActionPerformed
        filterByCardPoliklinik(0);
    }//GEN-LAST:event_liihatButton1ActionPerformed

    private void liihatButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liihatButton2ActionPerformed
        filterByCardPoliklinik(1);
    }//GEN-LAST:event_liihatButton2ActionPerformed

    private void searchAntrianPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAntrianPasienButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_searchAntrianPasienButtonActionPerformed

    private void liihatButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liihatButton3ActionPerformed
        filterByCardPoliklinik(2);
    }//GEN-LAST:event_liihatButton3ActionPerformed

    private void searchAntrianPasienTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAntrianPasienTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchAntrianPasienTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel antrianPasienHeaderLabel;
    private javax.swing.JPanel antrianPasienHeaderPanel;
    private javax.swing.JScrollPane antrianPasienScrollPane;
    private javax.swing.JTable antrianPasienTable;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel kodeAntrianLabel1;
    private javax.swing.JLabel kodeAntrianLabel2;
    private javax.swing.JLabel kodeAntrianLabel3;
    private javax.swing.JPanel kodeAntrianPanel1;
    private javax.swing.JPanel kodeAntrianPanel2;
    private javax.swing.JPanel kodeAntrianPanel3;
    private javax.swing.JButton liihatButton1;
    private javax.swing.JButton liihatButton2;
    private javax.swing.JButton liihatButton3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel namaDokterLabel1;
    private javax.swing.JLabel namaDokterLabel2;
    private javax.swing.JLabel namaDokterLabel3;
    private javax.swing.JPanel namaDokterPanel1;
    private javax.swing.JPanel namaDokterPanel2;
    private javax.swing.JPanel namaDokterPanel3;
    private javax.swing.JLabel namaPemeriksaanLabel1;
    private javax.swing.JLabel namaPemeriksaanLabel2;
    private javax.swing.JLabel namaPemeriksaanLabel3;
    private javax.swing.JLabel namaPoliklinik1;
    private javax.swing.JLabel namaPoliklinik2;
    private javax.swing.JLabel namaPoliklinik3;
    private javax.swing.JPanel panelAntrian1;
    private javax.swing.JPanel panelAntrian2;
    private javax.swing.JPanel panelAntrian3;
    private javax.swing.JComboBox<String> pilihPoliklinikComboBox;
    private javax.swing.JLabel pilihPoliklinikLabel;
    private javax.swing.JPanel pilihPoliklinikPanel;
    private javax.swing.JScrollPane scrollPaneRahasia;
    private javax.swing.JButton searchAntrianPasienButton;
    private javax.swing.JLabel searchAntrianPasienLabel;
    private javax.swing.JPanel searchAntrianPasienPanel;
    private javax.swing.JTextField searchAntrianPasienTextField;
    private javax.swing.JLabel statusAntrianLabel1;
    private javax.swing.JLabel statusAntrianLabel3;
    private javax.swing.JPanel statusAntrianPanel1;
    private javax.swing.JPanel statusAntrianPanel2;
    private javax.swing.JPanel statusAntrianPanel3;
    private javax.swing.JLabel subJudulAntrianLabel;
    private javax.swing.JTable tableRahasia;
    // End of variables declaration//GEN-END:variables
}
