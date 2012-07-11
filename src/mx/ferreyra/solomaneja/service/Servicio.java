package mx.ferreyra.solomaneja.service;

import mx.ferreyra.solomaneja.AppSoloManeja;
import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.SoloManejaActivity;
import mx.ferreyra.solomaneja.asynctasks.SendRouteAsyncTask;
import mx.ferreyra.solomaneja.persistencia.MyDB;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.TIPO;
import mx.ferreyra.solomaneja.utils.Utils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class Servicio extends Service {

	private Context context;

	/*Variables de la notificacion*/
	private static NotificationManager mNotificationManager;
	private static LocationManager locmgr;
	private static final int NOTIFICATION_ID = 1;
	private static final int icon = R.drawable.ic_launcher;

	private static int counter = -1;

	private static boolean  route = false;

	private static final String TIME_START = "TIME_START";
	private static Long timeStart = 0l; 

	private Preferencias prefs;
	private MyDB db;


	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();
		
		mNotificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE);

		locmgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		prefs = AppSoloManeja.getInstance().getPrefs();

		db = new MyDB(context);

		if (prefs.loadData(TIME_START) != null)
			try {
				strtRoute();
				binder.startRoute();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

	}


	@Override
	public void onDestroy() {
		super.onDestroy();

	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Servicio.START_STICKY;
	}



	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}


	private final Iservicio.Stub binder = new Iservicio.Stub() {

		@Override
		public boolean isRoute() throws RemoteException {
			return route;
		}

		@Override
		public long getCounterCrono() throws RemoteException {
			long ct = System.currentTimeMillis();
			long ans = (ct - timeStart)/1000;
			return ans;
		}

		@Override
		public void startRoute() throws RemoteException {
			strtRoute();

		}

		@Override
		public void stopRoute() throws RemoteException {
			stpRoute();
		}


	};


	private void strtRoute(){
		sendnotification(context.getString(R.string.app_name), "En Ruta...");
		route = true;

		counter =0;

		if ( prefs.loadData(TIME_START) != null)
			timeStart = Long.valueOf(prefs.loadData(TIME_START));
		else
			timeStart = System.currentTimeMillis();

		// 30*1000, 10
		locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*000, 0, mylocationListener);

		db.open();
		Long idRoute =  db.startRoute();
		db.close();

		prefs.saveData(Recursos.TL_ID_RTE, idRoute.toString());
		prefs.saveData(TIME_START, timeStart.toString());
	}

	public void stpRoute(){
		dissmisNotification();
		route = false;
		timeStart = 0l;
		counter =-1;
		locmgr.removeUpdates(mylocationListener);

		Long tmp = Long.valueOf( prefs.loadData(Recursos.TL_ID_RTE));
		db.open();
		db.stopRoute( tmp);// Long.valueOf( prefs.loadData(Recursos.TL_ID_RTE))
		db.close();

		new SendRouteAsyncTask(context,tmp).execute(); // Long.valueOf(prefs.loadData(Recursos.TL_ID_RTE))

		prefs.clearPreference(TIME_START);
		prefs.clearPreference(Recursos.TL_ID_RTE);

	} 



	private  void sendnotification (String title, String message) {


		CharSequence tickerText = message;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		CharSequence contentTitle = title;
		CharSequence contentText = message;
		Intent notificationIntent = new Intent(this, SoloManejaActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, notification);

	}



	private void dissmisNotification(){
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	private  LocationListener mylocationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			Log.d(getClass().getName(), "lat: " + location.getLatitude() + ", lng: " + location.getLongitude() );

			if (prefs.loadData(Recursos.TL_ID_RTE) != null){
				long idRoute = Long.valueOf( prefs.loadData(Recursos.TL_ID_RTE));
				db.open();
				db.addPoint(idRoute, location.getLatitude(), location.getLongitude(), TIPO.FLUIDO, Utils.getCurrentDate(), counter++);
				db.close();
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};



}
