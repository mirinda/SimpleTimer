package kz.mirinda.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import kz.mirinda.service.model.WTimer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class WTimerService extends Service {
	public static final long FINISH=-1;
	public static final String WT_SERV= "kz.mirinda.service.WT_SERVICE";
	public static interface OnTimeTickListener{
		void onTimeTick(long millisUntilFinished);
	}
	
	private class MyCountDownTimer extends CountDownTimer{
		static final long INTERVAL=1000;
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			
		}

		@Override
		public void onFinish() {
			Log.i("mirinda1","Finish Timer");
			//stop tick notification
			notificationManager.cancel(NOTIFICATION_ID);
			//start TIME!!! notification
			Notification timeNotification = new Notification(R.drawable.ic_launcher,
					                            getResources().getText(R.string.s_ticker_time), System.currentTimeMillis());
			timeNotification.defaults |= Notification.DEFAULT_SOUND;
			//timeNotification.defaults |= Notification.DEFAULT_VIBRATE;
			timeNotification.flags = Notification.FLAG_INSISTENT;
			timeNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			//timeNotification.sound = Uri.parse("android.resource://kz.mirinda.service/raw/feel_so");//Uri.parse("file:///sdcard/Audio/notification/1.mp3");
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode( AudioManager.RINGER_MODE_NORMAL);
			long[] vibrate = {0,100,200,300};
			timeNotification.vibrate = vibrate;
			
			Intent dialogIntent = new Intent(getBaseContext(), WTimerActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, dialogIntent, 0);
			
			timeNotification.setLatestEventInfo(getBaseContext(), curTimer.getName(),
					                            getResources().getText(R.string.s_ticker_time), pendingIntent);
			notificationManager.notify(NOTIFICATION_TIME_ID,timeNotification);
			
			
			//NOTIFY Activity if active
			if(((WTimerApplication)getApplication()).isActivityStarted()){
				((WTimerApplication)getApplication()).fire(FINISH);
			}
			stopSelf();
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//notify activity if subscribe
			((WTimerApplication)getApplication()).fire(millisUntilFinished);
			//notify notification
			Intent intent1= new Intent(getBaseContext(), WTimerActivity.class);
			PendingIntent pendingIntent =  PendingIntent.getActivity(getBaseContext(), 0, intent1, 0);
			tickNotification.setLatestEventInfo(getBaseContext(), curTimer.getName(), WTimer.timeToString(millisUntilFinished),pendingIntent);
			notificationManager.notify(NOTIFICATION_ID, tickNotification);
			Log.d("mirinda1","tick " + millisUntilFinished);
		}
		
	}
	
	private final int NOTIFICATION_ID=0;
	private final int NOTIFICATION_TIME_ID=1;
	WTimer curTimer;
	boolean isStarted;
	NotificationManager notificationManager;
	Notification tickNotification;
	MyCountDownTimer countDownTimer=null;
	
	//event methods
	
		
	//services lifecycle methods
	@Override
	public void onCreate() {
		Log.i("mirinda1", "Service Create");
		((WTimerApplication)getApplication()).setServiceStarted(true);
		super.onCreate();
		
	}
	@Override
	public void onDestroy() {
		Log.i("mirinda1", "Service Destroy");
		((WTimerApplication)getApplication()).setServiceStarted(false);
		countDownTimer.cancel();
		notificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("mirinda1", "Service Start");
		super.onStart(intent, startId);
		curTimer =  (WTimer) intent.getSerializableExtra("timer");
		
		//notify to start
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		tickNotification = new Notification(R.drawable.ic_launcher,getResources().getText(R.string.s_ticker_start),System.currentTimeMillis());
		//notification.defaults |= Notification.DEFAULT_SOUND;
		//notification.defaults |= Notification.DEFAULT_VIBRATE;
		//notification.flags |= Notification.FLAG_INSISTENT;
		tickNotification.flags |= Notification.FLAG_ONGOING_EVENT;
		tickNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		//notification.number+=1;
		
		Intent dialogIntent = new Intent(getBaseContext(), WTimerActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent pendingIntent =  PendingIntent.getActivity(this, 0, dialogIntent, 0);
		tickNotification.setLatestEventInfo(this, curTimer.getName(), curTimer.timeToString(),pendingIntent);
		notificationManager.notify(NOTIFICATION_ID, tickNotification);
		
		
		
		
		
		//start timer		
		countDownTimer = new MyCountDownTimer(curTimer.getTime(), MyCountDownTimer.INTERVAL);
		countDownTimer.start();		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
