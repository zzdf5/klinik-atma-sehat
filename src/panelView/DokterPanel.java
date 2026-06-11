/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panelView;

import control.*;
import model.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Component;
import java.awt.BorderLayout;
import table.TableItemResep;

public class DokterPanel extends javax.swing.JPanel {
    
    private KunjunganControl kunjunganControl;
    private PasienControl pasienControl;
    private ObatControl obatControl;
    private DiagnosaControl diagnosaControl;
    private ResepControl resepControl;
    private RujukanControl rujukanControl;
    private RekamMedisControl rekamMedisControl;
    
    private Kunjungan selectedKunjungan = null;
    private Pasien selectedPasien = null;
    private List<Resep.ItemResep> daftarObatResep = new ArrayList<>();
    private List<JPanel> patientCards = new ArrayList<>();
    private JPanel queueContainer = null;
    
    private String idDokterLogin;

    public DokterPanel(String idDokter) {
        this.idDokterLogin = idDokter;
        initializeControllers();
        queueContainer = new JPanel();
        queueContainer.setLayout(null);
        queueContainer.setBackground(new java.awt.Color(255, 255, 255));
        initComponents();
        setupQueuePanel();
        loadQueuePasien();
    }
    
    private static final int CARD_HEIGHT = 145;
    private static final int CARD_GAP   = 10;

    private void setupQueuePanel() {
        diplayQueuePasienPanel.removeAll();
        diplayQueuePasienPanel.setLayout(new BorderLayout());
        diplayQueuePasienPanel.setBackground(java.awt.Color.WHITE);

        queueContainer.setBackground(new java.awt.Color(245, 246, 250));
        queueContainer.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(queueContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new java.awt.Color(245, 246, 250));

        diplayQueuePasienPanel.add(scrollPane, BorderLayout.CENTER);

        // Tampilkan tepat 3 card; card lebih banyak bisa discroll
        int visibleHeight = CARD_GAP + 4 * (CARD_HEIGHT + CARD_GAP);
        diplayQueuePasienPanel.setPreferredSize(new java.awt.Dimension(205, visibleHeight));

        diplayQueuePasienPanel.revalidate();
        diplayQueuePasienPanel.repaint();
    }
    
    public DokterPanel() {
        this("DOK-001");
    }
    
    private void initializeControllers() {
        kunjunganControl = new KunjunganControl();
        pasienControl = new PasienControl();
        obatControl = new ObatControl();
        diagnosaControl = new DiagnosaControl();
        resepControl = new ResepControl();
        rujukanControl = new RujukanControl();
        rekamMedisControl = new RekamMedisControl();
    }
    
    private void loadQueuePasien() {
        clearForm();

        queueContainer.removeAll();
        patientCards.clear();

        List<Kunjungan> queueList = kunjunganControl.showData();
        List<Kunjungan> filteredQueue = new ArrayList<>();

        for (Kunjungan k : queueList) {
            if (k.getStatus() == Kunjungan.Status.BELUM_DILAKUKAN
                    && idDokterLogin.equals(k.getIdDokter())) {
                filteredQueue.add(k);
            }
        }

        // Urutkan berdasarkan jam terdahulu (nomor antrian paling kecil duluan)
        filteredQueue.sort((a, b) -> {
            String jamA = a.getJam() != null ? a.getJam() : "";
            String jamB = b.getJam() != null ? b.getJam() : "";
            return jamA.compareTo(jamB);
        });

        int yPos = CARD_GAP;
        for (Kunjungan kunjungan : filteredQueue) {
            Pasien pasien = pasienControl.searchByNomorRM(kunjungan.getNomorRekamMedis());
            if (pasien != null) {
                JPanel card = createPatientCard(pasien, kunjungan, yPos);
                queueContainer.add(card);
                yPos += CARD_HEIGHT + CARD_GAP;
            }
        }

        // Container setinggi konten asli — scroll aktif otomatis jika melebihi 3 card
        int totalHeight = yPos + CARD_GAP;
        queueContainer.setPreferredSize(new java.awt.Dimension(
            diplayQueuePasienPanel.getWidth() > 0 ? diplayQueuePasienPanel.getWidth() : 205,
            totalHeight
        ));
        queueContainer.revalidate();
        queueContainer.repaint();
    }
    
