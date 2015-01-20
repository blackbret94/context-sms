package com.bretblack.wesay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class HomeFragmentTab extends Fragment {
	/** The text view displaying a quote */
	private TextView quote;
	/** ArrayList containing all possible quotes */
	private ArrayList<String> sentMatches;
	/** The database storing favorites */
	private FavoritesDbAdapter mDbHelper;
	/** For share button */
	private ShareActionProvider mShareActionProvider;
	/** For random values */
	private Random r;
	/** The pattern used to get sentences */
	private Pattern p;
	/** Matcher used to match sentences */
	private Matcher pMatcher;
	/** LinkedList of generated sentences */
	private ArrayList<String> genList;
    private ArrayList<String> newList;
	/** cursor for arrayList */
	private int loc = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		// create view
		View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
				
		// SET UP SENTENCE GENERATION
        r = new Random(); // create random generator
        
        // get database
        GlobalDb mApp = (GlobalDb)getActivity().getApplicationContext();
        mDbHelper = mApp.getDbAdapter();

		// set up activity
        //getActivity().setContentView(R.layout.activity_random_thought);
		quote = (TextView) rootView.findViewById(R.id.quote_text);

		// create pattern
		p = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)");
		
		// restore
		if (savedInstanceState != null){
			Log.v("Home","Home is not null");
			genList = savedInstanceState.getStringArrayList("list");
			loc = savedInstanceState.getInt("loc");
			
			// update the quote
			quote.setText("\"" + genList.get(loc) + "\"");
		} else {
			genList = new ArrayList<String>();
			quote.setText("\"" + readSms() + "\"");
		}

        newList = readSms();
        Collections.shuffle(newList);

		// return
		return rootView;
	}
	
	/** responds to a next button press 
	 *  if it is at the end of the list,
	 *  generate more sentences. */
	public void next(View view) {
		// increment cursor
		loc++;
				
		// get view
		quote = (TextView) getView().findViewById(R.id.quote_text);
		String s;
		
		if(loc>=genList.size()){
            s = newList.remove(newList.size()-1);
			genList.add(s);
		} else {
			// get the correct element 
			s = genList.get(loc);
		}
		
		// modify text
		quote.setText("\"" + s + "\"");
	}
	
	/** responds to a back button press */
	public void back(View view) {
		// decrement cursor
		if (loc>0) {
			loc--;
		
			// get view
			quote = (TextView) getView().findViewById(R.id.quote_text);
			String s = genList.get(loc);
			
			// modify text
			quote.setText("\"" + s + "\"");
		}
	}

	/**
	 * Saves a quote as a favorite
	 */
	public void favorite(View view) {
		// eventually the button should change colors after the quote is
		// favorited
		// String favName = "Note " + mFavoriteNumber++;
		quote = (TextView) getView().findViewById(R.id.quote_text);
		String quoteString = quote.getText().toString();
		System.out.println(quoteString);

		// get time to use
		Calendar c = Calendar.getInstance();
		// String timeString = c.get()

		mDbHelper.open();
		mDbHelper.createNote(c.getTime().toString(), quoteString);
		mDbHelper.close();
	}
	
	/** Reads a random SMS
	 * @return
	 */
	public ArrayList<String> readSms(){
		// create tree
		String smsString = new String();
		
		// parse inbox
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c = getActivity().getContentResolver().query(uri, null, null, null, null);
		getActivity().startManagingCursor(c);
		
		c.moveToPosition(r.nextInt(c.getCount()));
		// add the body to the string
		String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
		//c.close();
		
		// get a sentences from the message
		pMatcher = p.matcher(body);

		sentMatches = new ArrayList<String>();
		while (pMatcher.find()) {
			sentMatches.add(pMatcher.group(0));
		}
		
		return sentMatches;
	}
	
	/** Handles change in instance state */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		Log.v("Instance state","Saving instance state...");
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putStringArrayList("list",genList);
		savedInstanceState.putInt("loc",loc);	
	}
	
	/* FOR SHARE FUNCTION
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate menu resource file.
	    getMenuInflater().inflate(R.menu.random_thought, menu);

	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_item_share);

	    // Fetch and store ShareActionProvider
	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();

	    // Return true to display menu
	    return true;
	}*/

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent);
	    }
	}
}
