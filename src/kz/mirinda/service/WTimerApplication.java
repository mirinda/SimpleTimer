package kz.mirinda.service;

import java.util.LinkedList;
import java.util.List;

import kz.mirinda.service.WTimerService.OnTimeTickListener;
import android.app.Application;
import android.util.Log;

public class WTimerApplication extends Application {
	private List<OnTimeTickListener> listeners = new LinkedList<OnTimeTickListener>();
	private boolean isServiceStarted=false;
	private boolean isActivityStarted=false;
	public boolean isServiceStarted() {
		return isServiceStarted;
	}
	public void setServiceStarted(boolean isServiceStarted) {
		this.isServiceStarted = isServiceStarted;
	}
	public boolean isActivityStarted() {
		return isActivityStarted;
	}
	public void setActivityStarted(boolean isActivityStarted) {
		this.isActivityStarted = isActivityStarted;
	}
	
	void fire(long millisUntilFinished){
		for(OnTimeTickListener listener: listeners){
			listener.onTimeTick(millisUntilFinished);
			Log.d("mirinda1", "FIRE!!");
		}
	}
	public void register(OnTimeTickListener listener){
		listeners.add(listener);
		Log.d("mirinda1", "registered " + listener.toString());
	}
	public void unregister(OnTimeTickListener listener){
		listeners.remove(listener);
	}
	

	
	
}
