package com.example.compositeurmusical;

import java.util.Vector;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class FichiersMusiqueDAO{
	private SQLiteDatabase mDb = null;
	private DatabaseHandler mHandler = null;

	//Constructeur
	public FichiersMusiqueDAO(Context pContext) {
		this.mHandler = new DatabaseHandler(pContext);

	}





	public SQLiteDatabase open() {
		// Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
		mDb = mHandler.getWritableDatabase();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}


	public void ajouter(FichierSon Son){
		ContentValues value = new ContentValues();


		value.put(mHandler.getDuree(), Son.getDuree());
		value.put(mHandler.getTitre(), Son.getTitre());
		mDb.insert(mHandler.getTableName(), null, value);
	}

	public void supprimer(String titre){

		mDb.delete(mHandler.getTableName(), mHandler.getTitre() + "= '" + titre + "'", null);


	}

	public void wipeAllData(){
		mDb.delete(mHandler.getTableName(),null,null);
	}

	public void modifier(FichierSon Son){

	}

	public Vector<FichierSon> listerFichier(long mduree){

		String tabColonne[] = new String[3];
		Vector<FichierSon> lFichierSon = new Vector<FichierSon>();

		tabColonne[0] = mHandler.getKey();
		tabColonne[1]= mHandler.getDuree(); 
		tabColonne[2] = mHandler.getTitre();


		Cursor cursor = mDb.query(mHandler.getTableName(),
				tabColonne,
				mHandler.getDuree() + " = ?", new String[] {String.valueOf(mduree)},  null, null,  null);


		// On récupère les valeurs des colonnes "id", "nom" et
		// "numero_telephone" des enregistrements stockés dans l'objet
		// cursor. On construit un vecteur de "Contact"
		int numeroColonneKey = cursor.getColumnIndexOrThrow(mHandler.getKey());
		int numeroColonneDuree =
				cursor.getColumnIndexOrThrow(mHandler.getDuree());
		int numeroColonneTitre =
				cursor.getColumnIndexOrThrow(mHandler.getTitre());
		if (cursor.moveToFirst() == true) {
			do {
				long key = cursor.getLong(numeroColonneKey);
				long duree = cursor.getLong(numeroColonneDuree);
				String titre =  
						cursor.getString(numeroColonneTitre);
				FichierSon son = new FichierSon(key, duree, titre);
				lFichierSon.add(son);
			} while (cursor.moveToNext());
		} 

		return  lFichierSon;


	}


}

