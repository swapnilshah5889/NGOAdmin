package com.example.ngoadmin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ngoadmin.Adapters.ViewEventsAdapter;
import com.example.ngoadmin.GeneralFunctions.transitions;
import com.example.ngoadmin.GeneralFunctions.webservice_calls;
import com.example.ngoadmin.Models.Category;
import com.example.ngoadmin.R;
import com.example.ngoadmin.database_call.NetworkCall;
import com.example.ngoadmin.database_call.jsn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.ngoadmin.GeneralFunctions.general_functions.SingleQuoteString;
import static com.example.ngoadmin.GeneralFunctions.transitions.FadeInOut;
import static com.example.ngoadmin.GeneralFunctions.transitions.SlideUpDown;

public class ngo_view_all_events extends AppCompatActivity {

    String ngo_id = "1";

    RelativeLayout rv_UniProgress,rl_title_viewevents,rv_transparent_dark;
    LinearLayout ll_add_category_dialog,ll_edit_category_dialog;
    CoordinatorLayout rv_view_events_main;
    LinearLayout ll_toolbar_viewevents;
    Boolean isVisible=false,isDarkVisible=false,b_progress_dialog=false;
    Animation animTitleSlideup,animTitleSlideDown;
    //ScrollView sv_main_view_events;
    int flag_title_scroll = 0;

    Button btn_cancel_add_category,btn_add_category;

    List<Category> categoryList;
    ViewEventsAdapter adapter;
    RecyclerView rv_categories;

    NestedScrollView nestedScrollView;

    FloatingActionButton fab_add_category;
    int oldx=0,oldy=0;

    EditText et_category_name;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_view_all_events);


        InitializeObjects();

        /*rv_categories.setOnScrollListener(new RecyclerView.OnScrollListener() {
              @Override
              public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                  super.onScrollStateChanged(recyclerView, newState);
              }

              @Override
              public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                  super.onScrolled(recyclerView, dx, dy);

                  if(oldy > dy){
                      fab_add_category.hide();
                  }
                  else
                      fab_add_category.show();

                  oldy = dy;
              }
          }

        );*/

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {

                flag_title_scroll = transitions.TransitionTitleBar(y,oldy,flag_title_scroll,
                        rv_view_events_main, ll_toolbar_viewevents,rl_title_viewevents,
                        animTitleSlideup, animTitleSlideDown);

            }
        });

    }

    private void InitializeObjects() {

        rv_transparent_dark = findViewById(R.id.rv_transparent_back);
        ll_add_category_dialog = findViewById(R.id.ll_add_category_dialog);
        ll_edit_category_dialog = findViewById(R.id.ll_edit_category_dialog);
        rv_UniProgress = findViewById(R.id.rv_UniProgress);
        //b_progress_dialog = FadeInOut(rv_UniProgress,b_progress_dialog,getApplicationContext(),100);

        btn_cancel_add_category = findViewById(R.id.btn_cancel_add_category);
        btn_add_category = findViewById(R.id.btn_add_category);
        et_category_name = findViewById(R.id.et_category_name);
        rv_view_events_main = findViewById(R.id.rv_view_events_main);
        ll_toolbar_viewevents = findViewById(R.id.ll_toolbar_viewevents);
        rl_title_viewevents = findViewById(R.id.rl_title_viewevents);
        nestedScrollView = findViewById(R.id.sv_main_view_events);

        fab_add_category = findViewById(R.id.fab_add_category);

        rv_categories =findViewById(R.id.rv_categories);
        InitializeAnimations();

        GetCategories();

        ClickEvents();
    }

    private void ClickEvents() {
        fab_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animate_Add_Category_Dialog();
            }
        });

        btn_cancel_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animate_Add_Category_Dialog();
            }
        });
        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_category_name.getText().toString().equals("")){
                    AddCategory();
                    HideKeyboard();
                    Animate_Add_Category_Dialog();
                }
                else{
                    et_category_name.setError("Required");
                }

            }
        });
    }


    public void HideKeyboard(){
        View view1 = this.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }


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


    private void SetRecyclerView() {


        adapter = new ViewEventsAdapter(this,categoryList,rv_view_events_main,
                rv_transparent_dark,ll_edit_category_dialog);
        rv_categories.setItemAnimator(new DefaultItemAnimator());
        rv_categories.setAdapter(adapter);
        rv_categories.setHasFixedSize(false);

    }

    private void GetCategories() {
        categoryList = new ArrayList<>();
        HashMap<String,String> params = new HashMap<>();
        params.put("type","GetEventCategories");
        params.put("ngo_id",ngo_id);
        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {
                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        JSONArray catreader = reader.getJSONArray("message");
                        for (int i = 0; i < catreader.length(); i++) {
                            JSONObject t = catreader.getJSONObject(i);
                            categoryList.add(new Category(t.getString("id"), t.getString("category_name")));
                        }
                        SetRecyclerView();
                    }
                    else{
                        Toast.makeText(ngo_view_all_events.this, "There was some problem. Please" +
                                "try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(ngo_view_all_events.this, "Exception GetEventCategories : "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

    }


    //Slide up and down transition for donation and volunteer layout
    public void Animate_Add_Category_Dialog(){

        if(isVisible)
            fab_add_category.setImageResource(R.drawable.ic_add);
        else
            fab_add_category.setImageResource(R.drawable.ic_cross_thin);


        isDarkVisible = FadeInOut(rv_transparent_dark,isDarkVisible,getApplicationContext(),300);
        isVisible = SlideUpDown(rv_view_events_main,ll_add_category_dialog,isVisible,0);

    }


    private void AddCategory() {

        HashMap<String,String> params = new HashMap<>();
        params.put("type","AddCategory");
        params.put("ngo_id",ngo_id);
        params.put("category_name", SingleQuoteString(et_category_name.getText().toString()) );

        NetworkCall.call(params).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public boolean setResponse(String responseStr) {

                try {
                    JSONObject reader = new JSONObject(responseStr);
                    if (reader.getString("action").equals("1")) {
                        JSONObject id = jsn.getJSONObjectAt0(responseStr);
                        categoryList.add(new Category( id.getString("id"),
                                et_category_name.getText().toString() ));

                        adapter.notifyItemInserted(categoryList.size());
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
                catch (Exception e){
                    Toast.makeText(ngo_view_all_events.this, "AddCategory: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }
}
