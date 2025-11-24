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

import jakarta.jms.Connection;
import jakarta.jms.ConnectionConsumer;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.ServerSessionPool;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.XAConnection;

/**
 * Disposable Connection handle.
 *
 * @author Ludovic Orban
 */
public class JmsConnectionHandle implements Connection {

    private final XAConnection xaConnection;
    private final JmsPooledConnection pooledConnection;
    private volatile boolean closed = false;

    public JmsConnectionHandle(JmsPooledConnection pooledConnection, XAConnection xaConnection) {
        this.pooledConnection = pooledConnection;
        this.xaConnection = xaConnection;
    }

    public XAConnection getXAConnection() throws JMSException {
        if (xaConnection == null)
            throw new JMSException("XA connection handle has been closed");
        return xaConnection;
    }

    public JmsPooledConnection getPooledConnection() {
        return pooledConnection;
    }

    @Override
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return pooledConnection.createSession(transacted, acknowledgeMode);
    }

    @Override
    public Session createSession(int sessionMode) throws JMSException {
        return pooledConnection.createSession(sessionMode == JMSContext.SESSION_TRANSACTED, sessionMode);
    }

    @Override
    public Session createSession() throws JMSException {
        return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void close() throws JMSException {
        if (closed)
            return;

        closed = true;
        pooledConnection.release();
    }

    @Override
    public String toString() {
        return "a JmsConnectionHandle of " + pooledConnection;
    }


    /* Connection implementation */

    @Override
    public String getClientID() throws JMSException {
        return getXAConnection().getClientID();
    }

    @Override
    public void setClientID(String jndiName) throws JMSException {
        getXAConnection().setClientID(jndiName);
    }

    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return getXAConnection().getMetaData();
    }

    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        return getXAConnection().getExceptionListener();
    }

    @Override
    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        getXAConnection().setExceptionListener(listener);
    }

    @Override
    public void start() throws JMSException {
        getXAConnection().start();
    }

    @Override
    public void stop() throws JMSException {
        getXAConnection().stop();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return getXAConnection().createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return getXAConnection().createSharedConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return getXAConnection().createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return getXAConnection().createSharedDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
    }
}
