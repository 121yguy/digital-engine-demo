package org.demo.common.utils;

import org.demo.common.domain.po.OperationLog;

public class OperationLogUtil {

    private OperationLogUtil(){}

    public static OperationLogBuilder builder() {
        return new OperationLogBuilder();
    }

    public static class OperationLogBuilder {

        private Long userId;
        private String action;
        private String ip;
        private String detail;

        public OperationLogBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public OperationLogBuilder action(String action) {
            this.action = action;
            return this;
        }

        public OperationLogBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public OperationLogBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public OperationLog build() {
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setAction(action);
            log.setIp(ip);
            log.setDetail(detail);
            return log;
        }

    }

}
