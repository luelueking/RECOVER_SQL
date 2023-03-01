# RECOVER_SQL
Help people who generate sql by code . And Fuck script kiddies who only attack mysql weak passwords!
### Use Example
```java
package org.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * dto 基础父类
 */
@Data
public class BaseDto {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	@TableField(fill = FieldFill.INSERT)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE)
	private String updateBy;

	/**
	 * 默认逻辑删除标记，is_deleted=0有效
	 */
	@TableLogic
	@JsonIgnore
	@TableField(value = "is_deleted", select = false)
	private Boolean deleted = false;

}
```
```java

