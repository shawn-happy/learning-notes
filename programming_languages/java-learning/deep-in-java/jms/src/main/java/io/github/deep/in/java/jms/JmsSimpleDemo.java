package io.github.deep.in.java.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsSimpleDemo {
  public static void main(String[] args) throws InterruptedException {
    run(new SimpleProducer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    Thread.sleep(1000);
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
    Thread.sleep(1000);
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleProducer());
    Thread.sleep(1000);
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
    run(new SimpleConsumer());
    run(new SimpleConsumer());
    run(new SimpleProducer());
  }

  private static void run(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.start();
  }

  private static class SimpleProducer implements Runnable {

    @Override
    public void run() {
      try {
        ActiveMQConnectionFactory activeMQConnectionFactory =
            new ActiveMQConnectionFactory("vm://localhost");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("TEST.FOO");
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        String text = "Hello world! From: " + Thread.currentThread().getName() + this.hashCode();
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println(
            "Sent message: " + this.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);
        producer.close();
        session.close();
        connection.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static class SimpleConsumer implements Runnable {

    @Override
    public void run() {
      try {
        ActiveMQConnectionFactory activeMQConnectionFactory =
            new ActiveMQConnectionFactory("vm://localhost");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("TEST.FOO");
        MessageConsumer consumer = session.createConsumer(destination);

        // Wait for a message
        Message consumerMessage = consumer.receive(1000);

        if (consumerMessage instanceof TextMessage) {
          TextMessage textMessage = (TextMessage) consumerMessage;
          String messageText = textMessage.getText();
          System.out.println("Received: " + messageText);
        } else {
          System.out.println("Received: " + consumerMessage);
        }
        consumer.close();
        session.close();
        connection.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
