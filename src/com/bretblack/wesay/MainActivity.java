package com.bretblack.wesay;

// scheme id 4F21Tw0w0w0w0

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
	//public final static String EXTRA_MESSAGE = "com.example.myrandomthought.MESSAGE";
	private ActionBar.Tab textAwkward,favorites;
	private HomeFragmentTab homeFragmentTab;
	private FavoritesFragmentTab favoritesFragmentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // save fragment manager
        FragmentManager fm = getFragmentManager();
        
        // create home fragment if it doesn't already exist
        if (savedInstanceState == null) {
        	fm.beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        	
        	homeFragmentTab = new HomeFragmentTab();
        	homeFragmentTab.setRetainInstance(true);
        	homeFragmentTab.setArguments(getIntent().getExtras());
     	   	fm.beginTransaction().add(homeFragmentTab,"home").commit();
        } else {
        	Log.v("Main","Main is not null");
        	homeFragmentTab = (HomeFragmentTab) fm.findFragmentByTag("home");
        }
        
        // create fragments if they do not already exist
       favoritesFragmentTab=new FavoritesFragmentTab(); // saving this fragment should not matter
        
        // get and set up action bar 
        ActionBar actionBar = getActionBar();
       // actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // set up tab icons
        Tab homeTab = actionBar.newTab().setText("Home");
        Tab favoritesTab = actionBar.newTab().setText("Favorites");
        
        // set up tab listeners
        homeTab.setTabListener(new TabListener(homeFragmentTab));
        favoritesTab.setTabListener(new TabListener(favoritesFragmentTab));
        
        // add to action bar
        actionBar.addTab(homeTab);
        actionBar.addTab(favoritesTab);
        
    }
    
    /** responds to a next button press 
	 *  if it is at the end of the list,
	 *  generate more sentences. */
	public void next(View view) {
		homeFragmentTab.next(view);
	}
	
	/** responds to a back button press */
	public void back(View view) {
		homeFragmentTab.back(view);
	}

	/**
	 * Saves a quote as a favorite
	 */
	public void favorite(View view) {
		homeFragmentTab.favorite(view);
	}
    
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
	    startActivityForResult(myIntent, 0);
	    return true;
	}*/
	
	/** Makes sure that the app does not crash when the back button is pressed */
	@Override
	public void onBackPressed() {
		// DO NOTHING
	}

    /**
     * A placeholder fragment containing a simple view.
     * This is deprecated, to be replaced later.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
