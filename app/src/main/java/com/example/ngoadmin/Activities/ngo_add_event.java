package com.example.ngoadmin.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ngoadmin.Adapters.ImagesAdapter;
import com.example.ngoadmin.GeneralFunctions.RequestPermisions;
import com.example.ngoadmin.GeneralFunctions.date_time;
import com.example.ngoadmin.GeneralFunctions.fixed_data;
import com.example.ngoadmin.GeneralFunctions.general_functions;
import com.example.ngoadmin.GeneralFunctions.transitions;
import com.example.ngoadmin.GeneralFunctions.webservice_calls;
import com.example.ngoadmin.Models.Category;
import com.example.ngoadmin.Models.DonationType;
import com.example.ngoadmin.Models.Image;
import com.example.ngoadmin.Models.ImageGallery;
import com.example.ngoadmin.R;
import com.example.ngoadmin.database_call.ImagePicker;
import com.example.ngoadmin.database_call.Master_Upload;
import com.example.ngoadmin.database_call.NetworkCall;
import com.example.ngoadmin.database_call.jsn;
import com.example.ngoadmin.database_call.utils_string;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.ngoadmin.GeneralFunctions.date_time.GetCurrentDate;
import static com.example.ngoadmin.GeneralFunctions.date_time.GetCurrentTime;
import static com.example.ngoadmin.GeneralFunctions.date_time.GetDateString;
import static com.example.ngoadmin.GeneralFunctions.date_time.GetImageName;
import static com.example.ngoadmin.GeneralFunctions.date_time.GetTextViewTimeString;
import static com.example.ngoadmin.GeneralFunctions.date_time.GetTimeString;
import static com.example.ngoadmin.GeneralFunctions.fixed_data.PICK_IMAGE;
import static com.example.ngoadmin.GeneralFunctions.fixed_data.READ_EXTERNAL_STORAGE;
import static com.example.ngoadmin.GeneralFunctions.fixed_data.WRITE_EXTERNAL_STORAGE;
import static com.example.ngoadmin.GeneralFunctions.general_functions.SingleQuoteString;
import static com.example.ngoadmin.GeneralFunctions.transitions.FadeInOut;
import static com.example.ngoadmin.GeneralFunctions.transitions.SlideUpDown;

public class ngo_add_event extends AppCompatActivity {

    private int ngo_id = 1;

    Animation animTitleSlideup,animTitleSlideDown;
    int flag_title_scroll = 0,
        flag_select_donation = 0,
        flag_select_event_type = 0,
        flag_add_event = 0,
        counter_image = 0;


    List<String> imagenames;

    LinearLayout ll_select_eventtype,ll_from_time,ll_to_time,ll_from_date,ll_to_date,
            ll_donation_volunteer,ll_add_photo,ll_toolbar_addevent;
    TextView tv_from_time,tv_to_time,tv_from_date,tv_to_date,tv_add_photo,tv_photos_counter,tv_event_category;
    TextView tv_final_submit,tv_donation_main,tv_volunteer_main;
    RelativeLayout rv_add_event_main,rl_title_addevent,rv_UniProgress;

    CheckBox cb_terms;
    PopupMenu popupMenu;
    List<Category> categories;

    private int mYear, mMonth, mDay;
    RecyclerView recycler_images;
    ImagesAdapter adapter;
    List<ImageGallery> ImageList;
    Uri imageUri;
    String uploadpath;

//    Donation Volunteer

    List<DonationType> donationtype;
    ScrollView sv_main_add_event;
    RelativeLayout rv_event_dv,rv_transparent_dark;
    TextView tv_dv_close,tv_dv_done,tv_donation_type_ae;

    LinearLayout ll_volunteer_submenu,ll_donation_submenu,ll_specify_others,ll_donation_goal,
    ll_donatio_type_ae;
    RadioButton rbtn_donation_yes,rbtn_donation_no,rbtn_volunteer_yes,rbtn_volunteer_no;
    RadioGroup rg_doantion,rg_volunteers;


