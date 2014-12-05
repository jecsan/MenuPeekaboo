package sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dyoed.menupeekaboo.R;

import java.util.ArrayList;
import java.util.List;

import library.EndlessListViewManager;
import library.HeaderViewRecyclerAdapter;
import library.RecycleViewAdapter;
import library.ShowHideController;

/**
 * Created by Joed on 11/28/14.
 */
public class FragmentSample extends Fragment {
    private RecyclerView sample;
    private boolean isVisible;
    private ShowHideController manager;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        sample = (RecyclerView) view.findViewById(R.id.listview_sample);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        final EndlessListViewManager mLayoutManager = new EndlessListViewManager(getActivity());
        sample.setLayoutManager(mLayoutManager);
        sample.setHasFixedSize(true);



        Log.d("Joed", "tae");




        int offset = ((MainActivity)getActivity()).getSupportActionBar().getHeight();
        Log.d("Joed", "offset:"+offset);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("test test "+i);
        }



        final RecycleViewAdapter adapter = new RecycleViewAdapter(list);
        final HeaderViewRecyclerAdapter recyclerAdapter = new HeaderViewRecyclerAdapter(adapter);
        recyclerAdapter.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.empty, sample, false));
        recyclerAdapter.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.empty, sample, false));
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, list);
//        mLayoutManager.addView(getActivity().getLayoutInflater().inflate(R.layout.empty, sample, false), -1);
//        mLayoutManager.addView(getActivity().getLayoutInflater().inflate(R.layout.empty, sample, false), -1);
        sample.setAdapter(recyclerAdapter);

//        sample.add


        mLayoutManager.setOnLastItemVisible(new EndlessListViewManager.OnLastItemVisible() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(getActivity(), "Test", Toast.LENGTH_LONG).show();
                final List<String> list2 = new ArrayList<>();
                int count = adapter.getItemCount();
                for (int i = count; i < count+10; i++) {
                    list2.add("test test "+i);
                }

                adapter.addAll(list2);
                mLayoutManager.isLoadingMore(false);
            }
        });

        if(manager == null) {
            manager = ((MainActivity) getActivity()).getManager();
        }

        sample.setOnScrollListener(manager.getOnScrollListener());

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());

            swipeRefreshLayout.setProgressViewOffset(false, dpToPx((30)), dpToPx(120));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    final List<String> list2 = new ArrayList<>();
                    int count = adapter.getItemCount();
                    for (int i = count; i < count+10; i++) {
                        list2.add("test test "+i);
                    }
                    adapter.addAll(list2);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }, 2000);
                }
            });
        }

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
        sample.setOnScrollListener(manager.getOnScrollListener());
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
