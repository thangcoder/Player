package com.cheng.musicdemo;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String ACTION_PLAY = "action_play";
	public static final String INDEX_MP3 = "index_mp3";

	private ArrayList<Song> songList;
	private ListView songView;
	private Button btnPlay;

	private MusicService musicSrv;
	private Intent playIntent;
	private boolean musicBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initPermission();
		songView = (ListView) findViewById(R.id.song_list);
		btnPlay = (Button) findViewById(R.id.btn_play);
		songList = new ArrayList<Song>();
		getSongList();

		if (playIntent == null) {
			playIntent = new Intent(this, MusicService.class);
			startService(playIntent);
		}

		SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ACTION_PLAY);
				sendBroadcast(intent);
			}
		});
	}

	public void getSongList() {
		songList = new SongsManager().getAllSongs(this);
	}

	public void songPicked(View view) {
		Intent intent = new Intent(ACTION_PLAY);
		intent.putExtra(INDEX_MP3, Integer.parseInt(view.getTag().toString()));
		sendBroadcast(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_shuffle:
			// shuffle
			break;
		case R.id.action_end:
			stopService(playIntent);
			musicSrv = null;
			System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initPermission(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

				//Permisson don't granted
				if (shouldShowRequestPermissionRationale(
						Manifest.permission.READ_EXTERNAL_STORAGE)) {
					Toast.makeText(MainActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
				}
				// Permisson don't granted and dont show dialog again.
				else {
					Toast.makeText(MainActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
				}
				//Register permission
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

			}
		}
	}
}
