package mx.ferreyra.solomaneja;

import mx.ferreyra.solomaneja.asynctasks.RecoverPassAsyncTask;
import mx.ferreyra.solomaneja.ui.UI;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class RecuperarPassActivity extends Activity {

	public static final int ID = 2;

	private View activityRootView;

	private EditText email;

	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.recuperar_pass);
		
		activityRootView = findViewById(R.id.login_root);
		//activityRootView.invalidate();
		email = (EditText) findViewById(R.id.et_email_to_recover);
		email.setOnEditorActionListener(action);


	}


	TextView.OnEditorActionListener action = new TextView.OnEditorActionListener(){
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			recoverEmail(null);	
			return false;
		}
	};

	public void recoverEmail (View view){
		checkAndHideKeyboard(null);
		if (email.getText().toString().equals("")== false){

			new RecoverPassAsyncTask(context, email.getText().toString()).execute();
		}

		else 
			UI.showAlertDialog(getString(android.R.string.dialog_alert_title), "Favor de introducir un correo", getString(android.R.string.ok), context, null);


	}


	public void checkAndHideKeyboard (View view){

		if(getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
		}

		return;
	}
}
