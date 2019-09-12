package org.example.nio;

import java.nio.ByteBuffer;

/**
 * Read-only buffer
 *  You can get a readonly buffer from a read/write buffer, cannot inversely
 */
public class NioTest07 {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(buffer.getClass());
        System.out.println(readOnlyBuffer.getClass());

        readOnlyBuffer.position(0);
        readOnlyBuffer.put((byte)2);
    }
}
