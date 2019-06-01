import java.util.Date;
 
 import javax.ejb.ActivationConfigProperty;
 import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
 import javax.jms.Message;
 import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
 
 @MessageDriven(activationConfig = {
 		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
 		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/colorQueue"),
 		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "color = 'GREEN'")
 })
 
 @TransactionManagement(value= TransactionManagementType.CONTAINER)
 @TransactionAttribute(value= TransactionAttributeType.REQUIRED)
 public class MDBGreen implements MessageListener {
 int x=0;
 	@Override
 	public void onMessage(Message message) {
 		try {
 			if (message instanceof TextMessage) {
 				TextMessage msg = (TextMessage) message;
 				System.out.println("green_mdb-Az �zenet: " + msg.getText());
 				if(x<9)
 				{
 					x++;
 				}
 				else
 				{
 					x=0;
 					sendMessage(msg);
 				}
 			} else {
 				System.out.println("hib�s �zenet");
 			}
 		} catch (JMSException e) {
 			e.printStackTrace();
 		}
 
 	}

 	private void sendMessage(Message message) {
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();
			QueueConnectionFactory qconFactory = (QueueConnectionFactory) initialContext
					.lookup("java:jboss/DefaultJMSConnectionFactory");
			QueueConnection qcon = qconFactory.createQueueConnection();
			QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) initialContext.lookup("java:jboss/exported/colorStatistics");
			QueueSender qsender = qsession.createSender(queue);
			qcon.start();
			qsender.send(message);
			qsender.close();
			qsession.close();
			qcon.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 }