package it.tai.notificationmanagement.server.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.common.collect.Lists;

public class GoogleCustomDataStoreFactory extends AbstractDataStoreFactory {

    private final HashMap<String, byte[]> keys;

    public GoogleCustomDataStoreFactory(InputStream inputStream) throws IOException {
        keys = IOUtils.deserialize(inputStream);
    }

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new AbstractDataStore<V>(this, id) {

            @Override
            public Set<String> keySet() throws IOException {

                return keys.keySet();
            }

            @Override
            public Collection<V> values() throws IOException {
                List<V> result = Lists.newArrayList();
                for (byte[] bytes : keys.values()) {
                    result.add(IOUtils.<V>deserialize(bytes));
                }
                return Collections.unmodifiableList(result);
            }

            @Override
            public V get(String key) throws IOException {

                return (V) IOUtils.deserialize(keys.get(key));
            }

            @Override
            public DataStore<V> set(String key, V value) throws IOException {
                return this;
            }

            @Override
            public DataStore<V> clear() throws IOException {
                return this;
            }

            @Override
            public DataStore<V> delete(String key) throws IOException {

                return this;
            }

        };
    }

}

