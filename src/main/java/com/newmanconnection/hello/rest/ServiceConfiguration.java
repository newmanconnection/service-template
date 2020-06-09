package com.newmanconnection.college.rest;

import org.servantscode.commons.rest.AuthFilter;
import org.servantscode.commons.security.OrganizationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        AuthFilter.registerPublicService("college");

        OrganizationContext.disableMultiTenancy();
    }
}
