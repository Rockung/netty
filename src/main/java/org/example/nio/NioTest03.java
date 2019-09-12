package org.example.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Write a file in java.nio
 *
 *   1. get a output stream
 *   2. get a channel from the output stream
 *   3. prepare a buffer
 *   4. fill something into the buffer
 *   5. flip the buffer
 *   6. use the channel to write the buffer
 *   7. close the output stream
 *
 */
public class NioTest03 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest03.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] message = "file write in java.nio".getBytes();

        for (int i = 0; i < message.length; i++) {
            byteBuffer.put(message[i]);
        }

        byteBuffer.flip();

        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
