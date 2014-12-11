package edu.miami.masonandluke.phlogging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.widget.SimpleCursorAdapter.ViewBinder, OnItemClickListener,
		OnItemLongClickListener {
	private static final int ACTIVITY_CAMERA_APP = 0;

	private static final int ACTIVITY_ADD = 0;

	private PhloggingDB phlogDB;
	private Cursor listCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] displayFields = { "image_data", "time",

		};
		int[] displayViews = { R.id.photo, R.id.title,

		};

		phlogDB = new PhloggingDB(this);

		SimpleCursorAdapter cursorAdapter;
		ListView theList;

		listCursor = phlogDB.fetchAllPhlogs();

		theList = (ListView) findViewById(R.id.the_list);
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_items,
				listCursor, displayFields, displayViews);
		cursorAdapter.setViewBinder(this);
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
		Intent cameraIntent;

		switch (item.getItemId()) {

		case R.id.action_add:
			Intent add = new Intent();
			add.setClassName("edu.miami.masonandluke.phlogging",
					"edu.miami.masonandluke.phlogging.AddActivity");
			startActivityForResult(add, ACTIVITY_ADD);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent showInfo = new Intent();
		showInfo.setClassName("edu.miami.masonandluke.phlogging",
				"edu.miami.masonandluke.phlogging.ShowInformation");
		showInfo.putExtra("edu.miami.masonandluke.phlogging.id", id);
		startActivity(showInfo);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Requery the cursor to update the list
		listCursor.requery();
		return;
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		// TODO Auto-generated method stub
		if (columnIndex == cursor.getColumnIndex("image_data")) {
			

			//((ImageView) view).setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex("image_data"))));
			return true;
		} else {
			return false;
		}
	}

}
