package mx.ferreyra.solomaneja;

import static mx.ferreyra.solomaneja.recursos.Recursos.COUNTRY;
import static mx.ferreyra.solomaneja.recursos.Recursos.EMAIL;
import static mx.ferreyra.solomaneja.recursos.Recursos.PASS;
import static mx.ferreyra.solomaneja.recursos.Recursos.SEX;
import static mx.ferreyra.solomaneja.recursos.Recursos.USER;
import mx.ferreyra.solomaneja.ui.UI;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RegistroActivity extends Activity {

	public static final int ID = 3;

	private View activityRootView;
	private EditText user;
	private EditText email;
	private EditText pass;
	private Spinner countries;
	private CheckBox sex;
	
	
	//private Button register;
	
	private Context context;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		setContentView(R.layout.registro);
		activityRootView = findViewById(R.id.login_root);
		//activityRootView.invalidate();
		
		user = (EditText) findViewById(R.id.et_user);
		email = (EditText) findViewById(R.id.et_email);
		pass = (EditText) findViewById(R.id.et_pass);
		
		countries = (Spinner) findViewById(R.id.sp_countries);
		sex = (CheckBox) findViewById(R.id.cb_sex);
		
		
		pass.setOnEditorActionListener(action);
		
		
	}
	
	TextView.OnEditorActionListener action = new TextView.OnEditorActionListener(){
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			checkAndHideKeyboard(null);	
			return false;
		}
	};

	
	public void register (View view){
		
		if (user.getText().toString().equals("") || email.getText().toString().equals("") || pass.getText().toString().equals("")){
			UI.showAlertDialog(getString(android.R.string.dialog_alert_title), "Favor de llenar todos los campos solicitados", getString(android.R.string.ok), context, null);
		}
		else {
			
			Bundle b = new Bundle();
			b.putBoolean("REGISTER", true);
			b.putString(USER, user.getText().toString());
			b.putString(EMAIL, email.getText().toString());
			b.putString(PASS, pass.getText().toString());
			
			
			String country = getResources().getStringArray(R.array.country_arrays_values)[(int) countries.getSelectedItemId()];
			b.putString(COUNTRY, country);
			b.putString(SEX, sex.isChecked()?"H": "M");

			setResult(Activity.RESULT_OK, new Intent().putExtras(b));
			finish();
			
			//TODO
		}
		
	}
	
	public void checkAndHideKeyboard (View view){

		if(getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
		}

		return;
	}

}
