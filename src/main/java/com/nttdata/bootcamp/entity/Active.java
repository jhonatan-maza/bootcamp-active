package com.nttdata.bootcamp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "customer")
public class Active {

    @Id
    private String id;

    private String dni;
    private String accountNumber;

    private Boolean staff;
    private Boolean business;

    private Boolean personalCreditCard;
    private Boolean businessCreditCard;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private Date creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private Date modificationDate;

}
