/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.PasienControl;
import control.RekamMedisControl;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Pasien;
import model.RekamMedis;

public class ManajemenPasienPanel extends javax.swing.JPanel {

    private final PasienControl pc = new PasienControl();
    private final RekamMedisControl rmc = new RekamMedisControl();
    private String action = null;
    private String selectedId = null;

    public ManajemenPasienPanel() {
        initComponents();
        setOpaque(false);

        inputJenisKelaminDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"L", "P"}));

        ((javax.swing.text.AbstractDocument) inputNoTeleponTextField.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int off, String str,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (str != null && str.matches("[0-9]+")) super.insertString(fb, off, str, a);
                }
                @Override
                public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int off, int len, String str,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (str == null || str.matches("[0-9]*")) super.replace(fb, off, len, str, a);
                }
            });

        setupTable();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(false);
        showPasien();

        tambahPasienButton.addActionListener(e -> {
            action = "add";
            selectedId = null;
            clearForm();
            inputIdPasienTextField.setText(pc.generateId());
            inputNomorRekamMedisTextField.setText(pc.generateNomorRekamMedis());
            setFormEnabled(true);
            setEditDeleteEnabled(false);
            setRekamMedisEnabled(true);
            inputTanggalPembuatanDateChooser.setDate(new java.util.Date());
        });

        barukanPasienButton.addActionListener(e -> {
            if (selectedId == null) return;
            action = "update";
            setFormEnabled(true);
            setRekamMedisEnabled(true);
        });

        hapusPasienButton.addActionListener(e -> {
            if (selectedId == null) return;
            int opsi = JOptionPane.showConfirmDialog(this, "Yakin ingin hapus pasien ini?", "Hapus Data", JOptionPane.YES_NO_OPTION);
            if (opsi != JOptionPane.YES_OPTION) return;
            pc.delete(selectedId);
            selectedId = null;
            clearForm();
            setFormEnabled(false);
            setEditDeleteEnabled(false);
            showPasien();
            JOptionPane.showMessageDialog(this, "Pasien berhasil dihapus.");
        });

        simpanPasienButton.addActionListener(e -> simpanPasien());

        batalPasienButton.addActionListener(e -> {
            action = null;
            selectedId = null;
            clearForm();
            setFormEnabled(false);
            setEditDeleteEnabled(false);
            setRekamMedisEnabled(false);
        });

        pencarianPasienButton.addActionListener(e -> doSearch());
        pencarianPasienTextField.addActionListener(e -> doSearch());

        pasienTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = pasienTable.getSelectedRow();
            if (row < 0) return;
            selectedId = (String) pasienTable.getValueAt(row, 0);
            Pasien p = pc.search(selectedId);
            if (p != null) {
                fillForm(p);
                setEditDeleteEnabled(true);
                setFormEnabled(false);
                setRekamMedisEnabled(false);
                action = null;
            }
        });
    }

    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Pasien", "No. Rekam Medis", "Nama", "Tgl Lahir", "J/K", "No. Telepon", "Alamat"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        pasienTable.setModel(model);
    }

    private void showPasien() {
        DefaultTableModel model = (DefaultTableModel) pasienTable.getModel();
        model.setRowCount(0);
        for (Pasien p : pc.showData()) {
            model.addRow(new Object[]{
                p.getId(), p.getNomorRekamMedis(), p.getNama(),
                p.getTanggalLahir(), p.getJenisKelamin(), p.getNoTelepon(), p.getAlamat()
            });
        }
    }

    private void setFormEnabled(boolean value) {
        inputIdPasienTextField.setEnabled(false);
        inputNomorRekamMedisTextField.setEnabled(false);
        inputNamaLengkapTextField.setEnabled(value);
        inputTanggalLahirDateChooser.setEnabled(value);
        inputJenisKelaminDropDown.setEnabled(value);
        inputNoTeleponTextField.setEnabled(value);
        inputAlamatTextField.setEnabled(value);
        simpanPasienButton.setEnabled(value);
        batalPasienButton.setEnabled(value);
    }

    private void setEditDeleteEnabled(boolean value) {
        barukanPasienButton.setEnabled(value);
        hapusPasienButton.setEnabled(value);
    }

    private void setRekamMedisEnabled(boolean value) {
        inputAlergiTextField.setEnabled(value);
        inputRiwayatPenyakitTextField.setEnabled(value);
        inputTanggalPembuatanDateChooser.setEnabled(false);
    }

    private void clearForm() {
        inputIdPasienTextField.setText("");
        inputNomorRekamMedisTextField.setText("");
        inputNamaLengkapTextField.setText("");
        inputTanggalLahirDateChooser.setDate(null);
        inputJenisKelaminDropDown.setSelectedIndex(0);
        inputNoTeleponTextField.setText("");
        inputAlamatTextField.setText("");
        inputAlergiTextField.setText("");
        inputRiwayatPenyakitTextField.setText("");
        inputTanggalPembuatanDateChooser.setDate(null);
    }

    private void fillForm(Pasien p) {
        inputIdPasienTextField.setText(p.getId());
        inputNomorRekamMedisTextField.setText(p.getNomorRekamMedis());
        inputNamaLengkapTextField.setText(p.getNama());
        if (p.getTanggalLahir() != null && !p.getTanggalLahir().isEmpty()) {
            try {
                inputTanggalLahirDateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(p.getTanggalLahir()));
            } catch (Exception ex) { inputTanggalLahirDateChooser.setDate(null); }
        }
        inputJenisKelaminDropDown.setSelectedItem(p.getJenisKelamin());
        inputNoTeleponTextField.setText(p.getNoTelepon());
        inputAlamatTextField.setText(p.getAlamat());

        inputAlergiTextField.setText("");
        inputRiwayatPenyakitTextField.setText("");
        inputTanggalPembuatanDateChooser.setDate(null);
        RekamMedis rm = rmc.search(p.getNomorRekamMedis());
        if (rm != null) {
            inputAlergiTextField.setText(String.join(", ", rm.getAlergi()));
            inputRiwayatPenyakitTextField.setText(String.join(", ", rm.getRiwayatPenyakit()));
            if (rm.getTanggalBuat() != null && !rm.getTanggalBuat().isEmpty()) {
                try {
                    inputTanggalPembuatanDateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(rm.getTanggalBuat()));
                } catch (Exception ex) { inputTanggalPembuatanDateChooser.setDate(null); }
            }
        }
    }

    private void doSearch() {
        String keyword = pencarianPasienTextField.getText().trim();
        if (keyword.isEmpty()) {
            showPasien();
            clearForm();
            selectedId = null;
            action = null;
            setFormEnabled(false);
            setEditDeleteEnabled(false);
            setRekamMedisEnabled(false);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) pasienTable.getModel();
        model.setRowCount(0);
        selectedId = null;
        setEditDeleteEnabled(false);
        setFormEnabled(false);
        clearForm();

        // Coba exact match by ID dulu
        Pasien byId = pc.search(keyword);
        if (byId != null) {
            model.addRow(new Object[]{
                byId.getId(), byId.getNomorRekamMedis(), byId.getNama(),
                byId.getTanggalLahir(), byId.getJenisKelamin(), byId.getNoTelepon(), byId.getAlamat()
            });
            fillForm(byId);
            selectedId = byId.getId();
            setEditDeleteEnabled(true);
            return;
        }

        // Kalau tidak ketemu by ID, cari by nama (LIKE)
        List<Pasien> byNama = pc.searchByNama(keyword);
        if (byNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pasien tidak ditemukan.", "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Pasien p : byNama) {
            model.addRow(new Object[]{
                p.getId(), p.getNomorRekamMedis(), p.getNama(),
                p.getTanggalLahir(), p.getJenisKelamin(), p.getNoTelepon(), p.getAlamat()
            });
        }
        // Jika hanya satu hasil, langsung isi form
        if (byNama.size() == 1) {
            fillForm(byNama.get(0));
            selectedId = byNama.get(0).getId();
            setEditDeleteEnabled(true);
        }
    }

    private void simpanPasien() {
        if (action == null) return;

        String id    = inputIdPasienTextField.getText().trim();
        String noRm  = inputNomorRekamMedisTextField.getText().trim();
        String nama  = inputNamaLengkapTextField.getText().trim();
        String jk    = (String) inputJenisKelaminDropDown.getSelectedItem();
        String telp  = inputNoTeleponTextField.getText().trim();
        String alamat = inputAlamatTextField.getText().trim();
        java.util.Date tglLahir = inputTanggalLahirDateChooser.getDate();

        if (id.isEmpty() || noRm.isEmpty() || nama.isEmpty() || tglLahir == null || telp.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tgl = new SimpleDateFormat("yyyy-MM-dd").format(tglLahir);

        int opsi = JOptionPane.showConfirmDialog(this, "Yakin ingin " + action + " data pasien?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (opsi != JOptionPane.YES_OPTION) return;

        Pasien p = new Pasien(id, noRm, nama, tgl, jk, telp, alamat);
        if ("add".equals(action)) {
            pc.insert(p);
            java.util.Date tglRm = inputTanggalPembuatanDateChooser.getDate();
            String tglRmStr = tglRm != null
                ? new SimpleDateFormat("yyyy-MM-dd").format(tglRm)
                : new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            RekamMedis rm = new RekamMedis(noRm, id, tglRmStr);
            String alergi  = inputAlergiTextField.getText().trim();
            String riwayat = inputRiwayatPenyakitTextField.getText().trim();
            if (!alergi.isEmpty())  rm.tambahAlergi(alergi);
            if (!riwayat.isEmpty()) rm.tambahRiwayatPenyakit(riwayat);
            rmc.insert(rm);
            JOptionPane.showMessageDialog(this, "Pasien dan rekam medis berhasil ditambahkan.");
        } else {
            pc.update(p, selectedId);
            java.util.Date tglRm = inputTanggalPembuatanDateChooser.getDate();
            String tglRmStr = tglRm != null
                ? new SimpleDateFormat("yyyy-MM-dd").format(tglRm)
                : new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            RekamMedis rm = new RekamMedis(noRm, id, tglRmStr);
            String alergi  = inputAlergiTextField.getText().trim();
            String riwayat = inputRiwayatPenyakitTextField.getText().trim();
            for (String a : alergi.split(",\\s*"))  { if (!a.isEmpty()) rm.tambahAlergi(a); }
            for (String r : riwayat.split(",\\s*")) { if (!r.isEmpty()) rm.tambahRiwayatPenyakit(r); }
            rmc.update(rm, noRm);
            JOptionPane.showMessageDialog(this, "Pasien dan rekam medis berhasil diupdate.");
        }

        action = null;
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(false);
        showPasien();
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
        pencarianPasienPanel = new javax.swing.JPanel();
        pencarianPasienLabel = new javax.swing.JLabel();
        pencarianPasienTextField = new javax.swing.JTextField();
        pencarianPasienButton = new javax.swing.JButton();
        formInputDataPasienPanel = new javax.swing.JPanel();
        inputDataPasienLabel = new javax.swing.JLabel();
        inputIdPasienPanel = new javax.swing.JPanel();
        inputIdPasienLabel = new javax.swing.JLabel();
        inputIdPasienTextField = new javax.swing.JTextField();
        inputNoTeleponPanel = new javax.swing.JPanel();
        inputNoTeleponLabel = new javax.swing.JLabel();
        inputNoTeleponTextField = new javax.swing.JTextField();
        inputNomorRekamMedisPanel = new javax.swing.JPanel();
        inputNomorRekamMedisLabel = new javax.swing.JLabel();
        inputNomorRekamMedisTextField = new javax.swing.JTextField();
        inputJenisKelaminPanel = new javax.swing.JPanel();
        inputJenisKelaminLabel = new javax.swing.JLabel();
        inputJenisKelaminDropDown = new javax.swing.JComboBox<>();
        inputTanggalLahirPanel = new javax.swing.JPanel();
        inputTanggalLahirLabel = new javax.swing.JLabel();
        inputTanggalLahirDateChooser = new com.toedter.calendar.JDateChooser();
        inputNamaLengkapPanel = new javax.swing.JPanel();
        inputNamaLengkapLabel = new javax.swing.JLabel();
        inputNamaLengkapTextField = new javax.swing.JTextField();
        inputAlamatPanel = new javax.swing.JPanel();
        inputAlamatLabel = new javax.swing.JLabel();
        inputAlamatTextField = new javax.swing.JTextField();
        simpanPasienButton = new javax.swing.JButton();
        batalPasienButton = new javax.swing.JButton();
        formInputRekamMedisPanel = new javax.swing.JPanel();
        inputRekamMedisLabel = new javax.swing.JLabel();
        inputAlergiPanel = new javax.swing.JPanel();
        inputAlergiLabel = new javax.swing.JLabel();
        inputAlergiTextField = new javax.swing.JTextField();
        inputRiwayatPenyakitPanel = new javax.swing.JPanel();
        inputRiwayatPenyakitLabel = new javax.swing.JLabel();
        inputRiwayatPenyakitTextField = new javax.swing.JTextField();
        inputTanggalPembuatanPanel = new javax.swing.JPanel();
        inputTanggalPembuatanLabel = new javax.swing.JLabel();
        inputTanggalPembuatanDateChooser = new com.toedter.calendar.JDateChooser();
        pasienScrollPane = new javax.swing.JScrollPane();
        pasienTable = new javax.swing.JTable();
        pasienButtonPanel = new javax.swing.JPanel();
        tambahPasienButton = new javax.swing.JButton();
        barukanPasienButton = new javax.swing.JButton();
        hapusPasienButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));

        pencarianPasienPanel.setBackground(new java.awt.Color(255, 255, 255));
        pencarianPasienPanel.setPreferredSize(new java.awt.Dimension(800, 70));

        pencarianPasienLabel.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 18)); // NOI18N
        pencarianPasienLabel.setText("Pencarian Pasien");

        pencarianPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        pencarianPasienButton.setText("Cari");

        javax.swing.GroupLayout pencarianPasienPanelLayout = new javax.swing.GroupLayout(pencarianPasienPanel);
        pencarianPasienPanel.setLayout(pencarianPasienPanelLayout);
        pencarianPasienPanelLayout.setHorizontalGroup(
            pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pencarianPasienLabel)
                    .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                        .addComponent(pencarianPasienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pencarianPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        pencarianPasienPanelLayout.setVerticalGroup(
            pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianPasienLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pencarianPasienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pencarianPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        formInputDataPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputDataPasienLabel.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 18)); // NOI18N
        inputDataPasienLabel.setText("Data Pasien");

        inputIdPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdPasienLabel.setText("ID Pasien");

        javax.swing.GroupLayout inputIdPasienPanelLayout = new javax.swing.GroupLayout(inputIdPasienPanel);
        inputIdPasienPanel.setLayout(inputIdPasienPanelLayout);
        inputIdPasienPanelLayout.setHorizontalGroup(
            inputIdPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdPasienLabel)
                .addContainerGap(261, Short.MAX_VALUE))
            .addComponent(inputIdPasienTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        inputIdPasienPanelLayout.setVerticalGroup(
            inputIdPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputIdPasienLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputIdPasienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputNoTeleponPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNoTeleponLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputNoTeleponLabel.setText("No Telepon");

        javax.swing.GroupLayout inputNoTeleponPanelLayout = new javax.swing.GroupLayout(inputNoTeleponPanel);
        inputNoTeleponPanel.setLayout(inputNoTeleponPanelLayout);
        inputNoTeleponPanelLayout.setHorizontalGroup(
            inputNoTeleponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNoTeleponPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNoTeleponLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputNoTeleponTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        inputNoTeleponPanelLayout.setVerticalGroup(
            inputNoTeleponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNoTeleponPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNoTeleponLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNoTeleponTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputNomorRekamMedisPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNomorRekamMedisLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputNomorRekamMedisLabel.setText("Nomor Rekam Medis");

        javax.swing.GroupLayout inputNomorRekamMedisPanelLayout = new javax.swing.GroupLayout(inputNomorRekamMedisPanel);
        inputNomorRekamMedisPanel.setLayout(inputNomorRekamMedisPanelLayout);
        inputNomorRekamMedisPanelLayout.setHorizontalGroup(
            inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputNomorRekamMedisTextField)
                    .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                        .addComponent(inputNomorRekamMedisLabel)
                        .addContainerGap(219, Short.MAX_VALUE))))
        );
        inputNomorRekamMedisPanelLayout.setVerticalGroup(
            inputNomorRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNomorRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNomorRekamMedisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNomorRekamMedisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputJenisKelaminPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputJenisKelaminLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputJenisKelaminLabel.setText("Jenis Kelamin");

        inputJenisKelaminDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout inputJenisKelaminPanelLayout = new javax.swing.GroupLayout(inputJenisKelaminPanel);
        inputJenisKelaminPanel.setLayout(inputJenisKelaminPanelLayout);
        inputJenisKelaminPanelLayout.setHorizontalGroup(
            inputJenisKelaminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJenisKelaminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputJenisKelaminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputJenisKelaminDropDown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(inputJenisKelaminPanelLayout.createSequentialGroup()
                        .addComponent(inputJenisKelaminLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        inputJenisKelaminPanelLayout.setVerticalGroup(
            inputJenisKelaminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJenisKelaminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputJenisKelaminLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputJenisKelaminDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputTanggalLahirPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputTanggalLahirLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalLahirLabel.setText("Tanggal Lahir");

        javax.swing.GroupLayout inputTanggalLahirPanelLayout = new javax.swing.GroupLayout(inputTanggalLahirPanel);
        inputTanggalLahirPanel.setLayout(inputTanggalLahirPanelLayout);
        inputTanggalLahirPanelLayout.setHorizontalGroup(
            inputTanggalLahirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalLahirLabel)
                .addContainerGap(239, Short.MAX_VALUE))
            .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                .addComponent(inputTanggalLahirDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        inputTanggalLahirPanelLayout.setVerticalGroup(
            inputTanggalLahirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalLahirLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTanggalLahirDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputNamaLengkapPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNamaLengkapLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputNamaLengkapLabel.setText("Nama Lengkap");

        javax.swing.GroupLayout inputNamaLengkapPanelLayout = new javax.swing.GroupLayout(inputNamaLengkapPanel);
        inputNamaLengkapPanel.setLayout(inputNamaLengkapPanelLayout);
        inputNamaLengkapPanelLayout.setHorizontalGroup(
            inputNamaLengkapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaLengkapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNamaLengkapLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputNamaLengkapTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        inputNamaLengkapPanelLayout.setVerticalGroup(
            inputNamaLengkapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaLengkapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputNamaLengkapLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNamaLengkapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputAlamatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputAlamatLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputAlamatLabel.setText("Alamat");

        javax.swing.GroupLayout inputAlamatPanelLayout = new javax.swing.GroupLayout(inputAlamatPanel);
        inputAlamatPanel.setLayout(inputAlamatPanelLayout);
        inputAlamatPanelLayout.setHorizontalGroup(
            inputAlamatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputAlamatLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(inputAlamatTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        inputAlamatPanelLayout.setVerticalGroup(
            inputAlamatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputAlamatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAlamatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        simpanPasienButton.setBackground(new java.awt.Color(51, 178, 73));
        simpanPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        simpanPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanPasienButton.setText("Simpan");

        batalPasienButton.setBackground(new java.awt.Color(237, 8, 0));
        batalPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        batalPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        batalPasienButton.setText("Batal");

        javax.swing.GroupLayout formInputDataPasienPanelLayout = new javax.swing.GroupLayout(formInputDataPasienPanel);
        formInputDataPasienPanel.setLayout(formInputDataPasienPanelLayout);
        formInputDataPasienPanelLayout.setHorizontalGroup(
            formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputNoTeleponPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputNamaLengkapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputAlamatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                                .addComponent(inputTanggalLahirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputJenisKelaminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(inputDataPasienLabel)
                            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                                .addComponent(simpanPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(batalPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                                .addComponent(inputIdPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNomorRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        formInputDataPasienPanelLayout.setVerticalGroup(
            formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputDataPasienLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputIdPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputNomorRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaLengkapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTanggalLahirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputJenisKelaminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNoTeleponPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAlamatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpanPasienButton)
                    .addComponent(batalPasienButton))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        formInputRekamMedisPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputRekamMedisLabel.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 18)); // NOI18N
        inputRekamMedisLabel.setText("Input Rekam Medis");

        inputAlergiPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputAlergiLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputAlergiLabel.setText("Alergi");

        javax.swing.GroupLayout inputAlergiPanelLayout = new javax.swing.GroupLayout(inputAlergiPanel);
        inputAlergiPanel.setLayout(inputAlergiPanelLayout);
        inputAlergiPanelLayout.setHorizontalGroup(
            inputAlergiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlergiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputAlergiLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(inputAlergiPanelLayout.createSequentialGroup()
                .addComponent(inputAlergiTextField)
                .addContainerGap())
        );
        inputAlergiPanelLayout.setVerticalGroup(
            inputAlergiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlergiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputAlergiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAlergiTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap())
        );

        inputRiwayatPenyakitPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputRiwayatPenyakitLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputRiwayatPenyakitLabel.setText("Riwayat Penyakit");

        javax.swing.GroupLayout inputRiwayatPenyakitPanelLayout = new javax.swing.GroupLayout(inputRiwayatPenyakitPanel);
        inputRiwayatPenyakitPanel.setLayout(inputRiwayatPenyakitPanelLayout);
        inputRiwayatPenyakitPanelLayout.setHorizontalGroup(
            inputRiwayatPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputRiwayatPenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputRiwayatPenyakitLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(inputRiwayatPenyakitPanelLayout.createSequentialGroup()
                .addComponent(inputRiwayatPenyakitTextField)
                .addContainerGap())
        );
        inputRiwayatPenyakitPanelLayout.setVerticalGroup(
            inputRiwayatPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputRiwayatPenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputRiwayatPenyakitLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputRiwayatPenyakitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputTanggalPembuatanPanel.setBackground(new java.awt.Color(255, 255, 255));
        inputTanggalPembuatanPanel.setPreferredSize(new java.awt.Dimension(47, 90));

        inputTanggalPembuatanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalPembuatanLabel.setText("Tanggal Pembuatan");

        javax.swing.GroupLayout inputTanggalPembuatanPanelLayout = new javax.swing.GroupLayout(inputTanggalPembuatanPanel);
        inputTanggalPembuatanPanel.setLayout(inputTanggalPembuatanPanelLayout);
        inputTanggalPembuatanPanelLayout.setHorizontalGroup(
            inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalPembuatanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputTanggalPembuatanPanelLayout.createSequentialGroup()
                        .addComponent(inputTanggalPembuatanLabel)
                        .addGap(0, 306, Short.MAX_VALUE))
                    .addComponent(inputTanggalPembuatanDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        inputTanggalPembuatanPanelLayout.setVerticalGroup(
            inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalPembuatanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalPembuatanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTanggalPembuatanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formInputRekamMedisPanelLayout = new javax.swing.GroupLayout(formInputRekamMedisPanel);
        formInputRekamMedisPanel.setLayout(formInputRekamMedisPanelLayout);
        formInputRekamMedisPanelLayout.setHorizontalGroup(
            formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputRekamMedisLabel)
                    .addGroup(formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(inputTanggalPembuatanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                        .addComponent(inputAlergiPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputRiwayatPenyakitPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formInputRekamMedisPanelLayout.setVerticalGroup(
            formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputRekamMedisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTanggalPembuatanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAlergiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputRiwayatPenyakitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pasienTable.setModel(new javax.swing.table.DefaultTableModel(
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
        pasienScrollPane.setViewportView(pasienTable);

        pasienButtonPanel.setBackground(new java.awt.Color(255, 255, 255));

        tambahPasienButton.setBackground(new java.awt.Color(51, 178, 73));
        tambahPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        tambahPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahPasienButton.setText("Tambah");
        tambahPasienButton.setPreferredSize(new java.awt.Dimension(124, 24));
        tambahPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPasienButtonActionPerformed(evt);
            }
        });

        barukanPasienButton.setBackground(new java.awt.Color(255, 189, 3));
        barukanPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        barukanPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        barukanPasienButton.setText("Barukan");

        hapusPasienButton.setBackground(new java.awt.Color(237, 8, 0));
        hapusPasienButton.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 12)); // NOI18N
        hapusPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusPasienButton.setText("Hapus");

        javax.swing.GroupLayout pasienButtonPanelLayout = new javax.swing.GroupLayout(pasienButtonPanel);
        pasienButtonPanel.setLayout(pasienButtonPanelLayout);
        pasienButtonPanelLayout.setHorizontalGroup(
            pasienButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pasienButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barukanPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pasienButtonPanelLayout.setVerticalGroup(
            pasienButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pasienButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pasienButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tambahPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(barukanPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hapusPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pasienButtonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pencarianPasienPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1269, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pasienScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(formInputDataPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(formInputRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(132, 132, 132))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pasienButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formInputDataPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formInputRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pasienScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tambahPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPasienButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambahPasienButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barukanPasienButton;
    private javax.swing.JButton batalPasienButton;
    private javax.swing.JPanel formInputDataPasienPanel;
    private javax.swing.JPanel formInputRekamMedisPanel;
    private javax.swing.JButton hapusPasienButton;
    private javax.swing.JLabel inputAlamatLabel;
    private javax.swing.JPanel inputAlamatPanel;
    private javax.swing.JTextField inputAlamatTextField;
    private javax.swing.JLabel inputAlergiLabel;
    private javax.swing.JPanel inputAlergiPanel;
    private javax.swing.JTextField inputAlergiTextField;
    private javax.swing.JLabel inputDataPasienLabel;
    private javax.swing.JLabel inputIdPasienLabel;
    private javax.swing.JPanel inputIdPasienPanel;
    private javax.swing.JTextField inputIdPasienTextField;
    private javax.swing.JComboBox<String> inputJenisKelaminDropDown;
    private javax.swing.JLabel inputJenisKelaminLabel;
    private javax.swing.JPanel inputJenisKelaminPanel;
    private javax.swing.JLabel inputNamaLengkapLabel;
    private javax.swing.JPanel inputNamaLengkapPanel;
    private javax.swing.JTextField inputNamaLengkapTextField;
    private javax.swing.JLabel inputNoTeleponLabel;
    private javax.swing.JPanel inputNoTeleponPanel;
    private javax.swing.JTextField inputNoTeleponTextField;
    private javax.swing.JLabel inputNomorRekamMedisLabel;
    private javax.swing.JPanel inputNomorRekamMedisPanel;
    private javax.swing.JTextField inputNomorRekamMedisTextField;
    private javax.swing.JLabel inputRekamMedisLabel;
    private javax.swing.JLabel inputRiwayatPenyakitLabel;
    private javax.swing.JPanel inputRiwayatPenyakitPanel;
    private javax.swing.JTextField inputRiwayatPenyakitTextField;
    private com.toedter.calendar.JDateChooser inputTanggalLahirDateChooser;
    private javax.swing.JLabel inputTanggalLahirLabel;
    private javax.swing.JPanel inputTanggalLahirPanel;
    private com.toedter.calendar.JDateChooser inputTanggalPembuatanDateChooser;
    private javax.swing.JLabel inputTanggalPembuatanLabel;
    private javax.swing.JPanel inputTanggalPembuatanPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pasienButtonPanel;
    private javax.swing.JScrollPane pasienScrollPane;
    private javax.swing.JTable pasienTable;
    private javax.swing.JButton pencarianPasienButton;
    private javax.swing.JLabel pencarianPasienLabel;
    private javax.swing.JPanel pencarianPasienPanel;
    private javax.swing.JTextField pencarianPasienTextField;
    private javax.swing.JButton simpanPasienButton;
    private javax.swing.JButton tambahPasienButton;
    // End of variables declaration//GEN-END:variables
}
