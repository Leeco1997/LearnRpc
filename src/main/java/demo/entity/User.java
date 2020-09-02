package demo.entity;

import com.rpcdemo02.seriablize.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liqiao
 * @date 2020/7/27 18:09
 * @description
 */
@AllArgsConstructor
@Data
@Builder
public class User {
    private String name;
    private Integer age;

}
