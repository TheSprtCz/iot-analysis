package com.redhat.iot.analysis.protocols;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import com.redhat.iot.analysis.interfaces.Protocol;

public class AMQP implements Protocol {

    private Connection connection;
    private MessageProducer producer;
    private Session session;
    @Override
    public void parseArguments(CommandLineParser parser, String[] args) throws Exception {
        Options options = new Options();
        options.addOption("u", true, "Url");
        
        CommandLine cmd = parser.parse(options, args);
        String url = cmd.getOptionValue("u");
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
        env.put("connectionfactory.URL", url);
        Context context = new InitialContext(env);
        ConnectionFactory factory = (ConnectionFactory) context.lookup("URL");
        connection = factory.createConnection();
    }

    @Override
    public void start() throws Exception {
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(session.createTopic("IoT"));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    @Override
    public boolean sendMessage(String message) throws Exception {
        producer.send(session.createTextMessage(message));
        return false;
    }

    @Override
    public void disconnect() throws Exception {
        connection.close();

    }

}
