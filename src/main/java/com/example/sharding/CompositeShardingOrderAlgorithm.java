//package com.example.sharding;
//
//import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
//import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
//import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Properties;
//
///**
// * 复合字段分片算法
// * 支持基于多个字段的组合进行分片计算
// * 配置示例：
// * sharding-columns: user_id,order_id
// * shard-count: 4
// * table-prefix: t_order
// */
//public class CompositeShardingOrderAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {
//
//    private Properties props = new Properties();
//    private List<String> shardingColumns = new ArrayList<>();
//
//    @Override
//    public void init(Properties props) {
//        this.props = props;
//
//        // 解析分片字段配置
//        String columnsConfig = props.getProperty("sharding-columns", "user_id");
//        String[] columns = columnsConfig.split(",");
//        for (String column : columns) {
//            shardingColumns.add(column.trim());
//        }
//    }
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<?>> shardingValue) {
//        String columnName = shardingValue.getColumnName();
//        Comparable<?> value = shardingValue.getValue();
//
//        // 检查是否是配置的分片字段
//        if (!shardingColumns.contains(columnName)) {
//            throw new IllegalArgumentException("Column " + columnName + " is not configured for sharding");
//        }
//
//        int shardCount = Integer.parseInt(props.getProperty("shard-count", "4"));
//        String tablePrefix = props.getProperty("table-prefix", "");
//
//        // 计算分片索引
//        int shardIndex = calculateCompositeShardIndex(columnName, value, shardCount);
//
//        // 构建表名
//        String tableName = tablePrefix + "_" + shardIndex;
//
//        // 验证表名是否在可用目标中
//        if (availableTargetNames.contains(tableName)) {
//            return tableName;
//        }
//
//        // 如果表名不存在，返回第一个可用的表
//        return availableTargetNames.iterator().next();
//    }
//
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Comparable<?>> shardingValue) {
//        // 范围查询时返回所有可能的分片
//        return availableTargetNames;
//    }
//
//    /**
//     * 计算复合分片索引
//     * 根据字段名和值计算分片位置
//     */
//    private int calculateCompositeShardIndex(String columnName, Comparable<?> value, int shardCount) {
//        if (value == null) {
//            return 0;
//        }
//
//        // 获取字段在配置中的权重
//        int columnWeight = getColumnWeight(columnName);
//
//        // 计算哈希值
//        int hashValue = value.hashCode();
//
//        // 应用权重和分片计算
//        return Math.abs((hashValue * columnWeight) % shardCount);
//    }
//
//    /**
//     * 获取字段权重
//     * 不同字段可以有不同的权重，影响分片分布
//     */
//    private int getColumnWeight(String columnName) {
//        switch (columnName) {
//            case "user_id":
//                return 1; // 租户ID权重为1
//            case "order_id":
//                return 2; // 用户ID权重为2
//            default:
//                return 1; // 默认权重为1
//        }
//    }
//
//    @Override
//    public String getType() {
//        return "COMPOSITE_SHARDING_ORDER";
//    }
//}
