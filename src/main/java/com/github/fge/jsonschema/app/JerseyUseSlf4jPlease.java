package com.github.fge.jsonschema.app;

import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class JerseyUseSlf4jPlease
    implements ServletContextListener
{
    @Override
    public void contextInitialized(final ServletContextEvent sce)
    {

        final Logger rootLogger = LogManager.getLogManager().getLogger("");
        for (final Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce)
    {
        SLF4JBridgeHandler.uninstall();
    }
}
