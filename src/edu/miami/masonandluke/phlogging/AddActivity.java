package edu.miami.masonandluke.phlogging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int ACTIVITY_CAMERA_APP = 0;
	private Uri fileUri;
	private PhloggingDB phlogDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		phlogDB = new PhloggingDB(this);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_activity_add, menu);
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

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_CAMERA_APP:
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image saved to:\n" + fileUri,
						Toast.LENGTH_LONG).show();

				String[] queryFields = { MediaStore.Images.Media._ID,
						MediaStore.Images.Media.DATA };
//
//				Cursor imagesCursor = managedQuery(
//						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//						queryFields, null, null,
//						MediaStore.Images.Media.DEFAULT_SORT_ORDER);
//				imagesCursor.moveToFirst();
//				int column = imagesCursor
//						.getColumnIndex(MediaStore.Images.Media._ID);
//				int id = imagesCursor.getInt(column);
				ContentValues content = new ContentValues();
//Log.i("image_id", id + "");
				long time = System.currentTimeMillis();

				content.put("time", time);
				content.put("image_data", fileUri.toString());
				
				phlogDB.addPhlog(content);
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}

			break;
		}
	}
}
