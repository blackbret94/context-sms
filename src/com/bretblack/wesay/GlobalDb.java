package com.bretblack.wesay;

import android.app.Application;
import android.content.res.Configuration;

public class GlobalDb extends Application {
	/** Singleton instance */
	private static GlobalDb singleton;
	/** Instance of the database adapter */
	private FavoritesDbAdapter mDbHelper;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		mDbHelper = new FavoritesDbAdapter(this);
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public GlobalDb getInstance(){
		return singleton;
	}
	
	public FavoritesDbAdapter getDbAdapter(){
		return mDbHelper;
	}
	
}
