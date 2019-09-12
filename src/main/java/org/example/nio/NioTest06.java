package org.example.nio;

import java.nio.ByteBuffer;

/**
 * Slicing a buffer
 *   The slicing buffer shares the underling data with the sliced buffer
 */
public class NioTest06 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        buffer.position(2);
        buffer.limit(6);

        ByteBuffer slice = buffer.slice();
        for (int i = 0; i < slice.capacity() ; i++) {
            byte b = slice.get();
            b *= 2;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while(buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
