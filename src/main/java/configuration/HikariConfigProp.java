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

	public HikariConfigProp() throws Exception {
		super("");
		try {
			final String hikariConfigFile = SystemConfigProp.getConfig().getProperty("hikari.default.config");
			final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());

			try(final FileInputStream yamlStream=new FileInputStream(hikariConfigFile); ){
				final JsonNode jsonNode = yamlReader.readTree(yamlStream);
				parseConfigJson(jsonNode.findValue("hikari"), "");
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

}
