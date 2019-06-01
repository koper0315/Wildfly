import java.util.Hashtable;
import java.util.Random;

import javax.jms.Connection;
  import javax.jms.Queue;
  import javax.jms.QueueConnection;
  import javax.jms.QueueConnectionFactory;
  import javax.jms.QueueSender;
  import javax.jms.QueueSession;
  import javax.jms.Session;
  import javax.jms.TextMessage;
  import javax.naming.Context;
  import javax.naming.InitialContext;
  import javax.naming.NamingException;
  
  public class Main {
  
  	public static void main(String[] args) throws Exception {
  		Connection connection = null;
  		InitialContext initialContext = null;
  		try {
  			initialContext = getInitialContext();
  			QueueConnectionFactory qconFactory = (QueueConnectionFactory) initialContext
  					.lookup("jms/RemoteConnectionFactory");
  			QueueConnection qcon = qconFactory.createQueueConnection("quser", "Password_1");
  			QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
  			Queue queue = (Queue) initialContext.lookup("colorQueue");
  			QueueSender qsender = qsession.createSender(queue);
  			qcon.start();
  			String[] colors = {"RED", "BLUE", "GREEN"};
  			String random;
  			TextMessage msg = qsession.createTextMessage();
  			for(;;)
  			{
  			random=(colors[new Random().nextInt(colors.length)]);
  			msg.setStringProperty("color", random);
  			msg.setText(random);
  			qsender.send(msg);
  			Thread.sleep(1000);
  			}
  			/*qsender.close();
  			qsession.close();
  			qcon.close();*/
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	private static InitialContext getInitialContext() throws NamingException {
  		Hashtable env = new Hashtable();
  		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
  		env.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
  		env.put("jboss.naming.client.ejb.context", false);
  		env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
  		return new InitialContext(env);
  	}
  }