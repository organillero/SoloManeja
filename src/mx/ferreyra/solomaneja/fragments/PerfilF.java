package mx.ferreyra.solomaneja.fragments;

import java.util.List;

import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.asynctasks.GetProfileAsyncTask;
import mx.ferreyra.solomaneja.asynctasks.UpdateProfileAsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class PerfilF extends SherlockFragment{

	public static final String TAG = "PerfilF";
	
	protected View view = null;
	protected Activity context;


	//private Context context;
	private View activityRootView;

	public EditText firstName;
	public EditText lastName;

	public EditText oldPass;
	public EditText newPass;

	public Spinner countries;
	public CheckBox sex;

	public ImageView photo;
	public ImageButton btnPhoto;
	public Button update;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		this.context = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = inflater.inflate(R.layout.perfil, container, false);

		activityRootView = view.findViewById(R.id.login_root);
		activityRootView.invalidate();
		activityRootView.setOnClickListener(checkAndHideKeyboardListener);

		firstName = (EditText)view.findViewById(R.id.et_first_name);
		lastName = (EditText)view.findViewById(R.id.et_last_name);
		oldPass = (EditText)view.findViewById(R.id.et_old_pass);
		newPass = (EditText)view.findViewById(R.id.et_new_pass);
		sex = (CheckBox)view.findViewById(R.id.cb_sex);
		countries = (Spinner)view.findViewById(R.id.sp_countries);
		photo = (ImageView)view.findViewById(R.id.img_photo);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		update = (Button)view.findViewById(R.id.bt_update);
		
		
		newPass.setOnEditorActionListener(action);
		
		update.setOnClickListener(updateListener);
		btnPhoto.setOnClickListener(photoListener);
		
		updateData();
		
		return view;
	}

	public void updateData(){
		GetProfileAsyncTask asyncTask = new GetProfileAsyncTask(context);
		asyncTask.setFragment(this);
		asyncTask.execute();
	}
	
	
	android.view.View.OnClickListener checkAndHideKeyboardListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			checkAndHideKeyboard(v);
		}
	};

	
	android.view.View.OnClickListener photoListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			getPhoto(v);
		}
	};
	
	android.view.View.OnClickListener updateListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			update(v);
		}
	};


	@Override
	public void onPause() {
		super.onPause();
		checkAndHideKeyboard(null);
	}



	TextView.OnEditorActionListener action = new TextView.OnEditorActionListener(){
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			checkAndHideKeyboard(null);	
			return false;
		}
	};


	public void getPhoto (View view){

		final CharSequence[] items = {"Desde mis imagenes", "Tomar una foto"};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Tomar Fotograf’a");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				if (item == 0)
					getPhotofromAlbum();
				else if (item == 1)
					takePhoto();
			}
		});
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}


	private void getPhotofromAlbum(){

		int actionCode = 1;
		final String action = Intent.ACTION_GET_CONTENT;

		Intent intent = new Intent(action);  
		intent.setType("image/*");
		startActivityForResult(intent, actionCode);


	}

	private void takePhoto(){

		int actionCode = 0;
		final String action = MediaStore.ACTION_IMAGE_CAPTURE;

		if (PerfilF.isIntentAvailable(context, action)){
			Intent takePictureIntent = new Intent(action);
			startActivityForResult(takePictureIntent, actionCode);
		}
	}


	public void update (View view){
		//TODO BIG TODO

		String stOldPass= null, stPass=null;

		String country = getResources().getStringArray(R.array.country_arrays_values)[(int) countries.getSelectedItemId()];
		if (oldPass.getText().toString().equals("") == false){
			stOldPass =oldPass.getText().toString();
			stPass =newPass.getText().toString();
		}

		UpdateProfileAsyncTask asyncTask=  new UpdateProfileAsyncTask(context, firstName.getText().toString(),
				lastName.getText().toString(),
				sex.isChecked()?"H": "M",
						country,
						stOldPass,
						stPass,
						null);
		asyncTask.setFragment(this);
		asyncTask.execute();


	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		Bitmap mBitmap = null;
		if (resultCode == Activity.RESULT_OK && intent != null){
			if (requestCode == 0 ){
				Bundle extras = intent.getExtras();
				mBitmap = (Bitmap) extras.get("data");

			}
			else if (requestCode == 1){
				Uri chosenImageUri = intent.getData();
				try { mBitmap = Media.getBitmap(context.getContentResolver(), chosenImageUri);}
				catch (Exception e) {e.printStackTrace();}
			}

			if (mBitmap != null)
				photo.setImageBitmap(mBitmap);
		}
	}


	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}



	public void checkAndHideKeyboard (View view){

		if(getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
		}

		return;
	}

}
