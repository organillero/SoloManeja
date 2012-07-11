package mx.ferreyra.solomaneja;

import static mx.ferreyra.solomaneja.recursos.Recursos.COUNTRY;
import static mx.ferreyra.solomaneja.recursos.Recursos.EMAIL;
import static mx.ferreyra.solomaneja.recursos.Recursos.PASS;
import static mx.ferreyra.solomaneja.recursos.Recursos.SEX;
import static mx.ferreyra.solomaneja.recursos.Recursos.USER;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.pojo.UserInfoFB;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.ui.UI;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;

public class LoginActivity extends Activity {

	public  static final int ID = 0;

	private EditText user, pass;
	private View activityRootView;

	private Context context;
	private Preferencias prefs;

	public static final String APP_ID = "216685258443624";
	private static final String[] PERMISSIONS = new String[] {"email",  "read_stream",  "publish_stream"};
	private Facebook facebook = new Facebook(APP_ID);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(R.layout.login);

		activityRootView = findViewById(R.id.login_root);
		activityRootView.invalidate();

		user = (EditText) findViewById(R.id.login_user);
		pass = (EditText) findViewById(R.id.login_pass);

		pass.setOnEditorActionListener(actionLogin);
		//user.addTextChangedListener(new HTCEditTextFix( user ));

		prefs = AppSoloManeja.getInstance().getPrefs();

	}




	protected void onResume() {
		super.onResume();

		//TODO Boorrar valores
		user.setText("ferreyr@gmail.com"); //raulagc99@gmail.com
		pass.setText("qwe123");
		Bundle extras = getIntent().getExtras();

		if( extras != null && extras.getBoolean("LOGIN_FAIL")== true ){
			String msgTitle = extras.getString("MSG_TITLE");
			String msg = extras.getString("MSG");
			UI.showAlertDialog(msgTitle, msg, context.getString(android.R.string.ok), context, null);
		}

	};



	TextView.OnEditorActionListener actionLogin = new TextView.OnEditorActionListener(){
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			login(null);	
			return false;
		}
	};



	public void login (View view){
		checkAndHideKeyboard(null);
		if (user.getText().toString().equals("")== false && pass.getText().toString().equals("") == false){

			//String user = intent.getStringExtra(USER), pass = intent.getStringExtra(PASS);
			//asyncTask = (LoginAsyncTask) new LoginAsyncTask(context, user.getText().toString(), pass.getText().toString()).execute();


			Bundle b = new Bundle();
			b.putBoolean("LOGIN", true);
			b.putString(USER, user.getText().toString());
			b.putString(PASS, pass.getText().toString());

			setResult(Activity.RESULT_OK, new Intent().putExtras(b));
			finish();

		}

		else 
			UI.showAlertDialog(getString(android.R.string.dialog_alert_title), "Favor de introducir un correo y contrase–a", getString(android.R.string.ok), context, null);
	}

	public void loginfb(View view){
		if(this.facebook.isSessionValid()) {
			new GetDataFB().run();
		}
		else{
			this.facebook.authorize(this, PERMISSIONS, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					new GetDataFB().run();

				}

				@Override
				public void onFacebookError(FacebookError error) {
					Log.e(Recursos.TAG, error.toString());
				}

				@Override
				public void onError(DialogError e) {
					Log.e(Recursos.TAG, e.toString());
				}

				@Override
				public void onCancel() {/**/}
			});
		}
		return;
	}


	class GetDataFB implements Runnable {
		@Override
		public void run() {
			try {
				prefs.saveData("access_token", facebook.getAccessToken());
				prefs.saveData("access_expires", Long.toString( facebook.getAccessExpires()) );

				String me = facebook.request("me");
				UserInfoFB user = new Gson().fromJson(me, UserInfoFB.class);
				Log.d(Recursos.TAG, user.toString());
				
				Bundle b = new Bundle();
				b.putBoolean("LOGIN", true);
				b.putString(USER, user.email);
				b.putString(PASS, "password_solo_maneja");

				setResult(Activity.RESULT_OK, new Intent().putExtras(b));
				finish();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void recuperarPass (View view){
		startActivity(new Intent(this, RecuperarPassActivity.class));
	}

	public void registra (View view){
		startActivityForResult(new Intent(this, RegistroActivity.class), RegistroActivity.ID);
	}


	public void checkAndHideKeyboard (View view){

		if(getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
		}

		return;
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (intent != null &&  intent.getExtras() != null){
			switch (requestCode) {

			case RegistroActivity.ID:
				if (resultCode == RESULT_OK) {
					if (intent.getBooleanExtra("REGISTER",false) == true ){

						Bundle b = new Bundle();
						b.putBoolean("REGISTER", true);
						b.putString(USER, intent.getStringExtra(USER));
						b.putString(EMAIL,intent.getStringExtra(EMAIL));
						b.putString(PASS, intent.getStringExtra(PASS));
						b.putString(COUNTRY,  intent.getStringExtra(COUNTRY));
						b.putString(SEX, intent.getStringExtra(SEX));

						setResult(Activity.RESULT_OK, new Intent().putExtras(b));
						finish();
					}
				}


				break;

			default:
				break;

			}
		}
	}


}