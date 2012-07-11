package mx.ferreyra.solomaneja.exceptions;

@SuppressWarnings("javadoc")
public class OfflineException extends Exception {



	private static final long serialVersionUID = 2008775094927516521L;
	private int intError = -1;

	public OfflineException(){
		super();
	}

	public OfflineException(int intErrNo){
		this();
		this.intError = intErrNo;
	}

	public OfflineException(String strMessage){
		super(strMessage);
	}

	@Override
	public String toString(){
		return "ApplicationException["+this.intError+"]";
	}  

}
