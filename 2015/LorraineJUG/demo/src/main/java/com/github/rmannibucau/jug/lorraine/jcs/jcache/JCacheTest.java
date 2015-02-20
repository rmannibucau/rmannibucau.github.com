package com.github.rmannibucau.jug.lorraine.jcs.jcache;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

@RunWith(ApplicationComposer.class)
@Classes(cdi = true, innerClassesAsBean = true)
public class JCacheTest {
    @Inject
    private JUGService service;

    @Test
    public void run() {
        final long raw, cached;
        {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            assertTrue(service.didYouLikeTheTalk());
            stopWatch.stop();
            raw = stopWatch.getNanoTime();
        }
        {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            assertTrue(service.didYouLikeTheTalk());
            stopWatch.stop();
            cached = stopWatch.getNanoTime();
        }
        System.out.println(TimeUnit.NANOSECONDS.toMillis(raw) + " " + TimeUnit.NANOSECONDS.toMillis(cached));
    }

    public static class JUGService {
        @CacheResult(cacheName = "JUGService")
        public boolean didYouLikeTheTalk() {
            System.out.println(">>> not an easy question so let's take time to think about it few seconds");
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }

            // finally it was really good ;)
            return true;
        }
    }
}
