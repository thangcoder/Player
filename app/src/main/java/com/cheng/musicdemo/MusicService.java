package com.cheng.musicdemo;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;

public class MusicService extends Service implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener {

	public static final int NOTIFICATION_ID = 1;
	// media player
	private MediaPlayer player;
	// song list
	private ArrayList<Song> songs;
	// current position
	private int songPosn;

	private final IBinder musicBind = new MusicBinder();

	public void onCreate() {
		// create the service
		super.onCreate();
		// initialize position
		songPosn = 0;
		// create player
		player =  new MediaPlayer();

		initMusicPlayer();
		playSong();
//
		setList(new SongsManager().getAllSongs(this));
		IntentFilter filter = new IntentFilter();
		filter.addAction(MainActivity.ACTION_PLAY);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}

	public void initMusicPlayer() {
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public void setList(ArrayList<Song> theSongs) {
		songs = theSongs;
	}

	public class MusicBinder extends Binder {
		MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		player.stop();
		player.release();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	public void setSong(int songIndex) {
		songPosn = songIndex;
	}

	public void playSong() {
//		Song playSong = songs.get(songPosn);
		try {
			player.reset();
			player.setDataSource("/storage/emulated/0/Download/Ánh Nắng Của Anh (Chờ Em Đến Ngày Mai OST)_Đức Phúc_-1075798557.mp3");
			player.prepare();
//			createNotification(playSong.getTitle());
			createNotification("Cheng");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createNotification(String songName) {
		final Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(songName);
		builder.setTicker("play " + songName);
		final Intent notificationIntent = new Intent(this, MainActivity.class);
		final PendingIntent pi = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		builder.setContentIntent(pi);
		final Notification notification = builder.build();
		startForeground(NOTIFICATION_ID, notification);
	}

	BroadcastReceiver mReceiver;

	// use this as an inner class like here or as a top-level class
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MainActivity.ACTION_PLAY)) {
				setSong(intent.getIntExtra(MainActivity.INDEX_MP3, 0));
				playSong();
			}
		}
	}

}