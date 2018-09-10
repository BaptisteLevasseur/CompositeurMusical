package com.example.compositeurmusical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {

	Button compositeurMusique;
	Button gestionnaireSon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		compositeurMusique = (Button) findViewById(R.id.compositeur_musique);
		gestionnaireSon= (Button) findViewById(R.id.gestionnaire_musique);

		gestionnaireSon.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent gestionnaireSon = new Intent(MenuActivity.this, GestionnaireSonActivity.class);
				startActivity(gestionnaireSon);


			}});
		
		compositeurMusique.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent compositeurMusique= new Intent(MenuActivity.this, CompositeurActivity.class);
				startActivity(compositeurMusique);
				
			}
		});

	}
}
