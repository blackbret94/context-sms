package com.bretblack.wesay;

// scheme id 4F21Tw0w0w0w0

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
	//public final static String EXTRA_MESSAGE = "com.example.myrandomthought.MESSAGE";
	private ActionBar.Tab textAwkward,favorites;
	private HomeFragmentTab homeFragmentTab;
	private FavoritesFragmentTab favoritesFragmentTab;
	private SettingsFragment settingsFragment;
	private static final int SETTINGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // set default settings values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // save fragment manager
        FragmentManager fm = getFragmentManager();
        homeFragmentTab = (HomeFragmentTab) fm.findFragmentByTag("home");
        
        // create home fragment if it doesn't already exist
        if (homeFragmentTab == null) {
        	fm.beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        	
        	Log.v("Activity","Activity says it's null");
        	
        	homeFragmentTab = new HomeFragmentTab();
        	homeFragmentTab.setRetainInstance(true);
        	homeFragmentTab.setArguments(getIntent().getExtras());
     	   	fm.beginTransaction().add(homeFragmentTab,"home").commit();
        } else {
        	Log.v("Main","Main is not null");
        	//homeFragmentTab = (HomeFragmentTab) fm.findFragmentByTag("home");
        }
        
        // create fragments if they do not already exist
       favoritesFragmentTab = new FavoritesFragmentTab(); // saving this fragment should not matter
       settingsFragment = new SettingsFragment();
       
       // get and set up action bar 
       ActionBar actionBar = getActionBar();
       // actionBar.setDisplayShowHomeEnabled(false);
       actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // set up tab icons
        Tab homeTab = actionBar.newTab().setText("Home");
        Tab favoritesTab = actionBar.newTab().setText("Favorites");
        Tab settingsTab = actionBar.newTab().setText(R.string.menu_settings);
        
        // set up tab listeners
        homeTab.setTabListener(new TabListener(homeFragmentTab));
        favoritesTab.setTabListener(new TabListener(favoritesFragmentTab));
        settingsTab.setTabListener(new TabListener(settingsFragment));
        
        // add to action bar
        actionBar.addTab(homeTab);
        actionBar.addTab(favoritesTab);
        actionBar.addTab(settingsTab);
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
	
	/** Shares with Facebook */
	public void shareWithFacebook(String s){
		String mySharedLink = "<some_link>";
		String myAppId = "1619930601560074";

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mySharedLink);

		// Include your Facebook App Id for attribution
		shareIntent.putExtra("com.facebook.platform.extra.APPLICATION_ID", myAppId);

		//startActivityForResult(Intent.createChooser(shareIntent, "Share"), myRequestId);
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// this is temporarily used to see that the list works
		boolean result = super.onCreateOptionsMenu(menu);
		// menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		menu.add(0, SETTINGS, 0, R.string.menu_settings);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SETTINGS:
			deleteAllFavorites();
			return true;
		}
		

		return super.onOptionsItemSelected(item);
	}*/
    
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
