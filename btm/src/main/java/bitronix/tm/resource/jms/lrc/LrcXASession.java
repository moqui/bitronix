/*
 * Copyright (C) 2006-2013 Bitronix Software (http://www.bitronix.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bitronix.tm.resource.jms.lrc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;
import jakarta.jms.XASession;
import javax.transaction.xa.XAResource;
import java.io.Serializable;

/**
 * XASession implementation for a non-XA JMS resource emulating XA with Last Resource Commit.
 *
 * @author Ludovic Orban
 */
public class LrcXASession implements XASession {

    private final static Logger log = LoggerFactory.getLogger(LrcXASession.class);

    private final Session nonXaSession;
    private final XAResource xaResource;

    public LrcXASession(Session session) {
        this.nonXaSession = session;
        this.xaResource = new LrcXAResource(session);
        if (log.isDebugEnabled()) { log.debug("creating new LrcXASession with " + xaResource); }
    }

    @Override
    public Session getSession() throws JMSException {
        return nonXaSession;
    }

    @Override
    public XAResource getXAResource() {
        return xaResource;
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return nonXaSession.createBytesMessage();
    }

    @Override
    public MapMessage createMapMessage() throws JMSException {
        return nonXaSession.createMapMessage();
    }

    @Override
    public Message createMessage() throws JMSException {
        return nonXaSession.createMessage();
    }

    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        return nonXaSession.createObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException {
        return nonXaSession.createObjectMessage(serializable);
    }

    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        return nonXaSession.createStreamMessage();
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        return nonXaSession.createTextMessage();
    }

    @Override
    public TextMessage createTextMessage(String text) throws JMSException {
        return nonXaSession.createTextMessage(text);
    }

    @Override
    public boolean getTransacted() throws JMSException {
        return nonXaSession.getTransacted();
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return nonXaSession.getAcknowledgeMode();
    }

    @Override
    public void commit() throws JMSException {
        nonXaSession.commit();
    }

    @Override
    public void rollback() throws JMSException {
        nonXaSession.rollback();
    }

    @Override
    public void close() throws JMSException {
        nonXaSession.close();
    }

    @Override
    public void recover() throws JMSException {
        nonXaSession.recover();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return nonXaSession.getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        nonXaSession.setMessageListener(messageListener);
    }

    @Override
    public void run() {
        nonXaSession.run();
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return nonXaSession.createProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return nonXaSession.createConsumer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return nonXaSession.createConsumer(destination, messageSelector);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException {
        return nonXaSession.createConsumer(destination, messageSelector, noLocal);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
        return nonXaSession.createSharedConsumer(topic, sharedSubscriptionName);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector) throws JMSException {
        return nonXaSession.createSharedConsumer(topic, sharedSubscriptionName, messageSelector);
    }

    @Override
    public Queue createQueue(String queueName) throws JMSException {
        return nonXaSession.createQueue(queueName);
    }

    @Override
    public Topic createTopic(String topicName) throws JMSException {
        return nonXaSession.createTopic(topicName);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        return nonXaSession.createDurableSubscriber(topic, name);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        return nonXaSession.createDurableSubscriber(topic, name, messageSelector, noLocal);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
        return nonXaSession.createDurableConsumer(topic, name);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        return nonXaSession.createDurableConsumer(topic, name, messageSelector, noLocal);
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
        return nonXaSession.createSharedDurableConsumer(topic, name);
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) throws JMSException {
        return nonXaSession.createSharedDurableConsumer(topic, name, messageSelector);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        return nonXaSession.createBrowser(queue);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
        return nonXaSession.createBrowser(queue, messageSelector);
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return nonXaSession.createTemporaryQueue();
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return nonXaSession.createTemporaryTopic();
    }

    @Override
    public void unsubscribe(String name) throws JMSException {
        nonXaSession.unsubscribe(name);
    }

    @Override
    public String toString() {
        return "a JMS LrcXASession on " + nonXaSession;
    }
}
