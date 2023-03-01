# RECOVER_SQL
Help people who generate sql by code . And Fuck script kiddies who only attack mysql weak passwords!
### Support Type
- [X] Mybatis-Plus
- [ ] Mybatis
- [ ] ...
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
package org.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName(value = "t_file", autoResultMap = true)
public class FileDto extends BaseDto {
	private String originalName;
	private String fileName;
	private String filePath;
	private String thumbFilePath;
	private Integer storageType;
	private Integer shared;
}
```
AND RUN [script](https://github.com/luelueking/RECOVER_SQL/blob/main/FromMybatisPlus.java) ,YOU WILL GET SQL LIKE THIS
```sql
drop table if exists t_file;

create table t_file(
id VARCHAR(50) PRIMARY KEY, 
create_at datetime , 
create_by VARCHAR(50), 
update_at datetime , 
update_by VARCHAR(50), 
deleted tinyint(3) , 
original_name VARCHAR(50), 
file_name VARCHAR(50), 
file_path VARCHAR(50), 
thumb_file_path VARCHAR(50), 
storage_type INTEGER , 
shared INTEGER 
) ENGINE = INNODB DEFAULT CHARSET = utf8;
```
