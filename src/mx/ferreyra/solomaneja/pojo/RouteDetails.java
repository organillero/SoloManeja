package mx.ferreyra.solomaneja.pojo;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.gson.annotations.SerializedName;

public class RouteDetails {

	@SerializedName("tiempo_transcurrido")
	public String tiempoTranscurrido;

	@SerializedName("trafico_lento")
	public Integer traficoLento;

	@SerializedName("trafico_detenido")
	public Integer traficoDetenido;

	@SerializedName("fecha_ruta")
	public String fechaRuta;

	@SerializedName("distancia_total")
	public Float distanciaTotal;

	@SerializedName("Puntaje_ruta")
	public Integer puntajeRuta;

	@SerializedName("id_ruta")
	public String idRuta;

	public ArrayList<MyPoint2> puntos;

	public  ArrayList<GeoPoint> getGeoPointsRoute (){
		return getGeoPoints(this.puntos);
	}

	private ArrayList<GeoPoint> getGeoPoints (ArrayList<MyPoint2> points){

		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		GeoPoint geoTmp;
		for (MyPoint2 point: points ){
			geoTmp= new GeoPoint((int) (Double.parseDouble(point.lat)* 1E6),(int) (Double.parseDouble(point.lng)* 1E6));
			geoPoints.add(geoTmp);
		}
		return geoPoints;
	}

}
