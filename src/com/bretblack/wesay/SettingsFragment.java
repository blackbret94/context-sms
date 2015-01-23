package com.bretblack.wesay;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	private String filename = "conTextSMSBackup";
	private FavoritesDbAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // open database
        GlobalDb mApp = (GlobalDb) getActivity().getApplicationContext();
		mDbHelper = mApp.getDbAdapter();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        // set preference listener for export
        Preference pref = getPreferenceManager().findPreference("export");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("export")) {
                    exportFavorites();
                    return true;
                }
                return false;
            }
        });
                
        // set preference listener for import
        pref = getPreferenceManager().findPreference("import");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("import")) {
                	importFavorites();
                    return true;
                }
                return false;
            }
        });
        
     // set preference listener for delete all
        pref = getPreferenceManager().findPreference("delete_all");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("delete_all")) {
                	deleteAllFavorites();
                    return true;
                }
                return false;
            }
        });
    }
	
	/** Parses the database and saves it to a text file */
	public void exportFavorites(){
		Activity act = getActivity();
		mDbHelper.open();
		
		// create and fill file
		try {
			 FileOutputStream fileout=act.openFileOutput(filename, Context.MODE_PRIVATE);
			 
			 // access database
			 Cursor c = mDbHelper.fetchAllNotes();
			 c.moveToFirst();
			 
			 // write strings
			 do{
				 String s = c.getString(2)+"\n ";
				 fileout.write(s.getBytes());
				 c.moveToNext();
			 } while (!c.isLast());
			 fileout.close();
			 
			 //display file saved message
			 Toast.makeText(act.getBaseContext(), "File saved successfully!",
			 Toast.LENGTH_SHORT).show();
			 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		// close DB
		mDbHelper.close();
	}
	
	/** Parses a text file and saves it to the database */
	public void importFavorites(){
		Activity act = getActivity();
		Calendar c = Calendar.getInstance();
		mDbHelper.open();
		
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            act.openFileInput(filename)));
		    String inputString;
		    StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) {
		    	if (!inputString.equals("")) mDbHelper.createNote(c.getTime().toString(), inputString);
		    }
		    
		    // temp toast
		    Toast.makeText(act.getBaseContext(), "File successfully imported",
					 Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		// close DB
		mDbHelper.close();
	}
	
	/** Deletes all favorites from the database */
	public void deleteAllFavorites(){
		Activity act = getActivity();
		mDbHelper.open();
		mDbHelper.deleteTable();
		mDbHelper.close();
		
		Toast.makeText(act.getBaseContext(), "Your favorited items have been removed",
				 Toast.LENGTH_SHORT).show();
	}
}
