package com.bc.calvalus.portal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * GWT-serializable version of the {@link com.bc.calvalus.commons.ProcessStatus} class.
 *
 * @author Norman
 */
public class DtoProcessStatus implements IsSerializable {
    private static final float EPS = 1.0E-04f;
    private DtoProcessState state;
    private String message;
    private float progress;
    private int processingSeconds;

    /**
     * No-arg constructor as required by {@link IsSerializable}.
     */
    public DtoProcessStatus() {
        this(DtoProcessState.UNKNOWN);
    }

    public DtoProcessStatus(DtoProcessState state) {
        this(state, "", 0.0f);
    }

    public DtoProcessStatus(DtoProcessState state, String message, float progress) {
        this(state, message, progress, 0);
    }

    public DtoProcessStatus(DtoProcessState state, String message, float progress, int processingSeconds) {
        if (state == null) {
            throw new NullPointerException("state");
        }
        if (message == null) {
            throw new NullPointerException("message");
        }
        this.state = state;
        this.message = message;
        this.progress = progress;
        this.processingSeconds = processingSeconds;
    }

    public DtoProcessState getState() {
        return state;
    }

    public boolean isDone() {
        return state.isDone();
    }

    public boolean isUnknown() {
        return state == DtoProcessState.UNKNOWN;
    }

    public String getMessage() {
        return message;
    }

    public float getProgress() {
        return progress;
    }

    public int getProcessingSeconds() {
        return processingSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DtoProcessStatus that = (DtoProcessStatus) o;

        float delta = that.progress - progress;
        if (delta < 0) {
            delta = -delta;
        }

        return delta <= EPS
                && processingSeconds == that.processingSeconds
                && message.equals(that.message)
                && state == that.state;
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (int) (progress / EPS);
        result = 31 * result + processingSeconds;
        return result;
    }

    @Override
    public String toString() {
        return "PortalProductionStatus{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", progress=" + progress +
                ", processingSeconds=" + processingSeconds +
                '}';
    }
}
