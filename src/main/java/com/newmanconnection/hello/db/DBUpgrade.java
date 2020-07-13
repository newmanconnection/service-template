package com.newmanconnection.hello.db;

import com.newmanconnection.commons.db.MySqlConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.db.AbstractDBUpgrade;
import org.servantscode.commons.db.DBAccess;
import org.servantscode.commons.search.Search;

import static org.servantscode.commons.search.Search.DBType.MYSQL;


public class DBUpgrade extends AbstractDBUpgrade {
    private static final Logger LOG = LogManager.getLogger(DBUpgrade.class);

    public DBUpgrade() { 
        DBAccess.setConnectionFactory(new MySqlConnectionFactory());
        Search.setDBType(MYSQL);
    }

    @Override
    public void doUpgrade() {
        LOG.info("Verifying database structures.");

//        LOG.info("-- Verifying hello table");
//        if(runSql("CREATE TABLE IF NOT EXISTS hello(id INT AUTO_INCREMENT PRIMARY KEY, name TEXT)"))
//            LOG.info("---- Created hello table");

    }
}
