package utility.cryptography;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import configuration.HikariConfigProp;

public class EncryptionAES256 {

	

	public static String encrypt(String strToEncrypt, String secret)
	{
		try
		{
			HikariConfigProp hikariConfigObject = new HikariConfigProp();
			String salt = hikariConfigObject.getAditionalKey();
			
			byte[] iv = new byte[128/8];
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance(hikariConfigObject.getSecretKeyFactory());
			KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), hikariConfigObject.getSecretKeySpec());

			Cipher cipher = Cipher.getInstance(hikariConfigObject.getCiperGetInstance());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

}
