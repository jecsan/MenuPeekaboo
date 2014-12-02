package library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.dyoed.menupeekaboo.R;

import java.util.List;

public abstract class BaseTabPagerAdapter extends FragmentPagerAdapter {

	private int currentView;
	protected List<Fragment> fragmentsList;
	protected List<String> tabNames;
	protected List<Integer> drawableIds;
	private FragmentManager fragmentManager;

	public BaseTabPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentManager = fm;
		initFragments();
		initSaveFragments();
		initTabsList();
	}
	
	public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

	/**
	 * Initialize fragments to be used by Fragment Pager
	 * Use {@link android.support.v4.app.FragmentTransaction}, add {@link android.support.v4.app.Fragment} then commit.
	 */
	protected abstract void initFragments();


	/**
	 * Save fragments to list to be used in getItem()
	 * Be sure to call notifyDataSetChanged()
	 */
	protected abstract void initSaveFragments();

	@Override
	protected void finalize() throws Throwable {
		fragmentsList = null;
		super.finalize();
	}

	/**
	 * Initialize names and drawables to be used by tabs
	 * Add names on tabNameslist and drawables on drawableIds
	 * Be sure to initialize both list before using them
	 */
	protected abstract void initTabsList();
	
	
	public int getResDrawable(int position){
	    return drawableIds.get(position);
	}
	

	public int getCurrentView() {
		return currentView;
	}

	public void setCurrentView(int currentView) {
		this.currentView = currentView;
	}

	public String getFragmentTag(int pos) {
		return "android:switcher:" + R.id.tab_pager + ":" + pos;
	}
	
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
	    currentView = position;
	    super.setPrimaryItem(container, position, object);
	}


	public CharSequence getPageTitle(int position) {
		return tabNames.get(position);
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	public int getFragmentCount() {
		return fragmentsList.size();
	}

}
