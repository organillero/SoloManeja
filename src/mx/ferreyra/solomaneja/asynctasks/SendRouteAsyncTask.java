package mx.ferreyra.solomaneja.asynctasks;

import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN_BADGES;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN_RUTAS;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOTAL_PUNTOS;
import static mx.ferreyra.solomaneja.recursos.Recursos.USER_ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.MyDB;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.NewRoute;
import mx.ferreyra.solomaneja.pojo.Token;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class SendRouteAsyncTask extends AsyncTask<Void, Void, Token> {

	private Context context;
	private Long idRoute;

	private ANS ans = null;
	//private ProgressDialog pd;

	public SendRouteAsyncTask(Context context, Long idRoute) {

		this.idRoute = idRoute;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		//pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		//pd.setCancelable(false);



	}

	@Override
	protected Token doInBackground(Void... params) {

		MyDB db = new MyDB(context);
		
		Preferencias prefs = AppSoloManeja.getInstance().getPrefs();
		
		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "new_rute/" + prefs.loadData(Recursos.TOKEN) + "/?usuario=" + prefs.loadData(Recursos.USER)); 

		db.open();
		NewRoute route = db.getRoute(idRoute);
		db.close();
		String data = new Gson().toJson(route);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("puntos", data));


		ans = ANS.OK;

		try {
			String is = net.inputStreamToString(net.sendData(url, nameValuePairs));
			
			Log.d(Recursos.TAG, is);
			
		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return null;

	}

	@Override
	protected void onPostExecute(Token token) {

		//pd.dismiss();

		if (isCancelled() )
			return;


		String msg = null;
		switch (ans) {

		case OK:
			if(token != null  && token.token != null){
				Preferencias prefs = AppSoloManeja.getInstance().getPrefs();



				prefs.saveData(USER_ID, token.user_id );

				if (token.totals != null && token.totals.size() > 0){

					prefs.saveData(TOTAL_PUNTOS, token.totals.get(0).total_puntos );
					prefs.saveData(TOKEN_BADGES, token.totals.get(0).total_badges );
					prefs.saveData(TOKEN_RUTAS, token.totals.get(0).total_rutas );
				}
				((Activity) context).startActivityForResult(new Intent(context, SoloManejaActivity.class), SoloManejaActivity.ID);
				//((Activity) context).finish();
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

			((Activity) context).startActivityForResult(intent, LoginActivity.ID);
			//UI.showAlertDialog("Error", msg, "OK", context, null);
		}


	}

}
