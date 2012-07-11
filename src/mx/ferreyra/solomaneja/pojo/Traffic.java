package mx.ferreyra.solomaneja.pojo;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;
import com.google.gson.annotations.SerializedName;

public class Traffic {

	public ArrayList<PointTraffic> detenidos;
	public Integer total_detenidos;

	public Integer total_lento;
	@SerializedName("lento")
	public ArrayList<PointTraffic> lentos;

	public class PointTraffic {

		public Date fecha;
		public String lat;

		@SerializedName("lon")
		public String lng;
	}


	public  ArrayList<GeoPoint> getGeoPointsStoped (){
		return getGeoPoints(this.detenidos);
	}
	
	public  ArrayList<GeoPoint> getGeoPointsSlow (){
		return getGeoPoints(this.lentos);
	}
	
	private ArrayList<GeoPoint> getGeoPoints (ArrayList<PointTraffic> points){
		
		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		GeoPoint geoTmp;
		for (PointTraffic point: points ){
			geoTmp= new GeoPoint((int) (Double.parseDouble(point.lat)* 1E6),(int) (Double.parseDouble(point.lng)* 1E6));
			geoPoints.add(geoTmp);
		}
		return geoPoints;
	}

}
