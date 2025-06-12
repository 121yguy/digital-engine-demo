package org.demo.common.domain.po;

import lombok.Data;

@Data
public class OperationLog {

    private Long logId;
    private Long userId;
    private String action;
    private String ip;
    private String detail;

}
