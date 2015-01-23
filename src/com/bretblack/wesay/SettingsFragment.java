package com.bretblack.wesay;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	private String filename = "conTextSMSBackup";
	private Activity act = getActivity();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
	
	/** Parses the database and saves it to a text file */
	public void exportFavorites(){
		// create and fill file
		try {
			 FileOutputStream fileout=act.openFileOutput(filename, Context.MODE_PRIVATE);
			 //OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
			 fileout.write(filename.getBytes());
			 fileout.close();
			 
			 //display file saved message
			 Toast.makeText(act.getBaseContext(), "File saved successfully!",
			 Toast.LENGTH_SHORT).show();
			 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/** Parses a text file and saves it to the database */
	public void importFavorites(){
		Activity act = getActivity();
		
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            act.openFileInput(filename)));
		    String inputString;
		    StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) {
		        stringBuffer.append(inputString + "\n");
		    }
		    
		    // temp toast
		    Toast.makeText(act.getBaseContext(), stringBuffer.toString(),
					 Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
