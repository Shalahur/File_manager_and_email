package com.prime.bank.test.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee implements Serializable {

    private static final long serialVersionUID = -2534840324939839170L;

    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date hireDate;
    private Integer jobId;
    private Integer salary;
    private Integer commissionPCT;
    private Integer managerId;
    private Integer departmentId;
}
