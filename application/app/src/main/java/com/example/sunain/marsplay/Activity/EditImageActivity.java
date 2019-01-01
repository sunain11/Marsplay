package com.example.sunain.marsplay.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sunain.marsplay.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import es.dmoral.toasty.Toasty;


public class EditImageActivity extends AppCompatActivity {
    CropImageView cropImageView;
//    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
//        Intent intent = new Intent(context, EditImageActivity.class);
//        intent.setData(uri);
//        context.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        Bundle b=getIntent().getExtras();
        Uri uri = Uri.parse(b.getString("url"));
        Log.e("urierror",uri.toString());
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropImageView.setImageUriAsync(uri);
        cropImageView.setOnSetImageUriCompleteListener(new CropImageView.OnSetImageUriCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                //
            }
        });

        ImageButton ib_done=findViewById(R.id.edit_done);
        ib_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.getCroppedImageAsync();
            }
        });
        ImageButton ib_cancel=findViewById(R.id.edit_cancel);
        ib_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton ib_right=findViewById(R.id.edit_rotate_right);
        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage((int)(cropImageView.getRotation()+90));
//                switch ((int)cropImageView.getRotation())
//                {
//                    case 0:
//                        cropImageView.rotateImage(90);
//                        break;
//                    case 90:
//                        cropImageView.rotateImage(180);
//                        break;
//                    case 180:
//                        cropImageView.rotateImage(270);
//                        break;
//                    case 270:
//                        cropImageView.rotateImage(0);
//                        break;
//                }
            }
        });
        ImageButton ib_left=findViewById(R.id.edit_rotate_left);
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("rotation left",cropImageView.getRotation()+" - "+(cropImageView.getRotation()-90));
                cropImageView.rotateImage((int)(cropImageView.getRotation()-90));
            }
        });
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                Uri outputUri=save_bitmap(result.getBitmap());

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                    return;
                }
                if(outputUri!=null){
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("result",outputUri.toString());
                    Log.e("DDD",outputUri.toString());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                else
                    {
                        Toasty.error(getApplicationContext(),"Error Saving the picture",Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(EditImageActivity.this,"PERMISSION IS GRANTED",Toast.LENGTH_SHORT).show();

                } else {
                    Toasty.error(EditImageActivity.this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }

    }

    private void save_result(Uri outputUri)
    {
        if(outputUri!=null){
            Intent returnIntent = getIntent();
            returnIntent.putExtra("result",outputUri.toString());
            Log.e("DDD",outputUri.toString());
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
        else
        {
            Toasty.error(getApplicationContext(),"Error Saving the picture",Toast.LENGTH_SHORT).show();
        }
    }

    private Uri save_bitmap(Bitmap bm)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/marsplay/images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i("BM FILE", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
