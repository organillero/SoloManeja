package mx.ferreyra.solomaneja.fragments;

import mx.ferreyra.solomaneja.SoloManejaActivity.Exchanger;
import mx.ferreyra.solomaneja.asynctasks.GetTrafficAsyncTask;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class TraficoF extends SherlockFragment{

	public static final String TAG = "TraficoF";
	private Context context;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		this.context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
		// The Activity created the MapView for us, so we can do some init stuff.
		Exchanger.mMapView.setClickable(true);
		Exchanger.mMapView.setBuiltInZoomControls(true); // If you want.

		/*
		 * If you're getting Exceptions saying that the MapView already has
		 * a parent, uncomment the next lines of code, but I think that it
		 * won't be necessary. In other cases it was, but in this case I
		 * don't this should happen.
		 */

		final ViewGroup parent = (ViewGroup) Exchanger.mMapView.getParent();
		if (parent != null) parent.removeView(Exchanger.mMapView);

		updateData();

		return Exchanger.mMapView;
	}
	
	public void updateData(){
		GetTrafficAsyncTask asyncTask = new GetTrafficAsyncTask(context);
		asyncTask.setFragment(this);
		asyncTask.execute();
	}
	
}


