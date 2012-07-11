package mx.ferreyra.solomaneja.pojo;

import com.google.gson.annotations.SerializedName;

public class MyPoint {


	public Integer consecutivo;

	public String lat;

	@SerializedName("long")
	public String lng;


	public String tipo;
	public String fecha;

}
