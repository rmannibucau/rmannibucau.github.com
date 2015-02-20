package com.github.rmannibucau.jug.lorraine.jcs.remote;

import org.apache.commons.jcs.auxiliary.remote.server.RemoteCacheServerFactory;

import java.io.IOException;
import java.util.Properties;

public class RemoteServerLauncher {
    public static void main(String[] args) throws IOException {
        RemoteCacheServerFactory.startup("localhost", 2000, new Properties() {{
            load(Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties"));
        }});
    }
}
