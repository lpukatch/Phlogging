package edu.miami.masonandluke.phlogging;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private PhloggingDB phlogDB;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String[] displayFields = {
				"image_id",
				"description",
				
		};
		int[] displayViews = {
				R.id.photo,
				R.id.description,
				

		};
		
		
		
		phlogDB = new PhloggingDB(this);
	
	
		
	
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
}
