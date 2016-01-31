package org.outofrange.crowdsupport.dto;

/**
 * @author Markus Möslinger
 */
public class ChangeDto<T> extends BaseDto {
    public enum ChangeType {
        ADD, REFRESH, REMOVE
    }

    private final ChangeType changeType;
    private final String entity;
    private T payload;

    public ChangeDto(ChangeType changeType, T payload) {
        this.changeType = changeType;
        this.entity = payload != null ? payload.getClass().getSimpleName() : "null";
        this.payload = payload;
    }

    public ChangeDto(ChangeType changeType, Class<?> entityType, long id) {
        this.changeType = changeType;
        this.entity = entityType.getSimpleName();
        setId(id);
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

    public static <S> ChangeDto<S> refresh(S payload) {
        return new ChangeDto<>(ChangeType.REFRESH, payload);
    }

    public static <S> ChangeDto<S> remove(long id, Class<?> entityType) {
        return new ChangeDto<>(ChangeType.REMOVE, entityType, id);
    }
}
