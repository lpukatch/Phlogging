package edu.miami.masonandluke.phlogging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	private static final int ACTIVITY_CAMERA_APP = 0;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private PhloggingDB phlogDB;
	private Cursor listCursor;
private Uri fileUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] displayFields = { "image_id", "title",

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
		case R.id.action_camera:
			 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			Log.i("fileuri", fileUri + "");
			cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(cameraIntent, ACTIVITY_CAMERA_APP);
			return true;

		case R.id.action_add:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_CAMERA_APP:
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image saved to:\n" + fileUri,
						Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}

			break;
		}
	}
}
