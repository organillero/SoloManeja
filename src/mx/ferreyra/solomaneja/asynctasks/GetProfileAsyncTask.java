package mx.ferreyra.solomaneja.asynctasks;

import java.io.IOException;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.fragments.PerfilF;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.Profile;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GetProfileAsyncTask extends AsyncTask<Void, Void, Profile> {

	private Context context;

	private ANS ans = null;
	//private ProgressDialog pd;

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	private Fragment fragment;

	public GetProfileAsyncTask(Context context) {

		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub)
		.cacheInMemory()
		.cacheOnDisc()
		.build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		((SoloManejaActivity)context).setVisibilityProgressBar(true);
		//pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		//pd.setCancelable(true);
	}

	@Override
	protected Profile doInBackground(Void... params) {

		Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "get_profile/" + prefs.loadData(Recursos.TOKEN) + "/?usuario=" + prefs.loadData(Recursos.USER)); 



		Profile profile = null;

		ans = ANS.OK;

		try {
			profile = (Profile) net.sendDataAndGetObject(url, null, Profile.class);
		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return profile;

	}

	@Override
	protected void onPostExecute(Profile profile) {

		//pd.dismiss();
		((SoloManejaActivity)context).setVisibilityProgressBar(false);

		if (isCancelled() )
			return;


		String msg = null;
		switch (ans) {

		case OK:
			if(profile != null){
				if (fragment != null){

					((PerfilF) fragment).firstName.setText(profile.nombre);
					((PerfilF) fragment).lastName.setText(profile.apellido);
					((PerfilF) fragment).sex.setChecked(profile.sexo.equals("H"));
					
					imageLoader.displayImage(profile.avatar, ((PerfilF) fragment).photo, options);
				}
			}

			//	msg = "Connection failed! Error";

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
