package com.cropprediction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DashAdapter dashAdapter;
    MySharedPreference mySharedPreference;
    private SliderLayout mDemoSlider;
    TextView tvIntro;
    public static final String title[] = {"Predict Crop", "Near By Farmer", "Pesticides", "Guide"};
    public static final int img[] = {R.drawable.iccrop, R.drawable.icfarmer, R.drawable.pesti, R.drawable.guide};
    GridView gridMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_activityy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySharedPreference = new MySharedPreference(this);

       /* tvIntro = (TextView) findViewById(R.id.tvIntro);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/sample.otf");
        tvIntro.setTypeface(type);*/
        //slider
        gridMenu = (GridView) findViewById(R.id.gridMenu);
        dashAdapter = new DashAdapter(DrawerActivity.this, title, img);
        gridMenu.setAdapter(dashAdapter);

        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    loadDialog();
                }

                if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), ShowNearBy.class);
                    startActivity(intent);
                }

                if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), PesticidesActivity.class);
                    startActivity(intent);
                }

                if (position == 3) {

                    Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                    startActivity(intent);
                }
            }

            
        });
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Pesticide", R.drawable.pesticide);
        file_maps.put("Crop Suggestion", R.drawable.crop);
        file_maps.put("Near By Farmers", R.drawable.farmer);


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);


            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);

           /* View view = navigationView.getHeaderView(0);
            tvHeaderName = (TextView)view.findViewById(R.id.tvHeaderName);
            tvHeaderEmail = (TextView)view.findViewById(R.id.tvHeaderEmail);
            tvHeaderName.setText("Sahil Nirbhavane");
            tvHeaderEmail.setText("sahilnirbhavane09@gmail.com");*/

        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvEmail.setText(mySharedPreference.getPref("user_email"));

    }

    private void loadDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrawerActivity.this);
        alertDialogBuilder.setMessage("Select Your Mode");
        alertDialogBuilder.setPositiveButton("Offline",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Online", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intent = new Intent(getApplicationContext(), HomeScreen1.class);
                startActivity(intent);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Crop) {
          loadDialog();
        } else if (id == R.id.nav_nearby) {

            Intent intent = new Intent(getApplicationContext(), ShowNearBy.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {

            mySharedPreference.clearSharedPrefs();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_pesticides) {
            Intent intent = new Intent(getApplicationContext(), PesticidesActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_guide) {

            Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18001801551"));
            startActivity(intent);
        }else if (id == R.id.nav_video) {
            Intent intent = new Intent(getApplicationContext(), VedioActivity.class);
            startActivity(intent);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class DashAdapter extends BaseAdapter {
        String title[];
        int img[];
        Context context;
        private LayoutInflater inflater = null;

        public DashAdapter(Context context, String title[], int img[]) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.title = title;
            this.img = img;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            TextView tvTitle;
            ImageView imgLogo;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_dash, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                holder.imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tvTitle.setText("" + title[position]);
            holder.imgLogo.setImageResource(img[position]);
            convertView.setTag(holder);
            return convertView;
        }

    }


}
