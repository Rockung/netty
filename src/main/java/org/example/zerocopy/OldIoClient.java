package org.example.zerocopy;

import java.io.*;
import java.net.Socket;

public class OldIoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8899);

        String fileName = "D:\\BaiduNetdiskDownload\\课件.rar";

        InputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送总字节数： " + total + ", 耗时： " + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
