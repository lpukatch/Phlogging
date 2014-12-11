package edu.miami.masonandluke.phlogging;

import android.app.Activity;
import android.os.Bundle;

public class ShowInformation extends Activity {

private long id = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinformation);

		
	id = this.getIntent().getLongExtra("edu.miami.masonandluke.phlogging.id", 0l);	
	
	
	
	}

}
