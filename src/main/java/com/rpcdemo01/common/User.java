package com.rpcdemo01.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liqiao
 * @date 2020/5/13 14:58
 * @description
 */
@AllArgsConstructor
@Data
public class User  implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
}
