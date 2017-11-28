package com.redhat.iot.analysis.protocols;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.redhat.iot.analysis.interfaces.Protocol;

public class MQTT implements Protocol {

    private String topic;
    private String url;

    private MqttClient client;
    @Override
    public void parseArguments(CommandLineParser parser, String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("t", true, "Topic to publish to");

        options.addOption("u", true, "Url of the broker");
        
        CommandLine cmd = parser.parse(options, args);
        url = cmd.getOptionValue("u");
        topic = cmd.getOptionValue("t");
    }

    @Override
    public void start() throws Exception {
        client = new MqttClient(url, "javaTest", new MemoryPersistence());
        client.connect();
    }

    @Override
    public boolean sendMessage(String message) throws Exception {
        client.publish(topic, new MqttMessage(message.getBytes()));
        return false;
    }

    @Override
    public void disconnect() throws Exception {
        client.disconnect();
    }

}
