package mx.ferreyra.solomaneja.asynctasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.fragments.BadgesF;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.Badge;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.ui.UI;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

public class GetBadgesAsyncTask extends AsyncTask<Void, Void, ArrayList<Badge>> {

	private Context context;

	private ANS ans = null;
	//private ProgressDialog pd;

	private Fragment fragment;

	public GetBadgesAsyncTask(Context context) {

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

	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<Badge> doInBackground(Void... params) {

		Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "user_badges/" + prefs.loadData(Recursos.TOKEN) + "/?usuario=" + prefs.loadData(Recursos.USER)); 

		ans = ANS.OK;

		ArrayList<Badge> badges = null;
		try {
			Type type = new TypeToken<Collection<Badge>>() {}.getType();
			//ArrayList<Route> type = new ArrayList<Route>();
			badges = (ArrayList<Badge>) net.sendDataAndGetObject(url, null, type);

		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return badges;

	}

	@Override
	protected void onPostExecute(	ArrayList<Badge> badges ) {

		//pd.dismiss();
		((SoloManejaActivity)context).setVisibilityProgressBar(false);

		if (isCancelled() )
			return;

		String msg = null;
		switch (ans) {

		case OK:

			if (fragment != null){
				if (badges != null && badges.size() >0){
					((BadgesF) fragment).adapter.clear();
					for( Badge badge :badges){
						((BadgesF) fragment).adapter.add(badge);
						((BadgesF) fragment).adapter.notifyDataSetChanged();
					}	
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
