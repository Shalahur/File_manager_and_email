package com.prime.bank.test.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job  implements Serializable {

    private static final long serialVersionUID = 8250850413459936415L;

    private String jobId;

    private String jobTitle;

    private Integer maxSalary;

    private Integer minSalary;
}
