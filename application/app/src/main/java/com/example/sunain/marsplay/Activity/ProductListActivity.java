package com.example.sunain.marsplay.Activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sunain.marsplay.Adapter.ProductListAdapter;
import com.example.sunain.marsplay.Model.Post;
import com.example.sunain.marsplay.R;
import com.example.sunain.marsplay.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private List<Post> postList;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
//    private SlideLayoutManager mSlideLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
//    private ItemTouchHelperCallback mItemTouchHelperCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        postList = new ArrayList<>();
        adapter = new ProductListAdapter(getApplicationContext(), postList);
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            setRecyclerView_portrait();
        }
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            setRecyclerView_landscape();
        }

    }

    private void setRecyclerView_portrait()
    {

//        recyclerView.setAdapter(adapter);
//        mItemTouchHelperCallback = new ItemTouchHelperCallback(recyclerView.getAdapter(), postList);
//        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
//        mSlideLayoutManager = new SlideLayoutManager(recyclerView, mItemTouchHelper);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setLayoutManager(mSlideLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        preparePosts();
    }

    private void setRecyclerView_landscape()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        preparePosts();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            setRecyclerView_landscape();
        }
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            setRecyclerView_portrait();
        }
    }

    private void preparePosts()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Posts");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Marsplay/All/"+Utils.encodeEmail(mAuth.getCurrentUser().getEmail())+"/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post Info = postSnapshot.getValue(Post.class);
                    postList.add(Info);
                }
                if(postList.isEmpty())
                {
                    Toasty.info(getApplicationContext(),"No Product in list",Toast.LENGTH_LONG).show();
                }
                Collections.reverse(postList);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),"Error Loading Products",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
