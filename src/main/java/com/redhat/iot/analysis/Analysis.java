package com.redhat.iot.analysis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import com.redhat.iot.analysis.interfaces.Protocol;
import com.redhat.iot.analysis.protocols.AMQP;
import com.redhat.iot.analysis.protocols.CoAP;
import com.redhat.iot.analysis.protocols.MQTT;
import com.redhat.iot.analysis.protocols.REST;

public class Analysis {

    private static boolean running = true;
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addRequiredOption("p", "protocol", true, "Selects given protocol, possible values: amqp;mqtt;rest;coap");
        
        Option delayOption = Option.builder("d")
                .argName("delay")
                .numberOfArgs(1)
                .desc("Delay between requests in seconds")
                .required()
                .type(Number.class)
                .hasArg()
                .longOpt("delay")
                .build();

        Option runtimeOption = Option.builder("r")
                .numberOfArgs(1)
                .argName("runtime")
                .desc("Runtime in minutes")
                .required()
                .type(Number.class)
                .hasArg()
                .longOpt("runtime")
                .build();

        options.addOption(delayOption);
        options.addOption(runtimeOption);
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);
        
        Protocol protocol;
        String protocolName = cmd.getOptionValue("p");
        switch (protocolName) {
            case "rest":
                protocol = new REST();
                break;
            case "mqtt":   
                protocol = new MQTT();
                break;
            case "coap":
                protocol = new CoAP();
                break;
            case "amqp":
                protocol = new AMQP();
                break;    
            default:
                protocol = new REST();
                break;
        }
        
        Long delay = ((Number) cmd.getParsedOptionValue("d")).longValue();
        Integer runtime = ((Number) cmd.getParsedOptionValue("r")).intValue();
        
        protocol.parseArguments(parser, cmd.getArgs());
        protocol.start();
        
        scheduleShutdown(runtime);

        String value = "HIGH";
        int i = 0;
        while (running) {
            protocol.sendMessage(value);
            i++;
            if (value.equals("HIGH")) {
                value = "LOW";
            } else {
                value = "HIGH";
            }
            Thread.sleep(delay * 1000);
        }
        protocol.disconnect();
        System.out.println("Odesláno celkem " + i + " požadavků");
    }
    
    private static void scheduleShutdown(Integer runtime) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            System.out.println("Shutting down");
            running = false;
        }, runtime, TimeUnit.MINUTES);
        executor.shutdown();
    }

}
