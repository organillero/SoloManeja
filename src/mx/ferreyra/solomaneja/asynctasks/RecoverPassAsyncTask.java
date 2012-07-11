package mx.ferreyra.solomaneja.asynctasks;

import java.io.IOException;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.exceptions.OfflineException;
import mx.ferreyra.solomaneja.interfaces.Action;
import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.pojo.RecoverPass;
import mx.ferreyra.solomaneja.recursos.Recursos.ANS;
import mx.ferreyra.solomaneja.ui.UI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RecoverPassAsyncTask extends AsyncTask<Void, Void, RecoverPass> {

	private Context context;
	private String email;

	private ProgressDialog pd;

	private ANS ans = null;

	public RecoverPassAsyncTask(Context context, String email) {

		this.email = email;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
	}

	@Override
	protected RecoverPass doInBackground(Void... params) {



		Net net = AppSoloManeja.getInstance().getNet();
		final String url = String.format(context.getResources().getString(R.string.url_server), "forgot_password/?usuario=" + email); 

		RecoverPass recoverPass = null;
		ans = ANS.OK;
		try {
			recoverPass = (RecoverPass) net.sendDataAndGetObject(url, null, RecoverPass.class);
		} catch (OfflineException e) {
			ans = ANS.OFFLINE_ERROR;
		} catch (IOException e) {
			ans = ANS.IO_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			ans = ANS.ERROR;
		}

		return recoverPass;

	}

	@Override
	protected void onPostExecute(RecoverPass recoverPass) {

		pd.dismiss();
		String msg = null;
		switch (ans) {

		case OK:
			if(recoverPass != null  && recoverPass.error_message != null){
				UI.showAlertDialog("Error", recoverPass.error_message, context.getString(android.R.string.ok), context, null);
			}
			else{
				UI.showAlertDialog("Gracias", context.getString(R.string.msg_ok_recover_pass), context.getString(android.R.string.ok), context, 
						new Action() {
					@Override
					public void action() { 
						((Activity) context).finish();  }});
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

		if (msg != null)
			UI.showAlertDialog("Error", msg, "OK", context, null);
	}

}
