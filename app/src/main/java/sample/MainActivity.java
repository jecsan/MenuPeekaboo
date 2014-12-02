package sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import com.dyoed.menupeekaboo.R;

import java.util.ArrayList;

import library.BaseTabPagerAdapter;
import library.ShowHideController;
import library.SlidingTabLayout;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private LinearLayout btmMenu;
    private ViewPager viewPager;
    private BaseTabPagerAdapter baseTabPagerAdapter;
    private SlidingTabLayout slidingTabLayout;
    ShowHideController manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btmMenu = (LinearLayout) findViewById(R.id.btm_menu);
        viewPager = (ViewPager) findViewById(R.id.tab_pager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        baseTabPagerAdapter = new BaseTabPagerAdapter(getSupportFragmentManager()) {
            @Override
            protected void initFragments() {
                fragmentsList = new ArrayList<Fragment>();
                fragmentsList.add(new FragmentSample());
                fragmentsList.add(new FragmentSample());
                fragmentsList.add(new FragmentSample());
            }

            @Override
            protected void initSaveFragments() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(new FragmentSample(), "1");
                transaction.add(new FragmentSample(), "1");
                transaction.add(new FragmentSample(), "1");
                transaction.commit();

            }

            @Override
            protected void initTabsList() {
                tabNames = new ArrayList<String>();
                tabNames.add("Trending");
                tabNames.add("Discover");
                tabNames.add("Nearby");

                drawableIds = new ArrayList<Integer>();
                drawableIds.add(android.R.drawable.ic_btn_speak_now);
                drawableIds.add(android.R.drawable.ic_btn_speak_now);
                drawableIds.add(android.R.drawable.ic_btn_speak_now);
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return fragmentsList.get(0);
                    case 1:
                        return fragmentsList.get(1);
                    case 2:
                        return fragmentsList.get(2);
                }
                return null;
            }
        };

        viewPager.setAdapter(baseTabPagerAdapter);
        slidingTabLayout.setViewPager(viewPager);


        manager = new ShowHideController();
        manager.setActionBar(toolbar);
        manager.setTabs(slidingTabLayout);
        manager.setBtmMenu(btmMenu);


        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               manager.forceShowViews();
            }

            @Override
            public void onPageSelected(int position) {
                manager.setAnimating(true);
                ((FragmentSample)baseTabPagerAdapter.getItem(position)).onPageSelected();
                manager.setAnimating(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public int getProgressBarOffset(){
        return slidingTabLayout.getBottom();
    }


    public ShowHideController getManager() {
        return manager;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
