package server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.config.ServerModule;

import java.io.IOException;

public class AppStarter {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new ServerModule());
        var server = injector.getInstance(Server.class);
        LOG.info("staring app");
        server.start();
    }
}