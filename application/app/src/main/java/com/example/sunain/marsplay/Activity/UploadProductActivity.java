package com.example.sunain.marsplay.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.example.sunain.marsplay.Adapter.ImageAdapter;
import com.example.sunain.marsplay.Model.Post;
import com.example.sunain.marsplay.R;
import com.example.sunain.marsplay.Utils.Utils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.robohorse.pagerbullet.PagerBullet;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UploadProductActivity extends AppCompatActivity {

    PagerBullet pagerBullet;
    ImageAdapter imageAdapter;
    int GALLERY=1,CAMERA=2,CUSTOM_CAMERA=3,EDIT=4;
    Uri FilePathUri;
    List<Uri> uriList;
    ImageButton imageButton,imageButton2;
    Button post_buttom;
    FirebaseAuth mAuth;
    EditText et_title,et_content;
    int MY_EXTERNAL_STORAGE_REQUEST=1,MY_CAMERA_REQUEST=2,MY_EXTERNAL_CAMERA=3;
    public static String Storage_Path = "Remiel/All/";
    public static String Database_Path = "Remiel/All";
    StorageReference storageReference;
    List<String> result_uri_list;
    ProgressDialog progressDialog ;
    int[] colors={R.color.red,R.color.blue,R.color.green,R.color.yellow};
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_pager_func();
        mAuth=FirebaseAuth.getInstance();
        if (savedInstanceState != null)
        {
            uriList = (ArrayList<Uri>)savedInstanceState.getSerializable("array");
            imageAdapter.clearList();
            for(Uri uri:uriList)
            {
                imageAdapter.addFragment(uri);
                imageAdapter.notifyDataSetChanged();
                pagerBullet.invalidateBullets();
            }
        }
        else
        {
            uriList=new ArrayList<>();
        }
        set_views();
    }

    private void add_pager_func()
    {
        pagerBullet=findViewById(R.id.pagerBullet);
        pagerBullet.setIndicatorTintColorScheme(Color.RED, Color.GREEN);
        imageAdapter=new ImageAdapter(getSupportFragmentManager(),false);
        pagerBullet.setAdapter(imageAdapter);
        pagerBullet.invalidateBullets();
        pagerBullet.getViewPager().setPageTransformer(true,new CubeInTransformer());
        pagerBullet.getViewPager().setOffscreenPageLimit(3);
    }
    private void set_views()
    {
        imageButton2=findViewById(R.id.add_image_button);
        imageButton=findViewById(R.id.add_image_button2);
        et_title=findViewById(R.id.et_post_title);
        et_content=findViewById(R.id.et_post_content);
        post_buttom=findViewById(R.id.upload_product_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(R.style.MyAlertDialogStyle);
        set_func();
    }
    private void set_func()
    {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_options();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_options();
            }
        });
        post_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        if(uriList.size()>0)
        {
            imageButton.setVisibility(View.GONE);
            imageButton2.setVisibility(View.VISIBLE);
        }
        else
            {
                imageButton.setVisibility(View.VISIBLE);
                imageButton2.setVisibility(View.GONE);
            }
    }

    private void validate()
    {
        if(et_title.getText()==null){Toasty.warning(getApplicationContext(),"Title error",Toast.LENGTH_LONG).show();return;}
        if(et_title.getText().toString().length()==0){Toasty.warning(getApplicationContext(),"Please enter some title",Toast.LENGTH_LONG).show();return;}
        if(et_content.getText()==null){Toasty.warning(getApplicationContext(),"Content error",Toast.LENGTH_LONG).show();return;}
        if(et_content.getText().toString().length()==0){Toasty.warning(getApplicationContext(),"Please enter some content",Toast.LENGTH_LONG).show();return;}
        if(uriList==null)
        {
            Toasty.warning(getApplicationContext(),"Image errors",Toast.LENGTH_LONG).show();
            return;
        }
        if(uriList.size()==0)
        {
            Toasty.warning(getApplicationContext(),"Please select atleast 1 image",Toast.LENGTH_LONG).show();
            return;
        }
        startupload();
    }

    private void startupload()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
        result_uri_list=new ArrayList<>();
        progressDialog.setTitle("Uploading image "+1+"/"+uriList.size());
        progressDialog.show();
        upload_func(0);

    }

    private void upload_func(int i)
    {
        if(i==uriList.size())
        {
//            progressDialog.dismiss();
//            Toasty.success(getApplicationContext(),"Uploaded "+i+" images",Toast.LENGTH_LONG).show();
            second_upload();
            return;
        }
        Uri u=uriList.get(i);
        progressDialog.setTitle("Uploading image "+(i+1)+"/"+uriList.size());
        String image_name = "uu" + "_" + "uu" + "_" + System.currentTimeMillis() + ".jpg";
        Log.e("Image Name",image_name);
        final StorageReference filePath = storageReference.child("Marsplay/All/" + image_name);
        filePath.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();
                    result_uri_list.add(downUri.toString());
                    upload_func(i+1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "error in uploading images ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
    private void second_upload()
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("Marsplay/All/"+Utils.encodeEmail(mAuth.getCurrentUser().getEmail())+"/");
        Toasty.success(getApplicationContext(),"DONE with "+result_uri_list.size()+" images",Toast.LENGTH_LONG).show();
        progressDialog.setTitle("SAVING DATA");
        String title=et_title.getText().toString().toUpperCase().trim();
        String content=et_content.getText().toString().trim();
        String ImageUploadId = databaseReference.push().getKey();
        Post p=new Post(ImageUploadId,mAuth.getCurrentUser().getEmail(),title,content,result_uri_list);
         databaseReference.child(ImageUploadId).setValue(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(getApplicationContext(),"All Images Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toasty.success(getApplicationContext(),"Error in saving data",Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
             }
         });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(UploadProductActivity.this,"PERMISSION IS GRANTED",Toast.LENGTH_SHORT).show();
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY);

                } else {
                    Toasty.error(UploadProductActivity.this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(UploadProductActivity.this,"PERMISSION IS GRANTED",Toast.LENGTH_SHORT).show();

                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_EXTERNAL_CAMERA);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA);
                    }
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA);

                } else {
                    Toasty.error(UploadProductActivity.this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(UploadProductActivity.this,"PERMISSION IS GRANTED",Toast.LENGTH_SHORT).show();

                    if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_REQUEST);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA);
                    }
                } else {
                    Toasty.error(UploadProductActivity.this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void show_options()
    {
        ArrayList<String> options=new ArrayList<>();
        options.add("Gallery");
        options.add("Device Camera");
//        options.add("Custom Camera");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Choose an option")
                .setIcon(R.drawable.ic_add_black_24dp)
                .setMessage("select an option to pick image")
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        if(position==0)
                        {
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                            {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_EXTERNAL_STORAGE_REQUEST);
                            }
                            else
                            {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, GALLERY);
                            }
                            return;
                        }
                        if(position==1)
                        {
                            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                            {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_REQUEST);
                            }
                            else
                                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                                {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_EXTERNAL_CAMERA);
                                }
                                else
                                    {
                                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(cameraIntent, CAMERA);
                                    }
                            return;
                        }

