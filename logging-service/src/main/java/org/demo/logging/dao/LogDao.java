package org.demo.logging.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.demo.common.domain.po.OperationLog;

@Mapper
public interface LogDao {

    @Insert("INSERT INTO operation_logs(log_id, user_id, action, ip, detail) VALUES (#{logId}, #{userId}, #{action}, #{ip}, #{detail})")
    void insertLog(OperationLog operationLog);

}
