package com.qianlan.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Relationship implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Long sourceid;

    private Long targetid;


}
