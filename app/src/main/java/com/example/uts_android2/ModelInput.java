package com.example.uts_android2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vaccine_table")
public class ModelInput {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nik")
    private String nik;

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "ttl")
    private String ttl;

    @ColumnInfo(name = "tglvaksin")
    private String tglvaksin;

    @ColumnInfo(name = "alamat")
    private String alamat;

    @ColumnInfo(name = "rumahsakit")
    private String rumahsakit;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public void setId(int id) {
        this.id = id;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public void setTglvaksin(String tglvaksin) {
        this.tglvaksin = tglvaksin;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setRumahsakit(String rumahsakit) {
        this.rumahsakit = rumahsakit;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ModelInput(String strNik, String strNama, String strTanggalLahir, String strTanggalVaksin, String strAlamat, String namaRS, byte[] imageBytes) {
        this.nik = nik;
        this.nama = nama;
        this.ttl = ttl;
        this.tglvaksin = tglvaksin;
        this.alamat = alamat;
        this.rumahsakit = rumahsakit;
        this.image = image;
    }
}
