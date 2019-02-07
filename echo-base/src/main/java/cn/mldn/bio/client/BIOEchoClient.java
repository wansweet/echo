package cn.mldn.bio.client;

import cn.mldn.info.HostInfo;
import cn.mldn.util.InputUtil;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class BIOEchoClient {
    public static void main(String[] args) throws Exception {
        // 定义连接的主机信息
        Socket client = new Socket(HostInfo.HOST_NAME,HostInfo.PORT) ;
        // 获取服务器端的响应数据
        Scanner scan = new Scanner(client.getInputStream()) ;
        scan.useDelimiter("\n") ;
        // 向服务器端发送信息内容
        PrintStream out = new PrintStream(client.getOutputStream()) ;
        // 交互的标记
        boolean flag = true ;
        while(flag) {
            String inputData = InputUtil.getString("请输入要发送的内容：").trim() ;
            // 把数据发送到服务器端上
            out.println(inputData);

            // 接收服务器的数据
            if(scan.hasNext()) {
                String str = scan.next().trim() ;
                System.out.println(str);
            }
            if ("byebye".equalsIgnoreCase(inputData)) {
                flag = false ;
            }
        }
        client.close();
    }
}
