package library;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class ShowHideManager {

    private boolean isAnimating;
    private List<ShowHideView> showHideViews;

    private static final int SCROLL_TO_TOP = - 1;
    private static final int SCROLL_DIRECTION_CHANGE_THRESHOLD = 5;
    private static final int SCROLL_TO_BOTTOM = 1;
    private static final int HIDESHOW_SPEED = 200;

    private int lastScrollDirection;
    private int lastScrollPos;

    public static final int HIDE_UP = 5;
    public static final int HIDE_DOWN = 6;


    /**
     *
     * @param v {@link android.view.View} to be hidden/shown
     * @param hideAction use {@link ShowHideManager#HIDE_DOWN} or {@link ShowHideManager#HIDE_UP}
     */
    public void addView(View v, int hideAction){
        ShowHideView showHideView = new ShowHideView(v,v.getHeight(),v.getTop(),v.getBottom());
        showHideView.setAction(hideAction);
        showHideViews.add(showHideView);
    }

    public ShowHideManager(){
        showHideViews = new ArrayList<>();
    }


    public AbsListView.OnScrollListener getScrollListener(){
        return scrollListener;
    }


    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            int newScrollPos = 0;
            View topChild = view.getChildAt(0);

            if(topChild == null){
                newScrollPos = 0;
            }
            else{
                newScrollPos = -topChild.getTop() + view.getFirstVisiblePosition() * topChild.getHeight();
            }

            if(newScrollPos <= 5){
                showViews();
                return;
            }


            if(Math.abs(newScrollPos - lastScrollPos) >= SCROLL_DIRECTION_CHANGE_THRESHOLD){
                onScrollChanged(lastScrollPos, newScrollPos);
            }

            lastScrollPos = newScrollPos;
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };



    private void onScrollChanged(int oldScroll, int newScroll){
        int newScrollDirection;

        if(newScroll < oldScroll) {
            newScrollDirection = SCROLL_TO_TOP;
            showViews();

        } else {
            newScrollDirection = SCROLL_TO_BOTTOM;
            hideViews();
        }

        if(newScrollDirection != lastScrollDirection) {
            lastScrollDirection = newScrollDirection;
        }

    }


    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isAnimating = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    public void showViews(){
        if(!isAnimating){
            for(ShowHideView view : showHideViews){
                switch (view.getAction()){
                    case HIDE_UP:
                        view.getView().animate().translationY(view.getTop())
                                .setDuration(HIDESHOW_SPEED)
                                .alpha(1)
                                .setInterpolator(new DecelerateInterpolator()).setListener(animationListener);
                        break;

                    case HIDE_DOWN:
                        view.getView().animate().translationY(-view.getHeight())
                                .setDuration(HIDESHOW_SPEED)
                                .setInterpolator(new DecelerateInterpolator());
                        break;

                    default:
                        break;
                }
            }
        }


    }

    public void hideViews(){
        if(!isAnimating){
            for(ShowHideView view : showHideViews){
                switch (view.getAction()){
                    case HIDE_UP:
                        view.getView().animate().translationY(-view.getView().getBottom())
                                .setDuration(HIDESHOW_SPEED)
                                .setInterpolator(new DecelerateInterpolator());
                        break;

                    case HIDE_DOWN:
                        view.getView().animate().translationY(view.getView().getHeight())
                                .setDuration(HIDESHOW_SPEED)
                                .setInterpolator(new DecelerateInterpolator());
                        break;

                    default:
                        break;
                }
            }
        }


    }

}
