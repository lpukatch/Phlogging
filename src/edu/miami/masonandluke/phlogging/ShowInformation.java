package edu.miami.masonandluke.phlogging;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowInformation extends Activity {

	private long id = 0;
	private PhloggingDB phlogDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinformation);

		phlogDB = new PhloggingDB(this);
		id = this.getIntent().getLongExtra(
				"edu.miami.masonandluke.phlogging.id", 0L);

		ContentValues values = phlogDB.getPhlogById(id);
		String image = values.getAsString("image_data");

		if (image != null) {
			Uri imageUri = Uri.parse(image);

			ImageView imageView = (ImageView) findViewById(R.id.show_photo);
			imageView.setImageURI(imageUri);
		}
		TextView textView = (TextView) findViewById(R.id.show_title);
		textView.setText(values.getAsString("title"));

		textView = (TextView) findViewById(R.id.show_description);
		textView.setText(values.getAsString("description"));
		Log.i("description", values.getAsString("description"));

		textView = (TextView) findViewById(R.id.show_date);
		Time timeObject = new Time();
		timeObject.set(values.getAsLong("time"));
		textView.setText(timeObject.format("%A %D %T"));

		 double lat = values.getAsDouble("lat");
		double lon = values.getAsDouble("long");
		//
		 textView = (TextView)findViewById(R.id.show_location);
		 textView.setText("Latitude is: " + lat + " Longitude is: "+ lon);
		//
		// textView = (TextView)findViewById(R.id.show_orientation);
		// textView.setText(values.getAsFloat("orientation").toString());
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		switch (item.getItemId()) {

		case R.id.action_trash:
			phlogDB.deletePhlog(id);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
