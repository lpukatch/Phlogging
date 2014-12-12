package edu.miami.masonandluke.phlogging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

public class AddActivity extends Activity {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int ACTIVITY_CAMERA_APP = 0;
	private Uri fileUri;
	private PhloggingDB phlogDB;
	private File photoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		phlogDB = new PhloggingDB(this);
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

				

				ImageView view = (ImageView) findViewById(R.id.photo);
				view.setImageURI(Uri.fromFile(photoFile));

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}

			break;
		}
	}

	String mCurrentPhotoPath;

	public void myClickHandler(View view) {

		switch (view.getId()) {

		case R.id.save:
			String description = ((EditText) findViewById(R.id.editDescription))
					.getText().toString();
			String title = ((EditText) findViewById(R.id.editTitle))
					.getText().toString();
			long time = System.currentTimeMillis();
			ContentValues values = new ContentValues();
			values.put("description", description);
			values.put("title", title);
			values.put("image_data", photoFile.toString());
			values.put("time", time);
			phlogDB.addPhlog(values);
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
}