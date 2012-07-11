package mx.ferreyra.solomaneja.fragments;

import mx.ferreyra.solomaneja.R;
import mx.ferreyra.solomaneja.service.Iservicio;
import mx.ferreyra.solomaneja.service.Servicio;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GrabarF extends SherlockFragment{

	protected View view = null;
	protected Activity context;

	private TextView tvCronometro;
	private static CheckBox ckRoute;

	private Intent intent;
	static Iservicio iservicio;
	private Handler handler = new Handler();
	private long counterCrono;

	public static final String TAG = "GrabarF";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		this.context = getActivity();

		if (intent == null){
			intent = new Intent( context , Servicio.class);
			context.startService(intent);
			
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = inflater.inflate(R.layout.grabar, container, false);

		tvCronometro = (TextView) view.findViewById(R.id.tv_cronometro);
		ckRoute = (CheckBox) view.findViewById(R.id.ck_Route);

		ckRoute.setOnClickListener(startStopRouteListener);
		
		return view;
	}

	android.view.View.OnClickListener startStopRouteListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			startStopRoute(v);
		}
	}; 
	
	//startStopRoute
	
	
	@Override
	public void onResume() {
		super.onResume();

		if(iservicio!= null)
			try {
				ckRoute.setChecked( iservicio.isRoute()  );
				handler.post(actCronometro);
				startStopRoute(null);
			} catch (Exception e) {
				e.printStackTrace();
			}


	}


	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(actCronometro);
	}

	@Override
	public void onStart() {
		super.onStart();
		// Bind to LocalService

		//getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		context.getApplicationContext().bindService(intent, svcConn, Context.BIND_AUTO_CREATE );
	}

	private void salir(){
		try {
			if (iservicio.isRoute()== false)
				context.stopService(intent);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		context.getApplicationContext().bindService(intent, svcConn, Context.BIND_AUTO_CREATE );
		// Unbind from the service
		/*
		if (mBound) {
			getApplicationContext().unbindService(mConnection);
			mBound = false;
		}
		 */
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		salir();
	}



	public  void startStopRoute (View v){
		try {
			if (ckRoute.isChecked()){

				if (v != null)
					iservicio.startRoute();
				this.counterCrono = iservicio.getCounterCrono();
				handler.removeCallbacks(actCronometro);
				handler.post(actCronometro);
			}
			else{

				if (v != null)
					iservicio.stopRoute();
				tvCronometro.setText(getResources().getString(R.string.inicio_cronometro));
				handler.removeCallbacks(actCronometro);


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	Runnable actCronometro = new Runnable() {

		@Override
		public void run() {

			int sec = (int) (counterCrono%60);
			int min = (int) ((counterCrono/60)%60);
			int hr = (int) ((counterCrono/3600)%24);

			tvCronometro.setText(String.format("%02d:%02d:%02d", hr, min, sec));
			counterCrono++;
			handler.postDelayed(this, 1000);
		}
	};





	/*
	// Handles the connection between the service and activity
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// Called when the connection is made.
			serviceBinder = (MyBinder) service;
			mBound = true;
			ckRoute.setChecked(serviceBinder.isRoute());
			startStopRoute(null);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			// Received when the service unexpectedly disconnects.
			serviceBinder = null;
			mBound = false;
		}

	};
	 */

	public ServiceConnection svcConn=new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iservicio=Iservicio.Stub.asInterface(service);
			//mBound = true;
			try {
				ckRoute.setChecked(iservicio.isRoute());
			} catch (Exception e) {
				e.printStackTrace();
			}
			startStopRoute(null);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iservicio=null;}
	};

}
