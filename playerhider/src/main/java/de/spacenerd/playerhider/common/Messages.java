package de.spacenerd.playerhider.common;

// Add new messages by using the same name as inside the config for the enum
// The Enum name is the path at the same time

public class Messages {
    public interface Message {
        public String getPath();
    }

    public enum Error implements Message{
        NO_PLAYER_FOUND("no-player-found"),
        NO_CONNECTION_TO_DB("a");

        public final String messagePath;

        private Error(String messagePath) {
            this.messagePath = messagePath;
        }

        public String getPath() {
            return "message." + this.messagePath;
        }
    }

    public enum Info implements Message {
        FRIEND_ADDED("friend-added");

        public final String messagePath;

        private Info(String messagePath) {
            this.messagePath = messagePath;
        }

        public String getPath() {
            return "messages" + this.messagePath;
        }
    }

    public enum Item implements Message {
        SELECTOR("selector");

        private final String messagePath;

        private Item(String messagePath) {
            this.messagePath = messagePath;
        }

        public String getPath() {
            return "messages" + this.messagePath;
        }
    }
}
