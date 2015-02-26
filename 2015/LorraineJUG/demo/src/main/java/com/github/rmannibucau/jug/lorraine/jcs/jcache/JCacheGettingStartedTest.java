package com.github.rmannibucau.jug.lorraine.jcs.jcache;

import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListener;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import javax.cache.spi.CachingProvider;

import static org.junit.Assert.assertEquals;

public class JCacheGettingStartedTest {
    @Test
    public void start() {
        try (CachingProvider provider = Caching.getCachingProvider()) {
            try (CacheManager manager = provider.getCacheManager()) {
                try (Cache<String, String> cache = manager.createCache("getting-started", new MutableConfiguration<String, String>())) {
                    cache.putIfAbsent("jug", "lorraine");
                    assertEquals("lorraine", cache.get("jug"));
                }
            }
        }
    }

    @Test
    public void processor() {
        try (CachingProvider provider = Caching.getCachingProvider()) {
            try (CacheManager manager = provider.getCacheManager()) {
                try (Cache<String, String> cache = manager.createCache("processor", new MutableConfiguration<String, String>())) {
                    cache.putIfAbsent("jug", "lorraine");
                    int l = cache.invoke("jug", new EntryProcessor<String, String, Integer>() {
                        @Override
                        public Integer process(final MutableEntry<String, String> entry, final Object... arguments) throws EntryProcessorException {
                            if (entry.exists()) {
                                entry.setValue(arguments[0].toString());
                            }
                            return entry.getValue().length();
                        }
                    }, "best jug ever");
                    assertEquals("best jug ever".length(), l);
                    assertEquals("best jug ever", cache.get("jug"));
                }
            }
        }
    }

    @Test
    public void event() {
        try (CachingProvider provider = Caching.getCachingProvider()) {
            try (CacheManager manager = provider.getCacheManager()) {
                try (final Cache<String, String> cache = manager.createCache("processor", new MutableConfiguration<String, String>())) {
                    cache.registerCacheEntryListener(new CacheEntryListenerConfiguration<String, String>() {
                        @Override
                        public Factory<CacheEntryListener<? super String, ? super String>> getCacheEntryListenerFactory() {
                            return new FactoryBuilder.SingletonFactory<CacheEntryListener<? super String, ? super String>>(new CacheEntryCreatedListener<String, String>() {
                                @Override
                                public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
                                    dumpEvent(cacheEntryEvents);
                                }
                            });
                        }

                        @Override
                        public boolean isOldValueRequired() {
                            return true;
                        }

                        @Override
                        public Factory<CacheEntryEventFilter<? super String, ? super String>> getCacheEntryEventFilterFactory() {
                            return null;
                        }

                        @Override
                        public boolean isSynchronous() {
                            return false;
                        }
                    });
                    cache.registerCacheEntryListener(new CacheEntryListenerConfiguration<String, String>() {
                        @Override
                        public Factory<CacheEntryListener<? super String, ? super String>> getCacheEntryListenerFactory() {
                            return new FactoryBuilder.SingletonFactory<CacheEntryListener<? super String, ? super String>>(new CacheEntryUpdatedListener<String, String>() {
                                @Override
                                public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
                                    dumpEvent(cacheEntryEvents);
                                }
                            });
                        }

                        @Override
                        public boolean isOldValueRequired() {
                            return true;
                        }

                        @Override
                        public Factory<CacheEntryEventFilter<? super String, ? super String>> getCacheEntryEventFilterFactory() {
                            return null;
                        }

                        @Override
                        public boolean isSynchronous() {
                            return false;
                        }
                    });
                    cache.putIfAbsent("jug", "lorraine");
                    cache.putIfAbsent("jug", "lorraine!");
                    cache.put("jug", "lorraine :)");
                }
            }
        }
    }

    private void dumpEvent(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) {
        // for the demo we know we'll get 1 entry
        final CacheEntryEvent<? extends String, ? extends String> event = cacheEntryEvents.iterator().next();
        System.out.println(">>> Created (key) " + event.getKey());
        System.out.println(">>> Created (old) " + event.getOldValue());
        System.out.println(">>> Created (new) " + event.getValue());
    }
}
