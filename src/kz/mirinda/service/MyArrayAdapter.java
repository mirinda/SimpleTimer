package kz.mirinda.service;

import java.util.List;

import kz.mirinda.service.model.WTimer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<WTimer> implements OnClickListener {
	WTimerActivity context;
	List<WTimer> timers;
	public class ViewHolder{
		TextView nameText;
		TextView timeText;
		int position;
	}
	public MyArrayAdapter(Context context, int textViewResourceId,
			List<WTimer> objects) {
		super(context, textViewResourceId, objects);
		this.context= (WTimerActivity) context;
		this.timers = objects;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("mirinda1","getView pos:"+position);
		View rowView = convertView;
		WTimer timer = timers.get(position); 
		if(rowView==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.main_row, parent,false);
			
			TextView nameText = (TextView) rowView.findViewById(R.id.m_name);
			TextView timeText = (TextView) rowView.findViewById(R.id.m_time);
			RelativeLayout layout= (RelativeLayout) rowView.findViewById(R.id.m_rel_layout_row);
			
			layout.setOnClickListener(context);			
			nameText.setText(timer.getName()); 
			timeText.setText(timer.timeToString());
			
			ViewHolder holder = new ViewHolder();
			holder.nameText = nameText;
			holder.timeText = timeText;
			holder.position = position;
			rowView.setTag(holder);
		}else{
			
			ViewHolder holder =  (ViewHolder) rowView.getTag();
			holder.nameText.setText(timer.getName());
			holder.timeText.setText(timer.timeToString());
			holder.position=position;
		}
		return rowView;
	}

	@Override
	public void onClick(View v) {
		Log.i("mirinda1","Rel Layout onClick");
		
	}

}
