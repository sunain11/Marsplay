package com.example.sunain.marsplay.Fragment;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.example.sunain.marsplay.R;
//import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment extends Fragment {

    ImageView i;
    AlertDialog mDialog;
    public static ImageFragment newInstance(String uri,boolean b) {

        Bundle args = new Bundle();
        ImageFragment fragment = new ImageFragment();
        args.putString("uri",uri);
        args.putBoolean("bool",b);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_image,container,false);
        i=v.findViewById(R.id.image_view);
        String u=this.getArguments().getString("uri");
        boolean b=this.getArguments().getBoolean("bool");
        Glide.with(getContext()).load(Uri.parse(u)).into(i);
        if(b==true) {
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                    ImageView photoView = mView.findViewById(R.id.zoomimageView);
                    Glide.with(getContext()).load(Uri.parse(u)).into(photoView);
                    ImageView b=mView.findViewById(R.id.dailog_close);
//                photoView.setImageResource(R.drawable.nature);
//                photoView.setImageURI(Uri.parse(u));
                    photoView.setOnTouchListener(new ImageMatrixTouchHandler(getContext()));
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try
                            {
                                mDialog.dismiss();
//                                dismiss_func();
                            }catch (Exception e){}
                        }
                    });
                    mBuilder.setView(mView);
                    mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
        }
        return v;
    }

    private void dismiss_func()
    {

    }

}
