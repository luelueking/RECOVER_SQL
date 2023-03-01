package org.example;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class RecoverSQL {

    public static void main(String[] a) {
        // 实体类的位置
        Class dtoClass = org.example.pojo.dto.FileDto.class;
        // 生成的sql语句的位置
        String outputPath = "src/main/resources/test.sql";
        generateTableSql(dtoClass, outputPath, getTableName(dtoClass));
        System.out.println("生成结束");
    }

    private static String getTableName(Class obj ) {
        TableName tableName = (TableName) obj.getAnnotation(TableName.class);
        return tableName.value();
    }

    // 写入sql到文件
    public static void writeFile(String content, String outputPath) {
        File file = new File(outputPath);
        System.out.println("文件路径: " + file.getAbsolutePath());
        // 输出文件的路径
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter out = null;

        try {
            // 如果文件存在，就删除
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fos);
            out = new BufferedWriter(osw);
            out.write(content);
            // 清空缓冲流，把缓冲流里的文本数据写入到目标文件里
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateTableSql(Class obj, String outputPath, String tableName) {
        // tableName 如果是 null，就用类名做表名
        if (tableName == null || tableName.equals("")) {
            tableName = obj.getName();
            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
        }

        Field[] fields = obj.getDeclaredFields();
        Object param;
        String column;

        StringBuilder sb = new StringBuilder();

        sb.append("drop table if exists ").append(tableName).append(";\r\n");

        sb.append("\r\n");

        sb.append("create table ").append(tableName).append("(\r\n");

        // 实体类BaseDto
        Class baseDtoClass = obj.getSuperclass();
        for (Field baseColumn : baseDtoClass.getDeclaredFields()) {
            sb.append(getColumn(baseColumn));
            sb.append(", ");
            sb.append("\n");
        }

        // 该类的column
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            column = convertName(f.getName());
            sb.append(getColumn(f));
            if (i != fields.length - 1) { // 最后一个属性后面不加逗号
                sb.append(", ");
            }
            sb.append("\n");
        }

        String sql = sb.toString();

        sql = sb.substring(0, sql.length() - 1) + "\n) " + "ENGINE = INNODB DEFAULT CHARSET = utf8;";

        writeFile(sql, outputPath);
    }

    /**
     * 获取column，可配置规则
     * @param f
     * @return
     */
    public static String getColumn (Field f) {
        String column = convertName(f.getName());
        String simpleName = f.getType().getSimpleName();
        int length = 50; // 默认varchar长度为50
        switch (simpleName) {
            case "Integer":
                column =  column + " INTEGER ";
                break;
            case "String":
                column =  column + " VARCHAR(" + length + ")";
                break;
            case "Date":
                column = column + " datetime ";
                break;
            case "Boolean":
                column = column + " tinyint(3) ";
                break;
            default:
                column =  column + " VARCHAR(" + length + ")";
        }
        // 设置名为id的column为主键
        TableId tableId = f.getAnnotation(TableId.class);
        if (tableId!=null) column = column + " PRIMARY KEY";
        return column;
    }

    // 命名转换，驼峰转换
    public static String convertName (String name) {
        String result = "";
        if (name != null) {
            if (!Pattern.compile( ".*[_]+.*" ).matcher( name ).matches()) {//是否是下划线命名
                if (!Pattern.compile( ".*[A-Z]+.*" ).matcher( name ).matches()) {//是否包含大写字母
                    return name;
                }
                result = name.replaceAll( "([a-z])([A-Z])", "$1" + "_" + "$2" ).toLowerCase();
            } else {
                for (String s : name.split( "_" )) {
                    result = result + s.substring( 0, 1 ).toUpperCase()+s.substring( 1 ).toLowerCase();
                }
                result=result.substring( 0, 1 ).toLowerCase()+result.substring( 1 );
            }
        } else {
            return null;
        }
        return result;
    }

}
