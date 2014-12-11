package edu.miami.masonandluke.phlogging;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
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
		id = this.getIntent().getLongExtra("edu.miami.masonandluke.phlogging.id", 0l);	

		ContentValues values = phlogDB.getImageById(id);
        Uri imageUri = Uri.parse(values.getAsString("imageData"));
		ImageView imageView = (ImageView)findViewById(R.id.show_photo);
		imageView.setImageURI(imageUri);
		
		TextView textView = (TextView)findViewById(R.id.show_title);
		textView.setText(values.getAsString("title"));
		
		
	    textView = (TextView)findViewById(R.id.show_description);
		textView.setText(values.getAsString("description"));
		
		textView = (TextView)findViewById(R.id.show_date);
		textView.setText(values.getAsInteger("time").toString());
		
		textView = (TextView)findViewById(R.id.show_location);
		textView.setText(values.getAsInteger("time").toString());
		
		
		




	}

}
