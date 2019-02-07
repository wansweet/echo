package cn.mldn.bio.server;


import cn.mldn.info.HostInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOEchoServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(HostInfo.PORT) ;// 设置监听端口
        System.out.println("服务器端已经启动，监听的端口为：" + HostInfo.PORT);
        boolean flag = true ;
        ExecutorService executorService = Executors.newFixedThreadPool(10) ;
        while(flag) {
            // 监听连接进来
            Socket client = serverSocket.accept() ;
            executorService.submit(new EchoClientHandler(client)) ;
        }
        executorService.shutdown() ;
        serverSocket.close() ;
    }

    private static class EchoClientHandler implements Runnable {
        // 每一个客户端都需要启动一个任务(task)来执行。
        private Socket client ;
        private Scanner scanner ;
        private PrintStream out ;
        // 循环标记
        private boolean flag = true ;
        public EchoClientHandler(Socket client) {
            // 保存每一个客户端操作
            this.client = client ;
            try {
                this.scanner = new Scanner(this.client.getInputStream()) ;
                // 设置换行符
                this.scanner.useDelimiter("\n") ;
                this.out = new PrintStream(this.client.getOutputStream()) ;
            } catch (IOException e) {
                e.printStackTrace();
            }

            String hostAddress = client.getInetAddress().getHostAddress();
            System.out.println("IP:【"+hostAddress+"】连接");
        }
        @Override
        public void run() {
            while(this.flag) {
                if (this.scanner.hasNext()) {   // 现在有数据进行输入
                    String val = this.scanner.next().trim() ; // 去掉多余的空格内容
                    System.err.println("{服务器端}" + val);
                    if("byebye".equalsIgnoreCase(val)) {
                        this.out.println("ByeByeByte...");
                        this.flag = false ;
                    } else {
                        out.println("【ECHO】" + val);
                    }
                }
            }

            // 退出后，进行关闭操作
            this.scanner.close();
            this.out.close();
            try {
                this.client.close();
            } catch (IOException e) {
            }
        }
    }
}
