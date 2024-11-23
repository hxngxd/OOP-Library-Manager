package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Service<T extends Entity> implements Loadable {

    protected static final Logger log = LogManager.getLogger(Service.class);

    protected static final DatabaseManager db = DatabaseManager.getInstance();

}
