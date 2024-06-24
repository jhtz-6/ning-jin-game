package com.myf;

import com.myf.zouding.netty.server.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.myf.zouding.database.mapper")
@SpringBootApplication(scanBasePackages = {"com.myf"})
public class NingJinGameStarterApplication {
    private static final int NETTY_PORT = 8084;

    public static void main(String[] args) {
        SpringApplication.run(NingJinGameStarterApplication.class, args);
        NettyServer nettyServer =new NettyServer(NETTY_PORT);
        nettyServer.start();
    }

}
