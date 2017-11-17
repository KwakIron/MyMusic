package com.guozhe.android.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.guozhe.android.myapp.Detail.DetailActivity;
import com.guozhe.android.myapp.domain.MusicItem;
import com.guozhe.android.myapp.domain.MyMusic;
import com.guozhe.android.myapp.list.ListFragment;
import com.guozhe.android.myapp.login.LoginActivity;
import com.guozhe.android.myapp.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ListFragment.ListFragmentOnclickListenner{
    private ViewPager pager;
    private TabLayout tab;
    private Button search;
    private MyEditText edit_search;
    private ListFragment listFragment;
    private ProfileFragment profileFragment;
    private List<Fragment> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setNavi();
        tab_and_pager();
        btnSearch();
        autoText();


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
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void initView(){
        edit_search = (MyEditText)findViewById(R.id.edit_search);
        search = (Button)findViewById(R.id.search);
        pager = (ViewPager)findViewById(R.id.pager);
        tab = (TabLayout)findViewById(R.id.tab);
        profileFragment = ProfileFragment.getInstance(get_intent());
        listFragment = new ListFragment();
    }
    private void setNavi(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    private void tab_and_pager(){
        datas.add(listFragment);
        datas.add(profileFragment);
        tab.addTab(tab.newTab().setText("목록"));
        tab.addTab(tab.newTab().setText("profile"));

        android.support.v4.view.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),datas);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }


    @Override
    public void goDetail(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
    private void btnSearch(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_search.getText().toString();
                Log.e("title","======="+title.toString());
                if(title != null) {
                    List<MusicItem> items = getData();
                    Log.e("items","========="+items.get(2).toString());
                    for (int postion = 0; postion < items.size(); postion++) {
                        if (items.get(postion).getTitle().equals(title)) {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("position",postion);
                            startActivity(intent);
                            return;
                        }else{
                            Toast.makeText(MainActivity.this,"노래가 없습니다",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    private List<MusicItem> getData(){
        MyMusic myMusic = MyMusic.getInstance();
        myMusic.loader(getApplicationContext());
        return myMusic.getItem();
    }
    private void autoText(){
        List<MusicItem> items =getData();
        String[] titles = new String[items.size()];
        for(int i=0;i<items.size();i++){
            for(int j=i;j==i;j++){
                titles[i] = items.get(j).getTitle();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,titles);
        edit_search.setAdapter(adapter);
    }
    private String get_intent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = "";
        if(bundle != null){
            result = bundle.getString("uid");
        }
        return result;
    }
}
