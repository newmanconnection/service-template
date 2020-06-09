package com.newmanconnection.college.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.EnvProperty;
import org.servantscode.commons.db.ConnectionFactory;

import javax.sql.DataSource;

import static java.lang.String.format;

public class MySqlConnectionFactory extends ConnectionFactory {
    private static Logger LOG = LogManager.getLogger(MySqlConnectionFactory.class);

    @Override
    public DataSource configureSource() {
        try {
            //Ensure driver is loaded into local context.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not present.", e);
        }

        String dbType = EnvProperty.get("DB_TYPE", "MySQL");
        String host = EnvProperty.get("DB_HOST", "seville-stage-pub.csk7nbyg0hvg.us-east-2.rds.amazonaws.com");
        String port = EnvProperty.get("DB_PORT","3306");
        String schema = EnvProperty.get("DB_NAME","seville-staging");
        String username = EnvProperty.get("DB_USER","admin");
        String password = EnvProperty.get("DB_PASSWORD", "");

        if(!dbType.equalsIgnoreCase("MySQL")) {
            LOG.error("DB Type requested (" + dbType + ") is not MySQL.");
            throw new RuntimeException("Illegal Database configuration encountered");
        }

        HikariDataSource source = new HikariDataSource();
        String jdbcUrl = format("jdbc:mysql://%s:%s/%s", host, port, schema);
        LOG.debug("Connecting to: " + jdbcUrl);
        source.setJdbcUrl(jdbcUrl);
        source.setUsername(username);
        source.setPassword(password);

        return source;
    }
}
