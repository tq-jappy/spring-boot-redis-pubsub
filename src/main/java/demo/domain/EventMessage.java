package demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventMessage {

    private final String text;

    public EventMessage(@JsonProperty("text") String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
