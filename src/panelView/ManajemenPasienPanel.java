/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.PasienControl;
import control.RekamMedisControl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
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

        inputJenisKelaminDropDown.setModel(new DefaultComboBoxModel<>(new String[]{"L", "P"}));

        setFormEnabled(false);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(false);
        showPasien();
    }

    private void showPasien() {
        pasienTable.setModel(pc.showTable(""));
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
            } catch (Exception ex) { 
                inputTanggalLahirDateChooser.setDate(null); 
            }
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
                } catch (Exception ex) { 
                    inputTanggalPembuatanDateChooser.setDate(null); 
                }
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

        selectedId = null;
        setEditDeleteEnabled(false);
        setFormEnabled(false);
        clearForm();

        Pasien byId = pc.search(keyword);
        if (byId != null) {
            pasienTable.setModel(pc.showTable(keyword));
            fillForm(byId);
            selectedId = byId.getId();
            setEditDeleteEnabled(true);
            return;
        }
        
        List<Pasien> byNama = pc.searchByNama(keyword);
        if (byNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pasien tidak ditemukan.", "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pasienTable.setModel(pc.showTable(keyword));
        
        if (byNama.size() == 1) {
            fillForm(byNama.get(0));
            selectedId = byNama.get(0).getId();
            setEditDeleteEnabled(true);
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
        formInputDataPasienPanel = new javax.swing.JPanel();
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
        inputDataPasienLabel = new javax.swing.JLabel();
        formInputRekamMedisPanel = new javax.swing.JPanel();
        inputAlergiPanel = new javax.swing.JPanel();
        inputAlergiLabel = new javax.swing.JLabel();
        inputAlergiTextField = new javax.swing.JTextField();
        inputRiwayatPenyakitPanel = new javax.swing.JPanel();
        inputRiwayatPenyakitLabel = new javax.swing.JLabel();
        inputRiwayatPenyakitTextField = new javax.swing.JTextField();
        inputTanggalPembuatanPanel = new javax.swing.JPanel();
        inputTanggalPembuatanLabel = new javax.swing.JLabel();
        inputTanggalPembuatanDateChooser = new com.toedter.calendar.JDateChooser();
        inputRekamMedisLabel1 = new javax.swing.JLabel();
        pasienScrollPane = new javax.swing.JScrollPane();
        pasienTable = new javax.swing.JTable();
        pasienButtonPanel = new javax.swing.JPanel();
        tambahPasienButton = new javax.swing.JButton();
        barukanPasienButton = new javax.swing.JButton();
        hapusPasienButton = new javax.swing.JButton();
        pencarianPasienPanel = new javax.swing.JPanel();
        judulPasienLabel = new javax.swing.JLabel();
        subJudulPasienLabel = new javax.swing.JLabel();
        pencarianPasienTextField = new javax.swing.JTextField();
        pencarianPasienButton = new javax.swing.JButton();
        pencarianPasienLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));
        mainPanel.setPreferredSize(new java.awt.Dimension(1155, 799));

        formInputDataPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputIdPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputIdPasienLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Keluhan.png"))); // NOI18N
        inputIdPasienLabel.setText("ID Pasien");

        javax.swing.GroupLayout inputIdPasienPanelLayout = new javax.swing.GroupLayout(inputIdPasienPanel);
        inputIdPasienPanel.setLayout(inputIdPasienPanelLayout);
        inputIdPasienPanelLayout.setHorizontalGroup(
            inputIdPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputIdPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputIdPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputIdPasienPanelLayout.createSequentialGroup()
                        .addComponent(inputIdPasienLabel)
                        .addContainerGap(261, Short.MAX_VALUE))
                    .addComponent(inputIdPasienTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
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
        inputNoTeleponLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/telephone.png"))); // NOI18N
        inputNoTeleponLabel.setText("No Telepon");

        javax.swing.GroupLayout inputNoTeleponPanelLayout = new javax.swing.GroupLayout(inputNoTeleponPanel);
        inputNoTeleponPanel.setLayout(inputNoTeleponPanelLayout);
        inputNoTeleponPanelLayout.setHorizontalGroup(
            inputNoTeleponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNoTeleponPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputNoTeleponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputNoTeleponPanelLayout.createSequentialGroup()
                        .addComponent(inputNoTeleponLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputNoTeleponTextField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
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
        inputNomorRekamMedisLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/health-report.png"))); // NOI18N
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
                        .addGap(0, 213, Short.MAX_VALUE)))
                .addContainerGap())
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
        inputJenisKelaminLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/equality.png"))); // NOI18N
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
                        .addGap(0, 250, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inputJenisKelaminPanelLayout.setVerticalGroup(
            inputJenisKelaminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJenisKelaminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputJenisKelaminLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputJenisKelaminDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputTanggalLahirPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputTanggalLahirLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalLahirLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rujukan.png"))); // NOI18N
        inputTanggalLahirLabel.setText("Tanggal Lahir");

        javax.swing.GroupLayout inputTanggalLahirPanelLayout = new javax.swing.GroupLayout(inputTanggalLahirPanel);
        inputTanggalLahirPanel.setLayout(inputTanggalLahirPanelLayout);
        inputTanggalLahirPanelLayout.setHorizontalGroup(
            inputTanggalLahirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputTanggalLahirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTanggalLahirDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                        .addComponent(inputTanggalLahirLabel)
                        .addContainerGap(239, Short.MAX_VALUE))))
        );
        inputTanggalLahirPanelLayout.setVerticalGroup(
            inputTanggalLahirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalLahirPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTanggalLahirLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTanggalLahirDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        inputNamaLengkapPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputNamaLengkapLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputNamaLengkapLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/id-card.png"))); // NOI18N
        inputNamaLengkapLabel.setText("Nama Lengkap");

        javax.swing.GroupLayout inputNamaLengkapPanelLayout = new javax.swing.GroupLayout(inputNamaLengkapPanel);
        inputNamaLengkapPanel.setLayout(inputNamaLengkapPanelLayout);
        inputNamaLengkapPanelLayout.setHorizontalGroup(
            inputNamaLengkapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaLengkapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputNamaLengkapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputNamaLengkapTextField)
                    .addGroup(inputNamaLengkapPanelLayout.createSequentialGroup()
                        .addComponent(inputNamaLengkapLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inputNamaLengkapPanelLayout.setVerticalGroup(
            inputNamaLengkapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputNamaLengkapPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputNamaLengkapLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputNamaLengkapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        inputAlamatPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputAlamatLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputAlamatLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gps.png"))); // NOI18N
        inputAlamatLabel.setText("Alamat");

        javax.swing.GroupLayout inputAlamatPanelLayout = new javax.swing.GroupLayout(inputAlamatPanel);
        inputAlamatPanel.setLayout(inputAlamatPanelLayout);
        inputAlamatPanelLayout.setHorizontalGroup(
            inputAlamatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputAlamatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                        .addComponent(inputAlamatTextField)
                        .addContainerGap())
                    .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                        .addComponent(inputAlamatLabel)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        inputAlamatPanelLayout.setVerticalGroup(
            inputAlamatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlamatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputAlamatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputAlamatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        simpanPasienButton.setBackground(new java.awt.Color(51, 178, 73));
        simpanPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        simpanPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanPasienButton.setText("Simpan");
        simpanPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanPasienButtonActionPerformed(evt);
            }
        });

        batalPasienButton.setBackground(new java.awt.Color(237, 8, 0));
        batalPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        batalPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        batalPasienButton.setText("Batal");
        batalPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalPasienButtonActionPerformed(evt);
            }
        });

        inputDataPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inputDataPasienLabel.setText("Data Pasien");

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
                        .addGap(6, 6, 6)
                        .addComponent(simpanPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(batalPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                        .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                                .addComponent(inputTanggalLahirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputJenisKelaminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                                .addComponent(inputIdPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNomorRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(inputDataPasienLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        formInputDataPasienPanelLayout.setVerticalGroup(
            formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputDataPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputDataPasienLabel)
                .addGap(22, 22, 22)
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
                .addComponent(inputAlamatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formInputDataPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(batalPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(simpanPasienButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        formInputRekamMedisPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputAlergiPanel.setBackground(new java.awt.Color(255, 255, 255));

        inputAlergiLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputAlergiLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/airborne.png"))); // NOI18N
        inputAlergiLabel.setText("Alergi");

        javax.swing.GroupLayout inputAlergiPanelLayout = new javax.swing.GroupLayout(inputAlergiPanel);
        inputAlergiPanel.setLayout(inputAlergiPanelLayout);
        inputAlergiPanelLayout.setHorizontalGroup(
            inputAlergiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputAlergiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputAlergiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputAlergiLabel)
                    .addComponent(inputAlergiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        inputRiwayatPenyakitLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Penyakit.png"))); // NOI18N
        inputRiwayatPenyakitLabel.setText("Riwayat Penyakit");

        javax.swing.GroupLayout inputRiwayatPenyakitPanelLayout = new javax.swing.GroupLayout(inputRiwayatPenyakitPanel);
        inputRiwayatPenyakitPanel.setLayout(inputRiwayatPenyakitPanelLayout);
        inputRiwayatPenyakitPanelLayout.setHorizontalGroup(
            inputRiwayatPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputRiwayatPenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputRiwayatPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputRiwayatPenyakitLabel)
                    .addComponent(inputRiwayatPenyakitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        inputTanggalPembuatanPanel.setPreferredSize(new java.awt.Dimension(346, 61));

        inputTanggalPembuatanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        inputTanggalPembuatanLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rujukan.png"))); // NOI18N
        inputTanggalPembuatanLabel.setText("Tanggal Pembuatan");

        javax.swing.GroupLayout inputTanggalPembuatanPanelLayout = new javax.swing.GroupLayout(inputTanggalPembuatanPanel);
        inputTanggalPembuatanPanel.setLayout(inputTanggalPembuatanPanelLayout);
        inputTanggalPembuatanPanelLayout.setHorizontalGroup(
            inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalPembuatanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTanggalPembuatanLabel)
                    .addComponent(inputTanggalPembuatanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inputTanggalPembuatanPanelLayout.setVerticalGroup(
            inputTanggalPembuatanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputTanggalPembuatanPanelLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(inputTanggalPembuatanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTanggalPembuatanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputRekamMedisLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inputRekamMedisLabel1.setText("Rekam Medis");

        javax.swing.GroupLayout formInputRekamMedisPanelLayout = new javax.swing.GroupLayout(formInputRekamMedisPanel);
        formInputRekamMedisPanel.setLayout(formInputRekamMedisPanelLayout);
        formInputRekamMedisPanelLayout.setHorizontalGroup(
            formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputAlergiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formInputRekamMedisPanelLayout.createSequentialGroup()
                        .addComponent(inputRekamMedisLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inputRiwayatPenyakitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputTanggalPembuatanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                .addContainerGap())
        );
        formInputRekamMedisPanelLayout.setVerticalGroup(
            formInputRekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputRekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputRekamMedisLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputTanggalPembuatanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputAlergiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputRiwayatPenyakitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
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
        pasienTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pasienTableMouseClicked(evt);
            }
        });
        pasienScrollPane.setViewportView(pasienTable);

        pasienButtonPanel.setBackground(new java.awt.Color(255, 255, 255));

        tambahPasienButton.setBackground(new java.awt.Color(51, 178, 73));
        tambahPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tambahPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahPasienButton.setText("Tambah");
        tambahPasienButton.setPreferredSize(new java.awt.Dimension(124, 24));
        tambahPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPasienButtonActionPerformed(evt);
            }
        });

        barukanPasienButton.setBackground(new java.awt.Color(255, 189, 3));
        barukanPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        barukanPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        barukanPasienButton.setText("Barukan");
        barukanPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barukanPasienButtonActionPerformed(evt);
            }
        });

        hapusPasienButton.setBackground(new java.awt.Color(237, 8, 0));
        hapusPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        hapusPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusPasienButton.setText("Hapus");
        hapusPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusPasienButtonActionPerformed(evt);
            }
        });

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

        pencarianPasienPanel.setBackground(new java.awt.Color(255, 255, 255));
        pencarianPasienPanel.setPreferredSize(new java.awt.Dimension(778, 74));

        judulPasienLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        judulPasienLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medical-information.png"))); // NOI18N
        judulPasienLabel.setText("Data Master Manajemen Pasien");

        subJudulPasienLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        subJudulPasienLabel.setText("Pasien");

        pencarianPasienTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pencarianPasienTextFieldKeyPressed(evt);
            }
        });

        pencarianPasienButton.setBackground(new java.awt.Color(0, 0, 153));
        pencarianPasienButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        pencarianPasienButton.setForeground(new java.awt.Color(255, 255, 255));
        pencarianPasienButton.setText("Cari");
        pencarianPasienButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencarianPasienButtonActionPerformed(evt);
            }
        });

        pencarianPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        pencarianPasienLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        pencarianPasienLabel.setText("Pencarian Pasien");

        javax.swing.GroupLayout pencarianPasienPanelLayout = new javax.swing.GroupLayout(pencarianPasienPanel);
        pencarianPasienPanel.setLayout(pencarianPasienPanelLayout);
        pencarianPasienPanelLayout.setHorizontalGroup(
            pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subJudulPasienLabel)
                    .addComponent(judulPasienLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                        .addComponent(pencarianPasienTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pencarianPasienButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pencarianPasienLabel))
                .addGap(20, 20, 20))
        );
        pencarianPasienPanelLayout.setVerticalGroup(
            pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                        .addComponent(judulPasienLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subJudulPasienLabel))
                    .addGroup(pencarianPasienPanelLayout.createSequentialGroup()
                        .addComponent(pencarianPasienLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pencarianPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pencarianPasienButton)
                            .addComponent(pencarianPasienTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pasienScrollPane)
                    .addComponent(pasienButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(formInputDataPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formInputRekamMedisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(pencarianPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pencarianPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pasienButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(formInputRekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formInputDataPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pasienScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tambahPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPasienButtonActionPerformed
        action = "add";
        selectedId = null;
        clearForm();
        inputIdPasienTextField.setText(pc.generateId());
        inputNomorRekamMedisTextField.setText(pc.generateNomorRekamMedis());
        setFormEnabled(true);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(true);
        inputTanggalPembuatanDateChooser.setDate(new java.util.Date());
    }//GEN-LAST:event_tambahPasienButtonActionPerformed

    private void barukanPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barukanPasienButtonActionPerformed
        if (selectedId == null) {
            return;
        }
            action = "update";
            setFormEnabled(true);
            setRekamMedisEnabled(true);
    }//GEN-LAST:event_barukanPasienButtonActionPerformed

    private void hapusPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusPasienButtonActionPerformed
        if (selectedId == null) {
            return;
        }
            int opsi = JOptionPane.showConfirmDialog(this, "Yakin ingin hapus pasien ini?", "Hapus Data", JOptionPane.YES_NO_OPTION);
            if (opsi != JOptionPane.YES_OPTION) {
                return;
            }
            pc.delete(selectedId);
            selectedId = null;
            clearForm();
            setFormEnabled(false);
            setEditDeleteEnabled(false);
            showPasien();

    }//GEN-LAST:event_hapusPasienButtonActionPerformed

    private void batalPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalPasienButtonActionPerformed
        action = null;
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(false);
        tambahPasienButton.setEnabled(true);
    }//GEN-LAST:event_batalPasienButtonActionPerformed

    private void simpanPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanPasienButtonActionPerformed
        if (action == null) {
            return;
        }

        String id    = inputIdPasienTextField.getText().trim();
        String noRm  = inputNomorRekamMedisTextField.getText().trim();
        String nama  = inputNamaLengkapTextField.getText().trim();
        String jk    = (String) inputJenisKelaminDropDown.getSelectedItem();
        String telp  = inputNoTeleponTextField.getText().trim();
        String alamat = inputAlamatTextField.getText().trim();
        Date tglLahir = inputTanggalLahirDateChooser.getDate();

        if (id.isEmpty() || noRm.isEmpty() || nama.isEmpty() || tglLahir == null || telp.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!telp.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Nomor telepon hanya boleh angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tgl = new SimpleDateFormat("yyyy-MM-dd").format(tglLahir);

        int opsi = JOptionPane.showConfirmDialog(this, "Yakin ingin " + action + " data pasien?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (opsi != JOptionPane.YES_OPTION) {
            return;
        }

        Pasien p = new Pasien(id, noRm, nama, tgl, jk, telp, alamat);
        if ("add".equals(action)) {
            pc.insert(p);
            Date tglRm = inputTanggalPembuatanDateChooser.getDate();
            String tglRmStr = tglRm != null ? new SimpleDateFormat("yyyy-MM-dd").format(tglRm) : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            RekamMedis rm = new RekamMedis(noRm, id, tglRmStr);
            String alergi  = inputAlergiTextField.getText().trim();
            String riwayat = inputRiwayatPenyakitTextField.getText().trim();
            if (!alergi.isEmpty()) {
                rm.tambahAlergi(alergi);
            }
            if (!riwayat.isEmpty()) {
                rm.tambahRiwayatPenyakit(riwayat);
            }
            rmc.insert(rm);

        } else {
            pc.update(p, selectedId);
            Date tglRm = inputTanggalPembuatanDateChooser.getDate();
            String tglRmStr = tglRm != null ? new SimpleDateFormat("yyyy-MM-dd").format(tglRm) : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            RekamMedis rm   = new RekamMedis(noRm, id, tglRmStr);
            String alergi   = inputAlergiTextField.getText().trim();
            String riwayat  = inputRiwayatPenyakitTextField.getText().trim();
            for (String a : alergi.split(",\\s*")) { 
                if (!a.isEmpty()) {
                    rm.tambahAlergi(a); 
                }
            }
            for (String r : riwayat.split(",\\s*")) { 
                if (!r.isEmpty()) {
                    rm.tambahRiwayatPenyakit(r); 
                }
            }
            rmc.update(rm, noRm);
        }

        action = null;
        selectedId = null;
        clearForm();
        setFormEnabled(false);
        setEditDeleteEnabled(false);
        setRekamMedisEnabled(false);
        showPasien();
    }//GEN-LAST:event_simpanPasienButtonActionPerformed

    private void pencarianPasienTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pencarianPasienTextFieldKeyPressed
        if(evt.getKeyChar() == '\n') {
            doSearch();
        }
    }//GEN-LAST:event_pencarianPasienTextFieldKeyPressed

    private void pencarianPasienButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencarianPasienButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_pencarianPasienButtonActionPerformed

    private void pasienTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasienTableMouseClicked
        int row = pasienTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        selectedId = (String) pasienTable.getValueAt(row, 0);
        Pasien p = pc.search(selectedId);
        if (p != null) {
            fillForm(p);
            setEditDeleteEnabled(true);
            setFormEnabled(false);
            setRekamMedisEnabled(false);
            action = null;
        }
    }//GEN-LAST:event_pasienTableMouseClicked


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
    private javax.swing.JLabel inputRekamMedisLabel1;
    private javax.swing.JLabel inputRiwayatPenyakitLabel;
    private javax.swing.JPanel inputRiwayatPenyakitPanel;
    private javax.swing.JTextField inputRiwayatPenyakitTextField;
    private com.toedter.calendar.JDateChooser inputTanggalLahirDateChooser;
    private javax.swing.JLabel inputTanggalLahirLabel;
    private javax.swing.JPanel inputTanggalLahirPanel;
    private com.toedter.calendar.JDateChooser inputTanggalPembuatanDateChooser;
    private javax.swing.JLabel inputTanggalPembuatanLabel;
    private javax.swing.JPanel inputTanggalPembuatanPanel;
    private javax.swing.JLabel judulPasienLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pasienButtonPanel;
    private javax.swing.JScrollPane pasienScrollPane;
    private javax.swing.JTable pasienTable;
    private javax.swing.JButton pencarianPasienButton;
    private javax.swing.JLabel pencarianPasienLabel;
    private javax.swing.JPanel pencarianPasienPanel;
    private javax.swing.JTextField pencarianPasienTextField;
    private javax.swing.JButton simpanPasienButton;
    private javax.swing.JLabel subJudulPasienLabel;
    private javax.swing.JButton tambahPasienButton;
    // End of variables declaration//GEN-END:variables
}
