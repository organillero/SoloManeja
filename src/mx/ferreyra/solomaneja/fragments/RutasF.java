package mx.ferreyra.solomaneja.fragments;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.asynctasks.GetRoutesAsyncTask;
import mx.ferreyra.solomaneja.pojo.Route;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class RutasF extends SherlockListFragment{

	public static final String TAG = "RutasF";
	
	public MyAdapter adapter=null;
	protected View view = null;
	private Context context;

	private static final Format formatterDate = new SimpleDateFormat("MMM dd, yyyy");
	private static final Format formatterTime = new SimpleDateFormat("HH:mm");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		this.context = getActivity();
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		view = inflater.inflate(R.layout.list, container, false);

		if (getListAdapter() == null){
			this.adapter = new MyAdapter(this.context, R.layout.route_item, new ArrayList<Route>());
			setListAdapter(this.adapter);
		}
		updateData();
		

		
		return view;
	}

	
	public void updateData(){
		GetRoutesAsyncTask asynctask = new GetRoutesAsyncTask(context);
		asynctask.setFragment(this);
		asynctask.execute();
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position < 0)
			return;
		Route o = this.adapter.getItem(position);
		if (o != null){
			
			((SoloManejaActivity) context).startMap(o.idruta);
			//Fragment fragment = new RutaMapF(o.idruta);
			//((SoloManejaActivity) context).showFragment(fragment); 
			//UI.showToast(o.idruta, context );
		}
	}


	public class MyAdapter extends ArrayAdapter<Route> {

		private int textViewResourceId; 

		public MyAdapter(Context context, int textViewResourceId, ArrayList<Route> routes ) {
			super(context , textViewResourceId, routes);
			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if (v == null || !(v.getTag() instanceof ViewHolder)) {

				LayoutInflater vi = LayoutInflater.from(context);
				v = vi.inflate(this.textViewResourceId, null);

				holder = new ViewHolder();

				holder.idRoute = (TextView) v.findViewById(R.id.tvIdRoute);
				holder.fecha = (TextView) v.findViewById(R.id.tvDate);
				holder.time = (TextView) v.findViewById(R.id.tvTime);
				holder.duration = (TextView) v.findViewById(R.id.tvDuration);
				holder.points = (TextView) v.findViewById(R.id.tvPoints);
				v.setTag(holder);
			}
			else {
				holder = (ViewHolder) v.getTag();
			}

			Route o = super.getItem(position);

			if (o != null){
				holder.idRoute.setText(o.idruta);

				String tmp = formatterDate.format(o.fecha_ruta);
				tmp= tmp.substring(0,1).toUpperCase() + tmp.substring(1);
				holder.fecha.setText(tmp);

				holder.time.setText(formatterTime.format(o.fecha_ruta));
				holder.duration.setText(o.duracion);
				holder.points.setText(o.valor);
			}
			return v;	
		}
	}


	private static class ViewHolder {
		TextView idRoute;
		TextView fecha;
		TextView time;
		TextView duration;
		TextView points;

	}

}
