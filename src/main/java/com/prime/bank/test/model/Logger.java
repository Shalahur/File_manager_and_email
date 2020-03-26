package com.prime.bank.test.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logger implements Serializable {

    private static final long serialVersionUID = 8250850413459936415L;

    private Integer id;

    private String description;

    private Date logTime;
}
