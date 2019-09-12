package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioTest13Server {
    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        registerServerChannel(selector);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(selectionKey -> {
                try {
                    if (selectionKey.isAcceptable()) {
                        acceptServerChannel(selector, (ServerSocketChannel) selectionKey.channel());
                    } else if (selectionKey.isReadable()) {
                        doReadAndWrite((SocketChannel) selectionKey.channel());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            selectionKeys.clear();
        }
    }

    private static void doReadAndWrite(SocketChannel client) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        int count = client.read(readBuffer);
        if (count > 0) {
            readBuffer.flip();

            Charset charset = Charset.forName("utf-8");
            String receivedMessage = String.valueOf(charset.decode(readBuffer).array());

            System.out.println(client + ": " + receivedMessage);

            String senderKey = null;

            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                if (client == entry.getValue()) {
                    senderKey = entry.getKey();
                    break;
                }
            }

            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                SocketChannel value = entry.getValue();

                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                writeBuffer.put((senderKey + ": " + receivedMessage).getBytes());
                writeBuffer.flip();

                value.write(writeBuffer);
            }
        }
    }

    private static void acceptServerChannel(Selector selector, ServerSocketChannel server) throws IOException {
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);

        clientMap.put("[" + UUID.randomUUID().toString() + "]", client);
    }

    private static void registerServerChannel(Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
}
