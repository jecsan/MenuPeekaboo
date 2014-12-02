package library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;

public class ShowHideController {

    private boolean isAnimating;
    private static final int SCROLL_TO_TOP = - 1;
    private static final int SCROLL_THRESHHOLD = 5;
    private static final int SCROLL_TO_BOTTOM = 1;
    private static final int HIDESHOW_SPEED = 200;

    private int lastScrollDirection;
    private int lastScrollPos;

    private boolean mIs1stStage;
    private boolean allHidden;

    private View actionBar;
    private View tabs;
    private View btmMenu;

    public View getActionBar() {
        return actionBar;
    }

    public void setActionBar(View actionBar) {
        this.actionBar = actionBar;
    }

    public View getTabs() {
        return tabs;
    }

    public void setTabs(View tabs) {
        this.tabs = tabs;
    }

    public View getBtmMenu() {
        return btmMenu;
    }

    public void setBtmMenu(View btmMenu) {
        this.btmMenu = btmMenu;
    }

    private int tabDefaultHeight;


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


            int scrollRate = Math.abs(newScrollPos - lastScrollPos);

            if(scrollRate >= SCROLL_THRESHHOLD){
                onChangeScroll(lastScrollPos, newScrollPos);
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



    private void onChangeScroll(int oldScroll, int newScroll){
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
            isAnimating = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };




    private void translateView(boolean up, View view){
        int val = up ? 1: 0;
        if(view != null){
            view.animate().translationY(val)
                    .setDuration(HIDESHOW_SPEED)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(animationListener);
        }
    }

    private void translateView(boolean up, View view, int duration){
        int val = up ? 1: 0;
        if(view != null){
            view.animate().translationY(val)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(animationListener);
        }
    }



    private void restoreTabHeight(){
        if(tabDefaultHeight != 0 && tabs != null){
            ViewGroup.LayoutParams layoutParams = tabs.getLayoutParams();
            layoutParams.height = tabDefaultHeight;
            tabs.setLayoutParams(layoutParams);
        }

    }


    public void animateHeight(final View view, int from, int to, final boolean hide) {
        if(view != null){

            ValueAnimator anim = ValueAnimator.ofInt(from, to);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = val;
                    view.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(HIDESHOW_SPEED);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    mIs1stStage = hide;
                    isAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimating = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }

    }

    public void setAnimating(boolean animating){
        isAnimating = animating;
    }

    public void showViews(){
        if(!isAnimating){

            if(allHidden){
                restoreTabHeight();
                translateView(false,actionBar);
                translateView(false, tabs);
                translateView(true, btmMenu);
                mIs1stStage = false;
                allHidden = false;
            }
            else if(mIs1stStage){
                translateView(false,actionBar);
                translateView(true, btmMenu);
                animateHeight(tabs, 0, tabDefaultHeight, false);
            }


            mIs1stStage = false;
        }
    }

    public void forceShowViews(){

        if(mIs1stStage){
            animateHeight(tabs, 0, tabDefaultHeight, false);
            translateView(true, btmMenu, 100);
            mIs1stStage = false;
        }

        if(allHidden){
            restoreTabHeight();
            translateView(false,actionBar, 100);
            translateView(false, tabs,100);
            translateView(true, btmMenu, 100);
            allHidden = false;
        }

        lastScrollDirection = SCROLL_TO_TOP;

    }


    public void hideViews(){
        if(!isAnimating){
            if(mIs1stStage){
                tabs.animate().translationY(-tabs.getBottom())
                        .setDuration(HIDESHOW_SPEED)
                        .setInterpolator(new DecelerateInterpolator());
                actionBar.animate().translationY(-actionBar.getBottom())
                        .setDuration(HIDESHOW_SPEED)
                        .setInterpolator(new DecelerateInterpolator());
                allHidden = true;

            }
            else {
                if(tabs.getHeight() != 0){
                    tabDefaultHeight = tabs.getHeight();
                }
                animateHeight(tabs, tabDefaultHeight, 0, true);
                btmMenu.animate().translationY(btmMenu.getHeight())
                        .setDuration(HIDESHOW_SPEED)
                        .setInterpolator(new DecelerateInterpolator());
            }
        }
    }

}
