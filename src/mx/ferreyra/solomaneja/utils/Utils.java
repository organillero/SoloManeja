package mx.ferreyra.solomaneja.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Utils {


	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/*
	public static void CopyStream(InputStream is, OutputStream os){
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){;;}
	}
	 */

	private  final static Pattern rfc2822 = Pattern.compile(
			"^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" );

	public static boolean isValidEmailAddress(String email){

		if (rfc2822.matcher(email).matches())
			return false;
		else 
			return true;

	}



	private static String bytesToHex(byte[] b) {
		char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		StringBuffer buf = new StringBuffer();
		for (int j=0; j<b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static String getMD5 (String msg){
		return getDigest(msg, "MD5");
	}

	public static String getSHA1 (String msg){
		return getDigest(msg, "SHA1");
	}

	private static String getDigest(String msg, String algorithm){
		String ans = null;

		try {
			MessageDigest md;
			md = MessageDigest.getInstance(algorithm);
			md.update(msg.getBytes());
			ans = Utils.bytesToHex( md.digest() );
		} catch (NoSuchAlgorithmException e) {
			ans = String.valueOf( msg.hashCode() );
		}

		return ans;
	} 

	
	public static String getCurrentDate (){
		return formatter.format(new Date());
	}


}
