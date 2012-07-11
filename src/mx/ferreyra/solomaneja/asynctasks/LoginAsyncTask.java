package mx.ferreyra.solomaneja.asynctasks;

import static mx.ferreyra.solomaneja.recursos.Recursos.*;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN_BADGES;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN_RUTAS;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOTAL_PUNTOS;
import static mx.ferreyra.solomaneja.recursos.Recursos.USER;
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
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.Token;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoginAsyncTask extends AsyncTask<Void, Void, Token> {

	private Context context;
	private String email, pass;

	private ANS ans = null;
	private ProgressDialog pd;

	public LoginAsyncTask(Context context, String email, String pass) {

		this.email = email;
		this.pass = pass;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		pd.setCancelable(true);
		
	}

	@Override
	protected Token doInBackground(Void... params) {

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "login"); 

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", pass));

		Token token = null;

		ans = ANS.OK;

		try {
			token = (Token) net.sendDataAndGetObject(url, nameValuePairs, Token.class);
		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return token;

	}

	@Override
	protected void onPostExecute(Token token) {

		pd.dismiss();
		
		if (isCancelled() )
			return;
		
		
		String msg = null;
		switch (ans) {

		case OK:
			if(token != null  && token.token != null){
				Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

				prefs.saveData(USER, this.email);
				prefs.saveData(PASS, this.pass);
				prefs.saveData(TOKEN, Utils.getMD5(token.token + Utils.getMD5(this.pass)) );

				prefs.saveData(USER_ID, token.user_id );

				if (token.totals != null && token.totals.size() > 0){

					prefs.saveData(TOTAL_PUNTOS, token.totals.get(0).total_puntos );
					prefs.saveData(TOKEN_BADGES, token.totals.get(0).total_badges );
					prefs.saveData(TOKEN_RUTAS, token.totals.get(0).total_rutas );
				}
				((Activity) context).startActivityForResult(new Intent(context, SoloManejaActivity.class), SoloManejaActivity.ID);
				//((Activity) context).finish();
			}
			else 
				msg = "Verifica tu nombre de usuario o contrase–a";

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
