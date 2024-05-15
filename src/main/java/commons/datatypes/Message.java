package commons.datatypes;

import java.io.Serializable;

/**
 * Represents message data type exchanged between client and server
 *
 * @param sender   is who the message was sent by
 * @param contents is the text of message
 */
public record Message(String sender, String contents) implements Serializable {
}
