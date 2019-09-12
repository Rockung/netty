package org.example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * File copy using direct buffer
 */
public class NioTest08 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("NioTest04-in.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest04-out.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        System.out.println(buffer.getClass());
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
