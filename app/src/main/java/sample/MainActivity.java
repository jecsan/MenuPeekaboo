package sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dyoed.menupeekaboo.R;

import java.util.ArrayList;
import java.util.List;

import library.ShowHideManager;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView sample;
    private ImageView img;
    private LinearLayout btmMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sample = (ListView) findViewById(R.id.listview_sample);
        img = (ImageView) findViewById(R.id.image);
        btmMenu = (LinearLayout) findViewById(R.id.btm_menu);

        //Create sample data for listview
        List<String> list = new ArrayList<>();
        for(int i=0; i<100; i++){
            list.add("test test");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        arrayAdapter.addAll(list);
        sample.setAdapter(arrayAdapter);

        sample.addHeaderView(getLayoutInflater().inflate(R.layout.empty, sample, false));


        ShowHideManager manager = new ShowHideManager();


        manager.setActionBar(toolbar);
        manager.setTabs(img);
        manager.setBtmMenu(btmMenu);

        sample.setOnScrollListener(manager.getScrollListener());

    }

    @Override
    protected void onStart() {
        super.onStart();

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
