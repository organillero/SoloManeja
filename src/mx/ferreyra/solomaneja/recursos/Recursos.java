package mx.ferreyra.solomaneja.recursos;

public class Recursos {

	public static final String USER = "USER";
	public static final String EMAIL = "EMAIL";
	public static final String USER_ID = "USER_ID";
	public static final String PASS = "PASS";
	public static final String TOKEN = "TOKEN";


	public static final String COUNTRY = "COUNTRY";
	public static final String SEX = "SEX";

	public static final String EMAIL_TO_RECOVER = "EMAIL_TO_RECOVER";

	public static final String TOTAL_PUNTOS = "TOTAL_PUNTOS";
	public static final String TOKEN_BADGES = "TOKEN_BADGES";
	public static final String TOKEN_RUTAS = "TOKEN_RUTAS";


	public static final String CHANGE_TITLE = "mx.ferreyra.solomaneja.CHANGE_TITLE";

	public static final String TAG = "SoloManeja";
	public static final String URL_SERVER = "http://app.solomaneja.com/beta/index.php/sistema/api/";//"http://app.solomaneja.com/beta/index.php/sistema";

	public static enum ANS {OK, OFFLINE_ERROR, IO_ERROR, ERROR};


	public static final String TABLE_LOG = "TABLE_LOG";

	public static final String TL_ID_RTE= "TL_ID_RTE";
	public static final String TL_LAT = "LAT";
	public static final String TL_LNG = "LNG";
	public static final String TL_TIPO = "TIPO";
	public static final String TL_TIME = "TIME";
	public static final String TL_CONSEC= "CONSEC";


	public static final String DATABASE_NAME = "DB_LOGS";

	public static final String TABLE_RTE = "TABLE_RTE";

	public static final String TRTE_TIME_INI = "TIME_INI";
	public static final String TRTE_TIME_FIN = "TIME_FIN";
	public static final String TRTE_ISROUTE = "ISROUTE";


	public static  enum TIPO {
		DETENIDO(0, "detenido"), LENTO(1, "lento"), FLUIDO(2, "fluido");

		private int value;
		private String text;

		private TIPO(int v, String txt) {
			value = v;
			text = txt;
		};

		public int getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

	}
}
