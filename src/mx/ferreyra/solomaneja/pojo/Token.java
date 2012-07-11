package mx.ferreyra.solomaneja.pojo;

import java.util.ArrayList;

public class Token{

	public Boolean login_by_username;
	public Boolean login_by_email;
	public ArrayList<Errors> error_mesagge;

	public String user_id;
	public String email;
	public String token;

	public ArrayList<Totals> totals;

	
	public class Errors{
		public  Boolean password;
		public  Boolean login; 
		
	}
	
	public class Totals{
		public String total_puntos;
		public String total_badges;
		public String total_rutas;

	}

}




