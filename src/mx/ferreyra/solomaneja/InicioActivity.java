package mx.ferreyra.solomaneja;

import static mx.ferreyra.solomaneja.recursos.Recursos.*;
import static mx.ferreyra.solomaneja.recursos.Recursos.USER;
import mx.ferreyra.solomaneja.asynctasks.LoginAsyncTask;
import mx.ferreyra.solomaneja.asynctasks.RegisterAsyncTask;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class InicioActivity extends Activity {

	Preferencias prefs;

	//private final int LOGIN =0;
	//private final int MAIN =1;
	
	private ProgressDialog pd;
	

	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(R.layout.loading);

		prefs = AppSoloManeja.getInstance().getPrefs();

		if (prefs.loadData(USER) == null)
			startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.ID);
		else
			startActivityForResult(new Intent(this, SoloManejaActivity.class), SoloManejaActivity.ID);	
	}

	@Override
	protected void onResume() {
		super.onResume();
		pd = ProgressDialog.show(context, "Espere", "Por favor espere un momento.", true, false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		pd.dismiss();
	}
	



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (intent != null &&  intent.getExtras() != null){
			switch (requestCode) {
			case LoginActivity.ID:
				if (resultCode == RESULT_OK) {
					if (intent.getBooleanExtra("LOGIN",false) == true ){

						String user = intent.getStringExtra(USER), pass = intent.getStringExtra(PASS);
						new LoginAsyncTask(context, user, pass).execute();
					}
					
					
					else if (intent.getBooleanExtra("REGISTER",false) == true ){
						
						String user = intent.getStringExtra(USER);
						String email = intent.getStringExtra(EMAIL);
						String pass = intent.getStringExtra(PASS);
						String country = intent.getStringExtra(COUNTRY);
						String sex = intent.getStringExtra(SEX);
						
						new RegisterAsyncTask(context, user, email, pass, country, sex ).execute();
					}
					
					
				}
				break;

			case SoloManejaActivity.ID:
				if (resultCode == RESULT_OK){
					if (intent.getBooleanExtra("LOGOUT",false) == false )
						finish();
					else 
						startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.ID);
				}
				break;	

			default:
				finish();
				break;
			}

		}
		else 
			finish();
	}





	


}