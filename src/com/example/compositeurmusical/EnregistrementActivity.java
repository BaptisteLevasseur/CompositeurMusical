package com.example.compositeurmusical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.widget.Spinner;
import android.widget.TextView;


public class EnregistrementActivity extends Activity {

	Spinner choixDureeSon;
	EditText nomSon;
	Button enregistrerSon;
	TextView duree;
	TextView titre;
	ProgressBar progress;

	long dureeSon;
	String nomFichier;
	FichierSon Son;
	Timer timer;
	MediaRecorder mRecorder;
	FichiersMusiqueDAO mesFichiersMusiqueDAO;

	private int mProgressStatus = 0;
	private Handler mHandler = new Handler();



	//Méthode permettant de lancer un Toast n'importe où
	public void showToast(final String toast)
	{
		runOnUiThread(new Runnable() {
			public void run()
			{
				Toast.makeText(EnregistrementActivity.this, toast, Toast.LENGTH_SHORT).show();
			}
		});
	}

	//Permet de lancer un enregistrement dans "nomFichier"
	public void startRecording(){

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

		mRecorder.setOutputFile(nomFichier);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.d("perso", "prepare() failed");
		}

		mRecorder.start();


	};
	public void stopRecording(){
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;

	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enregistrement);

		choixDureeSon = (Spinner) findViewById(R.id.choixDureeSon);
		nomSon = (EditText) findViewById(R.id.nomSon);
		enregistrerSon = (Button) findViewById(R.id.enregistrerSon);
		progress = (ProgressBar) findViewById(R.id.progress);


		//Initialise le bouton d'enregistrement comme inaccessible tant que l'on n'a pas écrit de nom
		enregistrerSon.setEnabled(false);

		//Permet de gérer l'accessibilité du bouton enregistrerSon en fonction des caractères saisis
		nomSon.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				//b renvoie true si il n'y a que des caractères du type a-z ou 0-9
				Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(nomSon.getText());
				boolean b = m.find();

				enregistrerSon.setEnabled(!b);
				if (nomSon.getText().toString().matches("")){
					enregistrerSon.setEnabled(false);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}});


		//Initialise la base de donnée
		mesFichiersMusiqueDAO = new FichiersMusiqueDAO(this);


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





		//Création du listener pour l'appuie du bouton
		enregistrerSon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dureeSon = Long.valueOf((String) choixDureeSon.getSelectedItem());
				nomFichier= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+String.valueOf(nomSon.getText())+".3gp";
				Son = new FichierSon(1, dureeSon, String.valueOf(nomSon.getText()));



				//Création d'une boite de Dialogue 
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnregistrementActivity.this);
				alertDialog.setTitle("Enregistrer Son");
				alertDialog.setMessage("Êtes vous sûr de vouloir réaliser un enregistrement de "+String.valueOf(dureeSon)+" s nommé  \" "+ String.valueOf(nomSon.getText())+" \" ?");
				//Bouton "Ok"
				alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {


						//On ajoute les informations dans la base de donnée
						mesFichiersMusiqueDAO.open();
						mesFichiersMusiqueDAO.ajouter(Son);
						mesFichiersMusiqueDAO.close();





						startRecording();
						//Lance un thread qui compte jusqu'à 100 tous les dureeSon*10 ms
						Thread progressThread = new Thread(new Runnable() {             
							public void run() {                 
								while (mProgressStatus < 100) {                                          
									// Update the progress bar                     
									mHandler.post(new Runnable() {                         
										public void run() {                             
											progress.setProgress(mProgressStatus);                         
										}                     
									});
									mProgressStatus ++;
									android.os.SystemClock.sleep(dureeSon*10); // Thread.sleep() doesn't guarantee 1000 msec sleep, it can be interrupted before               
								}  
								mProgressStatus = 0;
								showToast("Enregistrement terminé");
								stopRecording();
							}         
						});

						progressThread.start();




					}});
				//Bouton "Cancel"
				alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}});

				alertDialog.show();




			}
		});


	}
}
