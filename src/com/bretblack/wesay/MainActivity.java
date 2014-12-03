package com.bretblack.wesay;

// scheme id 4F21Tw0w0w0w0

import java.util.ArrayList;

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
import android.widget.EditText;

public class MainActivity extends Activity {
	//public final static String EXTRA_MESSAGE = "com.example.myrandomthought.MESSAGE";
	private FavoritesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        // get database
        GlobalDb mApp = (GlobalDb)getApplicationContext();
        mDbHelper = mApp.getDbAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    
    /** Called when the user clicks the favorites button */
    public void goToFavorites(View view){
    	// send a chunk to the intent
    	Intent intent = new Intent(this, FavoritesActivity.class);
    	
    	// get message
    	//String message = readSms().toString();
    	
    	// send
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the use SMS button */
    public void useSms(View view){
    	// send a chunk to the intent
    	Intent intent = new Intent(this, RandomThought.class);
    	
    	// get message
    	String message = readSms().toString();
    	
    	// send bundle
    	Bundle b = new Bundle();
    	b.putString("mes",message);
    	//b.putSerializable("db", mDbHelper);
    	intent.putExtras(b);
    	startActivity(intent);
    }
    
    /** Reads SMS bodies into a tree 
     * @return The SMS database as a giant string*/
	public String readSms(){
		// create tree
		String smsString = new String();
		
		// read in sms
		// parse inbox
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		startManagingCursor(c);
		
		if(c.moveToFirst()){
			for(int i=0;i<c.getCount();i++){
				// add the body to the string
				String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
				smsString = smsString + ". " + body;
				c.moveToNext();
			}
		}
		c.close();
		
		// return the string
		return smsString;
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
