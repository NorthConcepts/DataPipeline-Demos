/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.events.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.northconcepts.datapipeline.core.DataException;

public class DB {

    private Connection connection;

    public DB() throws DataException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    shutdown();
                }
            });
        } catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized void shutdown() throws DataException {
        if (connection != null) {
            try {
                System.out.println("Shutting down...");
                Statement st = connection.createStatement();
                st.execute("SHUTDOWN");
                connection.close();
            } catch (Throwable e) {
                throw DataException.wrap(e);
            } finally {
                connection = null;
                System.out.println("shutdown complete");
            }
        }
    }

    public synchronized int executeUpdate(String sql) throws DataException {
        try {
            PreparedStatement st = null;
            try {
                st = connection.prepareStatement(sql);
                return st.executeUpdate();
            } finally {
                st.close();
            }
        } catch (Throwable e) {
            throw DataException.wrap(e).set("sql", sql);
        }
    }

}
