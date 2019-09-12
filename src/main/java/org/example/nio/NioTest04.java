package org.example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * File Copy
 *
 */
public class NioTest04 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("NioTest04-in.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest04-out.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        while(true) {
            buffer.clear(); //  should clear the buffer before putting something into it

            int read = inputChannel.read(buffer);
            System.out.println("read: " + read);

            if (read < 0) {
                break;
            }

            buffer.flip();
            outputChannel.write(buffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
