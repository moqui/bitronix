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
package bitronix.tm.resource.jms;

import bitronix.tm.resource.common.TransactionContextHelper;

import jakarta.jms.CompletionListener;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;

/**
 * {@link MessageProducer} wrapper that adds XA enlistment semantics.
 *
 * @author Ludovic Orban
 */
public class MessageProducerWrapper implements MessageProducer {

    private final MessageProducer messageProducer;
    protected final DualSessionWrapper session;
    private final PoolingConnectionFactory poolingConnectionFactory;

    public MessageProducerWrapper(MessageProducer messageProducer, DualSessionWrapper session, PoolingConnectionFactory poolingConnectionFactory) {
        this.messageProducer = messageProducer;
        this.session = session;
        this.poolingConnectionFactory = poolingConnectionFactory;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    /**
     * Enlist this session into the current transaction if automaticEnlistingEnabled = true for this resource.
     * If no transaction is running then this method does nothing.
     * @throws JMSException if an exception occurs
     */
    protected void enlistResource() throws JMSException {
        if (poolingConnectionFactory.getAutomaticEnlistingEnabled()) {
            session.getSession(); // make sure the session is created before enlisting it
            try {
                TransactionContextHelper.enlistInCurrentTransaction(session);
            } catch (SystemException ex) {
                throw (JMSException) new JMSException("error enlisting " + this).initCause(ex);
            } catch (RollbackException ex) {
                throw (JMSException) new JMSException("error enlisting " + this).initCause(ex);
            }
        } // if getAutomaticEnlistingEnabled
    }

    @Override
    public String toString() {
        return "a MessageProducerWrapper of " + session;
    }

    /* MessageProducer with special XA semantics implementation */

    @Override
    public void send(Message message) throws JMSException {
        enlistResource();
        getMessageProducer().send(message);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        enlistResource();
        getMessageProducer().send(message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Destination destination, Message message) throws JMSException {
        enlistResource();
        getMessageProducer().send(destination, message);
    }

    @Override
    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        enlistResource();
        getMessageProducer().send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Message message, CompletionListener completionListener) throws JMSException {
        enlistResource();
        getMessageProducer().send(message, completionListener);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority, long timeToLive, CompletionListener completionListener) throws JMSException {
        enlistResource();
        getMessageProducer().send(message, deliveryMode, priority, timeToLive, completionListener);
    }

    @Override
    public void send(Destination destination, Message message, CompletionListener completionListener) throws JMSException {
        enlistResource();
        getMessageProducer().send(destination, message, completionListener);
    }

    @Override
    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive, CompletionListener completionListener) throws JMSException {
        enlistResource();
        getMessageProducer().send(destination, message, deliveryMode, priority, timeToLive, completionListener);
    }

    @Override
    public void close() throws JMSException {
        // do nothing as the close is handled by the session handle
    }

    /* dumb wrapping of MessageProducer methods */

    @Override
    public void setDisableMessageID(boolean value) throws JMSException {
        getMessageProducer().setDisableMessageID(value);
    }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return getMessageProducer().getDisableMessageID();
    }

    @Override
    public void setDisableMessageTimestamp(boolean value) throws JMSException {
        getMessageProducer().setDisableMessageTimestamp(value);
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        return getMessageProducer().getDisableMessageTimestamp();
    }

    @Override
    public void setDeliveryMode(int deliveryMode) throws JMSException {
        getMessageProducer().setDeliveryMode(deliveryMode);
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return getMessageProducer().getDeliveryMode();
    }

    @Override
    public void setPriority(int defaultPriority) throws JMSException {
        getMessageProducer().setPriority(defaultPriority);
    }

    @Override
    public int getPriority() throws JMSException {
        return getMessageProducer().getPriority();
    }

    @Override
    public void setTimeToLive(long timeToLive) throws JMSException {
        getMessageProducer().setTimeToLive(timeToLive);
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return getMessageProducer().getTimeToLive();
    }

    @Override
    public void setDeliveryDelay(long deliveryDelay) throws JMSException {
        getMessageProducer().setDeliveryDelay(deliveryDelay);
    }

    @Override
    public long getDeliveryDelay() throws JMSException {
        return getMessageProducer().getDeliveryDelay();
    }

    @Override
    public Destination getDestination() throws JMSException {
        return getMessageProducer().getDestination();
    }

}
