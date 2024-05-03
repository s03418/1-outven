package com.example.outven.dto;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class certificationDTO {
    private String user_id;
    private String email;
    private String certification_code;
    private Date logtime;
}
