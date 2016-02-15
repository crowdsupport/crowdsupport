/**
 * This package contains all types related to the eventing mechanism.
 * <p>
 * Events can be retrieved through the {@link org.outofrange.crowdsupport.event.Events} factory class, and every event
 * can be published with its {@link org.outofrange.crowdsupport.event.Event#publish()} method.
 * <p>
 * All instances of {@link org.outofrange.crowdsupport.event.ClientEvent} will also be sent over WebSockets.
 */
package org.outofrange.crowdsupport.event;