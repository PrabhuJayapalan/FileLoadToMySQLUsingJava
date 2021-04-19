package utility.connectionPool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import configuration.HikariConfigProp;
import utility.cryptography.DecryptionAES256;

public class HikariDataPool   {

	// static variable single_instance of type Singleton
	private static HikariDataPool HikariDataPool_instance = null;

	public HikariDataSource dataSource = null;

	private HikariDataPool()  throws Exception {

		HikariConfigProp hikariConfigObject = new HikariConfigProp();
		HikariConfig config = new HikariConfig();

		config.setJdbcUrl(hikariConfigObject.getHikariConfigJdbcurl());
		config.setUsername(hikariConfigObject.getHikariConfigUserName());
		config.setPassword(DecryptionAES256.decrypt(hikariConfigObject.getHikariConfigPassword(), hikariConfigObject.getSecretKey()));
		config.addDataSourceProperty("minimumIdle", hikariConfigObject.getHikariMinimumumIdleTime());
		config.addDataSourceProperty("maximumPoolSize", hikariConfigObject.getHikariMaximumPoolSize());

		dataSource = new HikariDataSource(config);
	}


	public static HikariDataPool getInstance() throws Exception
	{
		if (HikariDataPool_instance == null)
			HikariDataPool_instance = new HikariDataPool();

		return HikariDataPool_instance;
	}
}
