package mx.ferreyra.solomaneja.asynctasks;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.RutaMapActivity;
import mx.ferreyra.solomaneja.SoloManejaActivity.Exchanger;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.RouteDetails;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.ui.RoutePathOverlay;
import mx.ferreyra.solomaneja.ui.UI;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class GetRouteAsyncTask extends AsyncTask<Void, Void, RouteDetails> {

	private Context context;

	private ANS ans = null;
	//private ProgressDialog pd;

	private Fragment fragment;

	private String idRoute;

	public GetRouteAsyncTask(Context context, String idruta) {

		this.context = context;
		this.idRoute = idruta;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		//((SoloManejaActivity)context).setVisibilityProgressBar(true);
		//pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		//pd.setCancelable(false);
	}

	@Override
	protected RouteDetails doInBackground(Void... params) {

		Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "get_route/" + idRoute + "/?usuario=" + prefs.loadData(Recursos.USER)); 

		ans = ANS.OK;

		//ArrayList<RouteDetails> routeDetails = null;
		RouteDetails routeDetails = null;

		try {
			//Type type = new TypeToken<Collection<RouteDetails>>() {}.getType();
			routeDetails = (RouteDetails) net.sendDataAndGetObject(url, null, RouteDetails.class);

		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return routeDetails;

	}

	@Override
	protected void onPostExecute(	RouteDetails routeDetails ) {


		//pd.dismiss();
		//((SoloManejaActivity)context).setVisibilityProgressBar(false);

		if (isCancelled() )
			return;

		String msg = null;
		switch (ans) {

		case OK:


			if (routeDetails != null && routeDetails.puntos.size() >0){
				ArrayList<GeoPoint> tmp = routeDetails.getGeoPointsRoute();
				((RutaMapActivity)context).mapView.getOverlays().add(new RoutePathOverlay( tmp, true ));
				((RutaMapActivity)context).mapView.invalidate();
				
				MapController mapController = ((RutaMapActivity)context).mapView.getController();
				
				mapController.animateTo(tmp.get(0));
				mapController.setZoom(17);
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
