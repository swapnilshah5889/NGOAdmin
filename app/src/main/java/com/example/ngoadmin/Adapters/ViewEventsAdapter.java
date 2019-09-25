package com.example.ngoadmin.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngoadmin.Activities.ngo_add_event;
import com.example.ngoadmin.Models.Category;
import com.example.ngoadmin.R;
import com.example.ngoadmin.database_call.NetworkCall;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.example.ngoadmin.GeneralFunctions.general_functions.SingleQuoteString;
import static com.example.ngoadmin.GeneralFunctions.transitions.FadeInOut;
import static com.example.ngoadmin.GeneralFunctions.transitions.SlideUpDown;

public class ViewEventsAdapter extends RecyclerView.Adapter<ViewEventsAdapter.MyViewHolder> {

    Context context;
    List<Category> categories;
    AlertDialog alertDialog;
    Boolean isDarkVisible=false,isVisible=false;
    RelativeLayout rv_transparent_dark;
    CoordinatorLayout rv_view_events_main;
    LinearLayout ll_edit_category_dialog;
    final Dialog progress;

    public ViewEventsAdapter(Context context, List<Category> categories, CoordinatorLayout rv_view_events_main,
                             RelativeLayout rv_transparent_dark, LinearLayout ll_edit_category_dialog) {
        this.context = context;
        this.categories = categories;
        this.rv_transparent_dark = rv_transparent_dark;
        this.rv_view_events_main = rv_view_events_main;
        this.ll_edit_category_dialog = ll_edit_category_dialog;
        progress = new Dialog(context);
        progress.setContentView(R.layout.progress_dialog_2);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewevents_event_layout, parent, false);
        return new ViewEventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Category c = categories.get(position);
        holder.tv_category_name.setText(c.getName());


        holder.ll_view_all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.ll_category_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                //final List<String> donationtype = fixed_data.GetDonationTypeList();
                popupMenu.getMenu().add(Menu.NONE, 0, 0,"Edit");
                popupMenu.getMenu().add(Menu.NONE, 1, 1,"Delete");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        //Edit Clicked
                        if(menuItem.getItemId() == 0){
                            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                            final EditText et_cat_name = ll_edit_category_dialog.findViewById(R.id.et_category_name_edit);
                            et_cat_name.setText(c.getName());
                            isDarkVisible = FadeInOut(rv_transparent_dark,isDarkVisible,context,300);
                            isVisible = SlideUpDown(rv_view_events_main,ll_edit_category_dialog,isVisible,0);

                            Button btn_cancel = ll_edit_category_dialog.findViewById(R.id.btn_cancel_edit_category);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    isDarkVisible = FadeInOut(rv_transparent_dark,isDarkVisible,context,300);
                                    isVisible = SlideUpDown(rv_view_events_main,ll_edit_category_dialog,isVisible,0);
                                }
                            });

                            Button btn_update = ll_edit_category_dialog.findViewById(R.id.btn_update_category);
                            btn_update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(et_cat_name.getText().toString().equals(""))
                                        et_cat_name.setError("Required");
                                    else{

                                        if (!c.getName().equals(et_cat_name.getText().toString())) {
                                            isDarkVisible = FadeInOut(rv_transparent_dark,isDarkVisible,context,300);
                                            isVisible = SlideUpDown(rv_view_events_main,ll_edit_category_dialog,isVisible,0);
                                            progress.show();
                                            HashMap<String,String> params = new HashMap<>();
                                            params.put("type","UpdateCategory");
                                            params.put("category_id",c.getId());
                                            params.put("category_name",SingleQuoteString(et_cat_name.getText().toString()));
                                            NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
                                                @Override
                                                public boolean setResponse(String responseStr) {
                                                    try{

                                                        JSONObject reader = new JSONObject(responseStr);
                                                        if (reader.getString("action").equals("1")) {
                                                        }
                                                        else{
                                                            Toast.makeText(context, "UpdateCategory: "+responseStr, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    catch (Exception e) {
                                                        Toast.makeText(context, "UpdateCategory: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    progress.dismiss();
                                                    return false;
                                                }
                                            });


                                        } else {
                                            isDarkVisible = FadeInOut(rv_transparent_dark, isDarkVisible, context, 300);
                                            isVisible = SlideUpDown(rv_view_events_main, ll_edit_category_dialog, isVisible, 0);
                                        }
                                    }
                                }
                            });

                        }

                        //Delete Clicked
                        else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Deleting a category also deletes all the events under the category. " +
                                    "Are you sure you want to delete this category ?");
                            builder.setTitle("Delete "+categories.get(position).getName());
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    progress.show();

                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("type","DeleteCategory");
                                    params.put("category_id",c.getId());

                                    NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
                                        @Override
                                        public boolean setResponse(String responseStr) {

                                            try{
                                                JSONObject reader = new JSONObject(responseStr);
                                                if (reader.getString("action").equals("1")) {
                                                    categories.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position,categories.size());
                                                    notifyDataSetChanged();
                                                    progress.dismiss();
                                                }
                                                else{
                                                    progress.dismiss();
                                                    Toast.makeText(context, "Error : "+responseStr, Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (Exception e){
                                                progress.dismiss();
                                                Toast.makeText(context, "DeleteCategory: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            return false;
                                        }
                                    });

                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.show();

                            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            positiveButton.setTextColor(Color.parseColor("#ff000000"));
                            negativeButton.setTextColor(Color.parseColor("#ff000000"));


                        }

                        return true;
                    }
                });
                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_category_name;
        LinearLayout ll_view_event_layout_main,ll_view_all_events,ll_category_options;

        public MyViewHolder(@NonNull View view) {
            super(view);

            tv_category_name = view.findViewById(R.id.tv_category_name);
            ll_view_event_layout_main = view.findViewById(R.id.ll_view_event_layout_main);
            ll_view_all_events = view.findViewById(R.id.ll_view_all_events);
            ll_category_options = view.findViewById(R.id.ll_category_options);
        }
    }
}
