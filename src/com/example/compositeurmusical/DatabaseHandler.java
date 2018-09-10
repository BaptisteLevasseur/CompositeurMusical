package com.example.compositeurmusical;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Nous sommes à la première version de la base
	// Si je décide de la mettre à jour, il faudra changer cet attribut
	private final static int VERSION = 1;
	// Le nom du fichier qui représente ma base
	private final static String NOM = "database.db";


	private static final String FILE_KEY = "id";
	private static final String FILE_DURATION = "duree";
	private static final String FILE_TITLE = "titre";

	private static final String FILE_TABLE_NAME = "FichiersSonores";

	//Requête SQL de création de la table de fichiers sonores
	private static final String FILE_TABLE_CREATE =
			"CREATE TABLE " + FILE_TABLE_NAME + " (" +
					FILE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					FILE_DURATION+ " INTEGER, " +
					FILE_TITLE + " TEXT);";

	private static final String FILE_TABLE_DROP = "DROP TABLE IF EXISTS " + FILE_TABLE_NAME + ";";


	public DatabaseHandler(Context context) {
		super(context, FILE_TABLE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FILE_TABLE_CREATE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(FILE_TABLE_DROP);
		onCreate(db);
	}

	public  String getTableName(){
		return FILE_TABLE_NAME;
	}
	public String getDuree(){
		return FILE_DURATION;
	}
	public  String getTitre(){
		return FILE_TITLE;
	}
	public String getKey(){
		return FILE_KEY;
	}
}