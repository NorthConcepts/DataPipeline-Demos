package com.northconcepts.events.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DBShutdownListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        EventsResource.db.shutdown();
    }

}
