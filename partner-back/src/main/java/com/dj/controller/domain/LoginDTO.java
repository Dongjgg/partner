package com.dj.controller.domain;

import com.dj.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO implements Serializable {  //实现序列化
    private static final long serialVersionUID = 1L;

    private User user;
    private String token;
}
