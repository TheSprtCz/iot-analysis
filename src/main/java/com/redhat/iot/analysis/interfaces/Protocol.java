package com.redhat.iot.analysis.interfaces;

import org.apache.commons.cli.CommandLineParser;

public interface Protocol {

    /**
     * Method that should configure protocol from command line arguments
     * @param parser 
     * @param args command line arguments
     */
    void parseArguments(CommandLineParser parser, String[] args) throws Exception;
    
    /**
     * Method that starts said client
     */
    void start() throws Exception;
    
    boolean sendMessage(String message) throws Exception;

    void disconnect() throws Exception;
}
