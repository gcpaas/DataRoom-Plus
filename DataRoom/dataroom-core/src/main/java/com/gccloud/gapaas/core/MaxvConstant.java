package com.gccloud.gapaas.core;

public interface MaxvConstant {

    /**
     * 页面
     */
    interface PageDesign {
        /**
         * 页面状态
         */
        interface STATE {
            /**
             * 已发布
             */
            String PUBLISHED = "published";
            /**
             * 设计中
             */
            String DESIGN = "design";
        }
    }

    /**
     * 页面中转
     */
    interface PageStage {

        interface STATE {
            /**
             * 操作、记录增量的操作，非全量数据
             */
            String OPERATION = "operation";
            /**
             * 历史记录，全量的
             */
            String HISTORY = "history";
            /**
             * 快照、全量的
             */
            String SNAPSHOT = "snapshot";
        }

        interface TARGET {
            String PAGE_DESIGN = "pageDesign";
            String PAGE_RELEASE = "pageRelease";
        }
    }

    /**
     * 数据集
     */
    interface Dataset {
        /**
         * 编码前缀
         */
        String CODE_PREFIX = "dataset";

        /**
         * 数据集类型
         */
        interface TYPE {
            String JSON = "json";
            String MYSQL = "mysql";
            String PG = "pg";
            String ORACLE = "oracle";
            String HTTP = "http";
            String GROOVY = "groovy";
            /**
             * 服务bean后缀
             */
            String SERVICE_NAME = "DatasetService";
        }

        interface HTTP_DATASET {
            interface METHOD {
                String POST = "post";
                String GET = "get";
            }
        }

    }

    /**
     * 数据源
     */
    interface Datasource {
        /**
         * 编码前缀
         */
        String CODE_PREFIX = "datasource";

        /**
         * 数据源类型
         */
        interface TYPE {
            String MYSQL = "mysql";
            String POSTGRESQL = "postgresql";
            String ORACLE = "oracle";
        }
    }

    /**
     * 角色
     */
    interface Role {
        /**
         * 数据源类型
         */
        interface TYPE {
            String MYSQL = "mysql";
            String POSTGRESQL = "postgresql";
            String ORACLE = "oracle";
        }
    }
}
