package com.newmanconnection.hello.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newmanconnection.commons.db.AbstractDBUpgrade;
import org.newmanconnection.commons.db.MySqlConnectionFactory;


public class DBUpgrade extends AbstractDBUpgrade {
    private static final Logger LOG = LogManager.getLogger(DBUpgrade.class);

    public DBUpgrade() { 
        DBAccess.setConnectionFactory(new MySqlConnectionFactory()); 
        Search.setDBType(MYSQL);
    }

    @Override
    public void doUpgrade() {
        LOG.info("Verifying database structures.");

//        if(!tableExists("departments")) {
//            LOG.info("-- Created departments table");
//            runSql("CREATE TABLE departments(id SERIAL PRIMARY KEY, " +
//                                            "name TEXT, " +
//                                            "department_head_id INTEGER REFERENCES people(id) ON DELETE SET NULL, " +
//                                            "org_id INTEGER REFERENCES organizations(id) ON DELETE CASCADE)");
//        }

    }
}
