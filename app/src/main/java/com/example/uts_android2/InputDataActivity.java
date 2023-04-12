package com.example.uts_android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InputDataActivity extends AppCompatActivity {

    public static final int REQUEST_PICK_PHOTO = 1;
    int REQ_CAMERA = 101;
    int umurPeserta;
    byte[] imageBytes;
    File fileDirectory, imageFilename;
    InputViewModel inputViewModel;
    Toolbar toolbar;
    EditText inputNik, inputNama, inputTanggalLahir, inputTanggalVaksin, inputAlamat;
    ImageView imageKTP, btnGallery, btnCamera;
    ExtendedFloatingActionButton fabSave;
    String strNik, strNama, strTanggalLahir, strTanggalVaksin, strAlamat, strTimeStamp, strImageName, strFilePath, strEncodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        toolbar = findViewById(R.id.toolbar);
        inputNik = findViewById(R.id.inputNIK);
        inputNama = findViewById(R.id.inputNama);
        inputTanggalLahir = findViewById(R.id.inputTanggalLahir);
        inputTanggalVaksin = findViewById(R.id.inputTanggalVaksin);
        inputAlamat = findViewById(R.id.inputAlamat);
        imageKTP = findViewById(R.id.imageKTP);
        btnGallery = findViewById(R.id.imageGallery);
        btnCamera = findViewById(R.id.imageCamera);
        fabSave = findViewById(R.id.fabSave);

        setStaturBar();
        setInitLayout();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setInitLayout() {
        inputViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(InputViewModel.class);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        inputTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar tanggalLahir = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tanggalLahir.set(Calendar.YEAR, year);
                        tanggalLahir.set(Calendar.MONTH, monthOfYear);
                        tanggalLahir.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String strFormatDefault = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                        inputTanggalLahir.setText(simpleDateFormat.format(tanggalLahir.getTime()));
                    }
                };

                new DatePickerDialog(InputDataActivity.this, R.style.DialogTheme, date, tanggalLahir.get(Calendar.YEAR), tanggalLahir.get(Calendar.MONTH), tanggalLahir.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        inputTanggalVaksin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar tanggalVaksin = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tanggalVaksin.set(Calendar.YEAR, year);
                        tanggalVaksin.set(Calendar.MONTH, monthOfYear);
                        tanggalVaksin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String strFormatDefault = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                        inputTanggalVaksin.setText(simpleDateFormat.format(tanggalVaksin.getTime()));
                    }
                };

                new DatePickerDialog(InputDataActivity.this, R.style.DialogTheme, date, tanggalVaksin.get(Calendar.YEAR), tanggalVaksin.get(Calendar.MONTH), tanggalVaksin.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(InputDataActivity.this).withPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCES_COARR
                , Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(InputDataActivity.this).withPermission(Manifest, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()) {
                            try {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(InputDataActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                                startActivityForResult(intent, REQ_CAMERA);
                            } catch (IOException ex) {
                                Toast.makeText(InputDataActivity.this, "Gagal membuka kamera!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNik = inputNik.getText().toString();
                strNama = inputNama.getText().toString();
                strTanggalLahir = inputTanggalLahir.getText().toString();
                strTanggalVaksin = inputTanggalVaksin.getText().toString();
                strAlamat = inputAlamat.getText().toString();
                umurPeserta = Constant.cekUmurPeserta(inputTanggalLahir.getText().toString());

                if (strNik.trim().isEmpty() || strNama.trim().isEmpty() || strTanggalLahir.trim().isEmpty() || strAlamat.trim().isEmpty() || strFilePath == null) {
                    Toast.makeText(InputDataActivity.this, "Mohon lengkapi data di form perndaftaran!", Toast.LENGTH_SHORT).show();
                } else if (umurPeserta <= 17) {
                    Toast.makeText(InputDataActivity.this, "Umur harus lebih dari 17 tahun!", Toast.LENGTH_SHORT).show();
                } else {
                    ModelInput modelInput = new ModelInput(strNik,strNama,strTanggalLahir,strTanggalVaksin,strAlamat,Constant.namaRS,imageBytes);
                    inputViewModel.insert(modelInput);
                    Toast.makeText(InputDataActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

//    private void setSupportActionBar(Toolbar toolbar) {
//    }

    private File createImageFile() throws IOException {
        strTimeStamp = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date());
        strImageName = "IMG_";
        fileDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "");
        imageFilename = File.createTempFile(strImageName, ".jpg", fileDirectory);
        strFilePath = imageFilename.getAbsolutePath();
        return imageFilename;
    }

    @Override
    public void  onRequestPermissionResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        for (int grantResult  : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            convertImage(strFilePath);
        } else if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert  selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String mediaPath = cursor.getString(columnIndex);
            cursor.close();
            strFilePath = mediaPath;
            convertImage(mediaPath);
        }
    }

    private void convertImage(String strFilePath) {
        File imageFile = new File(imageFilePath);
        if(imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Glide.with(this).load(bitmap).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_image_upload).into(imageKTP);
            imageBytes = baos.toByteArray();
            strEncodedImage = Base64.getEncoder().encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    private void setStaturBar() {
        if(Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final  int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }


}