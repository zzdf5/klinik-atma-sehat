/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.AntrianControl;
import control.DokterControl;
import control.JadwalDokterControl;
import control.KunjunganControl;
import control.PasienControl;
import control.ResepControl;
import control.TagihanControl;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Antrian;
import model.Dokter;
import model.JadwalDokter;
import model.Kunjungan;
import model.Pasien;
import model.Resep;
import table.TableKunjungan;

public class ManajemenKunjunganPanel extends javax.swing.JPanel {

    private final KunjunganControl kc = new KunjunganControl();
    private final DokterControl dc = new DokterControl();
    private final PasienControl pasienCtrl = new PasienControl();
    private final JadwalDokterControl jdc = new JadwalDokterControl();
    private final AntrianControl ac = new AntrianControl();
    private List<Dokter> dokterList;
    private List<Pasien> cachedPasienList;
    private boolean suppressAutoComplete = false;
    private final List<Pasien> autoCompleteResult = new ArrayList<>();
    private JComboBox<String> jamJadwalComboBox;
    private List<Object[]> jadwalList = new ArrayList<>();
    private JTextArea keluhanUtamaArea;
    private JTextArea hasilPemeriksaanArea;
    private String action = null;
    private String selectedId = null;

    public ManajemenKunjunganPanel() {
        initComponents();
        setOpaque(false);

        totalPendapatanNumber.setFont(new Font("Arial", Font.BOLD, 20));

        keluhanUtamaArea = new JTextArea();
        keluhanUtamaArea.setLineWrap(true);
        keluhanUtamaArea.setWrapStyleWord(true);
        keluhanUtamaArea.setFont(inputKeluhanUtamaTextField.getFont());
        inputKeluhanUtamaScrollPane.setViewportView(keluhanUtamaArea);

        hasilPemeriksaanArea = new JTextArea();
        hasilPemeriksaanArea.setLineWrap(true);
        hasilPemeriksaanArea.setWrapStyleWord(true);
        hasilPemeriksaanArea.setFont(inputHasilPemeriksaanTextField.getFont());
        inputHasilPemeriksaanScrollPane.setViewportView(hasilPemeriksaanArea);

        loadDokterComboBox();
        cachedPasienList = pasienCtrl.showData();
        setupAutoComplete();
        jamJadwalComboBox = jComboBox1;
        jamJadwalComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Pilih Dokter Dulu --"}));
        jamJadwalComboBox.setEnabled(false);

        inputStatusKunjunganComboBox.setModel(new DefaultComboBoxModel<>(
            new String[]{"BELUM_DILAKUKAN", "SELESAI", "BATAL"}
        ));

        setFormEnabled(false);
        setEditDeleteEnabled(false);
        showKunjungan();
        updateMetrics();
    }
    
        private void loadDokterComboBox() {
        dokterList = dc.showData();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Pilih Dokter --");
        for (Dokter d : dokterList) {
            model.addElement(d.getId() + " - " + d.getNama());
        }
        inputPilihDokterComboBox.setModel(model);
    }

    private void showKunjungan() {
        kunjunganTable.setModel(kc.showTable(""));
    }

    private void updateMetrics() {
        List<Kunjungan> all = kc.showData();
        long antrian = all.stream()
            .filter(k -> k.getStatus() == Kunjungan.Status.BELUM_DILAKUKAN).count();
        long selesai = all.stream()
            .filter(k -> k.getStatus() == Kunjungan.Status.SELESAI).count();
        double totalPendapatan = all.stream()
            .filter(k -> k.getStatus() == Kunjungan.Status.SELESAI)
            .mapToDouble(Kunjungan::getBiayaKonsultasi).sum();
        antrianNumber.setText(String.valueOf(antrian));
        kunjunganAntrianNumber.setText(String.valueOf(selesai));
        totalPendapatanNumber.setText("Rp" + String.format("%,.0f", totalPendapatan));
    }

    private void setFormEnabled(boolean value) {
        inputIdKunjunganTextField.setEnabled(false);         // selalu disabled (auto-generate)
        inputNomorRekamMedisTextField.setEnabled(value);
        inputTanggalKunjunganDateChooser.setEnabled(value);
        inputPilihDokterComboBox.setEnabled(value);
        jamJadwalComboBox.setEnabled(value && !jadwalList.isEmpty());
        inputStatusKunjunganComboBox.setEnabled(false);      // selalu disabled, diatur sistem
        keluhanUtamaArea.setEnabled(value);
        hasilPemeriksaanArea.setEnabled(false);              // diisi dokter
        inputIdDiagnosaTextField.setEnabled(false);          // diisi dokter
        inputIdResepTextField.setEnabled(false);             // diisi dokter
        inputBiayaKonsultasiTextField.setEnabled(false);     // diisi dokter
        simpanButton.setEnabled(value);
        batalButton.setEnabled(value);
    }

    private String findIdPasien(String nomorRekamMedis) {
        for (Pasien p : cachedPasienList) {
            if (p.getNomorRekamMedis().equals(nomorRekamMedis)) return p.getId();
        }
        return "";
    }

    private String extractNomorRekamMedis() {
        String text = inputNomorRekamMedisTextField.getText().trim();
        if (text.contains(" - ")) return text.split(" - ")[0].trim();
        return text;
    }

    private String findNamaPasien(String nomorRekamMedis) {
        for (Pasien p : cachedPasienList) {
            if (p.getNomorRekamMedis().equals(nomorRekamMedis)) return p.getNama();
        }
        return "";
    }

    private void loadJamComboBox(String idDokter) {
        jadwalList = jdc.searchByDokterWithNames(idDokter);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (jadwalList.isEmpty()) {
            model.addElement("-- Tidak ada jadwal --");
        } else {
            model.addElement("-- Pilih Jadwal --");
            for (Object[] j : jadwalList) {
                model.addElement(j[1] + " - " + j[2] + "  |  Poli: " + j[3]);
            }
        }
        jamJadwalComboBox.setModel(model);
        jamJadwalComboBox.setEnabled(!jadwalList.isEmpty() && (action != null));
    }

