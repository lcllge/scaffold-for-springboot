package com.example.demo.util;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;
import java.util.Iterator;

/**
 * @version V1.0.0
 * @ClassName: {@link PlusHelper}
 * @Description: Mybatis Plus 分页插件批量帮助类
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
public class PlusHelper {

    /**
     * 批量新增
     *
     * @param entityList
     * @param <T>
     * @return
     */
    public static <T> boolean saveBatch(Collection<T> entityList) {
        if (entityList.isEmpty()) {
            return true;
        }
        Iterator<T> iterator = entityList.iterator();
        T next = iterator.next();
        String sqlStatement = SqlHelper.table(next.getClass()).getSqlStatement(SqlMethod.INSERT_ONE.getMethod());
        int batchSize = 1000;
        try (SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(next.getClass())) {
            int i = 0;
            for (T anEntityList : entityList) {
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * 批量更新
     *
     * @param entityList
     * @param <T>
     * @return
     */
    public static <T> boolean updateBatchById(Collection<T> entityList) {
        if (entityList.isEmpty()) {
            return false;
        }
        Iterator<T> iterator = entityList.iterator();
        T next = iterator.next();
        String sqlStatement = SqlHelper.table(next.getClass()).getSqlStatement(SqlMethod.UPDATE_BY_ID.getMethod());
        int batchSize = 1000;
        try (SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(next.getClass())) {
            int i = 0;
            for (T anEntityList : entityList) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

}
