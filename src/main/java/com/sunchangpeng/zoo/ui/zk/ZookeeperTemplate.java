package com.sunchangpeng.zoo.ui.zk;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.sunchangpeng.zoo.ui.component.AbstractLifeCycle;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ZookeeperTemplate extends AbstractLifeCycle implements ZookeeperOperations {
    private ZookeeperProperties properties;
    private RetryPolicy retryPolicy;
    private CuratorFramework curatorFramework;

    public ZookeeperTemplate(ZookeeperProperties properties) {
        this.properties = properties;
    }

    protected void doStart() throws Exception {
        this.retryPolicy = exponentialBackoffRetry(this.properties);
        this.curatorFramework = curatorFramework(this.retryPolicy, this.properties);
    }

    protected void doStop() throws Exception {
        if (this.curatorFramework != null) {
            this.curatorFramework.close();
        }
    }


    public CuratorFramework curatorFramework(RetryPolicy retryPolicy, ZookeeperProperties properties) throws Exception {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(properties.getConnectString());
        CuratorFramework curator = builder.retryPolicy(retryPolicy).defaultData(null).build();
        curator.start();
        log.trace("blocking until connected to zookeeper for " + properties.getBlockUntilConnectedWait() + properties.getBlockUntilConnectedUnit());
        curator.blockUntilConnected(properties.getBlockUntilConnectedWait(), properties.getBlockUntilConnectedUnit());
        log.trace("connected to zookeeper");
        return curator;
    }

    public RetryPolicy exponentialBackoffRetry(ZookeeperProperties properties) {
        return new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries(), properties.getMaxSleepMs());
    }

    @Override
    public byte[] getData(String path) {
        try {
            return curatorFramework.getData().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public String getDataString(String path) {
        byte[] data = getData(path);
        if (data == null) {
            return null;
        }

        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public <T> T getDataJson(String path, Class<T> clazz) {
        String json = getDataString(path);
        if (json == null) {
            return null;
        }

        return JSON.parseObject(json, clazz);
    }

    @Override
    public List<String> getChildren(String path) {
        if (!checkExists(path)) {
            return Collections.emptyList();
        }

        try {
            return curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public boolean checkExists(String path) {
        try {
            Stat pathStat = curatorFramework.checkExists().forPath(path);
            return pathStat != null;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public Stat getStat(String path) {
        try {
            return curatorFramework.checkExists().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public String create(String path, CreateMode mode) {
        try {
            return curatorFramework.create().withMode(mode).forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public String create(String path, byte[] data, CreateMode mode) {
        try {
            return curatorFramework.create().withMode(mode).forPath(path, data);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public String create(String path, String data, CreateMode mode) {
        return create(path, data == null ? null : data.getBytes(StandardCharsets.UTF_8), mode);
    }

    @Override
    public String create(String path, Object data, CreateMode mode) {
        return create(path, data == null ? null : JSON.toJSONString(data), mode);
    }

    @Override
    public boolean createIfNotExists(String path, CreateMode mode) {
        if (!checkExists(path)) {
            String nodePath = create(path, mode);
            return Strings.isNullOrEmpty(nodePath) ? Boolean.FALSE : Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean createIfNotExists(String path, byte[] data, CreateMode mode) {
        if (!checkExists(path)) {
            String nodePath = create(path, data, mode);
            return Strings.isNullOrEmpty(nodePath) ? Boolean.FALSE : Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean createIfNotExists(String path, String data, CreateMode mode) {
        return createIfNotExists(path, data == null ? null : data.getBytes(StandardCharsets.UTF_8), mode);
    }

    @Override
    public boolean createIfNotExists(String path, Object data, CreateMode mode) {
        return createIfNotExists(path, data == null ? null : JSON.toJSONString(data), mode);
    }

    @Override
    public boolean update(String path) {
        try {
            curatorFramework.setData().forPath(path);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public boolean update(String path, byte[] data) {
        try {
            curatorFramework.setData().forPath(path, data);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public boolean update(String path, String data) {
        return update(path, data == null ? null : data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean update(String path, Object data) {
        return update(path, data == null ? null : JSON.toJSONString(data));
    }

    @Override
    public boolean updateIfExists(String path) {
        if (checkExists(path)) {
            return update(path);
        }

        return false;
    }

    @Override
    public boolean updateIfExists(String path, byte[] data) {
        if (checkExists(path)) {
            return update(path, data);
        }

        return false;
    }

    @Override
    public boolean updateIfExists(String path, String data) {
        return updateIfExists(path, data == null ? null : data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean updateIfExists(String path, Object data) {
        return updateIfExists(path, data == null ? null : JSON.toJSONString(data));
    }

    @Override
    public void delete(String path) {
        try {
            curatorFramework.delete().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public void deleteIfExists(String path) {
        if (checkExists(path)) {
            delete(path);
        }
    }

    @Override
    public void deleteRecursively(String path) {
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public void deleteRecursivelyIfExists(String path) {
        if (checkExists(path)) {
            deleteRecursively(path);
        }
    }

    @Override
    public List<ACL> getACL(String path) {
        try {
            return curatorFramework.getACL().forPath(path);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public List<ACL> getACLIfExists(String path) {
        if (checkExists(path)) {
            return getACL(path);
        }

        return Collections.EMPTY_LIST;
    }
}