    Boolean isVisible,isDarkVisible,b_progress_dialog=false;


// Errors
    //Main
    TextView err_title,err_description,err_address,err_time,err_event_type,err_date;
    //DV
    TextView err_donation_type,err_specify_others,err_donation_goal,err_volunteers_goal;


// Input
    String title,address,description,f_date,t_date,f_time,t_time,category_id;
    int image_flag = 0;
    String selected_donation_type_id;

    Boolean b_donation_yn = false,b_volunteer_yn = false;

    EditText et_title,et_address,et_description,et_specify_others,et_donation_goal,et_volunteers_goal_ae,
            et_volunteers_qualification, et_volunteers_description;


    Dialog progress;
    TextView progress_msg;
    String event_id= "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_add_event);

        isVisible = false;
        isDarkVisible = false;

        InitializeObjects();

        sv_main_add_event.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
                /*
                int diff = y-oldy;
                if(diff<0)
                    diff*=-1;

                if(diff>10){
                    if(y>oldy && flag_title_scroll == 0 ){
                        flag_title_scroll = 1;
                        SlideUpDown(rv_add_event_main,ll_toolbar_addevent,true,1);
                        //transitions.FadeInOut(ll_toolbar_addevent,true,ngo_add_event.this);
                        rl_title_addevent.startAnimation(animTitleSlideup);
                    }
                    else if(y<oldy && flag_title_scroll==1 && y <100){
                        flag_title_scroll = 0;
                        SlideUpDown(rv_add_event_main,ll_toolbar_addevent,false,1);
                        //transitions.FadeInOut(ll_toolbar_addevent,false,ngo_add_event.this);
                        rl_title_addevent.startAnimation(animTitleSlideDown);
                    }
                    else{}
                }

                if(y==0 && flag_title_scroll==1){
                    flag_title_scroll = 0;
                    SlideUpDown(rv_add_event_main,ll_toolbar_addevent,false,1);
                    //transitions.FadeInOut(ll_toolbar_addevent,false,ngo_add_event.this);
                    rl_title_addevent.startAnimation(animTitleSlideDown);
                }

                 */

                flag_title_scroll = transitions.TransitionTitleBar(y,oldy,flag_title_scroll,
                        rv_add_event_main, ll_toolbar_addevent,rl_title_addevent,
                        animTitleSlideup, animTitleSlideDown);

            }
        });
    }


    //Intialize all the objects of all the layouts
    private void InitializeObjects() {
        // ll_select_eventtype = findViewById(R.id.ll_select_eventtype);

//        progress = new Dialog(ngo_add_event.this);
//        progress.setContentView(R.layout.progress_dialog);
//        progress_msg = progress.findViewById(R.id.tv_progress_text);
//        progress.setCancelable(false);
//        progress.show();

        rv_UniProgress = findViewById(R.id.rv_UniProgress);
        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);

        imagenames = new ArrayList<>();
        sv_main_add_event = findViewById(R.id.sv_main_add_event);
        rv_add_event_main = findViewById(R.id.rv_add_event_main);
        ll_toolbar_addevent = findViewById(R.id.ll_toolbar_addevent);
        rl_title_addevent = findViewById(R.id.rl_title_addevent);
        InitializeAnimations();

        ll_select_eventtype = findViewById(R.id.ll_select_eventtype);
        tv_event_category = findViewById(R.id.tv_event_category_ae);

        /*
        categories.add(new Category("123","Upcoming Event"));
        categories.add(new Category("431","Past Event"));
        categories.add(new Category("987","Assam Fund Relief"));
        categories.add(new Category("567","Children Education"));
         */

        ll_from_time = findViewById(R.id.ll_from_time);
        ll_to_time = findViewById(R.id.ll_to_time);
        tv_from_time = findViewById(R.id.tv_from_time_ae);
        tv_to_time = findViewById(R.id.tv_to_time_ae);

        ll_from_date = findViewById(R.id.ll_from_date);
        ll_to_date = findViewById(R.id.ll_to_date);
        tv_from_date = findViewById(R.id.tv_from_date_ae);
        tv_to_date = findViewById(R.id.tv_to_date_ae);

        tv_final_submit = findViewById(R.id.tv_submit_ae);

        ll_add_photo = findViewById(R.id.ll_add_photo);
        recycler_images = findViewById(R.id.recycler_images_ae);
        tv_add_photo = findViewById(R.id.tv_add_photo_ae);
        ImageList = new ArrayList<ImageGallery>();
        tv_photos_counter = findViewById(R.id.tv_photos_counter_ae);
        tv_photos_counter.setText("(0/10)");
        tv_donation_main = findViewById(R.id.tv_donation_main_ae);
        tv_volunteer_main = findViewById(R.id.tv_volunteer_main_ae);

        ll_donation_volunteer = findViewById(R.id.ll_donation_volunteer);
        cb_terms = findViewById(R.id.cb_terms_ae);




