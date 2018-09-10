package com.example.compositeurmusical;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExtractionActivity extends Activity {
	/**
	 * Repr�sente le texte qui s'affiche quand la liste est vide
	 */
	private TextView mEmpty = null;
	/**
	 * La liste qui contient nos fichiers et r�pertoires
	 */
	private ListView mList = null;
	/**
	 * Notre Adapter personnalis� qui lie les fichiers � la liste
	 */
	private FileAdapter mAdapter = null;
	
	/**
	 * Repr�sente le r�pertoire actuel
	 */
	private File mCurrentFile = null;
	/**
	 * Couleur voulue pour les r�pertoires
	 */
	
	private File fichier = null;
	private MediaManager monMediaManager;
	
	private boolean mCountdown = false;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraction);
        
        // On r�cup�re la ListView de notre activit�
        mList = (ListView) findViewById(R.id.list);
        
        monMediaManager = new MediaManager();
        
        // On v�rifie que le r�pertoire externe est bien accessible
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
        	// S'il ne l'est pas, on affiche un message
        	mEmpty = (TextView) mList.getEmptyView();
        	mEmpty.setText("Vous ne pouvez pas acc�der aux fichiers");
        } else {
        	// S'il l'est...
        	
        	
        	
        	
        	
        	// On r�cup�re la racine de la carte SD pour qu'elle soit 
        	mCurrentFile = Environment.getExternalStorageDirectory();
        	
        	// On change le titre de l'activit� pour y mettre le chemin actuel
        	setTitle(mCurrentFile.getAbsolutePath());
        	
        	// On r�cup�re la liste des fichiers dans le r�pertoire actuel
        	File[] fichiers = mCurrentFile.listFiles();
        	
        	// On transforme le tableau en une structure de donn�es de taille variable
        	ArrayList<File> liste = new ArrayList<File>();
        	for(File f : fichiers){
        		if(!f.isDirectory()){
        			String ext = f.getName().substring(f.getName().indexOf(".") + 1).toLowerCase();
        			Log.d("perso",ext);
        			if (ext.equals("3gp") || ext.equals("mp3") || ext.equals("wav"))
        			liste.add(f);
        		}
        		else
        		liste.add(f);
                
        	}
        	
        	
        	mAdapter = new FileAdapter(this, android.R.layout.simple_list_item_1, liste);
        	// On ajoute l'adaptateur � la liste
        	mList.setAdapter(mAdapter);
        	// On trie la liste
        	mAdapter.sort();
        	
        	// On ajoute un Listener sur les items de la liste
        	mList.setOnItemClickListener(new OnItemClickListener() {

        		// Que se passe-il en cas de cas de clic sur un �l�ment de la liste ?
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					 fichier = mAdapter.getItem(position);
					// Si le fichier est un r�pertoire...
					if(fichier.isDirectory())
						// On change de r�pertoire courant
						updateDirectory(fichier);
					else {
						String titre = fichier.getName().toString();
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExtractionActivity.this);
					alertDialog.setTitle(titre);
					alertDialog.setMessage("Que d�sirez vous faire ?");

					alertDialog.setPositiveButton("Lire", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which){
							String mFileName = fichier.getPath();
							//Ajoute un Player
							MediaPlayer monMediaPlayer = monMediaManager.addMediaPlayer(mFileName);
							//D�marre le Player selectionn�
							monMediaManager.startPlaying(monMediaPlayer);
							
						}
					});
					alertDialog.setNeutralButton("Ajouter", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog,
								int which) {


						}

					});
					alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog,
								int which) {


						}

					});
					alertDialog.show();
				}}
			});
        }
    }

   
    
    
    
    /**
     * Utilis� pour visualiser un fichier
     * @param pFile le fichier � visualiser
     */
    private void seeItem(File pFile) {
    	// On cr�� un Intent
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	
    	// On d�termine son type MIME
    	MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = pFile.getName().substring(pFile.getName().indexOf(".") + 1).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext);
    	
        // On ajoute les informations n�cessaires
    	i.setDataAndType(Uri.fromFile(pFile), type);
    	
    	try {
			startActivity(i);
			// Et s'il n'y a pas d'activit� qui puisse g�rer ce type de fichier
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Oups, vous n'avez pas d'application qui puisse lancer ce fichier", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    }
    
    /**
     * Utilis� pour naviguer entre les r�pertoires
     * @param pFile le nouveau r�pertoire dans lequel aller
     */
    
    public void updateDirectory(File pFile) {
    	// On change le titre de l'activit�
    	setTitle(pFile.getAbsolutePath());
    	
    	// L'utilisateur ne souhaite plus sortir de l'application
    	mCountdown = false;
    	
    	// On change le repertoire actuel
    	mCurrentFile = pFile;
    	// On vide les r�pertoires actuels
    	setEmpty();

    	// On r�cup�re la liste des fichiers du nouveau r�pertoire
    	File[] fichiers = mCurrentFile.listFiles();
    	
    	// Si le r�pertoire n'est pas vide...
    	if(fichiers != null)
    		// On les ajoute �  l'adaptateur
    		for(File f : fichiers)
    			mAdapter.add(f);
    	// Et on trie l'adaptateur
    	mAdapter.sort();
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// Si on a appuy� sur le retour arri�re
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		// On prend le parent du r�pertoire courant
    		File parent = mCurrentFile.getParentFile();
    		// S'il y a effectivement un parent
    		if(parent != null)
    			updateDirectory(parent);
    		else {
    			// Sinon, si c'est la premi�re fois qu'on fait un retour arri�re
    			if(mCountdown != true) {
    				// On indique � l'utilisateur qu'appuyer dessus une seconde fois le fera sortir
    				Toast.makeText(this, "Nous sommes d�j� � la racine ! Cliquez une seconde fois pour quitter", Toast.LENGTH_SHORT).show();
    	    		mCountdown  = true;
    			} else
    				// Si c'est la seconde fois on sort effectivement
    				finish();
    		}
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    /**
     * On enl�ve tous les �l�ments de la liste
     */
    
    public void setEmpty() {
    	// Si l'adapteur n'est pas vide...
    	if(!mAdapter.isEmpty())
    		// Alors on le vide !
    		mAdapter.clear();
    }
    
    /**
     * L'adaptateur sp�cifique � nos fichiers
     */
    
    private class FileAdapter extends ArrayAdapter<File> {
    	/**
    	 * Permet de comparer deux fichiers
    	 *
    	 */
        private class FileComparator implements Comparator<File> {

    		public int compare(File lhs, File rhs) {
    			// si lhs est un r�pertoire et pas l'autre, il est plus petit
    			if(lhs.isDirectory() && rhs.isFile())
    				return -1;
    			// dans le cas inverse, il est plus grand
    			if(lhs.isFile() && rhs.isDirectory())
    				return 1;
    			
    			//Enfin on ordonne en fonction de l'ordre alphab�tique sans tenir compte de la casse
    			return lhs.getName().compareToIgnoreCase(rhs.getName());
    		}
        	
        }
        
    	public FileAdapter(Context context, int textViewResourceId,
				List<File> objects) {
			super(context, textViewResourceId, objects);
			mInflater = LayoutInflater.from(context);
		}

		private LayoutInflater mInflater = null;
		
		/**
		 * Construit la vue en fonction de l'item
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView vue = null;
			
			if(convertView != null)
				vue = (TextView) convertView;
			else
				vue = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, null);
			File item = getItem(position);
			//Si c'est un r�pertoire, on choisit la couleur dans les pr�f�rences
			if(item.isDirectory())
				vue.setTextColor(Color.RED);
			else
				// Sinon c'est du noir
				vue.setTextColor(Color.BLACK);
			vue.setText(item.getName());
			return vue;
		}
		
		/**
		 * Pour trier rapidement les �l�ments de l'adaptateur
		 */
		public void sort () {
			super.sort(new FileComparator());
		}
    }
    
}
	
