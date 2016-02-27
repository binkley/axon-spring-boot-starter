/*
 * Copyright (c) 2010-2014. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hm.binkley.spring.axon;

import org.axonframework.auditing.AuditDataProvider;
import org.axonframework.domain.Message;

import java.util.Map;

/**
 * {@code MessageAuditDataProvider} is a look-a-like for {@link
 * AuditDataProvider} but more generally for messages rather than only command
 * messages.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
public interface MessageAuditDataProvider {
    /**
     * Return the relevant auditing information for the given message. This
     * method is called exactly once for each time the message is dispatched
     * (commands) or processed (events).
     *
     * @param message The message being dispatched, never missing
     *
     * @return a map containing key-value pairs of relevant information to
     * include in audit logs, never missing
     */
    Map<String, Object> provideAuditDataFor(final Message<?> message);

    /**
     * Shim method to coerce this interface into an {@link
     * AuditDataProvider}.
     *
     * @return this interface, never missing
     */
    default AuditDataProvider asAuditDataProvider() {
        return this::provideAuditDataFor;
    }
}
