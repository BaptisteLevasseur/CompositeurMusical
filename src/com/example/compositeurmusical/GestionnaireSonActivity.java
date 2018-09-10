package com.example.compositeurmusical;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class GestionnaireSonActivity extends Activity {

	ListView listSon;
	Button enregistrement;
	Button edition;
	FichiersMusiqueDAO mesFichiersMusiqueDAO;
	MediaPlayer mPlayer;
	Spinner choixDureeSon;
	ProgressBar progressBar;
	String titre;
	long duree;
	int dureeSon;
	
	MediaManager monMediaManager;
	private int mProgressStatus = 0;
	private Handler mHandler = new Handler();






	public void miseAJourListe(long duree){
		Vector<FichierSon> listeSon;
		mesFichiersMusiqueDAO.open(); 
		listeSon = mesFichiersMusiqueDAO.listerFichier(duree);

		mesFichiersMusiqueDAO.close();


		if (listeSon != null) {
			List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> element;

			for (int i = 0; i < listeSon.size(); i++) {

				element = new HashMap<String, String>();
				element.put("Titre",listeSon.get(i).getTitre());
				element.put("Duree", String.valueOf(listeSon.get(i).getDuree()));
				liste.add(element);
			}

			ListAdapter adapter = new SimpleAdapter(this,liste,android.R.layout.simple_list_item_2,
					new String[] {"Titre", "Duree"}, new int[] {android.R.id.text1, android.R.id.text2 });
			listSon.setAdapter(adapter);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestionnaire_son);

		enregistrement = (Button) findViewById(R.id.enregistrement);
		listSon = (ListView) findViewById(R.id.listSon);
		choixDureeSon = (Spinner) findViewById(R.id.choixDureeSon2);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		edition = (Button) findViewById(R.id.edition);


		mesFichiersMusiqueDAO = new FichiersMusiqueDAO(this);
		monMediaManager = new MediaManager();


	



		//Création de la liste contenant les différentes durées dans le Spinner choixDureeSon
		List<String> choixDuree = new ArrayList<String>();
		choixDuree.add("1");
		choixDuree.add("4");
		choixDuree.add("10");

		//Crée l'adapteur relatif au spinner
		ArrayAdapter<String> adapterChoixDuree = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choixDuree);
		//Permet d'afficher une liste selective lorsque l'on déroule le spinner
		adapterChoixDuree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//Affiche l'adaptateur
		choixDureeSon.setAdapter(adapterChoixDuree);


		duree = Long.valueOf((String) choixDureeSon.getSelectedItem());


		
		//Permet de choisir les Sons de la durée choisie
		miseAJourListe(duree);
		choixDureeSon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				duree = Long.valueOf(choixDureeSon.getSelectedItem().toString());
				miseAJourListe(duree);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});




//Permet de choisir les options à réaliser sur un fichier
		listSon.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap obj = (HashMap) parent.getItemAtPosition(position);
				titre = obj.get("Titre").toString();

				//Création d'une boite de Dialogue 
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(GestionnaireSonActivity.this);
				alertDialog.setTitle(titre);
				alertDialog.setMessage("Que désirez vous faire?");
				//Bouton "Lire"
				alertDialog.setPositiveButton("Lire", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {


						
						Thread progressThread = new Thread(new Runnable() {             
							public void run() {                 
								while (mProgressStatus < 100) {                                          
									// Update the progress bar     
								
									mHandler.post(new Runnable() {                         
										public void run() {                             
											progressBar.setProgress(mProgressStatus);                         
										}                     
									});
									mProgressStatus ++;

									android.os.SystemClock.sleep(dureeSon/100); // Thread.sleep() doesn't guarantee 1000 msec sleep, it can be interrupted before               
								}  
								mProgressStatus = 0;
								progressBar.setProgress(mProgressStatus);  
						

							}         
						});
						String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+titre+".3gp";
						//Ajoute un Player
						MediaPlayer monMediaPlayer = monMediaManager.addMediaPlayer(mFileName);
						//Recupère la durée du Player
						dureeSon = monMediaManager.getDuree(monMediaPlayer);
						//Démarre le Player selectionné
						monMediaManager.startPlaying(monMediaPlayer);
						
						
						progressThread.start();
						
						
						
					}});


				alertDialog.setNeutralButton("Supprimer", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mesFichiersMusiqueDAO.open();
						mesFichiersMusiqueDAO.supprimer(titre);
						mesFichiersMusiqueDAO.close();
						miseAJourListe(duree);
						File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+titre+".3gp");
						file.delete();

					}});

				alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				alertDialog.show();


			}});


		
		enregistrement.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent gestionnaireSon = new Intent(GestionnaireSonActivity.this, EnregistrementActivity.class);
				startActivity(gestionnaireSon);


			}});
		
		edition.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent editeurSon = new Intent(GestionnaireSonActivity.this, ExtractionActivity.class);
				startActivity(editeurSon);
				
			}});
	}

	protected void onResume(){
		super.onResume();
		miseAJourListe(duree);
	}
}
