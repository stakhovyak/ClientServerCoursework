package commons.data;

import commons.datatypes.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SimpleDataProcessor implements DataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleDataProcessor.class);

    @Override
    public Message deserialize(byte[] data) {
        try {
            LOG.info("Trying to deserialize the byte array.");
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            Message message = (Message) in.readObject();
            in.close();
            bis.close();
            return message;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] serialize(Message message) {
        try {
            LOG.info("Trying to serialize the Message to byte array.");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            byte[] serializedMessage = bos.toByteArray();
            out.close();
            bos.close();
            return serializedMessage;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