    private JPanel createPatientCard(Pasien pasien, Kunjungan kunjungan, int yPos) {
        JPanel card = new JPanel();
        card.setBackground(java.awt.Color.WHITE);
        card.setBounds(10, yPos, 185, CARD_HEIGHT);
        card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        card.setLayout(new java.awt.GridBagLayout());

        // Border definitions — compound supaya total space selalu 2px
        javax.swing.border.Border defaultBorder = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 218, 240), 1),
            javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)
        );
        javax.swing.border.Border selectedBorder = javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(90, 120, 210), 2
        );
        javax.swing.border.Border hoverBorder = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(150, 180, 240), 1),
            javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)
        );

        card.setBorder(defaultBorder);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Nama
        JLabel namLabel = new JLabel(pasien.getNama());
        namLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        namLabel.setForeground(new java.awt.Color(25, 25, 25));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new java.awt.Color(225, 230, 245));
        sep.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 1));

        // RM
        JLabel rmLabel = new JLabel("RM: " + pasien.getNomorRekamMedis());
        rmLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        rmLabel.setForeground(new java.awt.Color(90, 90, 90));

        // Jam
        JLabel jamLabel = new JLabel("Jam: " + kunjungan.getJam());
        jamLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        jamLabel.setForeground(new java.awt.Color(90, 90, 90));

        // Info usia & jenis kelamin
        JLabel infoLabel = new JLabel(
            calculateAge(pasien.getTanggalLahir()) + " th  •  " + pasien.getJenisKelamin()
        );
        infoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        infoLabel.setForeground(new java.awt.Color(130, 130, 130));

        gbc.gridy = 0; gbc.insets = new java.awt.Insets(10, 10, 4, 10);
        card.add(namLabel, gbc);

        gbc.gridy = 1; gbc.insets = new java.awt.Insets(0, 10, 6, 10);
        card.add(sep, gbc);

        gbc.gridy = 2; gbc.insets = new java.awt.Insets(2, 10, 2, 10);
        card.add(rmLabel, gbc);

        gbc.gridy = 3;
        card.add(jamLabel, gbc);

        gbc.gridy = 4; gbc.insets = new java.awt.Insets(2, 10, 10, 10);
        card.add(infoLabel, gbc);

        java.awt.Color colorDefault  = java.awt.Color.WHITE;
        java.awt.Color colorSelected = new java.awt.Color(232, 238, 255);
        java.awt.Color colorHover    = new java.awt.Color(248, 250, 255);

        java.awt.event.MouseAdapter cardClickListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayPasienData(kunjungan);
                // Reset semua card ke default
                for (JPanel c : patientCards) {
                    c.setBackground(colorDefault);
                    c.setBorder(defaultBorder);
                }
                // Highlight card yang dipilih
                card.setBackground(colorSelected);
                card.setBorder(selectedBorder);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!card.getBackground().equals(colorSelected)) {
                    card.setBackground(colorHover);
                    card.setBorder(hoverBorder);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!card.getBackground().equals(colorSelected)) {
                    card.setBackground(colorDefault);
                    card.setBorder(defaultBorder);
                }
            }
        };

        card.addMouseListener(cardClickListener);
        for (java.awt.Component comp : card.getComponents()) {
            comp.addMouseListener(cardClickListener);
        }

        patientCards.add(card);
        return card;
    }
    
    private void populateObatComboBox() {
        if (namaObatComboBox == null) return;
        
        List<Obat> daftarObat = obatControl.showData();
        Vector<String> obatNames = new Vector<>();
        obatNames.add("-- Pilih Obat --");
        for (Obat obat : daftarObat) {
            obatNames.add(obat.getIdObat() + " - " + obat.getNamaObat());
        }
        namaObatComboBox.setModel(new DefaultComboBoxModel<>(obatNames));
    }
    
    private void tambahObatKeResep() {
        if (namaObatComboBox == null || jumlahObatTextField == null || aturanPakaiTextField == null) return;
        
        if (namaObatComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih obat terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (jumlahObatTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah obat", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String selectedObat = namaObatComboBox.getSelectedItem().toString();
            String idObat = selectedObat.split(" - ")[0];
            int jumlah = Integer.parseInt(jumlahObatTextField.getText());
            String aturanPakai = aturanPakaiTextField.getText();
            
            Obat obat = obatControl.search(idObat);
            if (obat == null) {
                JOptionPane.showMessageDialog(this, "Obat tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Resep.ItemResep itemResep = new Resep.ItemResep(obat, jumlah, aturanPakai);
            daftarObatResep.add(itemResep);
            
            updateTabelObat();
            
            namaObatComboBox.setSelectedIndex(0);
            jumlahObatTextField.setText("");
            aturanPakaiTextField.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusObatDariResep() {
        if (daftarObatTabel == null) return;
        
        int selectedRow = daftarObatTabel.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih obat yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        daftarObatResep.remove(selectedRow);
        updateTabelObat();
    }
    
    private void updateTabelObat() {
        if (daftarObatTabel == null) return;

        daftarObatTabel.setModel(new TableItemResep(daftarObatResep));
    }
    
    private void selesaikanKonsultasi() {
        if (selectedKunjungan == null) {
            JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (hasilPemeriksaanTextField == null || hasilPemeriksaanTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan hasil pemeriksaan", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (namaPenyakitTextField == null || namaPenyakitTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama penyakit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create Diagnosa
            String idDiagnosa = diagnosaControl.generateIdDiagnosa();
            Diagnosa diagnosa = new Diagnosa(
                idDiagnosa,
                kodePenyakitTextField != null && !kodePenyakitTextField.getText().isEmpty() ? kodePenyakitTextField.getText() : "DIS-001",
                namaPenyakitTextField.getText(),
                keteranganDiagnosisTextField != null ? keteranganDiagnosisTextField.getText() : "",
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()),
                rujukanRadioButton != null && rujukanRadioButton.isSelected()
            );
            diagnosaControl.insert(diagnosa);
            
            // Create Resep if ada obat
            String idResep = null;
            Resep resepObj = null;
            if (!daftarObatResep.isEmpty()) {
                idResep = resepControl.generateIdResep();
                resepObj = new Resep(idResep, idDokterLogin, selectedPasien.getNomorRekamMedis(),
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
                for (Resep.ItemResep item : daftarObatResep) {
                    resepObj.tambahObat(item);
                }
                resepControl.insert(resepObj);
            }

            // Create Rujukan if diperlukan
            if (rujukanRadioButton != null && rujukanRadioButton.isSelected()) {
                if (tanggalRujukanDateChooser != null && tanggalRujukanDateChooser.getDate() != null &&
                    tanggalBerlakuDateChooser != null && tanggalBerlakuDateChooser.getDate() != null) {
                    String idRujukan = rujukanControl.generateIdRujukan();
                    Rujukan rujukan = new Rujukan(
                        idRujukan,
                        selectedKunjungan.getIdKunjungan(),
                        selectedPasien.getNomorRekamMedis(),
                        idDokterLogin,
                        tujuanRujukanTextField != null ? tujuanRujukanTextField.getText() : "",
                        alasanRujukanTextField != null ? alasanRujukanTextField.getText() : "",
                        new java.text.SimpleDateFormat("yyyy-MM-dd").format(tanggalRujukanDateChooser.getDate()),
                        new java.text.SimpleDateFormat("yyyy-MM-dd").format(tanggalBerlakuDateChooser.getDate())
                    );
                    rujukanControl.insert(rujukan);
                } else {
                    JOptionPane.showMessageDialog(this, "Isi tanggal rujukan terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Update Kunjungan ke SELESAI
            kunjunganControl.selesaikan(selectedKunjungan, hasilPemeriksaanTextField.getText(), idDiagnosa, idResep);

            // Otomatis buat tagihan dari kunjungan yang selesai
            new TagihanControl().buatDariKunjungan(selectedKunjungan, resepObj);

            JOptionPane.showMessageDialog(this, "Konsultasi berhasil diselesaikan", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadQueuePasien();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void clearForm() {
        if (kodePenyakitTextField != null) kodePenyakitTextField.setText("");
        if (aturanPakaiTextField != null) aturanPakaiTextField.setText("");
        if (namaPenyakitTextField != null) namaPenyakitTextField.setText("");
        if (keluhanTextField != null) keluhanTextField.setText("");
        if (hasilPemeriksaanTextField != null) hasilPemeriksaanTextField.setText("");
        if (jumlahObatTextField != null) jumlahObatTextField.setText("");
        if (keteranganDiagnosisTextField != null) keteranganDiagnosisTextField.setText("");
        if (tujuanRujukanTextField != null) tujuanRujukanTextField.setText("");
        if (alasanRujukanTextField != null) alasanRujukanTextField.setText("");
        if (namaObatComboBox != null) namaObatComboBox.setSelectedIndex(0);
        if (rujukanRadioButton != null) rujukanRadioButton.setSelected(false);
        if (tanggalRujukanDateChooser != null) tanggalRujukanDateChooser.setDate(null);
        if (tanggalBerlakuDateChooser != null) tanggalBerlakuDateChooser.setDate(null);
        daftarObatResep.clear();
        updateTabelObat();
        selectedKunjungan = null;
        selectedPasien = null;
    }
    
    public void displayPasienData(Kunjungan kunjungan) {
        try {
            
            selectedKunjungan = kunjungan;

            System.out.println("=================================");
            System.out.println("DEBUG DISPLAY PASIEN");
            System.out.println("ID Kunjungan : " + kunjungan.getIdKunjungan());
            System.out.println("Nomor RM     : " + kunjungan.getNomorRekamMedis());

    
            selectedPasien = pasienControl.searchByNomorRM(
                    kunjungan.getNomorRekamMedis()
            );

            System.out.println("Pasien ditemukan : "
                    + (selectedPasien != null));

            if (selectedPasien != null) {

                namaPasienLabel.setText(selectedPasien.getNama());

                kodePasienLabel.setText(selectedPasien.getId());

                usiaPasienLabel.setText(
                        calculateAge(
                                selectedPasien.getTanggalLahir()
                        ) + " Tahun"
                );

                jenisKelaminPasienLabel.setText(
                        selectedPasien.getJenisKelamin()
                );

                keluhanTextField.setText(
                        kunjungan.getKeluhanUtama()
                );

                keluhanTextField.setEnabled(false);


                loadRiwayatPasien(
                        kunjungan.getNomorRekamMedis()
                );

                populateObatComboBox();

                System.out.println(
                        "DEBUG: displayPasienData sukses"
                );

            } else {

                System.out.println(
                        "DEBUG: Pasien tidak ditemukan"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Data pasien tidak ditemukan!"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR displayPasienData: "
                    + e.getMessage()
            );

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error : " + e.getMessage()
            );
        }
    }
    
    private void loadRiwayatPasien(String nomorRM) {

        RekamMedis rekam = rekamMedisControl.search(nomorRM);

        if(rekam != null){

            alergiTextField.setText(String.join(", ", rekam.getAlergi()));

            riwayatPenyakitTextField.setText(String.join(", ", rekam.getRiwayatPenyakit()));
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Data rekam medis tidak ditemukan");
            System.out.println("Data rekam medis tidak ditemukan");

        }
    }
    
    private String calculateAge(String tanggalLahir) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(tanggalLahir);
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDate);
            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return String.valueOf(age);
        } catch (Exception e) {
            return "0";
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
        titlePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        dataPasienDisplayPanel = new javax.swing.JPanel();
        dataPasienLabel = new javax.swing.JLabel();
        namaPasienLabel = new javax.swing.JLabel();
        usiaPasienLabel = new javax.swing.JLabel();
        jenisKelaminPasienLabel = new javax.swing.JLabel();
        kodePasienLabel = new javax.swing.JLabel();
        rekamMedisPanel = new javax.swing.JPanel();
        rekamMedisLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        alergiTextField = new javax.swing.JTextArea();
        alergiLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        riwayatPenyakitTextField = new javax.swing.JTextArea();
        riwayatPenyakitLabel = new javax.swing.JLabel();
        formDiagnosaPasienPanel = new javax.swing.JPanel();
        diagnosisPanel = new javax.swing.JLabel();
        keluhanPasienPanel = new javax.swing.JPanel();
        keluhanLabel = new javax.swing.JLabel();
        keluhanTextField = new javax.swing.JTextField();
        hasiPemeriksaanPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        hasilPemeriksaanTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        kodePenyakitPanel = new javax.swing.JPanel();
        kodePenyakitLabel = new javax.swing.JLabel();
        kodePenyakitTextField = new javax.swing.JTextField();
        aturanPakaiPanel = new javax.swing.JPanel();
        aturanPakaiLabel = new javax.swing.JLabel();
        aturanPakaiTextField = new javax.swing.JTextField();
        namaPenyakitPanel = new javax.swing.JPanel();
        namaPenyakitLabel = new javax.swing.JLabel();
        namaPenyakitTextField = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        resepObatLabel = new javax.swing.JLabel();
        formInputNamaJumlahObat = new javax.swing.JPanel();
        namaObatLabel = new javax.swing.JLabel();
        namaObatComboBox = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        jumlahObatLabel = new javax.swing.JLabel();
        jumlahObatTextField = new javax.swing.JTextField();
        keteranganDiagnosisPanel = new javax.swing.JPanel();
        keteranganDiagnosisLabel = new javax.swing.JLabel();
        keteranganDiagnosisTextField = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        daftarObatTabel = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        tambahObatButton = new javax.swing.JButton();
        hapusObatButton = new javax.swing.JButton();
        rujukanPanel = new javax.swing.JPanel();
        rujukanLabel = new javax.swing.JLabel();
        tanggalRujukanPanel = new javax.swing.JPanel();
        tanggalRujukanLabel = new javax.swing.JLabel();
        tanggalRujukanDateChooser = new com.toedter.calendar.JDateChooser();
        tujuanTanggalBerlakuPanel = new javax.swing.JPanel();
        tanggalBerlakuLabel = new javax.swing.JLabel();
        tujuanRujukanPanel = new javax.swing.JPanel();
        tujuanRujukanLabel = new javax.swing.JLabel();
        tujuanRujukanTextField = new javax.swing.JTextField();
        tanggalBerlakuDateChooser = new com.toedter.calendar.JDateChooser();
        alasanRujukanLabel = new javax.swing.JLabel();
        alasanRujukanTextField = new javax.swing.JTextField();
        rujukanRadioButton = new javax.swing.JRadioButton();
        selesaikanKonsulButton = new javax.swing.JButton();
        diplayQueuePasienPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(238, 239, 253));
        setPreferredSize(new java.awt.Dimension(1224, 811));

        mainPanel.setBackground(new java.awt.Color(238, 239, 253));
        mainPanel.setPreferredSize(new java.awt.Dimension(1161, 799));

        titlePanel.setBackground(new java.awt.Color(255, 255, 255));
        titlePanel.setPreferredSize(new java.awt.Dimension(800, 70));

        titleLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        titleLabel.setText("Antrian Pasien");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addContainerGap(984, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        dataPasienDisplayPanel.setBackground(new java.awt.Color(255, 255, 255));

        dataPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        dataPasienLabel.setText("Data Pasien");

        namaPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        namaPasienLabel.setText("Budi Santoso");

        usiaPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        usiaPasienLabel.setText("19 Tahun");

        jenisKelaminPasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jenisKelaminPasienLabel.setText("Pria");

        kodePasienLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        kodePasienLabel.setText("PSN-001");

        javax.swing.GroupLayout dataPasienDisplayPanelLayout = new javax.swing.GroupLayout(dataPasienDisplayPanel);
        dataPasienDisplayPanel.setLayout(dataPasienDisplayPanelLayout);
        dataPasienDisplayPanelLayout.setHorizontalGroup(
            dataPasienDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPasienDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPasienDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataPasienLabel)
                    .addGroup(dataPasienDisplayPanelLayout.createSequentialGroup()
                        .addComponent(usiaPasienLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jenisKelaminPasienLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kodePasienLabel))
                    .addComponent(namaPasienLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dataPasienDisplayPanelLayout.setVerticalGroup(
            dataPasienDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPasienDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataPasienLabel)
                .addGap(22, 22, 22)
                .addComponent(namaPasienLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPasienDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usiaPasienLabel)
                    .addComponent(jenisKelaminPasienLabel)
                    .addComponent(kodePasienLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rekamMedisPanel.setBackground(new java.awt.Color(255, 255, 255));

        rekamMedisLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        rekamMedisLabel.setText("Rekam Medis");

        alergiTextField.setColumns(20);
        alergiTextField.setRows(5);
        jScrollPane1.setViewportView(alergiTextField);

        alergiLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        alergiLabel.setText("Alergi");

        riwayatPenyakitTextField.setColumns(20);
        riwayatPenyakitTextField.setRows(5);
        jScrollPane2.setViewportView(riwayatPenyakitTextField);

        riwayatPenyakitLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        riwayatPenyakitLabel.setText("Riwayat Penyakit");

        javax.swing.GroupLayout rekamMedisPanelLayout = new javax.swing.GroupLayout(rekamMedisPanel);
        rekamMedisPanel.setLayout(rekamMedisPanelLayout);
        rekamMedisPanelLayout.setHorizontalGroup(
            rekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rekamMedisLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rekamMedisPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(rekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(riwayatPenyakitLabel)
                            .addGroup(rekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(alergiLabel))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        rekamMedisPanelLayout.setVerticalGroup(
            rekamMedisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rekamMedisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rekamMedisLabel)
                .addGap(22, 22, 22)
                .addComponent(alergiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(riwayatPenyakitLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        formDiagnosaPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        diagnosisPanel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        diagnosisPanel.setText("Diagnosis Penyakit");

        keluhanPasienPanel.setBackground(new java.awt.Color(255, 255, 255));

        keluhanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        keluhanLabel.setText("Keluhan Utama");

        javax.swing.GroupLayout keluhanPasienPanelLayout = new javax.swing.GroupLayout(keluhanPasienPanel);
        keluhanPasienPanel.setLayout(keluhanPasienPanelLayout);
        keluhanPasienPanelLayout.setHorizontalGroup(
            keluhanPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keluhanPasienPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(keluhanPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(keluhanPasienPanelLayout.createSequentialGroup()
                        .addComponent(keluhanLabel)
                        .addGap(0, 169, Short.MAX_VALUE))
                    .addComponent(keluhanTextField)))
        );
        keluhanPasienPanelLayout.setVerticalGroup(
            keluhanPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keluhanPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keluhanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keluhanTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addContainerGap())
        );

        hasiPemeriksaanPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel18.setText("Hasil Pemeriksaan");

        javax.swing.GroupLayout hasiPemeriksaanPanelLayout = new javax.swing.GroupLayout(hasiPemeriksaanPanel);
        hasiPemeriksaanPanel.setLayout(hasiPemeriksaanPanelLayout);
        hasiPemeriksaanPanelLayout.setHorizontalGroup(
            hasiPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hasiPemeriksaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hasiPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hasiPemeriksaanPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 175, Short.MAX_VALUE))
                    .addComponent(hasilPemeriksaanTextField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        hasiPemeriksaanPanelLayout.setVerticalGroup(
            hasiPemeriksaanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hasiPemeriksaanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hasilPemeriksaanTextField)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));
        jPanel5.setForeground(new java.awt.Color(102, 102, 102));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 2));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        kodePenyakitPanel.setBackground(new java.awt.Color(255, 255, 255));

        kodePenyakitLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        kodePenyakitLabel.setText("Kode Penyakit");

        javax.swing.GroupLayout kodePenyakitPanelLayout = new javax.swing.GroupLayout(kodePenyakitPanel);
        kodePenyakitPanel.setLayout(kodePenyakitPanelLayout);
        kodePenyakitPanelLayout.setHorizontalGroup(
            kodePenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodePenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kodePenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kodePenyakitPanelLayout.createSequentialGroup()
                        .addComponent(kodePenyakitLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(kodePenyakitTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        kodePenyakitPanelLayout.setVerticalGroup(
            kodePenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kodePenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kodePenyakitLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(kodePenyakitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        aturanPakaiPanel.setBackground(new java.awt.Color(255, 255, 255));

        aturanPakaiLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aturanPakaiLabel.setText("Aturan Pakai");

        javax.swing.GroupLayout aturanPakaiPanelLayout = new javax.swing.GroupLayout(aturanPakaiPanel);
        aturanPakaiPanel.setLayout(aturanPakaiPanelLayout);
        aturanPakaiPanelLayout.setHorizontalGroup(
            aturanPakaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aturanPakaiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aturanPakaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aturanPakaiPanelLayout.createSequentialGroup()
                        .addComponent(aturanPakaiLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(aturanPakaiTextField)))
        );
        aturanPakaiPanelLayout.setVerticalGroup(
            aturanPakaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aturanPakaiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aturanPakaiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aturanPakaiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );

        namaPenyakitPanel.setBackground(new java.awt.Color(255, 255, 255));

        namaPenyakitLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        namaPenyakitLabel.setText("Nama Penyakit");

        namaPenyakitTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaPenyakitTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout namaPenyakitPanelLayout = new javax.swing.GroupLayout(namaPenyakitPanel);
        namaPenyakitPanel.setLayout(namaPenyakitPanelLayout);
        namaPenyakitPanelLayout.setHorizontalGroup(
            namaPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaPenyakitPanelLayout.createSequentialGroup()
                .addComponent(namaPenyakitLabel)
                .addGap(0, 302, Short.MAX_VALUE))
            .addComponent(namaPenyakitTextField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        namaPenyakitPanelLayout.setVerticalGroup(
            namaPenyakitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namaPenyakitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaPenyakitLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(namaPenyakitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(102, 102, 102));
        jPanel9.setForeground(new java.awt.Color(102, 102, 102));
        jPanel9.setPreferredSize(new java.awt.Dimension(0, 2));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        resepObatLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        resepObatLabel.setText("Resep Obat");

        formInputNamaJumlahObat.setBackground(new java.awt.Color(255, 255, 255));

        namaObatLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        namaObatLabel.setText("Nama Obat");

        namaObatComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jumlahObatLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jumlahObatLabel.setText("Jumlah");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jumlahObatLabel)
                        .addGap(0, 123, Short.MAX_VALUE))
                    .addComponent(jumlahObatTextField))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jumlahObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jumlahObatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout formInputNamaJumlahObatLayout = new javax.swing.GroupLayout(formInputNamaJumlahObat);
        formInputNamaJumlahObat.setLayout(formInputNamaJumlahObatLayout);
        formInputNamaJumlahObatLayout.setHorizontalGroup(
            formInputNamaJumlahObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputNamaJumlahObatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formInputNamaJumlahObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaObatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaObatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formInputNamaJumlahObatLayout.setVerticalGroup(
            formInputNamaJumlahObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formInputNamaJumlahObatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaObatLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaObatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(formInputNamaJumlahObatLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        keteranganDiagnosisPanel.setBackground(new java.awt.Color(255, 255, 255));

        keteranganDiagnosisLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        keteranganDiagnosisLabel.setText("Keterangan Diagnosis");

        javax.swing.GroupLayout keteranganDiagnosisPanelLayout = new javax.swing.GroupLayout(keteranganDiagnosisPanel);
        keteranganDiagnosisPanel.setLayout(keteranganDiagnosisPanelLayout);
        keteranganDiagnosisPanelLayout.setHorizontalGroup(
            keteranganDiagnosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keteranganDiagnosisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(keteranganDiagnosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(keteranganDiagnosisPanelLayout.createSequentialGroup()
                        .addComponent(keteranganDiagnosisLabel)
                        .addContainerGap(423, Short.MAX_VALUE))
                    .addComponent(keteranganDiagnosisTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        keteranganDiagnosisPanelLayout.setVerticalGroup(
            keteranganDiagnosisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keteranganDiagnosisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keteranganDiagnosisLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(keteranganDiagnosisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        daftarObatTabel.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(daftarObatTabel);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        tambahObatButton.setBackground(new java.awt.Color(51, 178, 73));
        tambahObatButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tambahObatButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahObatButton.setText("Tambah");
        tambahObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahObatButtonActionPerformed(evt);
            }
        });

        hapusObatButton.setBackground(new java.awt.Color(255, 0, 0));
        hapusObatButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        hapusObatButton.setForeground(new java.awt.Color(255, 255, 255));
        hapusObatButton.setText("Hapus");
        hapusObatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusObatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(tambahObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hapusObatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tambahObatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
            .addComponent(hapusObatButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout formDiagnosaPasienPanelLayout = new javax.swing.GroupLayout(formDiagnosaPasienPanel);
        formDiagnosaPasienPanel.setLayout(formDiagnosaPasienPanelLayout);
        formDiagnosaPasienPanelLayout.setHorizontalGroup(
            formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                        .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                            .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, formDiagnosaPasienPanelLayout.createSequentialGroup()
                                        .addComponent(keluhanPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hasiPemeriksaanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, formDiagnosaPasienPanelLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                                                .addComponent(formInputNamaJumlahObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(aturanPakaiPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                                                    .addGap(6, 6, 6)
                                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                        .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                                        .addComponent(kodePenyakitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(namaPenyakitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(keteranganDiagnosisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(diagnosisPanel)
                            .addComponent(resepObatLabel))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        formDiagnosaPasienPanelLayout.setVerticalGroup(
            formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(keluhanPasienPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hasiPemeriksaanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(diagnosisPanel)
                .addGap(22, 22, 22)
                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(kodePenyakitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaPenyakitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keteranganDiagnosisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(formDiagnosaPasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formDiagnosaPasienPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resepObatLabel)
                        .addGap(22, 22, 22)
                        .addComponent(formInputNamaJumlahObat, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aturanPakaiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addContainerGap())
        );

        rujukanPanel.setBackground(new java.awt.Color(255, 255, 255));

        rujukanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        rujukanLabel.setText("Rujukan");

        tanggalRujukanPanel.setBackground(new java.awt.Color(255, 255, 255));

        tanggalRujukanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tanggalRujukanLabel.setText("Tanggal Rujukan");

        javax.swing.GroupLayout tanggalRujukanPanelLayout = new javax.swing.GroupLayout(tanggalRujukanPanel);
        tanggalRujukanPanel.setLayout(tanggalRujukanPanelLayout);
        tanggalRujukanPanelLayout.setHorizontalGroup(
            tanggalRujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanggalRujukanPanelLayout.createSequentialGroup()
                .addComponent(tanggalRujukanLabel)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(tanggalRujukanPanelLayout.createSequentialGroup()
                .addComponent(tanggalRujukanDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );
        tanggalRujukanPanelLayout.setVerticalGroup(
            tanggalRujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanggalRujukanPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tanggalRujukanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tanggalRujukanDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tujuanTanggalBerlakuPanel.setBackground(new java.awt.Color(255, 255, 255));

        tanggalBerlakuLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tanggalBerlakuLabel.setText("Tanggal Berlaku");

        tujuanRujukanPanel.setBackground(new java.awt.Color(255, 255, 255));

        tujuanRujukanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tujuanRujukanLabel.setText("Tujuan Rujukan");

        tujuanRujukanTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tujuanRujukanTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tujuanRujukanPanelLayout = new javax.swing.GroupLayout(tujuanRujukanPanel);
        tujuanRujukanPanel.setLayout(tujuanRujukanPanelLayout);
        tujuanRujukanPanelLayout.setHorizontalGroup(
            tujuanRujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tujuanRujukanPanelLayout.createSequentialGroup()
                .addGroup(tujuanRujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tujuanRujukanPanelLayout.createSequentialGroup()
                        .addComponent(tujuanRujukanLabel)
                        .addGap(0, 221, Short.MAX_VALUE))
                    .addComponent(tujuanRujukanTextField))
                .addContainerGap())
        );
        tujuanRujukanPanelLayout.setVerticalGroup(
            tujuanRujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tujuanRujukanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tujuanRujukanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tujuanRujukanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tujuanTanggalBerlakuPanelLayout = new javax.swing.GroupLayout(tujuanTanggalBerlakuPanel);
        tujuanTanggalBerlakuPanel.setLayout(tujuanTanggalBerlakuPanelLayout);
        tujuanTanggalBerlakuPanelLayout.setHorizontalGroup(
            tujuanTanggalBerlakuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tujuanTanggalBerlakuPanelLayout.createSequentialGroup()
                .addGroup(tujuanTanggalBerlakuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tanggalBerlakuLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tujuanRujukanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(tujuanTanggalBerlakuPanelLayout.createSequentialGroup()
                .addComponent(tanggalBerlakuDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tujuanTanggalBerlakuPanelLayout.setVerticalGroup(
            tujuanTanggalBerlakuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tujuanTanggalBerlakuPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tanggalBerlakuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tanggalBerlakuDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tujuanRujukanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        alasanRujukanLabel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        alasanRujukanLabel.setText("Alasan Rujukan");

        rujukanRadioButton.setText("Perlu Rujukan");

        javax.swing.GroupLayout rujukanPanelLayout = new javax.swing.GroupLayout(rujukanPanel);
        rujukanPanel.setLayout(rujukanPanelLayout);
        rujukanPanelLayout.setHorizontalGroup(
            rujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rujukanPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rujukanPanelLayout.createSequentialGroup()
                        .addComponent(rujukanLabel)
                        .addGap(146, 146, 146)
                        .addComponent(rujukanRadioButton)
                        .addGap(8, 8, 8))
                    .addComponent(tanggalRujukanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(alasanRujukanLabel)
                        .addComponent(tujuanTanggalBerlakuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(alasanRujukanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        rujukanPanelLayout.setVerticalGroup(
            rujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rujukanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rujukanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rujukanLabel)
                    .addComponent(rujukanRadioButton))
                .addGap(22, 22, 22)
                .addComponent(tanggalRujukanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tujuanTanggalBerlakuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alasanRujukanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alasanRujukanTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );

        selesaikanKonsulButton.setBackground(new java.awt.Color(51, 178, 73));
        selesaikanKonsulButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        selesaikanKonsulButton.setForeground(new java.awt.Color(255, 255, 255));
        selesaikanKonsulButton.setText("Selesaikan Konsultasi");
        selesaikanKonsulButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selesaikanKonsulButtonActionPerformed(evt);
            }
        });

        diplayQueuePasienPanel.setBackground(new java.awt.Color(255, 255, 255));
        diplayQueuePasienPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diplayQueuePasienPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout diplayQueuePasienPanelLayout = new javax.swing.GroupLayout(diplayQueuePasienPanel);
        diplayQueuePasienPanel.setLayout(diplayQueuePasienPanelLayout);
        diplayQueuePasienPanelLayout.setHorizontalGroup(
            diplayQueuePasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        diplayQueuePasienPanelLayout.setVerticalGroup(
            diplayQueuePasienPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(diplayQueuePasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(formDiagnosaPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataPasienDisplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(rujukanPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(rekamMedisPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selesaikanKonsulButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(109, 109, 109))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(rekamMedisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rujukanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selesaikanKonsulButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addComponent(dataPasienDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(formDiagnosaPasienPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(diplayQueuePasienPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selesaikanKonsulButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selesaikanKonsulButtonActionPerformed
        selesaikanKonsultasi();
    }//GEN-LAST:event_selesaikanKonsulButtonActionPerformed

    private void tambahObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahObatButtonActionPerformed
        tambahObatKeResep();
    }//GEN-LAST:event_tambahObatButtonActionPerformed

    private void hapusObatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusObatButtonActionPerformed
        hapusObatDariResep();
    }//GEN-LAST:event_hapusObatButtonActionPerformed

    private void diplayQueuePasienPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diplayQueuePasienPanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_diplayQueuePasienPanelMouseClicked

    private void tujuanRujukanTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tujuanRujukanTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tujuanRujukanTextFieldActionPerformed

    private void namaPenyakitTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaPenyakitTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaPenyakitTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alasanRujukanLabel;
    private javax.swing.JTextField alasanRujukanTextField;
    private javax.swing.JLabel alergiLabel;
    private javax.swing.JTextArea alergiTextField;
    private javax.swing.JLabel aturanPakaiLabel;
    private javax.swing.JPanel aturanPakaiPanel;
    private javax.swing.JTextField aturanPakaiTextField;
    private javax.swing.JTable daftarObatTabel;
    private javax.swing.JPanel dataPasienDisplayPanel;
    private javax.swing.JLabel dataPasienLabel;
    private javax.swing.JLabel diagnosisPanel;
    private javax.swing.JPanel diplayQueuePasienPanel;
    private javax.swing.JPanel formDiagnosaPasienPanel;
    private javax.swing.JPanel formInputNamaJumlahObat;
    private javax.swing.JButton hapusObatButton;
    private javax.swing.JPanel hasiPemeriksaanPanel;
    private javax.swing.JTextField hasilPemeriksaanTextField;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel jenisKelaminPasienLabel;
    private javax.swing.JLabel jumlahObatLabel;
    private javax.swing.JTextField jumlahObatTextField;
    private javax.swing.JLabel keluhanLabel;
    private javax.swing.JPanel keluhanPasienPanel;
    private javax.swing.JTextField keluhanTextField;
    private javax.swing.JLabel keteranganDiagnosisLabel;
    private javax.swing.JPanel keteranganDiagnosisPanel;
    private javax.swing.JTextField keteranganDiagnosisTextField;
    private javax.swing.JLabel kodePasienLabel;
    private javax.swing.JLabel kodePenyakitLabel;
    private javax.swing.JPanel kodePenyakitPanel;
    private javax.swing.JTextField kodePenyakitTextField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox<String> namaObatComboBox;
    private javax.swing.JLabel namaObatLabel;
    private javax.swing.JLabel namaPasienLabel;
    private javax.swing.JLabel namaPenyakitLabel;
    private javax.swing.JPanel namaPenyakitPanel;
    private javax.swing.JTextField namaPenyakitTextField;
    private javax.swing.JLabel rekamMedisLabel;
    private javax.swing.JPanel rekamMedisPanel;
    private javax.swing.JLabel resepObatLabel;
    private javax.swing.JLabel riwayatPenyakitLabel;
    private javax.swing.JTextArea riwayatPenyakitTextField;
    private javax.swing.JLabel rujukanLabel;
    private javax.swing.JPanel rujukanPanel;
    private javax.swing.JRadioButton rujukanRadioButton;
    private javax.swing.JButton selesaikanKonsulButton;
    private javax.swing.JButton tambahObatButton;
    private com.toedter.calendar.JDateChooser tanggalBerlakuDateChooser;
    private javax.swing.JLabel tanggalBerlakuLabel;
    private com.toedter.calendar.JDateChooser tanggalRujukanDateChooser;
    private javax.swing.JLabel tanggalRujukanLabel;
    private javax.swing.JPanel tanggalRujukanPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel tujuanRujukanLabel;
    private javax.swing.JPanel tujuanRujukanPanel;
    private javax.swing.JTextField tujuanRujukanTextField;
    private javax.swing.JPanel tujuanTanggalBerlakuPanel;
    private javax.swing.JLabel usiaPasienLabel;
    // End of variables declaration//GEN-END:variables
}