    private void setupAutoComplete() {
        jPopupMenu.setFocusable(false);
        inputNomorRekamMedisTextField.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { triggerAutoComplete(); }
                @Override public void removeUpdate(DocumentEvent e) { triggerAutoComplete(); }
                @Override public void changedUpdate(DocumentEvent e) {}
            }
        );
    }

    private void triggerAutoComplete() {
        if (suppressAutoComplete || !inputNomorRekamMedisTextField.isEnabled()) return;
        String keyword = inputNomorRekamMedisTextField.getText().trim();

        jPopupMenu.setVisible(false);

        if (keyword.length() < 2) return;

        // Banyaknya saran mengikuti jumlah JMenuItem yang ada di jPopupMenu (dibuat di designer)
        int maxItem = jPopupMenu.getComponentCount();
        if (maxItem == 0) return;

        String kw = keyword.toLowerCase();
        autoCompleteResult.clear();
        for (Pasien p : cachedPasienList) {
            if (p.getNomorRekamMedis().toLowerCase().contains(kw)
                    || p.getNama().toLowerCase().contains(kw)) {
                autoCompleteResult.add(p);
                if (autoCompleteResult.size() >= maxItem) break;
            }
        }
        if (autoCompleteResult.isEmpty()) return;

        for (int i = 0; i < maxItem; i++) {
            JMenuItem mi = (JMenuItem) jPopupMenu.getComponent(i);
            if (i < autoCompleteResult.size()) {
                Pasien p = autoCompleteResult.get(i);
                mi.setText(p.getNomorRekamMedis() + "  –  " + p.getNama());
                mi.setFocusable(false);
                mi.setVisible(true);
            } else {
                mi.setVisible(false);
            }
        }
        jPopupMenu.show(inputNomorRekamMedisTextField, 0, inputNomorRekamMedisTextField.getHeight());
        inputNomorRekamMedisTextField.requestFocusInWindow();
    }

    private void setEditDeleteEnabled(boolean value) {
        barukanKunjunganButton.setEnabled(value);
        hapusKunjunganButton.setEnabled(value);
    }

    private void clearForm() {
        inputIdKunjunganTextField.setText("");
        inputNomorRekamMedisTextField.setText("");
        inputTanggalKunjunganDateChooser.setDate(null);
        inputPilihDokterComboBox.setSelectedIndex(0);
        jadwalList.clear();
        jamJadwalComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Pilih Dokter Dulu --"}));
        inputStatusKunjunganComboBox.setSelectedIndex(0);
        keluhanUtamaArea.setText("");
        hasilPemeriksaanArea.setText("");
        inputIdDiagnosaTextField.setText("");
        inputIdResepTextField.setText("");
        inputBiayaKonsultasiTextField.setText("");
    }

    private void fillForm(Kunjungan k) {
        inputIdKunjunganTextField.setText(k.getIdKunjungan());
        String noRm = k.getNomorRekamMedis();
        String namaPasien = findNamaPasien(noRm);
        suppressAutoComplete = true;
        inputNomorRekamMedisTextField.setText(namaPasien.isEmpty() ? noRm : noRm + " - " + namaPasien);
        suppressAutoComplete = false;
        if (k.getTanggal() != null && !k.getTanggal().isEmpty()) {
            try {
                inputTanggalKunjunganDateChooser.setDate(
                    new SimpleDateFormat("yyyy-MM-dd").parse(k.getTanggal()));
            } catch (Exception ex) {
                inputTanggalKunjunganDateChooser.setDate(null);
            }
        }
        inputPilihDokterComboBox.setSelectedIndex(0);
        String idDokter = k.getIdDokter();
        if (idDokter != null) {
            for (int i = 1; i < inputPilihDokterComboBox.getItemCount(); i++) {
                if (((String) inputPilihDokterComboBox.getItemAt(i)).startsWith(idDokter + " - ")) {
                    inputPilihDokterComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        String existingJam = k.getJam() != null ? k.getJam() : "";
        jamJadwalComboBox.setSelectedIndex(0);
        for (int i = 0; i < jadwalList.size(); i++) {
            if (existingJam.equals(jadwalList.get(i)[1])) {
                jamJadwalComboBox.setSelectedIndex(i + 1);
                break;
            }
        }
        inputStatusKunjunganComboBox.setSelectedItem(k.getStatus().name());
        keluhanUtamaArea.setText(k.getKeluhanUtama() != null ? k.getKeluhanUtama() : "");
        hasilPemeriksaanArea.setText(k.getHasilPemeriksaan() != null ? k.getHasilPemeriksaan() : "");
        inputIdDiagnosaTextField.setText(k.getIdDiagnosa() != null ? k.getIdDiagnosa() : "");
        inputIdResepTextField.setText(k.getIdResep() != null ? k.getIdResep() : "");
        inputBiayaKonsultasiTextField.setText(String.valueOf(k.getBiayaKonsultasi()));
    }

    private void doSearch() {
        String keyword = searchKunjunganTextField.getText().trim();
        if (keyword.isEmpty()) {
            showKunjungan();
            clearForm();
            selectedId = null;
            action = null;
            setFormEnabled(false);
            setEditDeleteEnabled(false);
            return;
        }

        selectedId = null;
        setEditDeleteEnabled(false);
        setFormEnabled(false);
        clearForm();

        List<Object[]> hasil = kc.searchByKeyword(keyword);
        if (hasil.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Kunjungan tidak ditemukan.", "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        kunjunganTable.setModel(new TableKunjungan(hasil));
        if (hasil.size() == 1) {
            Kunjungan k = kc.search((String) hasil.get(0)[0]);
            if (k != null) {
                fillForm(k);
                selectedId = k.getIdKunjungan();
                setEditDeleteEnabled(true);
            }
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

        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        mainPanel = new javax.swing.JPanel();
        formInputDataKunjunganPanel = new javax.swing.JPanel();
        formInputDataKunjunganLabel = new javax.swing.JLabel();
        inputIdKunjunganPanel = new javax.swing.JPanel();
        inputIdKunjunganLabel = new javax.swing.JLabel();
        inputIdKunjunganTextField = new javax.swing.JTextField();
        inputNomorRekamMedisPanel = new javax.swing.JPanel();
        inputNomorRekamMedisLabel = new javax.swing.JLabel();
        inputNomorRekamMedisTextField = new javax.swing.JTextField();
        inputTanggalKunjunganPanel = new javax.swing.JPanel();
        inputTanggalKunjunganLabel = new javax.swing.JLabel();
        inputTanggalKunjunganDateChooser = new com.toedter.calendar.JDateChooser();
        batalButton = new javax.swing.JButton();
        inputPilihDokterPanel = new javax.swing.JPanel();
        inputPilihDokterLabel = new javax.swing.JLabel();
        inputPilihDokterComboBox = new javax.swing.JComboBox<>();
        inputJamKunjunganPanel = new javax.swing.JPanel();
        inputJamKunjunganLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        inputStatusKunjunganPanel = new javax.swing.JPanel();
        inputStatusKunjunganLabel = new javax.swing.JLabel();
        inputStatusKunjunganComboBox = new javax.swing.JComboBox<>();
        inputIdDiagnosaPanel = new javax.swing.JPanel();
        inputIdDiagnosaLabel = new javax.swing.JLabel();
        inputIdDiagnosaTextField = new javax.swing.JTextField();
        inputIdResepPanel = new javax.swing.JPanel();
        inputIdResepLabel = new javax.swing.JLabel();
        inputIdResepTextField = new javax.swing.JTextField();
        inputBiayaKonsultasiPanel = new javax.swing.JPanel();
        inputBiayaKonsultasiLabel = new javax.swing.JLabel();
        inputBiayaKonsultasiTextField = new javax.swing.JTextField();
        inputKeluhanUtamaPanel = new javax.swing.JPanel();
        inputKeluhanUtamaLabel = new javax.swing.JLabel();
        inputKeluhanUtamaScrollPane = new javax.swing.JScrollPane();
        inputKeluhanUtamaTextField = new javax.swing.JTextField();
        simpanButton = new javax.swing.JButton();
        inputHasilPemeriksaanPanel = new javax.swing.JPanel();
        inputHasilPemeriksaanLabel = new javax.swing.JLabel();
        inputHasilPemeriksaanScrollPane = new javax.swing.JScrollPane();
        inputHasilPemeriksaanTextField = new javax.swing.JTextField();
        metricCardPanel = new javax.swing.JPanel();
        antrianCardPanel = new javax.swing.JPanel();
        antrianCardLabel = new javax.swing.JLabel();
        antrianNumber = new javax.swing.JLabel();
        kunjunganSelesaiCardPanel = new javax.swing.JPanel();
        kunjunganSelesaiCardLabel = new javax.swing.JLabel();
        kunjunganAntrianNumber = new javax.swing.JLabel();
        totalPendapatanCardPanel = new javax.swing.JPanel();
        totalPendapatanCardLabel = new javax.swing.JLabel();
        totalPendapatanNumber = new javax.swing.JLabel();
        kunjunganScrollPane = new javax.swing.JScrollPane();
        kunjunganTable = new javax.swing.JTable();
        kunjunganButtonPanel = new javax.swing.JPanel();
        tanbahKunjunganButton = new javax.swing.JButton();
        barukanKunjunganButton = new javax.swing.JButton();
        hapusKunjunganButton = new javax.swing.JButton();
        kunjunganButtonLabel = new javax.swing.JLabel();
        searchKunjunganPanel = new javax.swing.JPanel();
        judulKunjunganLabel = new javax.swing.JLabel();
        subJudulKunjunganLabel = new javax.swing.JLabel();
        searchKunjunganTextField = new javax.swing.JTextField();
        searchKunjunganButton = new javax.swing.JButton();
        searchKunjunganLabel = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem1");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem2);

        jMenuItem3.setText("jMenuItem3");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem3);

        jMenuItem4.setText("jMenuItem4");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem4);

        jMenuItem5.setText("jMenuItem5");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem5);

        jMenuItem6.setText("jMenuItem6");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem6);

        jMenuItem7.setText("jMenuItem7");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem7);

        jMenuItem8.setText("jMenuItem8");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItem8);

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));
        mainPanel.setPreferredSize(new java.awt.Dimension(1155, 799));

        formInputDataKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));

        formInputDataKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        formInputDataKunjunganLabel.setText("Data Kunjungan");

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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputIdKunjunganTextField))
                .addContainerGap())
        );
        inputIdKunjunganPanelLayout.setVerticalGroup(
            inputIdKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdKunjunganTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputNomorRekamMedisPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNomorRekamMedisLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputNomorRekamMedisLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/health-report.png"))); // NOI18N
        inputNomorRekamMedisLabel.setText("Nomor Rekam Medis");

        inputNomorRekamMedisTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputNomorRekamMedisTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputNomorRekamMedisPanelLayout = new javax.swing.GroupLayout(inputNomorRekamMedisPanel);
        inputNomorRekamMedisPanel.setLayout(inputNomorRekamMedisPanelLayout);
        inputNomorRekamMedisPanelLayout.setHorizontalGroup(
            inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                        .addComponent(inputNomorRekamMedisLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputNomorRekamMedisTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                .addContainerGap())
        );
        inputNomorRekamMedisPanelLayout.setVerticalGroup(
            inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNomorRekamMedisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNomorRekamMedisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputTanggalKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputTanggalKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rujukan.png"))); // NOI18N
        inputTanggalKunjunganLabel.setText("Tanggal");

        javax.swing.GroupLayout inputTanggalKunjunganPanelLayout = new javax.swing.GroupLayout(inputTanggalKunjunganPanel);
        inputTanggalKunjunganPanel.setLayout(inputTanggalKunjunganPanelLayout);
        inputTanggalKunjunganPanelLayout.setHorizontalGroup(
            inputTanggalKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputTanggalKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputTanggalKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(inputTanggalKunjunganLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputTanggalKunjunganDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                .addContainerGap())
        );
        inputTanggalKunjunganPanelLayout.setVerticalGroup(
            inputTanggalKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTanggalKunjunganDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        batalButton.setBackground(new java.awt.Color(237, 8, 0));
        batalButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        batalButton.setForeground(new java.awt.Color(255, 255, 255));
        batalButton.setText("Batal");
        batalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalButtonActionPerformed(evt);
            }
        });

        inputPilihDokterPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputPilihDokterLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputPilihDokterLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Spesialisasi.png"))); // NOI18N
        inputPilihDokterLabel.setText("Pilih Dokter");

        inputPilihDokterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        inputPilihDokterComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputPilihDokterComboBoxMouseClicked(evt);
            }
        });
        inputPilihDokterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPilihDokterComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputPilihDokterPanelLayout = new javax.swing.GroupLayout(inputPilihDokterPanel);
        inputPilihDokterPanel.setLayout(inputPilihDokterPanelLayout);
        inputPilihDokterPanelLayout.setHorizontalGroup(
            inputPilihDokterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPilihDokterPanelLayout.createSequentialGroup()
                .addGroup(inputPilihDokterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputPilihDokterLabel)
                    .addComponent(inputPilihDokterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 23, Short.MAX_VALUE))
        );
        inputPilihDokterPanelLayout.setVerticalGroup(
            inputPilihDokterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPilihDokterPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputPilihDokterLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputPilihDokterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        inputJamKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputJamKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputJamKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clockStart.png"))); // NOI18N
        inputJamKunjunganLabel.setText("Jam");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout inputJamKunjunganPanelLayout = new javax.swing.GroupLayout(inputJamKunjunganPanel);
        inputJamKunjunganPanel.setLayout(inputJamKunjunganPanelLayout);
        inputJamKunjunganPanelLayout.setHorizontalGroup(
            inputJamKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJamKunjunganPanelLayout.createSequentialGroup()
                .addGroup(inputJamKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputJamKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(inputJamKunjunganLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jComboBox1, 0, 254, Short.MAX_VALUE))
                .addContainerGap())
        );
        inputJamKunjunganPanelLayout.setVerticalGroup(
            inputJamKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJamKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputJamKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1)
                .addContainerGap())
        );

        inputStatusKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));
        inputStatusKunjunganPanel.setPreferredSize(new java.awt.Dimension(260, 59));

        inputStatusKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputStatusKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Status.png"))); // NOI18N
        inputStatusKunjunganLabel.setText("Status Kunjungan");

        inputStatusKunjunganComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout inputStatusKunjunganPanelLayout = new javax.swing.GroupLayout(inputStatusKunjunganPanel);
        inputStatusKunjunganPanel.setLayout(inputStatusKunjunganPanelLayout);
        inputStatusKunjunganPanelLayout.setHorizontalGroup(
            inputStatusKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStatusKunjunganPanelLayout.createSequentialGroup()
                .addGroup(inputStatusKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputStatusKunjunganLabel)
                    .addComponent(inputStatusKunjunganComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        inputStatusKunjunganPanelLayout.setVerticalGroup(
            inputStatusKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputStatusKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputStatusKunjunganLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputStatusKunjunganComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputIdDiagnosaPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdDiagnosaLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdDiagnosaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Penyakit.png"))); // NOI18N
        inputIdDiagnosaLabel.setText("ID Diagnosa");

        javax.swing.GroupLayout inputIdDiagnosaPanelLayout = new javax.swing.GroupLayout(inputIdDiagnosaPanel);
        inputIdDiagnosaPanel.setLayout(inputIdDiagnosaPanelLayout);
        inputIdDiagnosaPanelLayout.setHorizontalGroup(
            inputIdDiagnosaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdDiagnosaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputIdDiagnosaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputIdDiagnosaLabel)
                    .addComponent(inputIdDiagnosaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inputIdDiagnosaPanelLayout.setVerticalGroup(
            inputIdDiagnosaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdDiagnosaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdDiagnosaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdDiagnosaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputIdResepPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdResepLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdResepLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/recipe.png"))); // NOI18N
        inputIdResepLabel.setText("ID Resep");

        inputIdResepTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputIdResepTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputIdResepPanelLayout = new javax.swing.GroupLayout(inputIdResepPanel);
        inputIdResepPanel.setLayout(inputIdResepPanelLayout);
        inputIdResepPanelLayout.setHorizontalGroup(
            inputIdResepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdResepPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(inputIdResepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputIdResepPanelLayout.createSequentialGroup()
                        .addComponent(inputIdResepLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputIdResepTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)))
        );
        inputIdResepPanelLayout.setVerticalGroup(
            inputIdResepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdResepPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdResepLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputIdResepTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputBiayaKonsultasiPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputBiayaKonsultasiLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputBiayaKonsultasiLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Tarif.png"))); // NOI18N
        inputBiayaKonsultasiLabel.setText("Biaya Konsultasi");

        inputBiayaKonsultasiTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBiayaKonsultasiTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputBiayaKonsultasiPanelLayout = new javax.swing.GroupLayout(inputBiayaKonsultasiPanel);
        inputBiayaKonsultasiPanel.setLayout(inputBiayaKonsultasiPanelLayout);
        inputBiayaKonsultasiPanelLayout.setHorizontalGroup(
            inputBiayaKonsultasiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputBiayaKonsultasiPanelLayout.createSequentialGroup()
                .addGroup(inputBiayaKonsultasiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputBiayaKonsultasiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBiayaKonsultasiLabel))
                .addGap(0, 30, Short.MAX_VALUE))
        );
        inputBiayaKonsultasiPanelLayout.setVerticalGroup(
            inputBiayaKonsultasiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputBiayaKonsultasiPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputBiayaKonsultasiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputBiayaKonsultasiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputKeluhanUtamaPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputKeluhanUtamaLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputKeluhanUtamaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Keluhan.png"))); // NOI18N
        inputKeluhanUtamaLabel.setText("Keluhan Utama");

        inputKeluhanUtamaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputKeluhanUtamaTextFieldActionPerformed(evt);
            }
        });
        inputKeluhanUtamaScrollPane.setViewportView(inputKeluhanUtamaTextField);

        javax.swing.GroupLayout inputKeluhanUtamaPanelLayout = new javax.swing.GroupLayout(inputKeluhanUtamaPanel);
        inputKeluhanUtamaPanel.setLayout(inputKeluhanUtamaPanelLayout);
        inputKeluhanUtamaPanelLayout.setHorizontalGroup(
            inputKeluhanUtamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputKeluhanUtamaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputKeluhanUtamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputKeluhanUtamaLabel)
                    .addComponent(inputKeluhanUtamaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        inputKeluhanUtamaPanelLayout.setVerticalGroup(
            inputKeluhanUtamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputKeluhanUtamaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputKeluhanUtamaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputKeluhanUtamaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
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

        inputHasilPemeriksaanPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputHasilPemeriksaanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputHasilPemeriksaanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/HasilDiagnosa.png"))); // NOI18N
        inputHasilPemeriksaanLabel.setText("Hasil Pemeriksaan");

        inputHasilPemeriksaanTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHasilPemeriksaanTextFieldActionPerformed(evt);
            }
        });
        inputHasilPemeriksaanScrollPane.setViewportView(inputHasilPemeriksaanTextField);

        javax.swing.GroupLayout inputHasilPemeriksaanPanelLayout = new javax.swing.GroupLayout(inputHasilPemeriksaanPanel);
        inputHasilPemeriksaanPanel.setLayout(inputHasilPemeriksaanPanelLayout);
        inputHasilPemeriksaanPanelLayout.setHorizontalGroup(
            inputHasilPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inputHasilPemeriksaanPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(inputHasilPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputHasilPemeriksaanLabel)
                    .addComponent(inputHasilPemeriksaanScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        inputHasilPemeriksaanPanelLayout.setVerticalGroup(
            inputHasilPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputHasilPemeriksaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputHasilPemeriksaanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputHasilPemeriksaanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout formInputDataKunjunganPanelLayout = new javax.swing.GroupLayout(formInputDataKunjunganPanel);
        formInputDataKunjunganPanel.setLayout(formInputDataKunjunganPanelLayout);
        formInputDataKunjunganPanelLayout.setHorizontalGroup(
            formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formInputDataKunjunganLabel)
                    .addComponent(inputIdDiagnosaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputTanggalKunjunganPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(6, 6, 6)
                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(inputIdResepPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(inputBiayaKonsultasiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(inputNomorRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputJamKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputPilihDokterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputStatusKunjunganPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputKeluhanUtamaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(batalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputHasilPemeriksaanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formInputDataKunjunganPanelLayout.setVerticalGroup(
            formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(inputPilihDokterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(inputNomorRekamMedisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formInputDataKunjunganPanelLayout.createSequentialGroup()
                                .addComponent(formInputDataKunjunganLabel)
                                .addGap(18, 18, 18)
                                .addComponent(inputIdKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputTanggalKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(inputStatusKunjunganPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(inputJamKunjunganPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(inputIdResepPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(inputIdDiagnosaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(inputBiayaKonsultasiPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(formInputDataKunjunganPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputHasilPemeriksaanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputKeluhanUtamaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formInputDataKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(batalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        metricCardPanel.setBackground(new java.awt.Color(255, 255, 255));

        antrianCardPanel.setBackground(new java.awt.Color(0, 0, 153));
        antrianCardPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        antrianCardLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 14)); // NOI18N
        antrianCardLabel.setForeground(new java.awt.Color(255, 255, 255));
        antrianCardLabel.setText("Antrian Aktif");

        antrianNumber.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        antrianNumber.setForeground(new java.awt.Color(255, 255, 255));
        antrianNumber.setText("0");

        javax.swing.GroupLayout antrianCardPanelLayout = new javax.swing.GroupLayout(antrianCardPanel);
        antrianCardPanel.setLayout(antrianCardPanelLayout);
        antrianCardPanelLayout.setHorizontalGroup(
            antrianCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(antrianCardPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(antrianCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(antrianCardLabel)
                    .addGroup(antrianCardPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(antrianNumber)))
                .addContainerGap(148, Short.MAX_VALUE))
        );
        antrianCardPanelLayout.setVerticalGroup(
            antrianCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(antrianCardPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(antrianCardLabel)
                .addGap(18, 18, 18)
                .addComponent(antrianNumber)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kunjunganSelesaiCardPanel.setBackground(new java.awt.Color(153, 153, 255));
        kunjunganSelesaiCardPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        kunjunganSelesaiCardLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 14)); // NOI18N
        kunjunganSelesaiCardLabel.setForeground(new java.awt.Color(255, 255, 255));
        kunjunganSelesaiCardLabel.setText("Kunjungan Selesai");

        kunjunganAntrianNumber.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        kunjunganAntrianNumber.setForeground(new java.awt.Color(255, 255, 255));
        kunjunganAntrianNumber.setText("0");

        javax.swing.GroupLayout kunjunganSelesaiCardPanelLayout = new javax.swing.GroupLayout(kunjunganSelesaiCardPanel);
        kunjunganSelesaiCardPanel.setLayout(kunjunganSelesaiCardPanelLayout);
        kunjunganSelesaiCardPanelLayout.setHorizontalGroup(
            kunjunganSelesaiCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kunjunganSelesaiCardPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(kunjunganSelesaiCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kunjunganSelesaiCardLabel)
                    .addGroup(kunjunganSelesaiCardPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(kunjunganAntrianNumber)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        kunjunganSelesaiCardPanelLayout.setVerticalGroup(
            kunjunganSelesaiCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kunjunganSelesaiCardPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(kunjunganSelesaiCardLabel)
                .addGap(18, 18, 18)
                .addComponent(kunjunganAntrianNumber)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        totalPendapatanCardPanel.setBackground(new java.awt.Color(0, 0, 153));
        totalPendapatanCardPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        totalPendapatanCardLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 14)); // NOI18N
        totalPendapatanCardLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalPendapatanCardLabel.setText("Total Pendapatan");

        totalPendapatanNumber.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        totalPendapatanNumber.setForeground(new java.awt.Color(255, 255, 255));
        totalPendapatanNumber.setText("Rp0");

        javax.swing.GroupLayout totalPendapatanCardPanelLayout = new javax.swing.GroupLayout(totalPendapatanCardPanel);
        totalPendapatanCardPanel.setLayout(totalPendapatanCardPanelLayout);
        totalPendapatanCardPanelLayout.setHorizontalGroup(
            totalPendapatanCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPendapatanCardPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(totalPendapatanCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalPendapatanNumber)
                    .addComponent(totalPendapatanCardLabel))
                .addContainerGap(147, Short.MAX_VALUE))
        );
        totalPendapatanCardPanelLayout.setVerticalGroup(
            totalPendapatanCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPendapatanCardPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(totalPendapatanCardLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalPendapatanNumber)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout metricCardPanelLayout = new javax.swing.GroupLayout(metricCardPanel);
        metricCardPanel.setLayout(metricCardPanelLayout);
        metricCardPanelLayout.setHorizontalGroup(
            metricCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(metricCardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(antrianCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kunjunganSelesaiCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(totalPendapatanCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        metricCardPanelLayout.setVerticalGroup(
            metricCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(metricCardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(metricCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kunjunganSelesaiCardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(antrianCardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalPendapatanCardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        kunjunganTable.setModel(new javax.swing.table.DefaultTableModel(
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
        kunjunganTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kunjunganTableMouseClicked(evt);
            }
        });
        kunjunganScrollPane.setViewportView(kunjunganTable);

        kunjunganButtonPanel.setBackground(new java.awt.Color(255, 255, 255));

        tanbahKunjunganButton.setBackground(new java.awt.Color(51, 178, 73));
        tanbahKunjunganButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tanbahKunjunganButton.setForeground(new java.awt.Color(255, 255, 255));
        tanbahKunjunganButton.setText("Tambah");
        tanbahKunjunganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tanbahKunjunganButtonActionPerformed(evt);
            }
        });

        barukanKunjunganButton.setBackground(new java.awt.Color(255, 204, 51));
        barukanKunjunganButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        barukanKunjunganButton.setForeground(new java.awt.Color(255, 255, 255));
        barukanKunjunganButton.setText("Barukan");
        barukanKunjunganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barukanKunjunganButtonActionPerformed(evt);
            }
        });

        hapusKunjunganButton.setBackground(new java.awt.Color(255, 0, 0));
        hapusKunjunganButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        hapusKunjunganButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusKunjunganButton.setText("Hapus");
        hapusKunjunganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusKunjunganButtonActionPerformed(evt);
            }
        });

        kunjunganButtonLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        kunjunganButtonLabel.setText("Aksi");

        javax.swing.GroupLayout kunjunganButtonPanelLayout = new javax.swing.GroupLayout(kunjunganButtonPanel);
        kunjunganButtonPanel.setLayout(kunjunganButtonPanelLayout);
        kunjunganButtonPanelLayout.setHorizontalGroup(
            kunjunganButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kunjunganButtonPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(kunjunganButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kunjunganButtonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kunjunganButtonPanelLayout.createSequentialGroup()
                        .addComponent(tanbahKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barukanKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hapusKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        kunjunganButtonPanelLayout.setVerticalGroup(
            kunjunganButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kunjunganButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kunjunganButtonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kunjunganButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tanbahKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barukanKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapusKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        searchKunjunganPanel.setBackground(new java.awt.Color(255, 255, 255));

        judulKunjunganLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        judulKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/doctor-visit.png"))); // NOI18N
        judulKunjunganLabel.setText("Data Master Manajemen Kunjungan");

        subJudulKunjunganLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        subJudulKunjunganLabel.setText("Kunjungan");

        searchKunjunganTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKunjunganTextFieldKeyPressed(evt);
            }
        });

        searchKunjunganButton.setBackground(new java.awt.Color(0, 0, 153));
        searchKunjunganButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        searchKunjunganButton.setForeground(new java.awt.Color(255, 255, 255));
        searchKunjunganButton.setText("Cari");
        searchKunjunganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchKunjunganButtonActionPerformed(evt);
            }
        });

        searchKunjunganLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        searchKunjunganLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        searchKunjunganLabel.setText("Pencarian Kunjungan");

        javax.swing.GroupLayout searchKunjunganPanelLayout = new javax.swing.GroupLayout(searchKunjunganPanel);
        searchKunjunganPanel.setLayout(searchKunjunganPanelLayout);
        searchKunjunganPanelLayout.setHorizontalGroup(
            searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subJudulKunjunganLabel)
                    .addComponent(judulKunjunganLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(searchKunjunganTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchKunjunganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchKunjunganLabel))
                .addGap(20, 20, 20))
        );
        searchKunjunganPanelLayout.setVerticalGroup(
            searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchKunjunganPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(judulKunjunganLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subJudulKunjunganLabel))
                    .addGroup(searchKunjunganPanelLayout.createSequentialGroup()
                        .addComponent(searchKunjunganLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(searchKunjunganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchKunjunganButton)
                            .addComponent(searchKunjunganTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formInputDataKunjunganPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(metricCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kunjunganButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(searchKunjunganPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kunjunganScrollPane))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kunjunganButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(metricCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formInputDataKunjunganPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kunjunganScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inputNomorRekamMedisTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputNomorRekamMedisTextFieldActionPerformed
    }//GEN-LAST:event_inputNomorRekamMedisTextFieldActionPerformed

    private void inputIdResepTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputIdResepTextFieldActionPerformed
    }//GEN-LAST:event_inputIdResepTextFieldActionPerformed

    private void inputBiayaKonsultasiTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBiayaKonsultasiTextFieldActionPerformed
    }//GEN-LAST:event_inputBiayaKonsultasiTextFieldActionPerformed

    private void inputKeluhanUtamaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputKeluhanUtamaTextFieldActionPerformed
    }//GEN-LAST:event_inputKeluhanUtamaTextFieldActionPerformed

    private void inputHasilPemeriksaanTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHasilPemeriksaanTextFieldActionPerformed
    }//GEN-LAST:event_inputHasilPemeriksaanTextFieldActionPerformed

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        if (action == null) return;

        String id      = inputIdKunjunganTextField.getText().trim();
        String noRm    = extractNomorRekamMedis();
        Date tgl = inputTanggalKunjunganDateChooser.getDate();
        String keluhan = keluhanUtamaArea.getText().trim();

        int jamIdx = jamJadwalComboBox.getSelectedIndex();
        String jam = (jamIdx > 0 && jamIdx - 1 < jadwalList.size())
                ? (String) jadwalList.get(jamIdx - 1)[1] : "";

        if (id.isEmpty() || noRm.isEmpty() || tgl == null || keluhan.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "ID Kunjungan, No. Rekam Medis, Tanggal, dan Keluhan Utama wajib diisi!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jam.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Pilih dokter dan jadwal jam terlebih dahulu.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean rmValid = cachedPasienList.stream()
                .anyMatch(p -> p.getNomorRekamMedis().equals(noRm));
        if (!rmValid) {
            JOptionPane.showMessageDialog(this,
                "Nomor rekam medis tidak valid.\nGunakan kolom pencarian untuk memilih pasien yang terdaftar.",
                "Rekam Medis Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            inputNomorRekamMedisTextField.requestFocus();
            return;
        }

        String selectedDokter = (String) inputPilihDokterComboBox.getSelectedItem();
        String idDokter = null;
        if (selectedDokter != null && !selectedDokter.startsWith("--")) {
            idDokter = selectedDokter.split(" - ")[0];
        }

        String tanggal   = new SimpleDateFormat("yyyy-MM-dd").format(tgl);
        String statusStr = (String) inputStatusKunjunganComboBox.getSelectedItem();

        int opsi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin " + ("add".equals(action) ? "tambah" : "update") + " data kunjungan?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (opsi != JOptionPane.YES_OPTION) return;

        Kunjungan k = new Kunjungan(id, noRm, tanggal, jam, keluhan);
        k.setIdDokter(idDokter);
        k.setStatus(Kunjungan.Status.valueOf(statusStr));

        double biaya = 0;
        try {
            String biayaStr = inputBiayaKonsultasiTextField.getText().trim();
            if (!biayaStr.isEmpty()) biaya = Double.parseDouble(biayaStr);
        } catch (NumberFormatException ex) { biaya = 0; }
        k.setBiayaKonsultasi(biaya);

        if ("add".equals(action)) {
            k.setHasilPemeriksaan(null);
            k.setIdDiagnosa(null);
            k.setIdResep(null);
            kc.insert(k);
            
            String idPasien = findIdPasien(noRm);
            String idPoliklinik = "";
            if (jamIdx > 0 && jamIdx - 1 < jadwalList.size()) {
                String idJadwal = (String) jadwalList.get(jamIdx - 1)[0];
                JadwalDokter jadwal = jdc.search(idJadwal);
                if (jadwal != null) idPoliklinik = jadwal.getIdPoliklinik();
            }
            int nomorUrut = ac.generateNomorUrut(tanggal);
            Antrian antrian = new Antrian(0, nomorUrut, idPasien, idDokter != null ? idDokter : "",
                    idPoliklinik, tanggal, Antrian.JenisKunjungan.BARU);
            ac.insert(antrian);

            JOptionPane.showMessageDialog(this, "Kunjungan berhasil ditambahkan.");
        } else {
            Kunjungan existing = kc.search(selectedId);
            if (existing != null) {
                k.setHasilPemeriksaan(existing.getHasilPemeriksaan());
                k.setIdDiagnosa(existing.getIdDiagnosa());
                k.setIdResep(existing.getIdResep());
            }
            kc.update(k, selectedId);
            JOptionPane.showMessageDialog(this, "Kunjungan berhasil diupdate.");
        }

        if (k.getStatus() == Kunjungan.Status.SELESAI) {
            TagihanControl tc = new TagihanControl();
            ResepControl rc = new ResepControl();
            Resep resep = null;
            if (k.getIdResep() != null && !k.getIdResep().isEmpty()) {
                resep = rc.search(k.getIdResep());
            }
            tc.buatDariKunjungan(k, resep);
        }

        action = null;
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        showKunjungan();
        updateMetrics();
    }//GEN-LAST:event_simpanButtonActionPerformed

    private void tanbahKunjunganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tanbahKunjunganButtonActionPerformed
        action = "add";
        selectedId = null;
        clearForm();
        inputIdKunjunganTextField.setText(kc.generateId());
        inputTanggalKunjunganDateChooser.setDate(new Date());
        inputStatusKunjunganComboBox.setSelectedItem("BELUM_DILAKUKAN");
        setFormEnabled(true);
        setEditDeleteEnabled(false);
    }//GEN-LAST:event_tanbahKunjunganButtonActionPerformed

    private void barukanKunjunganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barukanKunjunganButtonActionPerformed
        if (selectedId == null) return;
        action = "update";
        setFormEnabled(true);
        inputNomorRekamMedisTextField.setEnabled(false);
    }//GEN-LAST:event_barukanKunjunganButtonActionPerformed

    private void hapusKunjunganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusKunjunganButtonActionPerformed
        if (selectedId == null) return;
        int opsi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin hapus kunjungan ini?", "Hapus Data", JOptionPane.YES_NO_OPTION);
        if (opsi != JOptionPane.YES_OPTION) return;
        kc.delete(selectedId);
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        showKunjungan();
        updateMetrics();
        JOptionPane.showMessageDialog(this, "Kunjungan berhasil dihapus.");
    }//GEN-LAST:event_hapusKunjunganButtonActionPerformed

    private void batalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalButtonActionPerformed
        action = null;
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
    }//GEN-LAST:event_batalButtonActionPerformed

    private void searchKunjunganTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKunjunganTextFieldKeyPressed
        if(evt.getKeyChar() == '\n') doSearch();
    }//GEN-LAST:event_searchKunjunganTextFieldKeyPressed

    private void searchKunjunganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchKunjunganButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_searchKunjunganButtonActionPerformed

    private void kunjunganTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kunjunganTableMouseClicked
        int row = kunjunganTable.getSelectedRow();
        if (row < 0) return;
        selectedId = (String) kunjunganTable.getValueAt(row, 0);
        Kunjungan k = kc.search(selectedId);
        if (k != null) {
            fillForm(k);
            setEditDeleteEnabled(true);
            setFormEnabled(false);
            action = null;
        }
    }//GEN-LAST:event_kunjunganTableMouseClicked

    private void inputPilihDokterComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputPilihDokterComboBoxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_inputPilihDokterComboBoxMouseClicked

    private void inputPilihDokterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPilihDokterComboBoxActionPerformed
        String selected = (String) inputPilihDokterComboBox.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            inputBiayaKonsultasiTextField.setText("0");
            jadwalList.clear();
            jamJadwalComboBox.setModel(new DefaultComboBoxModel<>(
                    new String[]{"-- Pilih Dokter Dulu --"}));
            return;
        }
        String idDokter = selected.split(" - ")[0];
        for (Dokter d : dokterList) {
            if (d.getId().equals(idDokter)) {
                inputBiayaKonsultasiTextField.setText(String.valueOf(d.getTarifKonsultasi()));
                break;
            }
        }
        loadJamComboBox(idDokter);
    }//GEN-LAST:event_inputPilihDokterComboBoxActionPerformed

    private void pilihPasienDariSaran(java.awt.event.ActionEvent evt) {
        int idx = -1;
        for (int i = 0; i < jPopupMenu.getComponentCount(); i++) {
            if (jPopupMenu.getComponent(i) == evt.getSource()) { idx = i; break; }
        }
        if (idx < 0 || idx >= autoCompleteResult.size()) return;
        Pasien p = autoCompleteResult.get(idx);
        suppressAutoComplete = true;
        inputNomorRekamMedisTextField.setText(p.getNomorRekamMedis() + " - " + p.getNama());
        suppressAutoComplete = false;
        jPopupMenu.setVisible(false);
        inputNomorRekamMedisTextField.requestFocusInWindow();
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        pilihPasienDariSaran(evt);
    }//GEN-LAST:event_jMenuItem8ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel antrianCardLabel;
    private javax.swing.JPanel antrianCardPanel;
    private javax.swing.JLabel antrianNumber;
    private javax.swing.JButton barukanKunjunganButton;
    private javax.swing.JButton batalButton;
    private javax.swing.JLabel formInputDataKunjunganLabel;
    private javax.swing.JPanel formInputDataKunjunganPanel;
    private javax.swing.JButton hapusKunjunganButton;
    private javax.swing.JLabel inputBiayaKonsultasiLabel;
    private javax.swing.JPanel inputBiayaKonsultasiPanel;
    private javax.swing.JTextField inputBiayaKonsultasiTextField;
    private javax.swing.JLabel inputHasilPemeriksaanLabel;
    private javax.swing.JPanel inputHasilPemeriksaanPanel;
    private javax.swing.JScrollPane inputHasilPemeriksaanScrollPane;
    private javax.swing.JTextField inputHasilPemeriksaanTextField;
    private javax.swing.JLabel inputIdDiagnosaLabel;
    private javax.swing.JPanel inputIdDiagnosaPanel;
    private javax.swing.JTextField inputIdDiagnosaTextField;
    private javax.swing.JLabel inputIdKunjunganLabel;
    private javax.swing.JPanel inputIdKunjunganPanel;
    private javax.swing.JTextField inputIdKunjunganTextField;
    private javax.swing.JLabel inputIdResepLabel;
    private javax.swing.JPanel inputIdResepPanel;
    private javax.swing.JTextField inputIdResepTextField;
    private javax.swing.JLabel inputJamKunjunganLabel;
    private javax.swing.JPanel inputJamKunjunganPanel;
    private javax.swing.JLabel inputKeluhanUtamaLabel;
    private javax.swing.JPanel inputKeluhanUtamaPanel;
    private javax.swing.JScrollPane inputKeluhanUtamaScrollPane;
    private javax.swing.JTextField inputKeluhanUtamaTextField;
    private javax.swing.JLabel inputNomorRekamMedisLabel;
    private javax.swing.JPanel inputNomorRekamMedisPanel;
    private javax.swing.JTextField inputNomorRekamMedisTextField;
    private javax.swing.JComboBox<String> inputPilihDokterComboBox;
    private javax.swing.JLabel inputPilihDokterLabel;
    private javax.swing.JPanel inputPilihDokterPanel;
    private javax.swing.JComboBox<String> inputStatusKunjunganComboBox;
    private javax.swing.JLabel inputStatusKunjunganLabel;
    private javax.swing.JPanel inputStatusKunjunganPanel;
    private com.toedter.calendar.JDateChooser inputTanggalKunjunganDateChooser;
    private javax.swing.JLabel inputTanggalKunjunganLabel;
    private javax.swing.JPanel inputTanggalKunjunganPanel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JLabel judulKunjunganLabel;
    private javax.swing.JLabel kunjunganAntrianNumber;
    private javax.swing.JLabel kunjunganButtonLabel;
    private javax.swing.JPanel kunjunganButtonPanel;
    private javax.swing.JScrollPane kunjunganScrollPane;
    private javax.swing.JLabel kunjunganSelesaiCardLabel;
    private javax.swing.JPanel kunjunganSelesaiCardPanel;
    private javax.swing.JTable kunjunganTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel metricCardPanel;
    private javax.swing.JButton searchKunjunganButton;
    private javax.swing.JLabel searchKunjunganLabel;
    private javax.swing.JPanel searchKunjunganPanel;
    private javax.swing.JTextField searchKunjunganTextField;
    private javax.swing.JButton simpanButton;
    private javax.swing.JLabel subJudulKunjunganLabel;
    private javax.swing.JButton tanbahKunjunganButton;
    private javax.swing.JLabel totalPendapatanCardLabel;
    private javax.swing.JPanel totalPendapatanCardPanel;
    private javax.swing.JLabel totalPendapatanNumber;
    // End of variables declaration//GEN-END:variables
}
