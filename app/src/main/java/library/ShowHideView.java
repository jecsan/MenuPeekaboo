package library;

import android.view.View;

/**
 * Created by Joed on 11/27/14.
 */
public class ShowHideView {

    private View view;
    private int action;
    private int height;
    private int top;
    private int bottom;


    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public ShowHideView(View v) {
        view = v;
    }

    public ShowHideView(View view, int height) {
        this.view = view;
        this.height = height;
    }

    public ShowHideView(View view, int height, int top, int bottom) {
        this.view = view;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
    }
}
