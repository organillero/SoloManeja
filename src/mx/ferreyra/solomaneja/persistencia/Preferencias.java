package mx.ferreyra.solomaneja.persistencia;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Preferencias {

	private SharedPreferences preferences;

	public Preferencias(Context context ){
		this.preferences  = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void saveData (String key,  String data){
		SharedPreferences.Editor editor = this.preferences.edit();	
		editor.putString(key,data);
		editor.commit();
	}

	public String loadData(String key){
		return this.preferences.getString(key, null);
	}


	public void clearPreference (String key){
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.remove(key);
		editor.commit();
		return;

	}

	public void clearPreferences(){
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.clear();
		editor.commit();
		return;
	} 

}