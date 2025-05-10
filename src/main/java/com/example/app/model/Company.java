package com.example.app.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Company {
    private UUID companyId;
    private String rsin;
    private String inpId;
    private String name;
    private String email;
    private String address;
    private String status;
}
