package edu.miami.masonandluke.phlogging;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MainActivity extends Activity implements OnItemClickListener,OnItemLongClickListener {
	private PhloggingDB phlogDB;
	private Cursor listCursor;

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
		
		SimpleCursorAdapter cursorAdapter;
		ListView theList;
		
		listCursor = phlogDB.fetchAllPhlogs();
		
		theList = (ListView)findViewById(R.id.the_list);
		cursorAdapter = new SimpleCursorAdapter(this,R.layout.list_items,listCursor,displayFields,displayViews);
		
		theList.setAdapter(cursorAdapter);
		theList.setOnItemClickListener(this);
		theList.setOnItemLongClickListener(this);
		
		
	
		
	
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_camera:
	            Log.i("Test", "testing this");
	            return true;
	        case R.id.action_add:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
	
		 Intent showInfo = new Intent();
         showInfo.setClassName("edu.miami.masonandluke.phlogging",
         "edu.miami.masonandluke.phlogging.ShowInformation");
		
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
