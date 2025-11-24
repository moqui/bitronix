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
package bitronix.tm.mock.resource.jms;

import bitronix.tm.mock.resource.MockXAResource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import jakarta.jms.Destination;
import jakarta.jms.XAJMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.XAConnection;
import jakarta.jms.XAConnectionFactory;
import jakarta.jms.XASession;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Ludovic Orban
 */
public class MockXAConnectionFactory implements XAConnectionFactory {

    private static JMSException staticCloseXAConnectionException;
    private static JMSException staticCreateXAConnectionException;

    private String endPoint;

    public XAConnection createXAConnection() throws JMSException {
        if (staticCreateXAConnectionException != null)
            throw staticCreateXAConnectionException;

        Answer xaSessionAnswer = new Answer<XASession>() {
            public XASession answer(InvocationOnMock invocation)throws Throwable {
                XASession mockXASession = mock(XASession.class);
                MessageProducer messageProducer = mock(MessageProducer.class);
                when(mockXASession.createProducer((Destination) any())).thenReturn(messageProducer);
                MessageConsumer messageConsumer = mock(MessageConsumer.class);
                when(mockXASession.createConsumer((Destination) any())).thenReturn(messageConsumer);
                when(mockXASession.createConsumer((Destination) any(), anyString())).thenReturn(messageConsumer);
                when(mockXASession.createConsumer((Destination) any(), anyString(), anyBoolean())).thenReturn(messageConsumer);
                Queue queue = mock(Queue.class);
                when(mockXASession.createQueue(anyString())).thenReturn(queue);
                Topic topic = mock(Topic.class);
                when(mockXASession.createTopic(anyString())).thenReturn(topic);
                MockXAResource mockXAResource = new MockXAResource(null);
                when(mockXASession.getXAResource()).thenReturn(mockXAResource);                
                Answer<Session> sessionAnswer = new Answer<Session>() {
                    public Session answer(InvocationOnMock invocation) throws Throwable {
                        Session session = mock(Session.class);
                        MessageProducer producer = mock(MessageProducer.class);
                        when(session.createProducer((Destination) any())).thenReturn(producer);
                        return session;
                    }
                };
                when(mockXASession.getSession()).thenAnswer(sessionAnswer);
                
                return mockXASession;
            }
        };

        XAConnection mockXAConnection = mock(XAConnection.class);
        when(mockXAConnection.createXASession()).thenAnswer(xaSessionAnswer);
        when(mockXAConnection.createSession(anyBoolean(), anyInt())).thenAnswer(xaSessionAnswer);
        if (staticCloseXAConnectionException != null)
            doThrow(staticCloseXAConnectionException).when(mockXAConnection).close();

        return mockXAConnection;
    }

    public XAConnection createXAConnection(String jndiName, String jndiName1) throws JMSException {
        return createXAConnection();
    }

    @Override
    public XAJMSContext createXAContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public XAJMSContext createXAContext(String userName, String password) {
        throw new UnsupportedOperationException();
    }

    public static void setStaticCloseXAConnectionException(JMSException e) {
        staticCloseXAConnectionException = e;
    }

    public static void setStaticCreateXAConnectionException(JMSException e) {
        staticCreateXAConnectionException = e;
    }

    public String getEndpoint() {
        return endPoint;
    }

    public void setEndpoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
