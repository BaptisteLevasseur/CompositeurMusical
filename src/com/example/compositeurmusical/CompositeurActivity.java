package com.example.compositeurmusical;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class CompositeurActivity extends Activity {
	
	Context context;
	RelativeLayout myLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compositeur);
		
		context = getApplicationContext();


		myLayout = (RelativeLayout) findViewById(R.id.layout);
		mesParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
	}
}
