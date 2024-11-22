package com.hxngxd.service;

import com.hxngxd.exceptions.DatabaseException;

public interface Loadable {

    void loadAll() throws DatabaseException;

}
