package com.bretblack.wesay;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class FavoritesFragmentTab extends ListFragment {
	private int mFavoriteNumber;
	private FavoritesDbAdapter mDbHelper;
	public static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int DELETE_ALL_ID = Menu.FIRST + 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.favorites_fragment_layout, container, false);
		return rootView;
	}
	
	public void onStart() {
		super.onStart();
		//setContentView(R.layout.fragment_favorites);

		GlobalDb mApp = (GlobalDb) getActivity().getApplicationContext();
		mDbHelper = mApp.getDbAdapter();
		mDbHelper.open();
		fillData();

		// make each element selectable
		registerForContextMenu(getListView());
		
		
		// retain fragment memory on orientation change
		//setRetainInstance(true);
	}
	
    /*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// this is temporarily used to see that the list works
		boolean result = super.onCreateOptionsMenu(menu);
		// menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		menu.add(0, DELETE_ALL_ID, 0, R.string.menu_delete_all);
		return result;
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * case INSERT_ID: createFavorite(); return true; }
		 */
		/*case DELETE_ALL_ID:
			deleteAllFavorites();
			return true;
		case android.R.id.home:
			Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
		    startActivityForResult(myIntent, 0);
		}
		

		return super.onOptionsItemSelected(item);
	}*/

	/** Deletes all values from the database */
	private void deleteAllFavorites(){
		mDbHelper.deleteTable();
		fillData();
	}
	
	/** Handles the creation of a new favorite */
	private void createFavorite() {
		String favName = "Note " + mFavoriteNumber++;
		mDbHelper.createNote(favName, "");
		fillData();
	}

	// context menu allows you to delete favorites
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	// responds to the selection of "DELETE" on a context menu
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteNote(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Uses a string to create a new favorite
	 * 
	 * @param s
	 *            The quote to use private void createFavorite(String s) {
	 *            String favName = "Note " + mFavoriteNumber++;
	 *            mDbHelper.createNote(favName, s); fillData(); }
	 */

	/** Fills the list with favorites */
	private void fillData() {
		// Get all of the notes from the database and create the item list
		Cursor c = mDbHelper.fetchAllNotes();
		getActivity().startManagingCursor(c);

		String[] from = new String[] { FavoritesDbAdapter.KEY_BODY };
		int[] to = new int[] { R.id.favorite_text };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(getActivity(),
				R.layout.favorite, c, from, to);
		setListAdapter(notes);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_favorites,
					container, false);
			return rootView;
		}
	}
}