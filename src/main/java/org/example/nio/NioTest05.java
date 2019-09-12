package org.example.nio;

import java.nio.ByteBuffer;

/**
 * Bytebuffer类型化put/get方法
 */
public class NioTest05 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(15);
        buffer.putLong(5000L);
        buffer.putDouble(123.45);
        buffer.putChar('中');
        buffer.putShort((short)3);
        buffer.putChar('文');

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
    }
}
