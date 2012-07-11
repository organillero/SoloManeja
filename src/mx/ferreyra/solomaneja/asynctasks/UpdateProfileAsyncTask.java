package mx.ferreyra.solomaneja.asynctasks;

import static mx.ferreyra.solomaneja.recursos.Recursos.PASS;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.LoginActivity;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.fragments.PerfilF;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.Profile;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.ui.UI;
import mx.ferreyra.solomaneja.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

public class UpdateProfileAsyncTask extends AsyncTask<Void, Void, Profile> {

	private Context context;
	private String nombre, apellido, sexo, lugar, password, oldPassword, avatar;

	private ANS ans = null;
	private ProgressDialog pd;
	
	private Fragment fragment;

	public UpdateProfileAsyncTask(Context context, String nombre, String apellido, String sexo, String lugar, String oldPassword, String password, String avatar) {

		this.context = context;
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.lugar = lugar;
		this.password = password;
		this.oldPassword = oldPassword;
		this.avatar = avatar;
	}
	
	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		pd.setCancelable(true);



	}

	@Override
	protected Profile doInBackground(Void... params) {

		Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server),"edit_profile/" + prefs.loadData(Recursos.TOKEN) + "/?usuario=" + prefs.loadData(Recursos.USER)); 

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		try {
			if (nombre != null)			nameValuePairs.add(new BasicNameValuePair("nombre", URLEncoder.encode(nombre,"UTF-8")));
			if (apellido != null)		nameValuePairs.add(new BasicNameValuePair("apellido", URLEncoder.encode(apellido,"UTF-8")));
			if (sexo != null)			nameValuePairs.add(new BasicNameValuePair("sexo", URLEncoder.encode(sexo,"UTF-8")));
			if (lugar != null)			nameValuePairs.add(new BasicNameValuePair("lugar", URLEncoder.encode(lugar,"UTF-8")));
			if (password != null)		nameValuePairs.add(new BasicNameValuePair("password", URLEncoder.encode(password,"UTF-8")));
			if (oldPassword != null)	nameValuePairs.add(new BasicNameValuePair("oldpassword", URLEncoder.encode(oldPassword,"UTF-8")));
			if (avatar != null)			nameValuePairs.add(new BasicNameValuePair("avatar", avatar));
		} catch (UnsupportedEncodingException e1) {
			Log.e(Recursos.TAG, "Error. Encode" + e1.toString());
			e1.printStackTrace();
		}

		Profile profile = null;

		ans = ANS.OK;

		try {
			profile = (Profile) net.sendDataAndGetObject(url, nameValuePairs, Profile.class);
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

		pd.dismiss();

		((PerfilF) fragment).newPass.setText("");
		((PerfilF) fragment).oldPass.setText("");
		
		if (isCancelled() )
			return;


		String msg = null;
		switch (ans) {

		case OK:
			if(profile != null){
				if (profile.token != null){
					Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

					if (this.password != null){
						prefs.saveData(TOKEN, Utils.getMD5(profile.token + Utils.getMD5(this.password)) );
						prefs.saveData(PASS, this.password);
					}
					else{
						prefs.saveData(TOKEN, Utils.getMD5(profile.token + Utils.getMD5(prefs.loadData(PASS))));
					}


				}
				else if (profile.error_message!= null)
					UI.showAlertDialog("Error", profile.error_message, "OK", context, null);

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
