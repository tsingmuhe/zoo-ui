package com.sunchangpeng.zoo.ui.zk;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
@Builder
public class ZookeeperProperties {
    @Builder.Default
    private String connectString = "localhost:2181";
    @Builder.Default
    private Integer baseSleepTimeMs = 50;
    @Builder.Default
    private Integer maxRetries = 10;
    @Builder.Default
    private Integer maxSleepMs = 500;
    @Builder.Default
    private Integer blockUntilConnectedWait = 10;
    @Builder.Default
    private TimeUnit blockUntilConnectedUnit = TimeUnit.SECONDS;
}
