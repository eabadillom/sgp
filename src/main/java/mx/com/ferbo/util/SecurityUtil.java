package mx.com.ferbo.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class SecurityUtil {
	private static Logger log = LogManager.getLogger(SecurityUtil.class);
	
	public String getRandomString() {
		String randomString = null;
		CharacterRule alphabets  = null;
		CharacterRule digits     = null;
		PasswordGenerator pwdGen = null;
		
		//Generar cadena de caracteres aleatoria en claro
		alphabets = new CharacterRule(EnglishCharacterData.Alphabetical);
		digits    = new CharacterRule(EnglishCharacterData.Digit);
		pwdGen    = new PasswordGenerator();
		
		randomString = pwdGen.generatePassword(20, alphabets, digits);
		log.info("Random String: {}", randomString);
		
		return randomString;
	}
	
	public String getSHA512(String texto) {
		String hash = null;
		hash = DigestUtils.sha512Hex(texto);
		return hash;
	}

	public void checkPassword(String password)
	throws Exception {
		List<Rule> rules = new ArrayList<>();
		//Rule 1: Password length should be in between 
        //8 and 16 characters
        rules.add(new LengthRule(8, 16));
        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());        
        //Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));        
        //Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));        
        //Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));        
        //Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        
        PasswordValidator validator = new PasswordValidator(rules);        
        PasswordData pwdData = new PasswordData(password);        
        RuleResult result = validator.validate(pwdData);
        
        if(result.isValid() == false){
        	throw new Exception("Contraseña no válida: " + validator.getMessages(result));
        }
	}

}
