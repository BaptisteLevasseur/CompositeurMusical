package com.example.compositeurmusical;

import java.io.IOException;
import java.util.Vector;

import android.media.MediaPlayer;
import android.util.Log;


public class MediaManager {

	Vector<MediaPlayer> mPlayer;


	public MediaManager(){
		mPlayer = new Vector<MediaPlayer>();
	}

	private MediaPlayer findPlayer(String mFileName){
		MediaPlayer monPlayer = new MediaPlayer();
		int i = 0;
		while (!mPlayer.get(i).equals(mFileName))
			i++;
		monPlayer = mPlayer.get(i);

		return monPlayer;
	}

	public MediaPlayer addMediaPlayer(String mFileName){
		MediaPlayer monPlayer = new MediaPlayer();
		try {
			monPlayer.setDataSource(mFileName);

			monPlayer.prepare();


		} catch (IOException e) {
			Log.d("perso", "prepare() failed");
			return null;

		}

		mPlayer.add(monPlayer);
		return monPlayer;
	}

	public MediaPlayer startPlaying(String mFileName) {
		MediaPlayer monPlayer = findPlayer(mFileName);
		monPlayer.start();

		return monPlayer;
	}

	public MediaPlayer startPlaying(MediaPlayer monPlayer){
		monPlayer.start();
		return monPlayer;
	}

	/*public void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}*/

	public int getDuree(String mFileName){

		MediaPlayer monPlayer = findPlayer(mFileName);
		int duree = Integer.valueOf(monPlayer.getDuration());
		return duree;
	}

	public int getDuree(MediaPlayer monPlayer){
		int duree = Integer.valueOf(monPlayer.getDuration());
		return duree;
	}

	public MediaPlayer closePlayer(String mFileName){
		MediaPlayer monPlayer = new MediaPlayer();
		int i = 0;
		while (!mPlayer.get(i).equals(mFileName))
			i++;
		monPlayer = mPlayer.get(i);

		mPlayer.remove(i);
		return monPlayer;
		

	}



}
