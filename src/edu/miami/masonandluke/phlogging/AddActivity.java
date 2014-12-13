package edu.miami.masonandluke.phlogging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddActivity extends Activity implements
LocationListener{

	private static final int ACTIVITY_CAMERA_APP = 0;
	private static final int SELECT_PICTURE = 1;
	private PhloggingDB phlogDB;
	private File photoFile;
	private LocationManager locationManager;
	private double Lat;
	private double Lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		phlogDB = new PhloggingDB(this);
		getLocation();

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

		switch (item.getItemId()) {
		case R.id.action_camera:
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				photoFile = null;
				try {
					photoFile = createImageFile();
				} catch (IOException ex) {
					// Error occurred while creating the File

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
					startActivityForResult(takePictureIntent,
							ACTIVITY_CAMERA_APP);
				}
			}
			return true;
		case R.id.action_add_gallery:
			// select a file
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent,
					"Select Picture"), SELECT_PICTURE);
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
				ImageView view = (ImageView) findViewById(R.id.photo);
				view.setImageURI(Uri.fromFile(photoFile));

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		case SELECT_PICTURE:
			if (resultCode == RESULT_OK) {

				// Image captured and saved to fileUri specified in the Intent
				ImageView view = (ImageView) findViewById(R.id.photo);
				view.setImageURI(data.getData());
				photoFile = new File(getPath(data.getData()));
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		}
	}
	public String getPath(Uri uri) {
		// just some safety built in 
		if( uri == null ) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}


	String mCurrentPhotoPath;

	public void myClickHandler(View view) {

		switch (view.getId()) {

		case R.id.save:
			String description = ((EditText) findViewById(R.id.editDescription))
			.getText().toString();
			String title = ((EditText) findViewById(R.id.editTitle)).getText()
					.toString();
			long time = System.currentTimeMillis();
			ContentValues values = new ContentValues();
			values.put("description", description);
			values.put("title", title);
			if (photoFile != null) {
				values.put("image_data", photoFile.toString());
			}
			values.put("time", time);
			values.put("long",Lon);
			values.put("lat",Lat);
			phlogDB.addPhlog(values);
			finish();
			break;

		default:
			break;
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getApplicationContext().getExternalFilesDir(null);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
				);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	//-----------------------------------------------------------------------------
	@Override
	public void onPause() {

		super.onPause();

		locationManager.removeUpdates(this);
	}
	//-----------------------------------------------------------------------------

    private void getLocation() {
    	locationManager = (LocationManager)(getSystemService(LOCATION_SERVICE));
    	detectLocators();
    }
    
	//-----------------------------------------------------------------------------
	private void detectLocators() {

		List<String> locators;
		locators = locationManager.getProviders(true);

			if (locators.contains("gps")) {
				locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);	
			}
			else if  (locators.contains("network")) {
				locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,this,null);
			}
			else {
				Toast.makeText(this,"Location Services Off",Toast.LENGTH_LONG).show();
		}
	}
	//-----------------------------------------------------------------------------

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Lon = location.getLatitude();
		Lat = location.getLongitude();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



}
