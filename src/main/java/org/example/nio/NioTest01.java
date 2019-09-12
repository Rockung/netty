package org.example.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest01 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        System.out.println("capacity: " + buffer.capacity());

        for (int i = 0; i < 5; i++) {
            int num = new SecureRandom().nextInt(20);
            buffer.put(num);
        }

        System.out.println("before flip limit: " + buffer.limit());
        buffer.flip();
        System.out.println("after flip limit: " + buffer.limit());

        System.out.println("enter while loop");
        while(buffer.hasRemaining()) {
            System.out.println("position: " + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            System.out.println(buffer.get());
        }
    }
}
