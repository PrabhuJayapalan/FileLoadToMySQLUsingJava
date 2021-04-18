package configuration;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class SystemConfigProp {

	protected final Properties properties = new Properties();
	private static SystemConfigProp globalConfig;

	public static SystemConfigProp getConfig() {
		if(globalConfig == null) {
			globalConfig = new SystemConfigProp();
		}
		return globalConfig;
	}

	private SystemConfigProp() {
		String mainConfFile = System.getProperty("config.location");
		if(mainConfFile == null) {
			throw new RuntimeException("Application config file not found");
		}

		try(FileReader input = new FileReader(mainConfFile)){
			properties.load(input);
		}catch(IOException ex) {
			throw new RuntimeException("Unable to load/process config file", ex);
		}

		/*also load environment variables in properties collection */
		Map<String, String> env = System.getenv();
		for(String envName : env.keySet()) {
			if(envName.toUpperCase().startsWith("FDDIF")) {
				properties.put(envName, env.get(envName));
			}
		}
	}

	/* Constructor used by sub-classes of SystemConfig */
	protected SystemConfigProp(@NotNull final String defConfigFile) {
		String mainConfFile = System.getProperty("config.location");
		if(mainConfFile == null) {
			throw new RuntimeException("Application config file not found");
		}

		/* If there is no default configuration file, just ignore and return */
		if(Strings.isNullOrEmpty(defConfigFile))
			return;

		try {
			final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
			try(final FileInputStream yamlStream=new FileInputStream(defConfigFile); ){
				final JsonNode jsonNode = yamlReader.readTree(yamlStream);
				parseConfigJson(jsonNode, "");
			}
		} catch(IOException ex) {
			throw new RuntimeException("Error loading yaml file", ex);
		}

		/* also load environment variables in properties collection */
		Map<String, String> env = System.getenv();
		for(String envName : env.keySet()) {
			if(envName.toUpperCase().startsWith("FDDIF")) {
				properties.put(envName, env.get(envName));
			}
		}
	}


	protected void parseConfigJson(final JsonNode jsonNode, final String prefix) {
		final Iterator<String> fieldNamesIt = jsonNode.fieldNames();
		while(fieldNamesIt.hasNext()) {
			final String fieldName = fieldNamesIt.next();
			final String newPrefix = prefix.isEmpty() ? fieldName.trim() : prefix + "." +fieldName.trim();
			final JsonNode newJsonNode = jsonNode.get(fieldName);
			if(newJsonNode.isObject()) {
				parseConfigJson(newJsonNode, newPrefix);
			} else {
				this.properties.put(newPrefix, newJsonNode.asText().trim());
			}

		}
	}


	public void setProperty(final String key, final String value) {	
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		this.properties.setProperty(key, value);
	}

	public String getProperty(final String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		String val = this.properties.getProperty(key);

		if(val == null) {
			throw new RuntimeException(String.format("Property %s not found", key));
		}

		/* fill place holders in the property value with system properties */
		for(String sysPropKey : System.getProperties().stringPropertyNames()) {
			String sysPropVal = System.getProperty(sysPropKey);
			val = val.replaceAll("\\$\\{"+ sysPropKey + "}", sysPropVal);
		}

		return val;
	}

}
