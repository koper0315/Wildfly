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
 import javax.jms.TextMessage;
 
 @MessageDriven(activationConfig = {
 		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
 		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/colorStatistics")
 })
 
 @TransactionManagement(value= TransactionManagementType.CONTAINER)
 @TransactionAttribute(value= TransactionAttributeType.REQUIRED)
 public class Statistic implements MessageListener {
 	@Override
 	public void onMessage(Message message) {
 		if (message instanceof TextMessage) {
			TextMessage msg = (TextMessage) message;
			String szin = null;
			try {
				szin=msg.getText();
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println("10 '"+szin+"' messages has been processed");
		} else {
			System.out.println("hibás üzenet");
		}
 
 	}
 
 }