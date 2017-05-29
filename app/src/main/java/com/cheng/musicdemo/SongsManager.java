package com.cheng.musicdemo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SongsManager {
	// SDCard Path
	final String MEDIA_PATH = new String("/storage/emulated/0/Download/");
	private ArrayList<Song> songsList = new ArrayList<Song>();

	// Constructor
	public SongsManager() {

	}

	/**
	 * Function to read all mp3 files from sdcard and store the details in
	 * ArrayList
	 * */
	public ArrayList<Song> getAllSongs(Context context) {
		ArrayList<Song> songsList = new ArrayList<Song>();
		Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] STAR=null;
		Cursor cursor = context.getContentResolver().query(allsongsuri, STAR, selection, null, null);
		//or
		//Cursor cursor = getActivity().getContentResolver().query(allsongsuri, null, null, null, selection);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					String song_name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					int song_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));

					String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
					String Duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
					Song song = new Song();
					song.setTitle(song_name);
					song.setPath(fullpath);

					// Adding each song to SongList
					songsList.add(song);
					Log.e("Cheng", "Song Name ::"+song_name+" Song Id :"+song_id+" fullpath ::"+fullpath+" Duration ::"+Duration);

				} while (cursor.moveToNext());

			}
			cursor.close();

		}
		return songsList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
