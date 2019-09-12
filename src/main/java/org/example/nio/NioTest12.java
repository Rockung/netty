package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Selector model in java.nio
 * ==========================
 *   create a selector
 *   register lots of server channel with the selector
 *   loop {
 *     wait for io events on the selector
 *     for each event {
 *       accept:
 *         accept an incoming channel and register it with the selector
 *       read:
 *         read from or write into a selected channel
 *     }
 *   }
 *
 * Key objects
 * ===========
 *   Selector
 *     a multiplexor of channel objects
 *   ServerSocketChannel
 *     a selectable channel for stream-oriented listening sockets
 *   SocketChannel
 *     a selectable channel for stream-oriented connecting sockets
 *   SelectionKey
 *     a token representing the registration of a select channel with a selector
 */
public class NioTest12 {
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        registerServerChannels(selector);

        while (true) {
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {
                    acceptServerChannel(selector, (ServerSocketChannel) selectionKey.channel());
                } else if (selectionKey.isReadable()) {
                    doReadAndWrite((SocketChannel) selectionKey.channel());
                }

                iterator.remove();
            }
        }
    }

    /**
     * Register a set of server channels with fixed ports
     *
     * @param selector
     * @throws IOException
     */
    private static void registerServerChannels(Selector selector) throws IOException {
        int[] ports = new int[] {5000, 5001, 5002, 5003, 5004};
        for (int i = 0; i < ports.length; i++) {
            registerServerChannel(selector, ports[i]);
        }
    }

    /**
     * Register a server channel with a selector
     *   - create a server channel and configure it
     *   - get the socket from the channel and bind it onto an address
     *   - register the channel with the selector
     *
     * @param selector: the selector the channel registers with
     * @param port: the port the channel binds on
     * @throws IOException
     */
    private static void registerServerChannel(Selector selector, int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        ServerSocket socket = serverChannel.socket();
        InetSocketAddress socketAddress = new InetSocketAddress(port);
        socket.bind(socketAddress);

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Listening port: " + port);
    }

    /**
     * Accept an incoming connection and register it with the selector
     *   - the incomming connection comes from the server channel
     *
     * @param selector
     * @param serverChannel
     * @throws IOException
     */
    private static void acceptServerChannel(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("Get client connection: " + socketChannel);
    }

    /**
     * Read and write between the endpoints
     *
     * @param socketChannel
     * @throws IOException
     */
    private static void doReadAndWrite(SocketChannel socketChannel) throws IOException {
        int bytesRead = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            byteBuffer.clear(); // must be cleared before reading

            int read = socketChannel.read(byteBuffer);
            if (read <= 0) {
                break;
            }

            byteBuffer.flip(); // must be flipped before writing

            socketChannel.write(byteBuffer);
            bytesRead += read;
        }
        System.out.println("Read bytes: " + bytesRead + " from: " + socketChannel);
    }
}
