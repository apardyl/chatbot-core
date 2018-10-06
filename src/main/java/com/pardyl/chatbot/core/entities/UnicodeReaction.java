package com.pardyl.chatbot.core.entities;

import java.util.Objects;

public final class UnicodeReaction implements Reaction {
    private final int code;

    public UnicodeReaction(int code) {
        this.code = code;
    }

    public UnicodeReaction(String emoji) {
        this(emoji.codePointAt(0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnicodeReaction that = (UnicodeReaction) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String getName() {
        return String.valueOf(Character.toChars(code));
    }

    @Override
    public String getId() {
        return null;
    }


    @Override
    public String toString() {
        return getName();
    }
}
