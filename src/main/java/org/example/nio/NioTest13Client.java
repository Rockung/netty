package org.example.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioTest13Client {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        registerChannel(selector);

        while (true) {
            selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();

            for (SelectionKey selectionKey : keySet) {
                if (selectionKey.isConnectable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        client.register(selector, SelectionKey.OP_READ);

                        startChat(client);
                    }
                } else if (selectionKey.isReadable()) {
                    doReadMessage((SocketChannel) selectionKey.channel());
                }
            }

            keySet.clear();
        }
    }

    private static void startChat(SocketChannel client) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        writeBuffer.put((LocalDateTime.now() + " 连接成功").getBytes());
        writeBuffer.flip();
        client.write(writeBuffer);

        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
        executorService.submit(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    writeBuffer.clear();

                    String message = br.readLine();
                    writeBuffer.put(message.getBytes());

                    writeBuffer.flip();
                    client.write(writeBuffer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void doReadMessage(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int count = client.read(buffer);
        if (count > 0) {
            String message = new String(buffer.array(), 0, count);
            System.out.println(message);
        }
    }

    private static void registerChannel(Selector selector) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
    }
}
