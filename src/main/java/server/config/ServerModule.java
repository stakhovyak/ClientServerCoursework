package server.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import server.broadcast.BroadcastManager;
import server.broadcast.BroadcastManagerImpl;
import commons.data.DataProcessor;
import commons.data.SimpleDataProcessor;
import server.transport.SocketTransportLayer;
import server.transport.TransportLayer;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransportLayer.class).to(SocketTransportLayer.class).in(Scopes.SINGLETON);
        bind(BroadcastManager.class).to(BroadcastManagerImpl.class).in(Scopes.SINGLETON);
        bind(DataProcessor.class).to(SimpleDataProcessor.class).in(Scopes.SINGLETON);
    }
}
