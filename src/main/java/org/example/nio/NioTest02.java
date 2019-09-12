package org.example.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Read a file using java.nio
 *
 *   1. get a input stream
 *   2. get a channel from the input stream
 *   3. prepare a buffer
 *   4. read into the buffer using the channel
 *   5. flip the buffer
 *   6. read from the buffer
 *   7. close the input stream
 *
 * Three main concepts in java.nio
 *   - Channel
 *   - Buffer
 *   - Selector
 */
public class NioTest02 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("NioTest02.txt");
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        fileChannel.read(byteBuffer); // read into buffer

        byteBuffer.flip(); // switch between reading and writing

        while(byteBuffer.remaining() > 0) {
            byte b =byteBuffer.get();
            System.out.println("Character: " + (char)b);
        }

        fileInputStream.close();
    }
}
