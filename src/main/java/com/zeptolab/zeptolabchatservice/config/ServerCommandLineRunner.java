package com.zeptolab.zeptolabchatservice.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!test")
public class ServerCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer server;

    public ServerCommandLineRunner(final SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) {
        server.start();
    }
}