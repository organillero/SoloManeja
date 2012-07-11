package mx.ferreyra.solomaneja.asynctasks;

import java.io.IOException;
import java.util.ArrayList;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.SoloManejaActivity.Exchanger;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.pojo.Traffic;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.ui.RoutePathOverlay;
import mx.ferreyra.solomaneja.ui.UI;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class GetTrafficAsyncTask extends AsyncTask<Void, Void, Traffic> {

	private Context context;

	private ANS ans = null;
	//private ProgressDialog pd;

	private Fragment fragment;



	public GetTrafficAsyncTask(Context context) {

		this.context = context;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		((SoloManejaActivity)context).setVisibilityProgressBar(true);
		//pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		//pd.setCancelable(false);
	}

	@Override
	protected Traffic doInBackground(Void... params) {

		//Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "puntos/" ); 

		ans = ANS.OK;

		//ArrayList<RouteDetails> routeDetails = null;
		Traffic traffic = null;

		try {
			//Type type = new TypeToken<Collection<RouteDetails>>() {}.getType();
			traffic = (Traffic) net.sendDataAndGetObject(url, null, Traffic.class);

		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return traffic;

	}

	@Override
	protected void onPostExecute(	Traffic traffic ) {


		//pd.dismiss();
		((SoloManejaActivity)context).setVisibilityProgressBar(false);

		if (isCancelled() )
			return;

		String msg = null;
		switch (ans) {

		case OK:

			if (fragment != null){
				
				
				
				if (traffic != null && traffic.detenidos.size() >0){
					
					Exchanger.mMapView.getOverlays().clear();
					
					ArrayList<GeoPoint> pointsStopped = traffic.getGeoPointsStoped();
					ArrayList<GeoPoint> pointsSlow = traffic.getGeoPointsSlow();
					Exchanger.mMapView.getOverlays().add(new RoutePathOverlay(pointsStopped, false));
					Exchanger.mMapView.getOverlays().add(new RoutePathOverlay(pointsSlow, false ));

					MapController mapController = Exchanger.mMapView.getController();
					
					mapController.animateTo(pointsSlow.get(0));
					mapController.setZoom(15); 

									

				}
			}
			break;


		case OFFLINE_ERROR:
			msg = "Verifique su conexi—n a internet,";
			break;

		case IO_ERROR:
			msg = "El servidor no responde. Intente m‡s tarde.";
			break;

		case ERROR:
			msg = "Connection failed! Error";
			break;
		}

		if (msg != null){

			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra("LOGIN_FAIL", true);
			intent.putExtra("MSG_TITLE", "Login Error");
			intent.putExtra("MSG", msg);
			UI.showAlertDialog("Error", msg, "OK", context, null);
		}


	}

}
