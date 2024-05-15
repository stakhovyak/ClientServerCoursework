package commons.data;

import commons.datatypes.Message;

public interface DataProcessor {

    Message deserialize(byte[] data);

    byte[] serialize(Message message);
}
