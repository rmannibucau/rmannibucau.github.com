package com.github.rmannibucau.jug.lorraine.jcs.remote;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.logging.CacheEventLoggerDebugLogger;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

// NOTE: you need to run {@see org.apache.commons.jcs.auxiliary.remote.server.RemoteCacheServerFactory} before launching this test
public class Client {
    @Test
    public void run() throws IOException, InterruptedException {
        // create cache
        JCS.setConfigFilename("/client.properties");
        final CacheAccess<String, String> cacheAccess = JCS.getInstance("default");

        // put some data
        cacheAccess.put("jug", "lorraine");
        Thread.sleep(1000); // wait a bit it syncs

        // clean up data from local cache
        cacheAccess.getCacheControl().localRemove("jug"); // not sexy but to ensure we use remote cache
        assertNull(cacheAccess.getCacheControl().localGet("jug"));

        // we can still get them from remote cache
        assertEquals("lorraine", cacheAccess.get("jug"));

        // now we have them locally as well
        assertEquals("lorraine", cacheAccess.getCacheControl().localGet("jug").getVal());
    }
}
