package com.test.messsage;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: MDBSample
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "testQ")
		}, 
		mappedName = "testQ")
public class MDBSample implements MessageListener {

    /**
     * Default constructor. 
     */
    public MDBSample() {
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	TextMessage textMessage = (TextMessage) message;
    	try {
    		System.out.println("\n\n\t(mdb) MyMDB Received \n"+ textMessage.getText());
    	}
    		catch (JMSException e)
    		{
    		e.printStackTrace();
    	}
        
    }

}
