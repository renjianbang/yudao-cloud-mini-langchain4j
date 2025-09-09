package cn.iocoder.yudao.module.travel.util;

import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化工具
 */
@Component
public class DatabaseInitUtil {

    private final DataSource dataSource;

    public DatabaseInitUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initDatabase() {
        try {
            // 读取SQL文件
            ClassPathResource resource = new ClassPathResource("sql/travel_init.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, StandardCharsets.UTF_8);

            // 执行SQL
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                // 按照分号分割SQL语句
                String[] sqlStatements = sql.split(";");

                for (String sqlStatement : sqlStatements) {
                    String trimmedSql = sqlStatement.trim();
                    if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                        System.out.println("执行SQL: " + trimmedSql.substring(0, Math.min(100, trimmedSql.length())) + "...");
                        statement.execute(trimmedSql);
                    }
                }

                System.out.println("数据库初始化完成!");
            }
        } catch (Exception e) {
            System.err.println("数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initRebookingTables() {
        try {
            // 读取改签SQL文件
            ClassPathResource resource = new ClassPathResource("sql/rebooking_init.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, StandardCharsets.UTF_8);

            // 执行SQL
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                // 按照分号分割SQL语句
                String[] sqlStatements = sql.split(";");

                for (String sqlStatement : sqlStatements) {
                    String trimmedSql = sqlStatement.trim();
                    if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                        System.out.println("执行改签SQL: " + trimmedSql.substring(0, Math.min(100, trimmedSql.length())) + "...");
                        statement.execute(trimmedSql);
                    }
                }

                System.out.println("改签模块数据库表初始化完成!");
            }
        } catch (Exception e) {
            System.err.println("改签模块数据库表初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}