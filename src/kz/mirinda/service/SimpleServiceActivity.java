package kz.mirinda.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import kz.mirinda.service.db.TimerDataSource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


public class SimpleServiceActivity extends Activity implements Button.OnClickListener {
   // Button startButton,stopButton;
    long untillFinished;
    TextView textView;
    CountDownTimer countDownTimer;
	private TimerDataSource dataSource;
    private class MyCountDownTimer extends CountDownTimer{
		
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
			textView.setText(dateFormat.format(new Date(millisUntilFinished)));
			untillFinished =millisUntilFinished;
		}
		
		@Override
		public void onFinish() {
			textView.setText("finish");
			
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("mirinda1","Activity on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //textView = (TextView) findViewById(R.id.text);
        dataSource = new TimerDataSource(getApplicationContext());        
    }

	@Override
	public void onClick(View arg0) {
		Log.i("mirinda1","Activity on click");
		/*int id = arg0.getId();
		if(id == R.id.button1){
			countDownTimer.start();
		}
		if(id == R.id.button2)
			stopService(new Intent(this, MyService.class));*/
	}

	@Override
	protected void onStop() {
		Log.i("mirinda1","Activity on sttop");
		if(untillFinished>0){
			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("timer", untillFinished);
			startService(intent);
		}
		super.onStop();
	}

	@Override
	protected void onStart() {
		untillFinished = getIntent().getLongExtra("timer", -1);
		if(untillFinished >= 0){
			countDownTimer = new MyCountDownTimer(untillFinished, 1000);
			countDownTimer.start();
		}else{
			countDownTimer = new MyCountDownTimer(20000, 1000);
		}
		Log.i("mirinda1","Activity on start");
		super.onStart();
		
	}
	
	
    
}