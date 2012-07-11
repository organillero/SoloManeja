package mx.ferreyra.solomaneja.pojo;

import com.google.gson.annotations.SerializedName;

public class MyPoint2 {


	public Integer continuo;

	public String lat;

	@SerializedName("lon")
	public String lng;

	@SerializedName("idruta")
	public String idRuta;
}