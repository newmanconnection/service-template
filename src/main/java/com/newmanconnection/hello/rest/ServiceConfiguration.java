package com.newmanconnection.hello.rest;

import com.newmanconnection.commons.rest.NCSessionVerifier;
import org.servantscode.commons.rest.AuthFilter;
import org.servantscode.commons.security.OrganizationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        AuthFilter.setSessionVerifier(new NCSessionVerifier());
        OrganizationContext.disableMultiTenancy();

//        AuthFilter.registerPublicApi("GET", "hello", true);
    }
}
