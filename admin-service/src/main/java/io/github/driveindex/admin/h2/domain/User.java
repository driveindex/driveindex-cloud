package io.github.driveindex.admin.h2.domain;

import lombok.Data;

/**
 * @author sgpublic
 * @Date 2022/8/5 17:20
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
}
