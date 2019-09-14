package org.example.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIoClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);

        String fileName = "D:\\BaiduNetdiskDownload\\课件.rar";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long fileSize = fileChannel.size();
        System.out.println("文件字节数：" + fileSize);

        long startTime = System.currentTimeMillis();

        long blockSize = 0;
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        if (transferCount < fileSize) {
            blockSize = transferCount;
            while (transferCount < fileSize) {
                transferCount += fileChannel.transferTo(transferCount, blockSize, socketChannel);
            }
        }

        System.out.println("发送总字节数：" + transferCount + "，耗时： " + (System.currentTimeMillis() - startTime));

        fileChannel.close();
        socketChannel.close();
    }
}
