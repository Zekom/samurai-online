package client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* V�rifie la validit� d'une adresse mail.
* Est utilis�e lors de la cr�ation d'un compte par le client.
*/
public class EmailValidation {
	public static boolean check(String email){
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
