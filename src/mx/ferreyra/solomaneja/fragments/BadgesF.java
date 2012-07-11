package mx.ferreyra.solomaneja.fragments;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.asynctasks.GetBadgesAsyncTask;
import mx.ferreyra.solomaneja.pojo.Badge;
import mx.ferreyra.solomaneja.ui.UI;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BadgesF extends SherlockListFragment {

	public static final String TAG = "BadgesF";

	public MyAdapter adapter=null;

	protected View view = null;
	private Context context;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private static final Format formatter = new SimpleDateFormat("MMM dd, yyyy - HH:mm");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		this.context = getActivity();

		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub)
		.cacheInMemory()
		.cacheOnDisc()
		.build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = inflater.inflate(R.layout.list, container, false);

		if (getListAdapter() == null){
			this.adapter = new MyAdapter(this.context, R.layout.badge_item, new ArrayList<Badge>());
			setListAdapter(this.adapter);
		}

		updateData();
		
		//GetBadgesAsyncTask asyncTask = new GetBadgesAsyncTask(context);
		

		return view;
	}

	@Override
	public void onStop() {
		imageLoader.stop();
		super.onStop();
	}


	public void updateData(){
		GetBadgesAsyncTask asyncTask = new GetBadgesAsyncTask(context);
		asyncTask.setFragment(this);
		asyncTask.execute();
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position < 0)
			return;
		Badge o = this.adapter.getItem(position);
		if (o != null){
			UI.showToast(o.nombre, context );
		}
	}


	public class MyAdapter extends ArrayAdapter<Badge> {

		private int textViewResourceId;



		public MyAdapter(Context context, int textViewResourceId, ArrayList<Badge> routes ) {
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

				holder.imgBadge = (ImageView) v.findViewById(R.id.imgBadge);
				holder.nombre = (TextView) v.findViewById(R.id.tvTitleBadge);
				holder.descripcion = (TextView) v.findViewById(R.id.tvDescBadge);
				holder.fecha = (TextView) v.findViewById(R.id.tvTimeBadge);
				v.setTag(holder);
			}
			else {
				holder = (ViewHolder) v.getTag();
			}

			Badge o = super.getItem(position);

			if (o != null){

				//this.imageDownloader.download(o.icono, holder.imgBadge);

				imageLoader.displayImage(o.icono, holder.imgBadge, options);



				holder.nombre.setText(o.nombre);
				holder.descripcion.setText(o.descripcion);

				String tmp = formatter.format(o.fecha);
				tmp= tmp.substring(0,1).toUpperCase() + tmp.substring(1);
				holder.fecha.setText(tmp);

			}
			return v;	
		}
	}


	private static class ViewHolder {
		ImageView imgBadge;
		TextView nombre;
		TextView descripcion;
		TextView fecha;


	}

}
