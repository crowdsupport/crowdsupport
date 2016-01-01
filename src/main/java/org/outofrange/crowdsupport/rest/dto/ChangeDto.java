package org.outofrange.crowdsupport.rest.dto;

/**
 * @author Markus Möslinger
 */
public class ChangeDto<T> {
    public enum ChangeType {
        ADD, UPDATE, DELETE
    }

    private final ChangeType changeType;
    private final String entity;
    private final T payload;

    public ChangeDto(ChangeType changeType, T payload) {
        this.changeType = changeType;
        this.entity = payload != null ? payload.getClass().getSimpleName() : "null";
        this.payload = payload;
    }

    public String getEntity() {
        return entity;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public T getPayload() {
        return payload;
    }

    // convenience methods
    public static <S> ChangeDto<S> add(S payload) {
        return new ChangeDto<>(ChangeType.ADD, payload);
    }

    public static <S> ChangeDto<S> update(S payload) {
        return new ChangeDto<>(ChangeType.UPDATE, payload);
    }

    public static <S> ChangeDto<S> delete(S payload) {
        return new ChangeDto<>(ChangeType.DELETE, payload);
    }
}
