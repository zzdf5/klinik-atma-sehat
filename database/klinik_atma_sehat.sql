-- ============================================================
--  Klinik Atma Sehat - Database Schema
--  MySQL / XAMPP
-- ============================================================

DROP DATABASE IF EXISTS klinik_atma_sehat;

CREATE DATABASE IF NOT EXISTS klinik_atma_sehat
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE klinik_atma_sehat;

SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- 1. POLIKLINIK
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS poliklinik (
    id_poliklinik   VARCHAR(10)  NOT NULL,
    nama_poliklinik VARCHAR(100) NOT NULL,
    lokasi_ruangan  VARCHAR(100) NOT NULL,
    jam_operasional VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id_poliklinik)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 2. PENGGUNA (tabel induk untuk login)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pengguna (
    id       VARCHAR(10)              NOT NULL,
    username VARCHAR(50)              NOT NULL UNIQUE,
    password VARCHAR(255)             NOT NULL,
    peran    ENUM('ADMIN', 'DOKTER')  NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 3. ADMIN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    id           VARCHAR(10)                 NOT NULL,
    nama         VARCHAR(100)                NOT NULL,
    no_telepon   VARCHAR(20)                 NOT NULL,
    jabatan      ENUM('KARYAWAN') NOT NULL DEFAULT 'KARYAWAN',
    status_aktif TINYINT(1)                  NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT fk_admin_pengguna FOREIGN KEY (id)
        REFERENCES pengguna (id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 4. DOKTER
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dokter (
    id               VARCHAR(10)   NOT NULL,
    nomor_str        VARCHAR(50)   NOT NULL,
    nama             VARCHAR(100)  NOT NULL,
    no_telepon       VARCHAR(20)   NOT NULL,
    spesialisasi     VARCHAR(100)  NOT NULL,
    tarif_konsultasi DECIMAL(12,2) NOT NULL DEFAULT 0,
    status_aktif     TINYINT(1)    NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT fk_dokter_pengguna FOREIGN KEY (id)
        REFERENCES pengguna (id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 5. PASIEN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pasien (
    id_pasien         VARCHAR(10)           NOT NULL,
    nomor_rekam_medis VARCHAR(20)           NOT NULL UNIQUE,
    nama              VARCHAR(100)          NOT NULL,
    tanggal_lahir     DATE                  NOT NULL,
    jenis_kelamin     ENUM('L', 'P')        NOT NULL,
    no_telepon        VARCHAR(20)           NOT NULL,
    alamat            TEXT                  NOT NULL,
    PRIMARY KEY (id_pasien)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 5. REKAM MEDIS
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rekam_medis (
    nomor_rekam_medis VARCHAR(20) NOT NULL,
    id_pasien         VARCHAR(10) NOT NULL,
    tanggal_buat      DATE        NOT NULL,
    PRIMARY KEY (nomor_rekam_medis),
    CONSTRAINT fk_rm_pasien FOREIGN KEY (id_pasien)
        REFERENCES pasien (id_pasien) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rekam_medis_alergi (
    id                INT         NOT NULL AUTO_INCREMENT,
    nomor_rekam_medis VARCHAR(20) NOT NULL,
    alergi            VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_alergi_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rekam_medis_riwayat (
    id                INT          NOT NULL AUTO_INCREMENT,
    nomor_rekam_medis VARCHAR(20)  NOT NULL,
    riwayat_penyakit  VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_riwayat_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 6. JADWAL DOKTER
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jadwal_dokter (
    id_jadwal      VARCHAR(10) NOT NULL,
    id_dokter      VARCHAR(10) NOT NULL,
    id_poliklinik  VARCHAR(10) NOT NULL,
    jam_mulai      TIME        NOT NULL,
    jam_selesai    TIME        NOT NULL,
    kuota_pasien   INT         NOT NULL DEFAULT 20,
    PRIMARY KEY (id_jadwal),
    CONSTRAINT fk_jadwal_dokter     FOREIGN KEY (id_dokter)
        REFERENCES dokter (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_jadwal_poliklinik FOREIGN KEY (id_poliklinik)
        REFERENCES poliklinik (id_poliklinik) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 7. DIAGNOSA
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS diagnosa (
    id_diagnosa      VARCHAR(10)  NOT NULL,
    kode_penyakit    VARCHAR(20)  NOT NULL,
    nama_penyakit    VARCHAR(200) NOT NULL,
    keterangan       TEXT,
    tanggal_diagnosa DATE         NOT NULL,
    perlu_rujukan    TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id_diagnosa)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 8. OBAT  (class-table inheritance: Herbal & Paten)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS obat (
    id_obat        VARCHAR(10)   NOT NULL,
    nama_obat      VARCHAR(100)  NOT NULL,
    bentuk_sediaan VARCHAR(50)   NOT NULL,
    dosis          VARCHAR(50)   NOT NULL,
    kategori       VARCHAR(50)   NOT NULL,
    harga_satuan   DECIMAL(12,2) NOT NULL DEFAULT 0,
    stok           INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (id_obat)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS obat_herbal (
    id_obat     VARCHAR(10)  NOT NULL,
    bahan_utama VARCHAR(200) NOT NULL,
    PRIMARY KEY (id_obat),
    CONSTRAINT fk_obat_herbal FOREIGN KEY (id_obat)
        REFERENCES obat (id_obat) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS obat_paten (
    id_obat VARCHAR(10)  NOT NULL,
    merk    VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_obat),
    CONSTRAINT fk_obat_paten FOREIGN KEY (id_obat)
        REFERENCES obat (id_obat) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 9. RESEP
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS resep (
    id_resep          VARCHAR(10)                       NOT NULL,
    id_dokter         VARCHAR(10)                       NOT NULL,
    nomor_rekam_medis VARCHAR(20)                       NOT NULL,
    tanggal_resep     DATE                              NOT NULL,
    status            ENUM('DIPROSES', 'SELESAI', 'BATAL') NOT NULL DEFAULT 'DIPROSES',
    PRIMARY KEY (id_resep),
    CONSTRAINT fk_resep_dokter FOREIGN KEY (id_dokter)
        REFERENCES dokter (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_resep_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS resep_detail (
    id           INT         NOT NULL AUTO_INCREMENT,
    id_resep     VARCHAR(10) NOT NULL,
    id_obat      VARCHAR(10) NOT NULL,
    jumlah       INT         NOT NULL DEFAULT 1,
    aturan_pakai VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_resep_detail_resep FOREIGN KEY (id_resep)
        REFERENCES resep (id_resep) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_resep_detail_obat  FOREIGN KEY (id_obat)
        REFERENCES obat (id_obat) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 10. KUNJUNGAN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS kunjungan (
    id_kunjungan      VARCHAR(10)                             NOT NULL,
    nomor_rekam_medis VARCHAR(20)                             NOT NULL,
    id_dokter         VARCHAR(10)                             NULL,
    tanggal           DATE                                    NOT NULL,
    jam               TIME                                    NOT NULL,
    keluhan_utama     TEXT                                    NOT NULL,
    hasil_pemeriksaan TEXT                                    NULL,
    id_diagnosa       VARCHAR(10)                             NULL,
    id_resep          VARCHAR(10)                             NULL,
    biaya_konsultasi  DECIMAL(12,2)                           NOT NULL DEFAULT 0,
    status            ENUM('BELUM_DILAKUKAN', 'SELESAI', 'BATAL') NOT NULL DEFAULT 'BELUM_DILAKUKAN',
    PRIMARY KEY (id_kunjungan),
    CONSTRAINT fk_kunjungan_rm       FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_kunjungan_dokter   FOREIGN KEY (id_dokter)
        REFERENCES dokter (id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_kunjungan_diagnosa FOREIGN KEY (id_diagnosa)
        REFERENCES diagnosa (id_diagnosa) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_kunjungan_resep    FOREIGN KEY (id_resep)
        REFERENCES resep (id_resep) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 11. ANTRIAN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS antrian (
    id_antrian      INT         NOT NULL AUTO_INCREMENT,
    nomor_urut      INT         NOT NULL,
    id_pasien       VARCHAR(10) NOT NULL,
    id_dokter       VARCHAR(10) NOT NULL,
    id_poliklinik   VARCHAR(10) NOT NULL,
    tanggal         DATE        NOT NULL,
    status          ENUM('MENUNGGU', 'DALAM_PEMERIKSAAN', 'SELESAI', 'BATAL') NOT NULL DEFAULT 'MENUNGGU',
    jenis_kunjungan ENUM('BARU', 'KONTROL', 'RUJUKAN') NOT NULL,
    PRIMARY KEY (id_antrian),
    CONSTRAINT fk_antrian_pasien     FOREIGN KEY (id_pasien)
        REFERENCES pasien (id_pasien) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_antrian_dokter     FOREIGN KEY (id_dokter)
        REFERENCES dokter (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_antrian_poliklinik FOREIGN KEY (id_poliklinik)
        REFERENCES poliklinik (id_poliklinik) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 12. RUJUKAN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rujukan (
    id_rujukan        VARCHAR(10)                       NOT NULL,
    id_kunjungan      VARCHAR(10)                       NOT NULL,
    nomor_rekam_medis VARCHAR(20)                       NOT NULL,
    id_dokter_pengirim VARCHAR(10)                      NOT NULL,
    tujuan_rujukan    VARCHAR(200)                      NOT NULL,
    alasan_rujukan    TEXT                              NOT NULL,
    tanggal_rujukan   DATE                              NOT NULL,
    tanggal_berlaku   DATE                              NOT NULL,
    status            ENUM('AKTIF', 'DIGUNAKAN', 'BATAL') NOT NULL DEFAULT 'AKTIF',
    PRIMARY KEY (id_rujukan),
    CONSTRAINT fk_rujukan_kunjungan FOREIGN KEY (id_kunjungan)
        REFERENCES kunjungan (id_kunjungan) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rujukan_rm        FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rujukan_dokter    FOREIGN KEY (id_dokter_pengirim)
        REFERENCES dokter (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 13. TAGIHAN
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tagihan (
    id_tagihan        VARCHAR(10)                            NOT NULL,
    id_kunjungan      VARCHAR(10)                            NOT NULL,
    tanggal_tagihan   DATE                                   NOT NULL,
    total_tagihan     DECIMAL(12,2)                          NOT NULL DEFAULT 0,
    jumlah_bayar      DECIMAL(12,2)                          NOT NULL DEFAULT 0,
    kembalian         DECIMAL(12,2)                          NOT NULL DEFAULT 0,
    metode_pembayaran ENUM('TUNAI', 'BPJS', 'TRANSFER', 'DEBIT') NULL,
    status            ENUM('BELUM_BAYAR', 'LUNAS')           NOT NULL DEFAULT 'BELUM_BAYAR',
    PRIMARY KEY (id_tagihan),
    CONSTRAINT fk_tagihan_kunjungan FOREIGN KEY (id_kunjungan)
        REFERENCES kunjungan (id_kunjungan) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tagihan_detail (
    id           INT           NOT NULL AUTO_INCREMENT,
    id_tagihan   VARCHAR(10)   NOT NULL,
    nama_item    VARCHAR(200)  NOT NULL,
    jumlah       INT           NOT NULL DEFAULT 1,
    harga_satuan DECIMAL(12,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_tagihan_detail FOREIGN KEY (id_tagihan)
        REFERENCES tagihan (id_tagihan) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
--  SAMPLE DATA
-- ============================================================

-- Poliklinik
INSERT INTO poliklinik VALUES
('POL001', 'Umum',              'Lantai 1 Ruang A', '08:00-16:00'),
('POL002', 'Penyakit Dalam',    'Lantai 1 Ruang B', '08:00-14:00'),
('POL003', 'Anak',              'Lantai 2 Ruang A', '08:00-15:00'),
('POL004', 'Gigi',              'Lantai 2 Ruang B', '09:00-17:00'),
('POL005', 'Mata',              'Lantai 1 Ruang C', '08:00-15:00'),
('POL006', 'THT',               'Lantai 2 Ruang C', '08:00-14:00'),
('POL007', 'Kulit dan Kelamin', 'Lantai 3 Ruang A', '09:00-16:00'),
('POL008', 'Saraf',             'Lantai 3 Ruang B', '08:00-15:00'),
('POL009', 'Jantung',           'Lantai 3 Ruang C', '08:00-14:00'),
('POL010', 'Kandungan',         'Lantai 1 Ruang D', '09:00-17:00');

-- Pengguna (login)
INSERT INTO pengguna VALUES
('ADM001', 'siti.admin',   'admin123',  'ADMIN'),
('ADM002', 'budi.admin',   'admin123',  'ADMIN'),
('ADM003', 'ani.admin',    'admin123',  'ADMIN'),
('ADM004', 'rina.admin',   'admin123',  'ADMIN'),
('ADM005', 'dewi.admin',   'admin123',  'ADMIN'),
('ADM006', 'joko.admin',   'admin123',  'ADMIN'),
('ADM007', 'lina.admin',   'admin123',  'ADMIN'),
('ADM008', 'agus.admin',   'admin123',  'ADMIN'),
('ADM009', 'maya.admin',   'admin123',  'ADMIN'),
('ADM010', 'rudi.admin',   'admin123',  'ADMIN'),
('DOK001', 'andi.dokter',   'dokter123', 'DOKTER'),
('DOK002', 'rina.dokter',   'dokter123', 'DOKTER'),
('DOK003', 'hendra.dokter', 'dokter123', 'DOKTER'),
('DOK004', 'sari.dokter',   'dokter123', 'DOKTER'),
('DOK005', 'budi.dokter',   'dokter123', 'DOKTER'),
('DOK006', 'maya.dokter',   'dokter123', 'DOKTER'),
('DOK007', 'doni.dokter',   'dokter123', 'DOKTER'),
('DOK008', 'fitri.dokter',  'dokter123', 'DOKTER'),
('DOK009', 'eko.dokter',    'dokter123', 'DOKTER'),
('DOK010', 'nina.dokter',   'dokter123', 'DOKTER');

-- Admin
INSERT INTO admin VALUES
('ADM001', 'Siti Rahayu',     '08111000001', 'KARYAWAN', 1),
('ADM002', 'Budi Santoso',    '08111000002', 'KARYAWAN', 1),
('ADM003', 'Ani Yuliana',     '08111000003', 'KARYAWAN', 1),
('ADM004', 'Rina Marlina',    '08111000004', 'KARYAWAN', 1),
('ADM005', 'Dewi Anggraini',  '08111000005', 'KARYAWAN', 1),
('ADM006', 'Joko Susilo',     '08111000006', 'KARYAWAN', 1),
('ADM007', 'Lina Kusuma',     '08111000007', 'KARYAWAN', 1),
('ADM008', 'Agus Salim',      '08111000008', 'KARYAWAN', 1),
('ADM009', 'Maya Sari',       '08111000009', 'KARYAWAN', 1),
('ADM010', 'Rudi Hartanto',   '08111000010', 'KARYAWAN', 1);

-- Dokter
INSERT INTO dokter VALUES
('DOK001', 'STR-001-2020', 'dr. Andi Wijaya',      '08122000001', 'Umum',              150000, 1),
('DOK002', 'STR-002-2019', 'dr. Rina Susanti',     '08122000002', 'Penyakit Dalam',    200000, 1),
('DOK003', 'STR-003-2021', 'dr. Hendra Lim',       '08122000003', 'Anak',              175000, 1),
('DOK004', 'STR-004-2018', 'drg. Sari Indah',      '08122000004', 'Gigi',              160000, 1),
('DOK005', 'STR-005-2017', 'dr. Budi Hartono',     '08122000005', 'Mata',              180000, 1),
('DOK006', 'STR-006-2022', 'dr. Maya Putri',       '08122000006', 'THT',               170000, 1),
('DOK007', 'STR-007-2016', 'dr. Doni Saputra',     '08122000007', 'Kulit dan Kelamin', 165000, 1),
('DOK008', 'STR-008-2015', 'dr. Fitri Handayani',  '08122000008', 'Saraf',             210000, 1),
('DOK009', 'STR-009-2014', 'dr. Eko Prasetyo',     '08122000009', 'Jantung',           250000, 1),
('DOK010', 'STR-010-2023', 'dr. Nina Lestari',     '08122000010', 'Kandungan',         190000, 1);

-- Pasien
INSERT INTO pasien VALUES
('PAS001', 'RM-2024-001', 'Ahmad Fauzi',       '1990-05-15', 'L', '08133000001', 'Jl. Merdeka No.1, Jakarta'),
('PAS002', 'RM-2024-002', 'Dewi Lestari',      '1985-08-22', 'P', '08133000002', 'Jl. Sudirman No.5, Jakarta'),
('PAS003', 'RM-2024-003', 'Riko Prasetyo',     '2010-03-10', 'L', '08133000003', 'Jl. Gatot Subroto No.10, Jakarta'),
('PAS004', 'RM-2024-004', 'Sinta Maharani',    '1995-11-30', 'P', '08133000004', 'Jl. Diponegoro No.7, Bandung'),
('PAS005', 'RM-2024-005', 'Bayu Saputra',      '1988-07-19', 'L', '08133000005', 'Jl. Ahmad Yani No.12, Surabaya'),
('PAS006', 'RM-2024-006', 'Rani Oktaviani',    '2001-02-14', 'P', '08133000006', 'Jl. Pahlawan No.3, Semarang'),
('PAS007', 'RM-2024-007', 'Fajar Nugroho',     '1979-09-05', 'L', '08133000007', 'Jl. Pemuda No.21, Yogyakarta'),
('PAS008', 'RM-2024-008', 'Indah Permata',     '2015-12-01', 'P', '08133000008', 'Jl. Kartini No.8, Jakarta'),
('PAS009', 'RM-2024-009', 'Hadi Kurniawan',    '1992-04-25', 'L', '08133000009', 'Jl. Veteran No.15, Bekasi'),
('PAS010', 'RM-2024-010', 'Lestari Wulandari', '1983-06-17', 'P', '08133000010', 'Jl. Cendrawasih No.4, Depok');

-- Rekam Medis
INSERT INTO rekam_medis VALUES
('RM-2024-001', 'PAS001', '2024-01-10'),
('RM-2024-002', 'PAS002', '2024-02-15'),
('RM-2024-003', 'PAS003', '2024-03-20'),
('RM-2024-004', 'PAS004', '2024-03-25'),
('RM-2024-005', 'PAS005', '2024-04-02'),
('RM-2024-006', 'PAS006', '2024-04-10'),
('RM-2024-007', 'PAS007', '2024-04-18'),
('RM-2024-008', 'PAS008', '2024-04-22'),
('RM-2024-009', 'PAS009', '2024-04-28'),
('RM-2024-010', 'PAS010', '2024-05-01');

INSERT INTO rekam_medis_alergi (nomor_rekam_medis, alergi) VALUES
('RM-2024-001', 'Penisilin'),
('RM-2024-002', 'Debu'),
('RM-2024-003', 'Seafood'),
('RM-2024-004', 'Kacang'),
('RM-2024-005', 'Sulfa'),
('RM-2024-006', 'Telur'),
('RM-2024-007', 'Aspirin'),
('RM-2024-008', 'Susu Sapi'),
('RM-2024-009', 'Lateks'),
('RM-2024-010', 'Serbuk Sari');

INSERT INTO rekam_medis_riwayat (nomor_rekam_medis, riwayat_penyakit) VALUES
('RM-2024-001', 'Hipertensi'),
('RM-2024-002', 'Asma'),
('RM-2024-003', 'DBD'),
('RM-2024-004', 'Maag Kronis'),
('RM-2024-005', 'Diabetes Melitus'),
('RM-2024-006', 'Anemia'),
('RM-2024-007', 'Kolesterol Tinggi'),
('RM-2024-008', 'Bronkitis'),
('RM-2024-009', 'Gastritis'),
('RM-2024-010', 'Migrain');

-- Jadwal Dokter
INSERT INTO jadwal_dokter VALUES
('JDW001', 'DOK001', 'POL001', '08:00:00', '12:00:00', 30),
('JDW002', 'DOK002', 'POL002', '08:00:00', '14:00:00', 20),
('JDW003', 'DOK003', 'POL003', '09:00:00', '15:00:00', 25),
('JDW004', 'DOK004', 'POL004', '09:00:00', '17:00:00', 15),
('JDW005', 'DOK005', 'POL005', '08:00:00', '15:00:00', 20),
('JDW006', 'DOK006', 'POL006', '08:00:00', '14:00:00', 18),
('JDW007', 'DOK007', 'POL007', '09:00:00', '16:00:00', 16),
('JDW008', 'DOK008', 'POL008', '08:00:00', '15:00:00', 22),
('JDW009', 'DOK009', 'POL009', '08:00:00', '14:00:00', 12),
('JDW010', 'DOK010', 'POL010', '09:00:00', '17:00:00', 20);

-- Obat
INSERT INTO obat (id_obat, nama_obat, bentuk_sediaan, dosis, kategori, harga_satuan, stok) VALUES
('OBTP001', 'Paracetamol',     'Tablet', '500mg',    'Analgesik',       2500, 100),
('OBTP002', 'Amoxicillin',     'Kapsul', '500mg',    'Antibiotik',      5000,  80),
('OBTP003', 'Ibuprofen',       'Tablet', '400mg',    'Analgesik',       3500,  60),
('OBTP004', 'Cetirizine',      'Tablet', '10mg',     'Antihistamin',    3000,  70),
('OBTP005', 'Omeprazole',      'Kapsul', '20mg',     'Antasida',        4500,  50),
('OBTP006', 'Amlodipine',      'Tablet', '5mg',      'Antihipertensi',  4000,  90),
('OBTP007', 'Metformin',       'Tablet', '500mg',    'Antidiabetes',    3800,  75),
('OBTP008', 'Salbutamol',      'Sirup',  '2mg/5ml',  'Bronkodilator',  12000,  40),
('OBTP009', 'Dexamethasone',   'Tablet', '0.5mg',    'Kortikosteroid',  2000,  85),
('OBTP010', 'Antasida Doen',   'Tablet', '200mg',    'Antasida',        1500, 120),
('OBTH001', 'Jahe Merah',      'Kapsul', '250mg',    'Obat Herbal',     8000,  50),
('OBTH002', 'Kunyit Asam',     'Kapsul', '300mg',    'Obat Herbal',     7500,  45),
('OBTH003', 'Temulawak',       'Kapsul', '250mg',    'Obat Herbal',     9000,  40),
('OBTH004', 'Sambiloto',       'Kapsul', '200mg',    'Obat Herbal',     8500,  35),
('OBTH005', 'Daun Sirsak',     'Kapsul', '300mg',    'Obat Herbal',    10000,  30),
('OBTH006', 'Mengkudu',        'Kapsul', '250mg',    'Obat Herbal',     8800,  38),
('OBTH007', 'Habbatussauda',   'Kapsul', '500mg',    'Obat Herbal',    12000,  60),
('OBTH008', 'Daun Kelor',      'Kapsul', '250mg',    'Obat Herbal',     9500,  42),
('OBTH009', 'Kayu Manis',      'Kapsul', '200mg',    'Obat Herbal',     7000,  55),
('OBTH010', 'Pegagan',         'Kapsul', '300mg',    'Obat Herbal',     9200,  33);

INSERT INTO obat_herbal (id_obat, bahan_utama) VALUES
('OBTH001', 'Zingiber officinale'),
('OBTH002', 'Curcuma longa'),
('OBTH003', 'Curcuma xanthorrhiza'),
('OBTH004', 'Andrographis paniculata'),
('OBTH005', 'Annona muricata'),
('OBTH006', 'Morinda citrifolia'),
('OBTH007', 'Nigella sativa'),
('OBTH008', 'Moringa oleifera'),
('OBTH009', 'Cinnamomum verum'),
('OBTH010', 'Centella asiatica');

INSERT INTO obat_paten (id_obat, merk) VALUES
('OBTP001', 'Generik'),
('OBTP002', 'Amoxil'),
('OBTP003', 'Proris'),
('OBTP004', 'Incidal'),
('OBTP005', 'Losec'),
('OBTP006', 'Norvask'),
('OBTP007', 'Glucophage'),
('OBTP008', 'Ventolin'),
('OBTP009', 'Kalmethasone'),
('OBTP010', 'Promag');

-- Diagnosa
INSERT INTO diagnosa VALUES
('DIS001', 'J00', 'Common Cold',               'Infeksi saluran napas atas',        '2024-05-01', 0),
('DIS002', 'I10', 'Hipertensi',                'Tekanan darah tinggi primer',       '2024-05-02', 1),
('DIS003', 'E11', 'Diabetes Melitus Tipe 2',   'Gangguan metabolisme gula darah',   '2024-05-03', 1),
('DIS004', 'J45', 'Asma',                      'Penyempitan saluran napas',         '2024-05-04', 0),
('DIS005', 'A09', 'Diare',                     'Gangguan pencernaan akut',          '2024-05-05', 0),
('DIS006', 'K29', 'Gastritis',                 'Peradangan pada lambung',           '2024-05-06', 0),
('DIS007', 'L20', 'Dermatitis Atopik',         'Peradangan kulit kronis',           '2024-05-07', 0),
('DIS008', 'G43', 'Migrain',                   'Nyeri kepala sebelah berulang',     '2024-05-08', 0),
('DIS009', 'J02', 'Faringitis Akut',           'Radang tenggorokan',                '2024-05-09', 0),
('DIS010', 'M54', 'Nyeri Punggung Bawah',      'Low back pain mekanik',             '2024-05-10', 1);

-- Resep
INSERT INTO resep VALUES
('RES001', 'DOK001', 'RM-2024-001', '2024-05-01', 'SELESAI'),
('RES002', 'DOK002', 'RM-2024-002', '2024-05-02', 'DIPROSES'),
('RES003', 'DOK003', 'RM-2024-003', '2024-05-03', 'SELESAI'),
('RES004', 'DOK004', 'RM-2024-004', '2024-05-04', 'SELESAI'),
('RES005', 'DOK005', 'RM-2024-005', '2024-05-05', 'DIPROSES'),
('RES006', 'DOK006', 'RM-2024-006', '2024-05-06', 'SELESAI'),
('RES007', 'DOK007', 'RM-2024-007', '2024-05-07', 'BATAL'),
('RES008', 'DOK008', 'RM-2024-008', '2024-05-08', 'SELESAI'),
('RES009', 'DOK009', 'RM-2024-009', '2024-05-09', 'DIPROSES'),
('RES010', 'DOK010', 'RM-2024-010', '2024-05-10', 'SELESAI');

INSERT INTO resep_detail (id_resep, id_obat, jumlah, aturan_pakai) VALUES
('RES001', 'OBTP001', 10, '3x1 sesudah makan'),
('RES001', 'OBTH001',  6, '2x1 pagi dan malam'),
('RES002', 'OBTP002', 15, '3x1 habiskan'),
('RES003', 'OBTP003', 12, '3x1 sesudah makan'),
('RES004', 'OBTP004',  5, '1x1 malam hari'),
('RES005', 'OBTP005', 14, '2x1 sebelum makan'),
('RES006', 'OBTP008',  1, '3x1 sendok teh'),
('RES007', 'OBTP009',  9, '3x1 sesudah makan'),
('RES008', 'OBTH003',  8, '2x1 sesudah makan'),
('RES009', 'OBTP006', 30, '1x1 pagi hari'),
('RES010', 'OBTP007', 20, '2x1 bersama makan'),
('RES010', 'OBTH005',  6, '1x1 malam hari');

-- Kunjungan
INSERT INTO kunjungan VALUES
('KUN001', 'RM-2024-001', 'DOK001', '2024-05-01', '09:00:00',
 'Demam dan pilek sejak 2 hari', 'Pasien terdiagnosa flu ringan',
 'DIS001', 'RES001', 150000, 'SELESAI'),
('KUN002', 'RM-2024-002', 'DOK002', '2024-05-02', '10:30:00',
 'Pusing dan tekanan darah tinggi', NULL, 'DIS002', NULL, 200000, 'BELUM_DILAKUKAN'),
('KUN003', 'RM-2024-003', 'DOK003', '2024-05-03', '08:30:00',
 'Batuk berdahak sejak seminggu', 'ISPA, diberi terapi simtomatik',
 'DIS009', 'RES003', 175000, 'SELESAI'),
('KUN004', 'RM-2024-004', 'DOK004', '2024-05-04', '11:00:00',
 'Sakit gigi geraham bawah', 'Karies gigi, dilakukan penambalan',
 NULL, 'RES004', 160000, 'SELESAI'),
('KUN005', 'RM-2024-005', 'DOK005', '2024-05-05', '09:15:00',
 'Mata merah dan berair', NULL, NULL, NULL, 180000, 'BELUM_DILAKUKAN'),
('KUN006', 'RM-2024-006', 'DOK006', '2024-05-06', '13:00:00',
 'Telinga berdenging', 'Tinitus ringan',
 NULL, 'RES006', 170000, 'SELESAI'),
('KUN007', 'RM-2024-007', 'DOK007', '2024-05-07', '10:00:00',
 'Gatal dan ruam di kulit', NULL, 'DIS007', NULL, 165000, 'BATAL'),
('KUN008', 'RM-2024-008', 'DOK008', '2024-05-08', '08:45:00',
 'Sakit kepala sebelah', 'Migrain tanpa aura',
 'DIS008', 'RES008', 210000, 'SELESAI'),
('KUN009', 'RM-2024-009', 'DOK009', '2024-05-09', '09:30:00',
 'Nyeri dada saat aktivitas', NULL, 'DIS002', NULL, 250000, 'BELUM_DILAKUKAN'),
('KUN010', 'RM-2024-010', 'DOK010', '2024-05-10', '10:00:00',
 'Kontrol kehamilan trimester 2', 'Kehamilan normal, ibu dan janin sehat',
 NULL, 'RES010', 190000, 'SELESAI');

-- Antrian
INSERT INTO antrian (nomor_urut, id_pasien, id_dokter, id_poliklinik, tanggal, status, jenis_kunjungan) VALUES
(1,  'PAS001', 'DOK001', 'POL001', '2024-05-01', 'SELESAI',          'BARU'),
(2,  'PAS002', 'DOK002', 'POL002', '2024-05-02', 'MENUNGGU',         'KONTROL'),
(3,  'PAS003', 'DOK003', 'POL003', '2024-05-03', 'SELESAI',          'BARU'),
(4,  'PAS004', 'DOK004', 'POL004', '2024-05-04', 'SELESAI',          'BARU'),
(5,  'PAS005', 'DOK005', 'POL005', '2024-05-05', 'DALAM_PEMERIKSAAN', 'BARU'),
(6,  'PAS006', 'DOK006', 'POL006', '2024-05-06', 'SELESAI',          'KONTROL'),
(7,  'PAS007', 'DOK007', 'POL007', '2024-05-07', 'BATAL',            'BARU'),
(8,  'PAS008', 'DOK008', 'POL008', '2024-05-08', 'SELESAI',          'RUJUKAN'),
(9,  'PAS009', 'DOK009', 'POL009', '2024-05-09', 'MENUNGGU',         'BARU'),
(10, 'PAS010', 'DOK010', 'POL010', '2024-05-10', 'SELESAI',          'KONTROL');

-- Rujukan
INSERT INTO rujukan VALUES
('RUJ001', 'KUN001', 'RM-2024-001', 'DOK001',
 'RS Umum Daerah - Poli Umum',
 'Pemeriksaan lanjutan kondisi pasien', '2024-05-01', '2024-05-15', 'DIGUNAKAN'),
('RUJ002', 'KUN002', 'RM-2024-002', 'DOK002',
 'RS Cipto Mangunkusumo - Kardiologi',
 'Hipertensi stadium 2 memerlukan penanganan spesialis jantung', '2024-05-02', '2024-05-16', 'AKTIF'),
('RUJ003', 'KUN003', 'RM-2024-003', 'DOK003',
 'RS Anak Harapan Kita - Poli Anak',
 'Evaluasi infeksi saluran napas berulang', '2024-05-03', '2024-05-17', 'AKTIF'),
('RUJ004', 'KUN004', 'RM-2024-004', 'DOK004',
 'RS Gigi dan Mulut - Bedah Mulut',
 'Tindakan pencabutan gigi impaksi', '2024-05-04', '2024-05-18', 'AKTIF'),
('RUJ005', 'KUN005', 'RM-2024-005', 'DOK005',
 'RS Mata Aini - Poli Mata',
 'Pemeriksaan konjungtivitis kronis', '2024-05-05', '2024-05-19', 'AKTIF'),
('RUJ006', 'KUN006', 'RM-2024-006', 'DOK006',
 'RS THT Proklamasi - Poli THT',
 'Audiometri lanjutan untuk tinitus', '2024-05-06', '2024-05-20', 'DIGUNAKAN'),
('RUJ007', 'KUN007', 'RM-2024-007', 'DOK007',
 'RS Kulit dan Kelamin - Dermatologi',
 'Penanganan dermatitis kronis', '2024-05-07', '2024-05-21', 'BATAL'),
('RUJ008', 'KUN008', 'RM-2024-008', 'DOK008',
 'RS Neurologi Nasional - Poli Saraf',
 'CT scan kepala untuk migrain berulang', '2024-05-08', '2024-05-22', 'AKTIF'),
('RUJ009', 'KUN009', 'RM-2024-009', 'DOK009',
 'RS Jantung Harapan Kita - Kardiologi',
 'Pemeriksaan EKG dan ekokardiografi', '2024-05-09', '2024-05-23', 'AKTIF'),
('RUJ010', 'KUN010', 'RM-2024-010', 'DOK010',
 'RS Ibu dan Anak - Obstetri',
 'USG kandungan lanjutan', '2024-05-10', '2024-05-24', 'DIGUNAKAN');

-- Tagihan
INSERT INTO tagihan VALUES
('TAG001', 'KUN001', '2024-05-01', 175000, 200000, 25000, 'TUNAI',    'LUNAS'),
('TAG002', 'KUN002', '2024-05-02', 200000,      0,     0, NULL,       'BELUM_BAYAR'),
('TAG003', 'KUN003', '2024-05-03', 217000, 220000,  3000, 'TUNAI',    'LUNAS'),
('TAG004', 'KUN004', '2024-05-04', 175000, 175000,     0, 'DEBIT',    'LUNAS'),
('TAG005', 'KUN005', '2024-05-05', 180000,      0,     0, NULL,       'BELUM_BAYAR'),
('TAG006', 'KUN006', '2024-05-06', 182000, 200000, 18000, 'TUNAI',    'LUNAS'),
('TAG007', 'KUN007', '2024-05-07', 165000,      0,     0, NULL,       'BELUM_BAYAR'),
('TAG008', 'KUN008', '2024-05-08', 282000, 282000,     0, 'TRANSFER', 'LUNAS'),
('TAG009', 'KUN009', '2024-05-09', 250000,      0,     0, NULL,       'BELUM_BAYAR'),
('TAG010', 'KUN010', '2024-05-10', 266000, 300000, 34000, 'TUNAI',    'LUNAS');

INSERT INTO tagihan_detail (id_tagihan, nama_item, jumlah, harga_satuan) VALUES
('TAG001', 'Biaya Konsultasi',  1, 150000),
('TAG001', 'Paracetamol 500mg', 10,  2500),
('TAG003', 'Biaya Konsultasi',  1, 175000),
('TAG003', 'Ibuprofen 400mg',   12,  3500),
('TAG004', 'Biaya Konsultasi',  1, 160000),
('TAG004', 'Cetirizine 10mg',   5,   3000),
('TAG006', 'Biaya Konsultasi',  1, 170000),
('TAG006', 'Salbutamol Sirup',  1,  12000),
('TAG008', 'Biaya Konsultasi',  1, 210000),
('TAG008', 'Temulawak 250mg',   8,   9000),
('TAG010', 'Biaya Konsultasi',  1, 190000),
('TAG010', 'Metformin 500mg',   20,  3800);

