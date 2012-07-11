package mx.ferreyra.solomaneja.ui;

import mx.ferreyra.solomaneja.interfaces.Action;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class UI {
	
	public static void showToast(String message, Context context){
		Toast.makeText(context,message, Toast.LENGTH_LONG).show();	
	}
	
	//AlertDialog.Builder 
	public static void showAlertDialog(String title, String message,String button, Context context, final Action o ){
		AlertDialog.Builder builder = new AlertDialog.Builder( context );
		builder	.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setNeutralButton(button, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		        	   if (o != null) o.action();
		           }
		       })
		       
		       .create()
		       .show();
		return; 
	}

}