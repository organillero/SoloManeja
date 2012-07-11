package mx.ferreyra.solomaneja.recoverPass;

import java.util.ArrayList;

public class Token implements java.io.Serializable{


	private static final long serialVersionUID = -4845892632030736966L;


	public boolean login_by_username;
	public boolean login_by_email;
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




