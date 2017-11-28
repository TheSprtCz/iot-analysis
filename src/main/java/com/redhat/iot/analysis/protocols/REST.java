package com.redhat.iot.analysis.protocols;

import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import com.redhat.iot.analysis.interfaces.Protocol;

public class REST implements Protocol {

    private WebTarget target;

    @Override
    public void parseArguments(CommandLineParser parser, String[] args) throws Exception {
        Options options = new Options();
        Option urlOption = Option.builder("u")
                .argName("url")
                .desc("Url for request")
                .hasArg()
                .numberOfArgs(1)
                .type(URL.class)
                .longOpt("url")
                .build();
        
        options.addOption(urlOption);
        
        CommandLine cmd = parser.parse(options, args);
        URI url = ((URL) cmd.getParsedOptionValue("u")).toURI();
        Client client = ClientBuilder.newClient();
        target = client.target(url);

    }

    @Override
    public void start() throws Exception {
        
    }

    @Override
    public boolean sendMessage(String message) throws Exception {
        target.request().post(Entity.entity("HIGH", MediaType.TEXT_PLAIN));
        return false;
    }

    @Override
    public void disconnect() throws Exception {

    }

}
