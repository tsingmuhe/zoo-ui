package com.sunchangpeng.zoo.ui.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public interface ZookeeperOperations {
    //get
    byte[] getData(String path);

    String getDataString(String path);

    <T> T getDataJson(String path, Class<T> clazz);


    //create
    String create(String path, CreateMode mode);

    String create(String path, byte[] data, CreateMode mode);

    String create(String path, String data, CreateMode mode);

    String create(String path, Object data, CreateMode mode);

    boolean createIfNotExists(String path, CreateMode mode);

    boolean createIfNotExists(String path, byte[] data, CreateMode mode);

    boolean createIfNotExists(String path, String data, CreateMode mode);

    boolean createIfNotExists(String path, Object data, CreateMode mode);

    //update
    boolean update(String path);

    boolean update(String path, byte[] data);

    boolean update(String path, String data);

    boolean update(String path, Object data);

    boolean updateIfExists(String path);

    boolean updateIfExists(String path, byte[] data);

    boolean updateIfExists(String path, String data);

    boolean updateIfExists(String path, Object data);

    //delete
    void delete(String path);

    void deleteIfExists(String path);

    void deleteRecursively(String path);

    void deleteRecursivelyIfExists(String path);

    //children
    List<String> getChildren(String path);

    //state
    boolean checkExists(String path);

    Stat getStat(String path);
    //acl
    List<ACL> getACL(String path);

    List<ACL> getACLIfExists(String path);
}
