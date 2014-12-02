package sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dyoed.menupeekaboo.R;

import java.util.ArrayList;
import java.util.List;

import library.ShowHideController;

/**
 * Created by Joed on 11/28/14.
 */
public class FragmentSample extends Fragment {
    ListView sample;
    private boolean isVisible;
    private ShowHideController manager;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        sample = (ListView) view.findViewById(R.id.listview_sample);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            swipeRefreshLayout.setProgressViewOffset(false, dpToPx((30)), dpToPx(120));
        }


        int offset = ((MainActivity)getActivity()).getSupportActionBar().getHeight();
        Log.d("Joed", "offset:"+offset);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add("test test "+i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, list);
        sample.setAdapter(arrayAdapter);

        sample.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.empty, sample, false));

        if(manager == null) {
            manager = ((MainActivity) getActivity()).getManager();
        }

        sample.setOnScrollListener(manager.getScrollListener());

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int offset = ((MainActivity)getActivity()).getProgressBarOffset();


    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public void onPageSelected(){
        manager.setAnimating(true);
        manager.forceShowViews();
        sample.setOnScrollListener(manager.getScrollListener());
        manager.setAnimating(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!isVisible){
            sample.setOnScrollListener(null);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        if(!isVisibleToUser && sample != null){
            sample.setOnScrollListener(null);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
