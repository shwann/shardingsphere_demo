package com.example.sharding;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * 多字段分片算法实现
 * 支持基于多个字段进行分片计算
 */
public class MultiColumnShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    private Properties props = new Properties();

    @Override
    public void init(Properties props) {
        this.props = props;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<?>> shardingValue) {
        // 获取分片字段名
        String columnName = shardingValue.getColumnName();
        Comparable<?> value = shardingValue.getValue();

        // 根据配置的字段和分片数量计算分片
        int shardCount = Integer.parseInt(props.getProperty("shard-count", "4"));

        // 计算分片索引
        int shardIndex = calculateShardIndex(columnName, value, shardCount);

        // 返回对应的分片表名
        String tablePrefix = props.getProperty("table-prefix", "");
        return tablePrefix + "_" + shardIndex;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Comparable<?>> shardingValue) {
        // 范围查询时返回所有可能的分片
        return availableTargetNames;
    }

    /**
     * 计算分片索引
     * 可以根据不同的字段使用不同的分片策略
     */
    private int calculateShardIndex(String columnName, Comparable<?> value, int shardCount) {
        if (value == null) {
            return 0;
        }

        // 根据字段名使用不同的分片策略
        switch (columnName) {
            case "user_id":
                // 租户ID直接取模
                return Math.abs(value.hashCode()) % shardCount;
            case "order_id":
                // 用户ID使用哈希后取模
                return Math.abs(value.hashCode()) % shardCount;
            default:
                // 默认使用哈希取模
                return Math.abs(value.hashCode()) % shardCount;
        }
    }

    @Override
    public String getType() {
        return "MULTI_COLUMN_ORDER";
    }
}

