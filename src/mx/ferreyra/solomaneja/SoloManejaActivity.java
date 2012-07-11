package mx.ferreyra.solomaneja;

import mx.ferreyra.solomaneja.fragments.BadgesF;
import mx.ferreyra.solomaneja.fragments.GrabarF;
import mx.ferreyra.solomaneja.fragments.PerfilF;
import mx.ferreyra.solomaneja.fragments.RutasF;
import mx.ferreyra.solomaneja.fragments.TraficoF;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.recursos.Recursos;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.MapView;

public class SoloManejaActivity extends SherlockFragmentActivity{


	public  static final int ID = 1;

	private Preferencias prefs;

	private static final int SALIR = 0;
	private static final int REFRESH =1;

	// We use this fragment as a pointer to the visible one, so we can hide it easily.
	private Fragment mVisible = null;



	private static Fragment fragments[] = new Fragment[5]; 

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Titulo oculto
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		//Progress oculto 
		setSupportProgressBarIndeterminateVisibility(false);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(context, R.array.sections, R.layout.sherlock_spinner_item);
		listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setListNavigationCallbacks(listAdapter, new myOnNavigationListener());


		prefs = AppSoloManeja.getInstance().getPrefs();


		Exchanger.mMapView = new MapView(this, "0ggmsULpx5gWnKdIEw2m1AmGVAPURVYY-LhPUYw");



		setupFragments();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Used to put dark icons on light action bar

		if (mVisible instanceof PerfilF){
			menu.add(0,REFRESH, Menu.NONE,"Refresh")
			.setIcon(R.drawable.ic_refresh)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			
			menu.add(0,SALIR, Menu.NONE, "Salir")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		else if (mVisible instanceof BadgesF ||
				 mVisible instanceof RutasF  ||
				 mVisible instanceof PerfilF ||
				 mVisible instanceof TraficoF){
			
			menu.add(0,REFRESH, Menu.NONE,"Refresh")
			.setIcon(R.drawable.ic_refresh)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case SALIR:
			logout();
			break;

		case REFRESH:
			if (mVisible instanceof BadgesF)
				((BadgesF)mVisible).updateData();
			else if (mVisible instanceof RutasF)
				((RutasF)mVisible).updateData();
			else if (mVisible instanceof PerfilF)
				((PerfilF)mVisible).updateData();
			else if (mVisible instanceof TraficoF)
				((TraficoF)mVisible).updateData();
			break;	

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (prefs.loadData(Recursos.USER) == null){

			Bundle b = new Bundle();
			b.putBoolean("LOGOUT", true);
			setResult(Activity.RESULT_OK, new Intent().putExtras(b));
			finish();
		}

	}

	public void setVisibilityProgressBar(boolean b){
		setSupportProgressBarIndeterminateVisibility(b);
	}

	public class myOnNavigationListener implements OnNavigationListener {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {

			showFragment(fragments[itemPosition]);

			invalidateOptionsMenu();
			return true;
		}
	}


	/**
	 * This method does the setting up of the Fragments. It basically checks if
	 * the fragments exist and if they do, we'll hide them. If the fragments
	 * don't exist, we create them, add them to the FragmentManager and hide
	 * them.
	 */
	private void setupFragments() {


		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fragments[0] = addFragment( ft, GrabarF.class,  GrabarF.TAG);
		fragments[1] = addFragment( ft, PerfilF.class,  PerfilF.TAG);
		fragments[2] = addFragment( ft, BadgesF.class,  BadgesF.TAG);
		fragments[3] = addFragment( ft, RutasF.class,   RutasF.TAG);
		fragments[4] = addFragment( ft, TraficoF.class, TraficoF.TAG);

		ft.commit();

	}


	private Fragment addFragment ( FragmentTransaction ft, Class<?> fragClass, String TAG){

		Fragment fragment;
		fragment =  getSupportFragmentManager().findFragmentByTag(TAG);
		if (fragment == null) {
			try { fragment = (Fragment) fragClass.newInstance();}
			catch (Exception e) { e.printStackTrace();}
			ft.add(R.id.fragment_container, fragment, TAG);
		}
		ft.hide(fragment);
		return fragment;
	}



	public void showFragment(Fragment fragmentIn) {
		if (fragmentIn == null) return;

		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

		if (mVisible != null) ft.hide(mVisible);

		ft.show(fragmentIn).commit();
		mVisible = fragmentIn;
	}

	
	
	public void startNextFragment(Fragment fragment){

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// and add the transaction to the back stack
		ft.replace(R.id.fragment_container, fragment);
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);

		// Commit the transaction
		//fragmentTransaction.commitAllowingStateLoss();
		ft.commit();
		return;
	}

	public void removeFragment (){
		//onKeyDown (KeyEvent.KEYCODE_BACK, null);
		getSupportFragmentManager().popBackStack();
		
		return;
	}
	
	
	
	

	private void logout(){
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setMessage("ÀDesea finalizar su sesi—n?")
		.setCancelable(false)


		.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				prefs.clearPreferences();
				finish();
			}
		})
		.setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public static class Exchanger {
		// We will use this MapView always.
		public static MapView mMapView;
	}
	
	public void startMap(String idRoute){
		Intent intent = new Intent(this, RutaMapActivity.class);
		intent.putExtra("IDROUTE", idRoute);
		startActivity(intent);
		
	}


}