package kz.mirinda.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.Inflater;

import kz.mirinda.service.MyArrayAdapter.ViewHolder;
import kz.mirinda.service.WTimerService.OnTimeTickListener;
import kz.mirinda.service.db.TimerDataSource;
import kz.mirinda.service.model.WTimer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.backup.RestoreObserver;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class WTimerActivity extends ListActivity implements
		Button.OnClickListener, OnTimeTickListener {
	private final int DIALOG_NEW_TIMER = 1;
	TimerDataSource dataSource;
	List<WTimer> timers;
	MyDialogOnClickListener myDialogOnClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("mirinda1", "Activity create");
		super.onCreate(savedInstanceState);
		((WTimerApplication)getApplication()).setActivityStarted(true);
		dataSource = new TimerDataSource(getApplicationContext());
		setContentView(R.layout.main);
		dataSource.open();
		Button button = (Button) findViewById(R.id.m_new_item);
		button.setOnClickListener(this);
		Button button2 = (Button) findViewById(R.id.m_stop_btn);
		button2.setOnClickListener(this);
		
		
		timers = dataSource.getAllWTimers();

		setListAdapter(new MyArrayAdapter(this, R.layout.main_row, timers));
		registerForContextMenu(getListView());
	}
	@Override
	protected void onStart() {
		Log.i("mirinda1", "Activity start");
		super.onStart();
		((WTimerApplication)getApplication()).register(this);
		if (((WTimerApplication)getApplication()).isServiceStarted()) {
			changeToTimeGo();
		} else {
			changeToTimeChoice();
		}
	}
	@Override
	protected void onStop() {
		Log.i("mirinda1", "Activity stop");
		super.onStop();
		((WTimerApplication)getApplication()).unregister(this);
		dataSource.close();
		((WTimerApplication)getApplication()).setActivityStarted(false);
		finish();
	}
	@Override
	protected void onDestroy() {
		Log.i("mirinda1", "Activity destroy");
		
		super.onDestroy();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_item_context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item_delete) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo();
			dataSource.deleteWTimer(timers.get(info.position));
			timers.remove(info.position);
			((ArrayAdapter<WTimer>) getListAdapter()).notifyDataSetChanged();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.m_new_item)
			showDialog(DIALOG_NEW_TIMER);
		else if (id == R.id.m_rel_layout_row) { /* нужно врубить сервис передав ему выбранный таймер*/
			Log.i("mirinda1","on click view:" + v.toString());
			View rowLayout = (View) v.getParent();
			ViewHolder viewHolder = (ViewHolder) rowLayout.getTag();
			Intent intent= new Intent(WTimerService.WT_SERV);
			intent.putExtra("timer", timers.get(viewHolder.position));
			startService(intent);
			
			/* изменить вид */
			changeToTimeGo();
			
			/* регистрируемся на событие оповещения таймера*/
			((WTimerApplication)getApplication()).register(this);
				
		}else if(id == R.id.m_stop_btn){
			stopService(new Intent(WTimerService.WT_SERV));
			changeToTimeChoice();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_NEW_TIMER) {
			AlertDialog.Builder builder = new Builder(this);
			View view = getLayoutInflater().inflate(R.layout.m_d_new_timer,
					null);

			builder.setTitle(R.string.dialog_title);
			myDialogOnClickListener = new MyDialogOnClickListener(view);
			builder.setNegativeButton(R.string.dialog_no,
					myDialogOnClickListener);
			builder.setPositiveButton(R.string.dialog_ok,
					myDialogOnClickListener);

			builder.setView(view);
			return builder.create();
		}
		return null;
	}
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (id == DIALOG_NEW_TIMER)
			myDialogOnClickListener.clear();
		super.onPrepareDialog(id, dialog);
	}
	private class MyDialogOnClickListener implements
			DialogInterface.OnClickListener {
		TextView nameTextView, minutesTextView, secondsTextView;
		View view;

		public MyDialogOnClickListener(View view) {
			super();
			this.view = view;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				nameTextView = (TextView) view.findViewById(R.id.m_d_text);
				minutesTextView = (TextView) view
						.findViewById(R.id.m_d_minutes);
				secondsTextView = (TextView) view
						.findViewById(R.id.m_d_seconds);
				Log.d("mirinda1", "1");

				String name = nameTextView.getText().toString();
				int minutes = Integer.parseInt(minutesTextView.getText()
						.toString());
				int seconds = Integer.parseInt(secondsTextView.getText()
						.toString());
				long time = (seconds + 60 * minutes) * 1000;
				timers.add(dataSource.createWTimer(name, time));
				((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
		}

		public void clear() {
			// nameTextView.setText("");
			// minutesTextView.setText("");
			// secondsTextView.setText("");
		}

	}

	@Override
	public void onTimeTick(long millisUntilFinished) {
		// add time to textview
		if(millisUntilFinished == WTimerService.FINISH) {
			changeToTimeChoice();
		}else{
			TextView view = (TextView) findViewById(R.id.m_time_text);
			java.text.DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = dateFormat.format(new Date(millisUntilFinished));
			view.setText(date);
		}
	}
	
	private void changeToTimeGo(){
		View layout = findViewById(R.id.m_rel_layout);
		getListView().setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
	}
	private void changeToTimeChoice(){
		View layout = findViewById(R.id.m_rel_layout);
		layout.setVisibility(View.GONE);
		getListView().setVisibility(View.VISIBLE);
	}
}
