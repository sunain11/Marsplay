package com.example.sunain.marsplay.Activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.example.sunain.marsplay.Adapter.ImageAdapter;
import com.example.sunain.marsplay.Model.Post;
import com.example.sunain.marsplay.R;
import com.example.sunain.marsplay.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robohorse.pagerbullet.PagerBullet;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductActivity extends AppCompatActivity {


    PagerBullet pagerBullet;
    ImageAdapter imageAdapter;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        add_pager_func();
        String key=getIntent().getExtras().getString("key");
        if(!new Utils(getApplicationContext()).isNetworkAvailable())
        {
            Toasty.warning(getApplicationContext(),"Oops! not internet",Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference("Marsplay/All/"+Utils.encodeEmail(mAuth.getCurrentUser().getEmail())+"/")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post Info = postSnapshot.getValue(Post.class);
                    if(Info.getKey().equals(key))
                    {
                        setFunction(Info);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void add_pager_func()
    {
        pagerBullet=findViewById(R.id.pagerBullet1);
        pagerBullet.setIndicatorTintColorScheme(Color.RED, Color.GREEN);
        imageAdapter=new ImageAdapter(getSupportFragmentManager(),true);
        pagerBullet.setAdapter(imageAdapter);
        pagerBullet.invalidateBullets();
        pagerBullet.getViewPager().setPageTransformer(true,new CubeOutTransformer());
        pagerBullet.getViewPager().setOffscreenPageLimit(3);
    }
    private void setFunction(Post p)
    {
        List<String> uriList = p.getUriList();
        imageAdapter.clearList();
        for(String uri:uriList)
        {
            imageAdapter.addFragment(Uri.parse(uri));
            imageAdapter.notifyDataSetChanged();
            pagerBullet.invalidateBullets();
        }
        TextView tv_title=findViewById(R.id.title_textview);
        tv_title.setText(p.getTitle()+"  ("+p.getUriList().size()+" Images)");
        TextView tv_content=findViewById(R.id.content_textview);
        tv_content.setText(p.getContent());
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