//                        Toast.makeText(getApplicationContext(), position+" "+item,Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

//    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
//        Intent intent = new Intent(context, UploadProductActivity.class);
//        intent.setData(uri);
//        context.startActivity(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            FilePathUri = data.getData();
            Intent i=new Intent(UploadProductActivity.this,EditImageActivity.class);
            i.putExtra("url",FilePathUri.toString());
            startActivityForResult(i, EDIT);
            return;
        }

        if (requestCode == CAMERA)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            FilePathUri = tempUri;
            Intent i=new Intent(UploadProductActivity.this,EditImageActivity.class);
            i.putExtra("url",FilePathUri.toString());
            startActivityForResult(i, EDIT);
            return;
        }
        if (requestCode == EDIT && resultCode == RESULT_OK && data != null && data.getStringExtra("result") != null)
        {
            Log.e("DDD",data.getStringExtra("result"));
            Uri result_uri=Uri.parse(data.getStringExtra("result"));
            uriList.add(result_uri);
            Log.e("List Size",uriList.size()+"");
//            ImageFragment i=ImageFragment.newInstance(result_uri.toString());
//            imageAdapter.addFragment(i);
            imageAdapter.addFragment(result_uri);
            imageAdapter.notifyDataSetChanged();
            pagerBullet.setCurrentItem(imageAdapter.getCount()-1);
//            Log.e("Actor"," "+imageAdapter.getItemPosition(i));
            pagerBullet.invalidateBullets();
            if(uriList.size()>0)
            {
               imageButton.setVisibility(View.GONE);
               imageButton2.setVisibility(View.VISIBLE);
            }
            Toasty.info(UploadProductActivity.this,uriList.size()+" images",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("array", (Serializable) uriList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                super. onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
