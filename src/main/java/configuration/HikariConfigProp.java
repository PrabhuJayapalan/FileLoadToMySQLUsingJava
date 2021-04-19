package configuration;

import java.io.FileInputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class HikariConfigProp extends SystemConfigProp{

	private static final String HIKARI_CONFIG_JDBCURL = "hikariConfig.setJdbcUrl";
	private static final String HIKARI_CONFIG_USER_NAME = "hikariConfig.setUsername";
	private static final String HIKARI_CONFIG_PASSWORD = "hikariConfig.setPassword";
	private static final String HIKARI_MINIMUMUM_IDLE_TIME = "hikariConfig.minimumIdle";
	private static final String HIKARI_MAXIMUM_POOL_SIZE = "hikariConfig.maximumPoolSize";
	private static final String SECRET_KEY_FACTORY = "secretKeyFactory.getInstance"; 
	private static final String CIPHER_GETINSTANCE = "cipher.getInstance";
	private static final String SECRET_KEY_SPEC = "secretKeySpec";
	private static final String ADITIONAL_KEY = "aes256.aditionalKey";
	private static final String SECRET_KEY = "aes256.secretKey";

	public HikariConfigProp() throws Exception {
		super("");
		try {
			final String hikariConfigFile = SystemConfigProp.getConfig().getProperty("hikari.default.config");
			final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());

			try(final FileInputStream yamlStream=new FileInputStream(hikariConfigFile); ){
				final JsonNode jsonNode = yamlReader.readTree(yamlStream);
				parseConfigJson(jsonNode.findValue("hikari"), "");
				parseConfigJson(jsonNode.findValue("AES256Decryption"), "");
			} catch(IOException ex) {
				throw new RuntimeException("Error loading yaml file", ex);
			}
		} catch(Exception ex) {
			throw new RuntimeException("Error loading yaml file", ex);
		}

	}
	
	public String getHikariConfigJdbcurl() {
		return getProperty(HIKARI_CONFIG_JDBCURL);
	}

	public String getHikariConfigUserName() {
		return getProperty(HIKARI_CONFIG_USER_NAME);
	}

	public String getHikariConfigPassword() {
		return getProperty(HIKARI_CONFIG_PASSWORD);
	}

	public String getHikariMinimumumIdleTime() {
		return getProperty(HIKARI_MINIMUMUM_IDLE_TIME);
	}

	public String getHikariMaximumPoolSize() {
		return getProperty(HIKARI_MAXIMUM_POOL_SIZE);
	}
	
	public String getSecretKeyFactory() {
		return getProperty(SECRET_KEY_FACTORY);
	}
	
	public String getCiperGetInstance() {
		return getProperty(CIPHER_GETINSTANCE);
	}
	
	public String getSecretKeySpec() {
		return getProperty(SECRET_KEY_SPEC);
	}
	
	public String getAditionalKey() {
		return getProperty(ADITIONAL_KEY);
	}

	public String getSecretKey() {
		return getProperty(SECRET_KEY);
	}
	
}
