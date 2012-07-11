package mx.ferreyra.solomaneja.pojo;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Route {

	public Integer cantidad;
	public Integer cantidal;
	public String idruta;
	public Date fecha_ruta;
	
	@SerializedName("diferencia")
	public String duracion;
	
	public String valor;
	
	
}
