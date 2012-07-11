package mx.ferreyra.solomaneja.asynctasks;

import static mx.ferreyra.solomaneja.recursos.Recursos.PASS;
import static mx.ferreyra.solomaneja.recursos.Recursos.TOKEN;
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
import mx.ferreyra.solomaneja.pojo.Register;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class RegisterAsyncTask extends AsyncTask<Void, Void, Register> {

	private Context context;
	private String user, email, pass, country, sex;




	private ANS ans = null;
	private ProgressDialog pd;

	public RegisterAsyncTask(Context context, String user, String email, String pass, String country, String sex) {

		this.context = context;
		this.user = user;
		this.email = email;
		this.pass = pass;
		this.country = country;
		this.sex = sex;


	}
	
	public void facebook (){
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
		pd.setCancelable(true);



	}

	@Override
	protected Register doInBackground(Void... params) {

		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "register"); 

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", user));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", pass));
		nameValuePairs.add(new BasicNameValuePair("lugar", country));
		nameValuePairs.add(new BasicNameValuePair("sexo", sex));

		Register register = null;

		ans = ANS.OK;

		try {
			register = (Register) net.sendDataAndGetObject(url, nameValuePairs, Register.class);
		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}


		return register;

	}

	@Override
	protected void onPostExecute(Register register) {

		pd.dismiss();

		if (isCancelled() )
			return;


		String msg = null;
		switch (ans) {

		case OK:
			if(register != null){
				if (register.error_message == null){
					Preferencias prefs = AppSoloManeja.getInstance().getPrefs();

					prefs.saveData(USER, this.email);
					prefs.saveData(PASS, this.pass);
					prefs.saveData(TOKEN, register.token );

					prefs.saveData(USER_ID, register.user_id.toString() );

					((Activity) context).startActivityForResult(new Intent(context, SoloManejaActivity.class), SoloManejaActivity.ID);
				}
				else if (register.error_message != null){
					
					msg = register.error_message.password != null? register.error_message.password + "\n\n" : "";
					msg += register.error_message.email != null? register.error_message.email + "\n\n" : "";
					msg += register.error_message.username != null? register.error_message.username : "";
					 
					//msg = error1 +"\n\n"+ error2 + "\n\n" + error3;
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
