package mx.ferreyra.solomaneja;

import mx.ferreyra.solomaneja.asynctasks.GetRouteAsyncTask;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RutaMapActivity extends MapActivity{

	public static final String TAG = "RutaMapF";
	private String idruta = null;



	private Context context;
	public MapView mapView;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;

		setContentView(R.layout.trafico);

		this.mapView = (MapView)findViewById(R.id.mapview);
		this.mapView.setClickable(true);
		this.mapView.setBuiltInZoomControls(true);

		idruta = getIntent().getExtras().getString("IDROUTE");
		if (idruta == null)
			finish();
		
		updateData();
	}



	public void updateData(){
		GetRouteAsyncTask asynctask = new GetRouteAsyncTask(context, idruta);
		asynctask.execute();
	}




}
