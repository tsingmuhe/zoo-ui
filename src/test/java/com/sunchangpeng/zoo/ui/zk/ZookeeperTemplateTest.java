package com.sunchangpeng.zoo.ui.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

public class ZookeeperTemplateTest {
    private static ZookeeperTemplate template;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ZookeeperProperties properties = ZookeeperProperties.builder().build();
        template = new ZookeeperTemplate(properties);
        template.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        template.stop();
    }

    @Before
    public void before() {
        template.deleteRecursivelyIfExists("/sunchp");
    }

    @After
    public void after() {
        template.deleteRecursivelyIfExists("/sunchp");
    }

    //search
    @Test(expected = ZookeeperException.class)
    public void getData1() {
        System.out.println(template.getDataJson("/sunchp", List.class));
    }

    @Test
    public void getData2() {
        template.create("/sunchp", Arrays.asList("sunchp"), CreateMode.PERSISTENT);
        System.out.println(template.getDataJson("/sunchp", List.class));
    }

    @Test
    public void getData3() {
        template.create("/sunchp", (Object) null, CreateMode.PERSISTENT);
        System.out.println(template.getDataJson("/sunchp", List.class));
    }

    @Test
    public void checkExists() {
        System.out.println(template.checkExists("/sunchp"));
        template.create("/sunchp", CreateMode.PERSISTENT);
        System.out.println(template.checkExists("/sunchp"));
    }

    @Test
    public void getChildren() {
        System.out.println(template.getChildren("/sunchp"));
        template.create("/sunchp", CreateMode.PERSISTENT);
        System.out.println(template.getChildren("/sunchp"));
    }

    //create
    @Test(expected = ZookeeperException.class)
    public void create1() {
        System.out.println(template.create("/sunchp", CreateMode.PERSISTENT));
        System.out.println(template.create("/sunchp", CreateMode.PERSISTENT));
    }

    @Test(expected = ZookeeperException.class)
    public void create2() {
        System.out.println(template.create("/sunchp", Arrays.asList("sunchp"), CreateMode.PERSISTENT));
        System.out.println(template.create("/sunchp", Arrays.asList("sunchp"), CreateMode.PERSISTENT));
    }

    @Test
    public void createIfNotExists1() {
        System.out.println(template.createIfNotExists("/sunchp", CreateMode.PERSISTENT));
        System.out.println(template.createIfNotExists("/sunchp", CreateMode.PERSISTENT));
    }

    @Test
    public void createIfNotExists2() {
        System.out.println(template.createIfNotExists("/sunchp", Arrays.asList("sunchp"), CreateMode.PERSISTENT));
        System.out.println(template.createIfNotExists("/sunchp", Arrays.asList("sunchp"), CreateMode.PERSISTENT));
    }

    //update
    @Test(expected = ZookeeperException.class)
    public void update1() {
        System.out.println(template.update("/sunchp"));
    }

    @Test(expected = ZookeeperException.class)
    public void update2() {
        System.out.println(template.update("/sunchp", Arrays.asList("sunchp update")));
    }

    @Test
    public void update3() {
        System.out.println(template.updateIfExists("/sunchp"));
    }

    @Test
    public void update4() {
        System.out.println(template.updateIfExists("/sunchp", Arrays.asList("sunchp update")));
    }

    @Test
    public void update5() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        System.out.println(template.updateIfExists("/sunchp"));
        System.out.println(template.getDataString("/sunchp"));
    }

    @Test
    public void update6() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        System.out.println(template.updateIfExists("/sunchp", Arrays.asList("sunchp update")));
        System.out.println(template.getDataString("/sunchp"));
    }

    //delete
    @Test(expected = ZookeeperException.class)
    public void delete1() {
        template.delete("/sunchp");
    }

    @Test
    public void delete2() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        template.delete("/sunchp");
    }

    @Test
    public void deleteIfExists1() {
        template.deleteIfExists("/sunchp");
    }

    @Test
    public void deleteIfExists2() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        template.deleteIfExists("/sunchp");
    }

    @Test(expected = ZookeeperException.class)
    public void deleteRecursively1() {
        template.deleteRecursively("/sunchp");
    }

    @Test
    public void deleteRecursively2() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        template.deleteRecursively("/sunchp");
    }

    @Test
    public void deleteRecursivelyIfExists() {
        template.deleteRecursivelyIfExists("/sunchp");
    }

    @Test
    public void getStat1() {
        System.out.println(template.getStat("/sunchp"));
    }

    @Test
    public void getStat2() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        System.out.println(template.getStat("/sunchp"));
    }

    @Test
    public void getACL1() {
        template.create("/sunchp", CreateMode.PERSISTENT);
        List<ACL> acls = template.getACL("/sunchp");
        System.out.println(acls);
    }

    @Test(expected = ZookeeperException.class)
    public void getACL2() {
        List<ACL> acls = template.getACL("/sunchp");
        System.out.println(acls);
    }

    @Test
    public void getACLIfExists() {
        List<ACL> acls = template.getACLIfExists("/sunchp");
        System.out.println(acls);
    }
}