/** This class represents the activity that displays sentences.
 * @author Bret Black
 */

package com.bretblack.wesay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class QuoteFinderActivity extends Activity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		r = new Random(); // create random generator

		// set up activity
		setContentView(R.layout.activity_random_thought);
		quote = (TextView) findViewById(R.id.quote_text);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// get the intent
		Intent intent = getIntent();
		//Bundle b = intent.getExtras();
		// String message = b.getString(MainActivity.EXTRA_MESSAGE);
		//String message = readSms();
		GlobalDb mApp = (GlobalDb) getApplicationContext();
		mDbHelper = mApp.getDbAdapter();

		// split into substrings
		p = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)");
		/*pMatcher = p.matcher(message);

		sentMatches = new ArrayList<String>();
		while (pMatcher.find()) {
			sentMatches.add(pMatcher.group(0));
		}

		// get random
		int size = sentMatches.size();
		
		int stringChoose = r.nextInt(size);
		String sendString = sentMatches.get(stringChoose);*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** responds to a button press */
	public void next(View view) {
		// get random
		//String message = readSms();
		quote = (TextView) findViewById(R.id.quote_text);
		//int size = sentMatches.size();
		//int stringChoose = r.nextInt(size);
		//String sendString = sentMatches.get(stringChoose);

		// create the text view
		String s = readSms();
		quote.setText("\"" + s + "\"");
	}

	/**
	 * Saves a quote as a favorite
	 */
	public void favorite(View view) {
		// eventually the button should change colors after the quote is
		// favorited
		// String favName = "Note " + mFavoriteNumber++;
		quote = (TextView) findViewById(R.id.quote_text);
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
	public String readSms(){
		// create tree
		String smsString = new String();
		
		// parse inbox
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		startManagingCursor(c);
		
		c.moveToPosition(r.nextInt(c.getCount()));
		// add the body to the string
		String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
		c.close();
		
		// get a sentences from the message
		pMatcher = p.matcher(body);

		sentMatches = new ArrayList<String>();
		while (pMatcher.find()) {
			sentMatches.add(pMatcher.group(0));
		}

		String sendString = sentMatches.get(r.nextInt(sentMatches.size()));
		
		// return the string
		return sendString;
	}

	/** Makes sure that the app does not crash when the back button is pressed */
	@Override
	public void onBackPressed() {
		// Add data to your intent
		finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_random_thought,
					container, false);
			return rootView;
		}
	}

}
