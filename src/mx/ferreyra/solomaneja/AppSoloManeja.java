package mx.ferreyra.solomaneja;


import mx.ferreyra.solomaneja.net.Net;
import mx.ferreyra.solomaneja.persistencia.Preferencias;
import android.app.Application;

public class AppSoloManeja extends Application{

	private static AppSoloManeja m_singleton;
	private Preferencias prefs;
	private Net net;


	public final void onCreate()
	{
		super.onCreate();
		m_singleton = this;
		prefs=  new Preferencias(this);
		net = new Net(this);
	}

	public static AppSoloManeja getInstance(){
		return m_singleton;
	}

	public Preferencias getPrefs() {
		return prefs;
	}
	
	public Net getNet(){
		return net;
	}

}
