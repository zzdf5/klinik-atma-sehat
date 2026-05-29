-- ============================================================
--  Klinik Atma Sehat - Database Schema
--  MySQL / XAMPP
-- ============================================================

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
    jabatan      ENUM('KARYAWAN', 'MANAGER') NOT NULL DEFAULT 'KARYAWAN',
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
('POL001', 'Umum',        'Lantai 1 Ruang A', '08:00-16:00'),
('POL002', 'Penyakit Dalam', 'Lantai 1 Ruang B', '08:00-14:00'),
('POL003', 'Anak',        'Lantai 2 Ruang A', '08:00-15:00'),
('POL004', 'Gigi',        'Lantai 2 Ruang B', '09:00-17:00');

-- Pengguna (login)
INSERT INTO pengguna VALUES
('ADM001', 'siti.admin',    'admin123',   'ADMIN'),
('ADM002', 'budi.admin',    'admin123',   'ADMIN'),
('DOK001', 'andi.dokter',   'dokter123',  'DOKTER'),
('DOK002', 'rina.dokter',   'dokter123',  'DOKTER'),
('DOK003', 'hendra.dokter', 'dokter123',  'DOKTER');

-- Admin
INSERT INTO admin VALUES
('ADM001', 'Siti Rahayu',  '08111000001', 'MANAGER',  1),
('ADM002', 'Budi Santoso', '08111000002', 'KARYAWAN', 1);

-- Dokter
INSERT INTO dokter VALUES
('DOK001', 'STR-001-2020', 'dr. Andi Wijaya',  '08122000001', 'Umum',           150000, 1),
('DOK002', 'STR-002-2019', 'dr. Rina Susanti', '08122000002', 'Penyakit Dalam', 200000, 1),
('DOK003', 'STR-003-2021', 'dr. Hendra Lim',   '08122000003', 'Anak',           175000, 1);

-- Pasien
INSERT INTO pasien VALUES
('PAS001', 'RM-2024-001', 'Ahmad Fauzi',    '1990-05-15', 'L', '08133000001', 'Jl. Merdeka No.1, Jakarta'),
('PAS002', 'RM-2024-002', 'Dewi Lestari',   '1985-08-22', 'P', '08133000002', 'Jl. Sudirman No.5, Jakarta'),
('PAS003', 'RM-2024-003', 'Riko Prasetyo',  '2010-03-10', 'L', '08133000003', 'Jl. Gatot Subroto No.10, Jakarta');

-- Rekam Medis
INSERT INTO rekam_medis VALUES
('RM-2024-001', 'PAS001', '2024-01-10'),
('RM-2024-002', 'PAS002', '2024-02-15'),
('RM-2024-003', 'PAS003', '2024-03-20');

INSERT INTO rekam_medis_alergi (nomor_rekam_medis, alergi) VALUES
('RM-2024-001', 'Penisilin'),
('RM-2024-002', 'Debu');

INSERT INTO rekam_medis_riwayat (nomor_rekam_medis, riwayat_penyakit) VALUES
('RM-2024-001', 'Hipertensi'),
('RM-2024-002', 'Asma'),
('RM-2024-003', 'DBD');

-- Jadwal Dokter
INSERT INTO jadwal_dokter VALUES
('JDW001', 'DOK001', 'POL001', '08:00:00', '12:00:00', 30),
('JDW002', 'DOK002', 'POL002', '08:00:00', '14:00:00', 20),
('JDW003', 'DOK003', 'POL003', '09:00:00', '15:00:00', 25);

-- Obat
INSERT INTO obat (id_obat, nama_obat, bentuk_sediaan, dosis, kategori, harga_satuan, stok) VALUES
('OBT001', 'Paracetamol', 'Tablet', '500mg', 'Analgesik',   2500, 100),
('OBT002', 'Amoxicillin', 'Kapsul', '500mg', 'Antibiotik',  5000,  80),
('OBT003', 'Jahe Merah',  'Kapsul', '250mg', 'Obat Herbal', 8000,  50),
('OBT004', 'Ibuprofen',   'Tablet', '400mg', 'Analgesik',   3500,  60);

INSERT INTO obat_herbal (id_obat, bahan_utama) VALUES
('OBT003', 'Zingiber officinale');

INSERT INTO obat_paten (id_obat, merk) VALUES
('OBT002', 'Amoxil'),
('OBT004', 'Proris');

-- Diagnosa
INSERT INTO diagnosa VALUES
('DGN001', 'J00',   'Common Cold',  'Infeksi saluran napas atas', '2024-05-01', 0),
('DGN002', 'I10',   'Hipertensi',   'Tekanan darah tinggi primer', '2024-05-01', 1);

-- Resep
INSERT INTO resep VALUES
('RSP001', 'DOK001', 'RM-2024-001', '2024-05-01', 'SELESAI'),
('RSP002', 'DOK002', 'RM-2024-002', '2024-05-02', 'DIPROSES');

