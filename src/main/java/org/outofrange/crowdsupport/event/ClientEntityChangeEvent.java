package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.model.BaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code ClientEntityChangeEvent} will be published to all subscribed clients.
 * <br/>
 * When constructing, at least one topic name (typically named liked the entity) has to be passed.
 * This topic can result in sending the event to different subscriptions:<br/>
 * <ul>
 *     <li>If the payload has an Id and the {@link ChangeType} is not {@code CREATE},
 *     the event will be published to {@code {entity}/{id}}</li>
 *     <li>If the {@link ChangeType} is either {@code CREATE} or {@code DELETE},
 *     an empty event will be published to {@code {entity}/{changeType}}</li>
 * </ul>
 * <br/>
 * This way, it's not possible to monitor detailed changes to all entities, but only quantitative data
 * <br/>
 * It's also possible to specify futher topics where the event should be sent to; these topics will be used unconditionally,
 * not like {@code mainTopic}
 * @param <T> the type of the payload
 */
public class ClientEntityChangeEvent<T> extends NestedEvent implements ClientEvent {
    private final List<String> topics = new ArrayList<>();

    private final ChangeType changeType;
    private final T payload;

    public ClientEntityChangeEvent(ChangeType changeType, T payload, String mainTopic, String... furtherTopics) {
        this(changeType, payload, mainTopic, Arrays.asList(furtherTopics));
    }

    public ClientEntityChangeEvent(ChangeType changeType, T payload, String mainTopic, List<String> furtherTopics) {
        this.changeType = changeType;
        this.payload = payload;

        spliceMainTopic(mainTopic);
        this.topics.addAll(furtherTopics);
    }

    private void spliceMainTopic(String mainTopic) {
        if (getChangeType() != ChangeType.CREATE) {
            if (getPayload() instanceof BaseEntity) {
                topics.add(mainTopic + "/" + ((BaseEntity) getPayload()).getId());
            } else if (getPayload() instanceof BaseDto) {
                topics.add(mainTopic + "/" + ((BaseDto) getPayload()).getId());
            }
        }

        if (getChangeType() == ChangeType.CREATE || getChangeType() == ChangeType.DELETE) {
            addSubEvent(new EmptyClientEvent(mainTopic + "/" + getChangeType().name().toLowerCase()));
        }
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public T getPayload() {
        return payload;
    }
}
