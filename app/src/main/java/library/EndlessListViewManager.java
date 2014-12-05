package library;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Joed on 12/5/14.
 */
public class EndlessListViewManager extends LinearLayoutManager {

    private static final int LAST_ITEM_VISIBLE_TRIGGER_COUNT = 5;
    private int HEADER_COUNT = 0;
    private int FOOTER_COUNT = 0;
    private boolean mIsLoadingMore;
    private OnLastItemVisible onLastItemVisible;
    private int SCROLL_STATE = 0;
    private Handler handler;

    public interface OnLastItemVisible{
        public void onLastItemVisible();
    }


    public void isLoadingMore(boolean loading){
        mIsLoadingMore = loading;
    }

    public void setHeaderCount(int count){
        HEADER_COUNT = count;
    }

    public void setFooterCount(int count){
        FOOTER_COUNT = count;
    }

    public void setOnLastItemVisible(OnLastItemVisible onLastItemVisible) {
        this.onLastItemVisible = onLastItemVisible;
    }

    public EndlessListViewManager(Context context) {
        super(context);
        handler = new Handler(Looper.getMainLooper());
    }

    public EndlessListViewManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() - (HEADER_COUNT + FOOTER_COUNT);
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int scroll = super.scrollVerticallyBy(dy, recycler, state);
        if(getItemCount() >= 10 ){
            checkIfLastItemVisible();
        }
        return scroll;

    }

    private void checkIfLastItemVisible(){
        if(findFirstVisibleItemPosition() <= getItemCount() && findFirstVisibleItemPosition() >= getItemCount()-LAST_ITEM_VISIBLE_TRIGGER_COUNT){
            if(onLastItemVisible != null && !mIsLoadingMore){
                Log.d("Joed", "Firing onLastItemVisible");
                mIsLoadingMore = true;
                handler.postDelayed(lastItemNotifier,10);
            }
        }
    }

    private Runnable lastItemNotifier = new Runnable() {
        @Override
        public void run() {
            onLastItemVisible.onLastItemVisible();
        }
    };


}
