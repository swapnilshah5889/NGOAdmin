package com.example.ngoadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngoadmin.GeneralFunctions.general_functions;
import com.example.ngoadmin.Models.ImageGallery;
import com.example.ngoadmin.GeneralFunctions.general_functions;
import com.example.ngoadmin.Models.ImageGallery;
import com.example.ngoadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.ngoadmin.GeneralFunctions.fixed_data.add_event_max_images;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{

    Context context;
    private List<ImageGallery> images;
    TextView counter,tv_add_photo;
    LinearLayout ll_add_photo;

    public ImagesAdapter(Context context, List<ImageGallery> images, TextView counter,
                         TextView tv_addphoto, LinearLayout ll_add_photo) {
        this.context = context;
        this.images = images;
        this.counter = counter;
        this.ll_add_photo = ll_add_photo;
        this.tv_add_photo = tv_addphoto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagelayout_add_event, parent, false);
        return new ImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ImageGallery curImage = images.get(position);
        holder.img.setImageBitmap(curImage.getImgBitmap());

        holder.fab_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(images.size() == add_event_max_images){
                    general_functions.EnabledView(context,ll_add_photo,tv_add_photo);
                }
                images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,images.size());
                notifyDataSetChanged();
                counter.setText("("+images.size()+"/10)");
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        FloatingActionButton fab_delete_image;


        public MyViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img_event_add);
            fab_delete_image = view.findViewById(R.id.fab_delete_img);
        }
    }
}
