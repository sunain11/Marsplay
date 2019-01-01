package com.example.sunain.marsplay.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sunain.marsplay.Activity.ProductActivity;
import com.example.sunain.marsplay.Model.Post;
import com.example.sunain.marsplay.R;

import java.util.List;

//import com.bumptech.glide.Glide;
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> postList;
    private static Context sContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
//        , overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    private static void setContext(Context c)
    {
        sContext=c;
    }

    public ProductListAdapter(Context mContext, List<Post> albumList) {
        this.mContext = mContext;
        this.postList = albumList;
        setContext(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Post post = postList.get(position);
        holder.title.setText(post.getTitle()+" ("+post.getUriList().size()+")");
        holder.count.setText(post.getContent());
        Glide.with(mContext).load(Uri.parse(post.getUriList().get(0))).into(holder.thumbnail);
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow,position);
//            }
//        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,ProductActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("key",post.getKey());
                mContext.startActivity(i);
            }
        });

    }


    private static void animate(Post p,MyViewHolder holder)
    {
        int oldp,newp;
        int[] i = {0};
        Handler handler = new Handler();
        while (true)
        {
            handler.postDelayed(new Runnable() {
                public void run() {
                    Glide.with(sContext).load(Uri.parse(p.getUriList().get(i[0]))).into(holder.thumbnail);
                    handler.postDelayed(this, 500);
                    i[0]++;
                    if (i[0] == p.getUriList().size()) {
                        i[0] = 0;
                    }
                }
            }, 500);
        }
    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view,int position) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener() {
        }

        public MyMenuItemClickListener(int position) {
            this.position=position;
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
//                    Toasty.info(mContext, "Delete "+postList.get(position).getP_name(), Toast.LENGTH_SHORT).show();
//                    try {
//                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Remiel/All_Post_Uploads_Database").child(Utils.encodeEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//                        databaseReference1.child(postList.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toasty.info(mContext, "Product " + postList.get(position).getP_name() + " deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Toast.makeText(context,"error:"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }catch (Exception e){}
                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}