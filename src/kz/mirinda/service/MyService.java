package kz.mirinda.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	private static long SECOND=1000;
	private static Service instance=null;
	public static Service getInstance(){
		return instance;
	}
	long untilFinished;
	
	public long getUntilFinished() {
		return untilFinished;
	}
	private MyCountDownTimer countDownTimer;
	private class MyCountDownTimer extends CountDownTimer {
		
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			untilFinished = millisUntilFinished;			
		}
		
		@Override
		public void onFinish() {
			Log.i("mirinda1","Finish Timer");
			//NOTIFICATION.
			Intent dialogIntent = new Intent(getBaseContext(), SimpleServiceActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			dialogIntent.putExtra("timer",(long) 0);
			getApplication().startActivity(dialogIntent);
			stopSelf();
		}
	};
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		countDownTimer =  new MyCountDownTimer(intent.getLongExtra("timer", 0), SECOND);
		countDownTimer.start();
		Log.i("mirinda1","Service Start");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
		Log.i("mirinda1","Service Create");
	}

	@Override
	public void onDestroy() {
		instance=null;
		Log.i("mirinda1","Service Destroy");
		super.onDestroy();		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {	
		Log.i("mirinda1","onStartCommand: flag -"+flags+"; startid -"+startId+";");
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	

}