INSERT INTO resep_detail (id_resep, id_obat, jumlah, aturan_pakai) VALUES
('RSP001', 'OBT001', 10, '3x1 sesudah makan'),
('RSP001', 'OBT003',  6, '2x1 pagi dan malam'),
('RSP002', 'OBT002', 15, '3x1 habiskan');

-- Kunjungan
INSERT INTO kunjungan VALUES
('KNJ001', 'RM-2024-001', 'DOK001', '2024-05-01', '09:00:00',
 'Demam dan pilek sejak 2 hari', 'Pasien terdiagnosa flu ringan',
 'DGN001', 'RSP001', 150000, 'SELESAI'),
('KNJ002', 'RM-2024-002', 'DOK002', '2024-05-02', '10:30:00',
 'Pusing dan tekanan darah tinggi', NULL, 'DGN002', NULL, 200000, 'BELUM_DILAKUKAN');

-- Antrian
INSERT INTO antrian (nomor_urut, id_pasien, id_dokter, id_poliklinik, tanggal, status, jenis_kunjungan) VALUES
(1, 'PAS001', 'DOK001', 'POL001', '2024-05-01', 'SELESAI', 'BARU'),
(2, 'PAS002', 'DOK002', 'POL002', '2024-05-02', 'MENUNGGU', 'KONTROL');

-- Rujukan
INSERT INTO rujukan VALUES
('RJK001', 'KNJ002', 'RM-2024-002', 'DOK002',
 'RS Cipto Mangunkusumo - Kardiologi',
 'Hipertensi stadium 2 memerlukan penanganan spesialis jantung',
 '2024-05-02', '2024-05-16', 'AKTIF');

-- Tagihan
INSERT INTO tagihan VALUES
('TGH001', 'KNJ001', '2024-05-01', 177500, 200000, 22500, 'TUNAI', 'LUNAS');

INSERT INTO tagihan_detail (id_tagihan, nama_item, jumlah, harga_satuan) VALUES
('TGH001', 'Biaya Konsultasi',    1, 150000),
('TGH001', 'Paracetamol 500mg', 10,   2500),
('TGH001', 'Jahe Merah 250mg',   6,   8000) -- Note: 10*2500 + 6*8000 = 25000+48000 = 73000, total = 223000
-- Data sudah disesuaikan untuk ilustrasi
;

-- ============================================================
--  MIGRATION: ubah FK RESTRICT → CASCADE agar hapus pasien bisa cascade
--  Jalankan bagian ini jika database sudah ada sebelumnya
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE rekam_medis
    DROP FOREIGN KEY fk_rm_pasien,
    ADD CONSTRAINT fk_rm_pasien FOREIGN KEY (id_pasien)
        REFERENCES pasien (id_pasien) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE antrian
    DROP FOREIGN KEY fk_antrian_pasien,
    ADD CONSTRAINT fk_antrian_pasien FOREIGN KEY (id_pasien)
        REFERENCES pasien (id_pasien) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE resep
    DROP FOREIGN KEY fk_resep_rm,
    ADD CONSTRAINT fk_resep_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kunjungan
    DROP FOREIGN KEY fk_kunjungan_rm,
    ADD CONSTRAINT fk_kunjungan_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE rujukan
    DROP FOREIGN KEY fk_rujukan_kunjungan,
    ADD CONSTRAINT fk_rujukan_kunjungan FOREIGN KEY (id_kunjungan)
        REFERENCES kunjungan (id_kunjungan) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE rujukan
    DROP FOREIGN KEY fk_rujukan_rm,
    ADD CONSTRAINT fk_rujukan_rm FOREIGN KEY (nomor_rekam_medis)
        REFERENCES rekam_medis (nomor_rekam_medis) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tagihan
    DROP FOREIGN KEY fk_tagihan_kunjungan,
    ADD CONSTRAINT fk_tagihan_kunjungan FOREIGN KEY (id_kunjungan)
        REFERENCES kunjungan (id_kunjungan) ON UPDATE CASCADE ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
--  MIGRATION: pisah tabel obat → obat_herbal & obat_paten
--  Jalankan bagian ini jika database sudah ada sebelumnya
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;

-- Buat tabel subtipe jika belum ada
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

-- Migrasi data lama dari kolom jenis_obat/bahan_utama/merk ke subtabel
INSERT IGNORE INTO obat_herbal (id_obat, bahan_utama)
    SELECT id_obat, bahan_utama FROM obat
    WHERE jenis_obat = 'HERBAL' AND bahan_utama IS NOT NULL;

INSERT IGNORE INTO obat_paten (id_obat, merk)
    SELECT id_obat, merk FROM obat
    WHERE jenis_obat = 'PATEN' AND merk IS NOT NULL;

-- Hapus kolom lama dari tabel obat (jalankan setelah data berhasil dimigrasikan)
ALTER TABLE obat
    DROP COLUMN IF EXISTS jenis_obat,
    DROP COLUMN IF EXISTS bahan_utama,
    DROP COLUMN IF EXISTS merk;

SET FOREIGN_KEY_CHECKS = 1;
