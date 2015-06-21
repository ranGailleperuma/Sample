package com.test;

import java.io.IOException;
import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueSend {
	
	// Defines the JNDI context factory.
	public final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

	// Defines the JMS context factory.
	public final static String JMS_FACTORY = "QCF";

	// Defines the queue.
	public final static String QUEUE = "testQ";
	
	public final static String HOST = "t3://localhost:7001";

	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueSender qsender;
	private Queue queue;
	private TextMessage msg;

	public void init(Context ctx, String queueName) throws NamingException,
			JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
		qcon = qconFactory.createQueueConnection();
		qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(queueName);
		qsender = qsession.createSender(queue);
		msg = qsession.createTextMessage();
		qcon.start();
	}

	public void send(String message, int counter) throws JMSException {
		msg.setText(message);
		msg.setIntProperty("counter", counter);
		qsender.send(msg);
	}

	public void close() throws JMSException {
		qsender.close();
		qsession.close();
		qcon.close();
	}

	public static void main(String[] args) throws Exception {
		
		
		InitialContext ic = getInitialContext(HOST);
		QueueSend qs = new QueueSend();
		qs.init(ic, QUEUE);
		readAndSend(qs);
		qs.close();
	}

	private static void readAndSend(QueueSend qs) throws IOException,
			JMSException {
		
		String line = "Test Message Body with counter = ";
		for (int i = 0; i < 10; i++) {
			qs.send(line + i, i);
			System.out.println("JMS Message Sent: " + line + i + "\n");
		}
	}

	private static InitialContext getInitialContext(String url)
			throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);
		return new InitialContext(env);
	}

}
