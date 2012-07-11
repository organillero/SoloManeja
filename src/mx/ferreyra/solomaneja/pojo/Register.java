package mx.ferreyra.solomaneja.pojo;



public class Register {

	
	public String username;
	public String email;
	public Integer user_id;
	public String token;

	public Error_Message error_message;
	
	public class Error_Message{
		public String username;
		public String email;
		public String password;
	}

}