//    Donate Volunteer
        rv_event_dv = findViewById(R.id.rv_event_dv);
        rv_transparent_dark = findViewById(R.id.rv_transparent_back);

        tv_dv_close = findViewById(R.id.tv_dv_close);
        tv_dv_done = findViewById(R.id.tv_dv_done_ae);
        ll_donation_submenu = findViewById(R.id.ll_donation_submenu);
        ll_volunteer_submenu = findViewById(R.id.ll_volunteers_submenu);
        ll_specify_others = findViewById(R.id.ll_specify_donation_type);
        ll_donation_goal = findViewById(R.id.ll_specify_donation_goal);

        rg_doantion = findViewById(R.id.rg_donation);
        rg_volunteers = findViewById(R.id.rg_volunteers);
        rbtn_donation_yes = findViewById(R.id.rb_donation_yes);
        rbtn_donation_no = findViewById(R.id.rb_donation_no);
        rbtn_volunteer_no = findViewById(R.id.rb_volunteers_no);
        rbtn_volunteer_yes = findViewById(R.id.rb_volunteers_yes);
        rbtn_volunteer_no.setChecked(true);
        rbtn_donation_no.setChecked(true);
        ll_volunteer_submenu.setVisibility(View.GONE);
        ll_donation_submenu.setVisibility(View.GONE);
        tv_donation_type_ae = findViewById(R.id.tv_donation_type_ae);
        ll_donatio_type_ae = findViewById(R.id.ll_select_donationtype);


        ll_donation_goal.setVisibility(View.GONE);
        ll_specify_others.setVisibility(View.GONE);

        InitializeEditTexts();
        InitializeErrors();

        SetTodaysDate();
        ClickMethods();
        SetImagesRecyclerView();
        FetchData();

    }

    private void FetchData() {
        categories = new ArrayList<>();
        categories = webservice_calls.GetEventCategories(this,""+ngo_id);

        donationtype = new ArrayList<>();
        donationtype = webservice_calls.GetDonationTypes(this,""+ngo_id);
        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
    }


    //Initialize edittexts
    private void InitializeEditTexts() {

        //Main
        et_title = findViewById(R.id.et_title_ae);
        et_address = findViewById(R.id.et_address_ae);
        et_description = findViewById(R.id.et_description_ae);


        //Donation Volunteer
        et_donation_goal = findViewById(R.id.et_donation_goal_ae);
        et_specify_others = findViewById(R.id.et_specify_others_ae);
        et_volunteers_goal_ae = findViewById(R.id.et_volunteers_goal_ae);
        et_volunteers_description = findViewById(R.id.et_volunteers_description_ae);
        et_volunteers_qualification = findViewById(R.id.et_volunteers_qualification_ae);

    }

    //Initialize Errors
    private void InitializeErrors() {
        //Main
        err_event_type = findViewById(R.id.err_event_type_ae);
        err_title = findViewById(R.id.err_title_ae);
        err_date = findViewById(R.id.err_date_ae);
        err_time = findViewById(R.id.err_time_ae);
        err_address = findViewById(R.id.err_address_ae);
        err_description = findViewById(R.id.err_description_ae);

        //DV
        err_donation_type = findViewById(R.id.err_donation_type_ae);
        err_donation_goal = findViewById(R.id.err_donation_goal_ae);
        err_specify_others = findViewById(R.id.err_specify_others_ae);
        err_volunteers_goal = findViewById(R.id.err_volunteers_goal_ae);

    }


    //Initialize Animations for titlebar
    private void InitializeAnimations() {

        animTitleSlideup = AnimationUtils.loadAnimation(this,
                R.anim.slide_up);

        animTitleSlideup.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animTitleSlideDown = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);

        animTitleSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    //Initialize today's date
    private void SetTodaysDate() {

        int[] currDate = GetCurrentDate();
        mYear = currDate[0];
        mMonth = currDate[1];
        mDay = currDate[2];

        tv_from_date.setText(mDay+" "+date_time.months[mMonth]+" "+mYear );
        tv_to_date.setText(mDay+" "+date_time.months[mMonth]+" "+mYear );
        f_date = t_date = mYear+"-"+mMonth+"-"+mDay;
        f_time = "10:00";
        t_time = "17:00";
        //GetDate(tv_from_date,mYear,mMonth,mDay);
        //GetDate(tv_to_date,mYear,mMonth,mDay);
    }

    //Click events for main layout
    private void ClickMethods() {

        ll_select_eventtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(ngo_add_event.this, view);
                for (int i = 0; i < categories.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, i, i, categories.get(i).getName());
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        flag_select_event_type = 1;
                        category_id = categories.get(menuItem.getItemId()).getId();
                        Toast.makeText(ngo_add_event.this, "ID:"+category_id
                                , Toast.LENGTH_SHORT).show();
                        tv_event_category.setText(menuItem.getTitle().toString());
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        ll_from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTimeFromTimePicker(tv_from_time);
            }
        });

        ll_to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTimeFromTimePicker(tv_to_time);
            }
        });

        ll_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDateFromDatePicker(tv_from_date,mYear,mMonth,mDay);
            }
        });

        ll_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDateFromDatePicker(tv_to_date,mYear,mMonth,mDay);
            }
        });

        tv_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestPermisions r = new RequestPermisions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE, ngo_add_event.this);
                RequestPermisions r1 = new RequestPermisions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE, ngo_add_event.this);

                if (r.checkPermission() && r1.checkPermission()) {
//                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                    startActivityForResult(gallery, PICK_IMAGE);
                    startActivityForResult(ImagePicker.getPickImageIntent(ngo_add_event.this), PICK_IMAGE);
                } else {
                    if (!r.checkPermission())
                        r.getPermission();
                    if (!r1.checkPermission())
                        r1.getPermission();
                }
            }
        });

        ll_donation_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animate_DV();
            }
        });


        tv_final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int b_main = ValidateMain();
                if(b_main == 1)
                    AddEvent();
                else{
                    sv_main_add_event.smoothScrollTo(0,0);
                }
            }
        });


        DonateVolunteer();
    }



    private int ValidateMain() {

        title = SingleQuoteString(et_title.getText().toString());
        address = SingleQuoteString(et_address.getText().toString());
        description = SingleQuoteString(et_description.getText().toString());

        if(
                !(title.equals("")) &&
                !(address.equals("")) &&
                !(description.equals("")) &&
                (flag_select_event_type != 0) &&
                (cb_terms.isChecked())  ) {
            return 1;
        }
        else {

            if(flag_select_event_type == 0)
                err_event_type.setVisibility(View.VISIBLE);
            else
                err_event_type.setVisibility(View.INVISIBLE);

            if(et_title.getText().toString().equals(""))
                err_title.setVisibility(View.VISIBLE);
            else
                err_title.setVisibility(View.INVISIBLE);

            if(et_address.getText().toString().equals(""))
                err_address.setVisibility(View.VISIBLE);
            else
                err_address.setVisibility(View.INVISIBLE);

            if(et_description.getText().toString().equals(""))
                err_description.setVisibility(View.VISIBLE);
            else
                err_description.setVisibility(View.INVISIBLE);

            if(!cb_terms.isChecked())
                Toast.makeText(this, "You must agree to the " +
                        "terms and conditions.", Toast.LENGTH_LONG).show();
            return 0;
        }

    }


    //Click events for donation volunteer layout
    private void DonateVolunteer() {

        tv_dv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // rv_event_dv.setVisibility(View.GONE);
                CheckDV();
            }
        });

        rg_doantion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rbtn = findViewById(id);
                if(rbtn.getId() == rbtn_donation_yes.getId()){
                    ll_donation_submenu.setVisibility(View.VISIBLE);
                    SetDonationErrors(View.GONE,View.GONE,View.GONE);
                    tv_donation_main.setText("Donation : Yes");
                    b_donation_yn = true;
                }
                else {
                    ll_donation_submenu.setVisibility(View.GONE);
                    tv_donation_main.setText("Donation : No");
                    b_donation_yn = false;
                }

            }
        });

        rg_volunteers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                RadioButton rbtn = findViewById(id);
                if(rbtn.getId() == rbtn_volunteer_yes.getId()){
                    ll_volunteer_submenu.setVisibility(View.VISIBLE);
                    tv_volunteer_main.setText("Volunteers : Yes");

                    b_volunteer_yn = true;
                }
                else{
                    ll_volunteer_submenu.setVisibility(View.GONE);
                    tv_volunteer_main.setText("Volunteers : No");
                    b_volunteer_yn = false;
                }
            }
        });

        tv_dv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckDV();

            }
        });

        ll_donatio_type_ae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(ngo_add_event.this, view);
                //final List<String> donationtype = fixed_data.GetDonationTypeList();
                for (int i = 0; i < donationtype.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, i, i, donationtype.get(i).getDonation_type());
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        tv_donation_type_ae.setText(menuItem.getTitle().toString());
                        SetDonationErrors(View.GONE,View.GONE,View.GONE);

                        selected_donation_type_id = donationtype.get(menuItem.getItemId()).getId();

                        if(selected_donation_type_id.equals("4")){
                            Toast.makeText(ngo_add_event.this, "Other Selected", Toast.LENGTH_SHORT).show();
                            flag_select_donation = 2;
                            ll_donation_goal.setVisibility(View.GONE);
                            ll_specify_others.setVisibility(View.VISIBLE);
                        }
                        else if(selected_donation_type_id.equals("1")){
                            flag_select_donation = 1;
                            Toast.makeText(ngo_add_event.this, "Money Selected", Toast.LENGTH_SHORT).show();
                            ll_donation_goal.setVisibility(View.VISIBLE);
                            ll_specify_others.setVisibility(View.GONE);
                        }
                        else{
                            flag_select_donation = 3;
                            ll_donation_goal.setVisibility(View.GONE);
                            ll_specify_others.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    //Handle back/done/close pressed in donation volunteer layout
    private void CheckDV() {

        Boolean bdon=false,bvol=false;

        if(rg_doantion.getCheckedRadioButtonId() == rbtn_donation_no.getId() &&
                rg_volunteers.getCheckedRadioButtonId() == rbtn_volunteer_no.getId()){
            Animate_DV();
            //Toast.makeText(this, b_donation_yn+" | "+b_volunteer_yn, Toast.LENGTH_SHORT).show();
        }
        else if(rg_doantion.getCheckedRadioButtonId() == rbtn_donation_yes.getId() ||
                rg_volunteers.getCheckedRadioButtonId() == rbtn_volunteer_yes.getId()){

            if(rg_doantion.getCheckedRadioButtonId() == rbtn_donation_yes.getId()){
                bdon = ValidateDonation();
            }
            else{
                bdon = true;
            }

            if(rg_volunteers.getCheckedRadioButtonId() == rbtn_volunteer_yes.getId()){
                bvol = ValidateVolunteers();
            }
            else{
                bvol = true;
            }

            if(bdon && bvol){
                Animate_DV();
                //Toast.makeText(this, b_donation_yn+" | "+b_volunteer_yn, Toast.LENGTH_SHORT).show();
            }

        }


    }

    private boolean ValidateDonation() {
        if(flag_select_donation == 0){
            SetDonationErrors(View.VISIBLE,View.GONE,View.GONE);
            return false;
        }
        else {

            if (flag_select_donation == 1) {
                if (et_donation_goal.getText().toString().equals("")) {
                    SetDonationErrors(View.GONE, View.GONE, View.VISIBLE);
                    return false;
                } else {
                    SetDonationErrors(View.GONE, View.GONE, View.GONE);
                    return true;
                }
            } else if (flag_select_donation == 2) {
                if (et_donation_goal.getText().toString().equals("")) {
                    SetDonationErrors(View.GONE, View.VISIBLE, View.GONE);
                    return false;
                } else {
                    SetDonationErrors(View.GONE, View.GONE, View.GONE);
                    return true;
                }
            } else {
                SetDonationErrors(View.GONE, View.GONE, View.GONE);
                return true;
            }
        }
    }

    private boolean ValidateVolunteers(){
        if(et_volunteers_goal_ae.getText().toString().equals("")){
            err_volunteers_goal.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            err_volunteers_goal.setVisibility(View.GONE);
            return true;
        }
    }

    private void SetDonationErrors(int donationtype, int specifyother, int donationgoal){

        err_donation_type.setVisibility(donationtype);
        err_specify_others.setVisibility(specifyother);
        err_donation_goal.setVisibility(donationgoal);

    }

    //Slide up and down transition for donation and volunteer layout
    public void Animate_DV(){

        isDarkVisible = FadeInOut(rv_transparent_dark,isDarkVisible,getApplicationContext(),300);
        isVisible = SlideUpDown(rv_add_event_main,rv_event_dv,isVisible,0);

    }

    //Initialize images in the recycler view
    private void SetImagesRecyclerView() {
        adapter = new ImagesAdapter(this,ImageList,tv_photos_counter,tv_add_photo,ll_add_photo);
        recycler_images.setItemAnimator(new DefaultItemAnimator());
        recycler_images.setAdapter(adapter);
        recycler_images.setHasFixedSize(false);
    }


    //After Picking Image From Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            try {

                Bitmap bitmap1 = ImagePicker.getImageFromResult(ngo_add_event.this, resultCode, data);
                uploadpath = MediaStore.Images.Media.insertImage
                        (ngo_add_event.this.getContentResolver()
                                , bitmap1, "testImg1", null);

//                uploadImage(finalUri);

                ImageList.add(new ImageGallery(1,bitmap1,uploadpath));

                tv_photos_counter.setText("("+ImageList.size()+"/10)");

                adapter.notifyItemInserted(ImageList.size());
                //adapter.notifyAll();
                if(ImageList.size() == fixed_data.add_event_max_images){
                    general_functions.DisabledView(this,ll_add_photo,tv_add_photo);
                }
                recycler_images.smoothScrollToPosition(ImageList.size()-1);
                //img_upload.setImageBitmap(bitmap1);

            } catch (Exception e) {
                Toast.makeText(this, "Catch:" + e, Toast.LENGTH_LONG).show();
            }

        }
    }


    //On Back Arrow Pressed
    @Override
    public void onBackPressed()
    {
        // code here on back pressed
        if(rv_event_dv.getVisibility() == View.VISIBLE){
            CheckDV();
        }
        // optional depending on your needs
        else
            super.onBackPressed();
    }


    //Open datepicker and fetch the date
    public void GetDateFromDatePicker(final TextView textView,int mYear,int mMonth,int mDay){

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mon, int day) {
                String mon1 = date_time.months[mon];
                Toast.makeText(ngo_add_event.this, year+" "+mon1+" "+day, Toast.LENGTH_SHORT).show();
                if(textView.getId() == tv_from_date.getId()){
                    f_date = GetDateString(day,mon,year);
                }
                else{
                    t_date = GetDateString(day,mon,year);
                }
                textView.setText(day+" "+mon1+" "+year );
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }

    //Open timepicker and fetch the time
    public void GetTimeFromTimePicker(final TextView textView){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.MyDatePickerDialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String finaltime = GetTimeString(hourOfDay,minute);

                        //Toast.makeText(ngo_add_event.this, ""+finaltime, Toast.LENGTH_SHORT).show();

                        String ftime = GetTextViewTimeString(finaltime);

                        if(textView.getId() == R.id.tv_from_time_ae){
                            tv_from_time.setText(""+ftime);
                            f_time = finaltime;
                        }
                        else{
                            tv_to_time.setText(""+ftime);
                            t_time = finaltime;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }



    private void AddEvent() {
        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
        flag_add_event = 0;
        //Toast.makeText(this, "Add Event", Toast.LENGTH_SHORT).show();
        int[] currTime = GetCurrentTime();
        String datetime_added = GetDateString(mDay,mMonth,mYear)+" "+currTime[0]+":"+currTime[1]+":"+currTime[2];

        title = SingleQuoteString(et_title.getText().toString());
        address = SingleQuoteString(et_address.getText().toString());
        description = SingleQuoteString(et_description.getText().toString());

        if(adapter.getItemCount() > 0)
            image_flag = 1;
        else {
            image_flag = 0;
            flag_add_event++;
        }
        /*String flag_add_event = webservice_calls.AddEvent(this,""+ngo_id,
                category_id,title,f_date,t_date,f_time,t_time,
                address,description,""+image_flag,datetime_added);
         */


        final HashMap<String,String> params = new HashMap<>();
        params.put("type","AddEvent");
        params.put("ngo_id",""+ngo_id);
        params.put("category_id",category_id);
        params.put("title",title);
        params.put("f_date",f_date);
        params.put("t_date",t_date);
        params.put("f_time",f_time);
        params.put("t_time",t_time);
        params.put("address",address);
        params.put("description",description);
        params.put("image_flag","0"+image_flag);
        params.put("datetime_added",datetime_added);


        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {

                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        flag_add_event++;
                        JSONObject id = jsn.getJSONObjectAt0(responseStr);
                        event_id = id.getString("id");
                        Toast.makeText(ngo_add_event.this, "Event ID : "+event_id, Toast.LENGTH_SHORT).show();

                        AddEventDonation(event_id);

                        AddEventVolunteers(event_id);

                        counter_image=0;

                        if(image_flag ==1) {
                            new UploadInBackground().execute();
                        }
                        dismissAddEventProgress();
                    }
                    else{
                        event_id = "";
                        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                        Toast.makeText(ngo_add_event.this, "AddEvent: "+responseStr, Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    event_id = "";
                    b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                    Toast.makeText(ngo_add_event.this, "Exception AddEvent : "+e.getMessage(), Toast.LENGTH_LONG).show();
                }


                return false;
            }
        });

    }



    private void AddEventVolunteers(String event_id) {
        if(b_volunteer_yn){

            HashMap<String, String> params1 = new HashMap<String, String>();
            params1.put("type","AddEventVolunteers");
            params1.put("event_id",event_id);
            params1.put("volunteers_goal",
                    SingleQuoteString(et_volunteers_goal_ae.getText().toString()));

            //money selected
            if(!et_volunteers_qualification.getText().toString().equals("")){
                params1.put("qualifications",
                        SingleQuoteString(et_volunteers_qualification.getText().toString()) );
            }
            //others selected
            else if(!et_volunteers_description.getText().toString().equals("")){
                params1.put("work_description",
                        SingleQuoteString(et_volunteers_description.getText().toString()) );
            }

            NetworkCall.call(params1).setDataResponseListener(new NetworkCall.SetDataResponse() {
                @Override
                public boolean setResponse(String responseStr) {
                    try {
                        JSONObject reader = new JSONObject(responseStr);
                        if (reader.getString("action").equals("1")) {
                            flag_add_event++;
                            dismissAddEventProgress();
                            JSONObject id = jsn.getJSONObjectAt0(responseStr);
                            final String event_donation = id.getString("id");
                            Toast.makeText(ngo_add_event.this, ""+event_donation, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                            Toast.makeText(ngo_add_event.this, "AddEventVolunteers : "+responseStr, Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e){
                        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                        Toast.makeText(ngo_add_event.this, "Exception AddEventVolunteers :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

        }
        else{
            flag_add_event++;
            dismissAddEventProgress();

        }
    }

    private void AddEventDonation(String event_id) {

        if(b_donation_yn){

            HashMap<String, String> params1 = new HashMap<String, String>();
            params1.put("type","AddEventDonation");
            params1.put("event_id",event_id);
            params1.put("donation_type_id",selected_donation_type_id);

            //money selected
            if(flag_select_donation == 1){
                params1.put("donation_goal",
                        SingleQuoteString(et_donation_goal.getText().toString()) );
            }
            //others selected
            else if(flag_select_donation == 2){
                params1.put("specify_others",
                        SingleQuoteString(et_specify_others.getText().toString()) );
            }

            NetworkCall.call(params1).setDataResponseListener(new NetworkCall.SetDataResponse() {
                @Override
                public boolean setResponse(String responseStr) {
                    try {
                        JSONObject reader = new JSONObject(responseStr);
                        if (reader.getString("action").equals("1")) {
                            flag_add_event++;
                            dismissAddEventProgress();
                            JSONObject id = jsn.getJSONObjectAt0(responseStr);
                            final String event_donation = id.getString("id");
                            Toast.makeText(ngo_add_event.this, ""+event_donation, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                            Toast.makeText(ngo_add_event.this, "AddEventDonation : "+responseStr, Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e){
                        b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                        Toast.makeText(ngo_add_event.this, "Exception AddEventDonation :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

        }
        else{
            flag_add_event++;
            dismissAddEventProgress();
        }

    }

    public void dismissAddEventProgress(){
        if(flag_add_event == 3){
            b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
            Toast.makeText(this, "Event Add Successful !", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImages(Uri imageUri,String imagetitle) {


        Master_Upload master_upload = new Master_Upload();
        JSONObject jsonObject = master_upload.Master_Upload(imageUri, this, imagetitle, ".jpeg",
                utils_string.IMAGE_URL.EVENT_IMAGES);

        counter_image++;

        try {
            if (jsonObject.getString("action").equals("1")) {

                HashMap<String, String> params = new HashMap<>();
                params.put("type", "AddEventImages");
                params.put("event_id", "" + event_id);
                params.put("imagename", imagetitle + ".jpeg");
                NetworkCall.call(params);
                flag_add_event++;
                dismissAddEventProgress();

            } else {
                b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
                Toast.makeText(this, "Image Upload Unsuccessful ! ", Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);
            Toast.makeText(this, "CatchJson:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public class UploadInBackground extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            for(int i = 0; i < ImageList.size(); i++){
                String imagetitle = GetImageName();
                UploadImages(Uri.parse(ImageList.get(i).getUploadPath()),imagetitle);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


}
