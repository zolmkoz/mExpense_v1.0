package com.example.tabactivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabactivity.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  implements LocationListener {

    double lat;
    Date strDate;
    Date strDate2;
    double log;
    ProgressBar progressBar;
    protected static int trip_id;
    String trip_name;
    String ldate,date1;
    TextView t3,t2;
    int t_budget,left;
    LocationManager lm;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Bundle bundle = getIntent().getExtras();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);

        getSupportActionBar().setElevation(0);
        progressBar = findViewById(R.id.progressBar2);
        t3 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView2);
        t3.setTextColor(Color.WHITE);
        t2.setTextColor(Color.WHITE);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundColor(Color.parseColor("#F93943"));
        SQLiteDatabase db = openOrCreateDatabase("Trip",MODE_PRIVATE,null);

     if(bundle == null) {
         String sql = "Select * from tripdetails";
         Cursor cursor = db.rawQuery(sql, null);

         while (cursor.moveToNext()) {
//
             trip_id = cursor.getInt(0);
             t_budget = cursor.getInt(6);
             left = cursor.getInt(7);
             trip_name = cursor.getString(1);
             ldate = cursor.getString(5);
         }
         t2.setText("LEFT: £" + left);
         int left2 = t_budget - left;
         t3.setText(" £ " + left2);
         if (trip_name == null) {
             actionBar.setTitle("No Trips");
         } else {
             actionBar.setTitle("" + trip_name);
         }
         progressBar.setMax(t_budget);
         progressBar.setProgress(left2);

     }
     else
     {
         trip_id = bundle.getInt("fromid");
         String sql = "Select * from tripdetails where trip_id="+trip_id;
         Cursor cursor = db.rawQuery(sql, null);

         while (cursor.moveToNext()) {

             t_budget = cursor.getInt(6);
             left = cursor.getInt(7);
             trip_name = cursor.getString(1);
             ldate = cursor.getString(5);

         }
         t2.setText("LEFT: £" + left);
         int left2 = t_budget - left;
         t3.setText(" £ " + left2);

         actionBar.setTitle("" + trip_name);

         progressBar.setMax(t_budget);
         progressBar.setProgress(left2);

     }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date1= ""+day+"/"+(month+1)+"/"+year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        strDate = null;
        strDate2 = null;


        try {
            strDate = sdf.parse(ldate);
            strDate2 = sdf.parse(date1);
        } catch (ParseException e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(strDate2.compareTo(strDate)<=0) {
                        Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
                        i.putExtra("id", trip_id);
                        startActivity(i);
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Your Trip has Ended!! You can't add Expense Now! .", Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.parseColor("#eeeeee"));
                        snackbar.show();
                    }
                }
            });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
         lat = location.getLatitude();
         log = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disable", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(MainActivity.this,ViewAllTripActivity.class);
                startActivity(i);
                return true;
            case R.id.share:
                shareIt();
                return true;
            case R.id.maps:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps"));
                startActivity(intent);
                return true;
            case R.id.about:
                Intent git = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://zolmkoz.github.io/FAQ-Question/"));
                startActivity(git);
                return true;
            case R.id.exit:
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        try {
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "M-Expense - Trip expenses report");
            sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "zolmkoz1@gmail.com" });
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "{\n" +
                    "  \"expense_id\": 1,\n" +
                    "  \"notes\": \"food\",\n" +
                    "  \"category\": \"Food\"\n" +
                    "  \"amount_spend\": 35,\n" +
                    "  \"date\": \"18/11/2022\"\n" +
                    "\n" +
                    "  \"expense_id\": 2,\n" +
                    "  \"notes\": \"Hotel\",\n" +
                    "  \"category\": \"Hotels\"\n" +
                    "  \"amount_spend\": 163,\n" +
                    "  \"date\": \"18/11/2022\"\n" +
                    "\n" +
                    "  \"expense_id\": 3,\n" +
                    "  \"notes\": \"Ticket bus\",\n" +
                    "  \"category\": \"Travels\"\n" +
                    "  \"amount_spend\": 70,\n" +
                    "  \"date\": \"18/11/2022\"\n" +
                    "}\n " );
            startActivity(android.content.Intent.createChooser(sharingIntent, "Share via"));
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).getSubMenu().getItem(4).setVisible(false);
        menu.getItem(0).getSubMenu().getItem(5).setVisible(true);

        MenuItem.OnActionExpandListener onActionExpandListener=new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Search is Expanded", Toast.LENGTH_SHORT).show();

                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Search is Collapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView=(SearchView) menu.findItem(R.id.search).getActionView();
        return super.onCreateOptionsMenu(menu);
    }
}