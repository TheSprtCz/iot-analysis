package com.redhat.iot.analysis.protocols;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.redhat.iot.analysis.interfaces.Protocol;

public class CoAP implements Protocol {

    private CoapClient client;
    @Override
    public void parseArguments(CommandLineParser parser, String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("u", true, "Url");
        
        CommandLine cmd = parser.parse(options, args);
        String url = cmd.getOptionValue("u");
        client = new CoapClient(url);

    }

    @Override
    public void start() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sendMessage(String message) throws Exception {
        client.post(message, MediaTypeRegistry.TEXT_PLAIN);
        return false;
    }

    @Override
    public void disconnect() throws Exception {
        // TODO Auto-generated method stub

    }

}
