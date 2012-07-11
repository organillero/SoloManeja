package mx.ferreyra.solomaneja.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class NewRoute {

	@SerializedName("ini_date")
	public String iniDate;

	@SerializedName("end_date")
	public String endDate;

	public ArrayList<MyPoint> points;
}
