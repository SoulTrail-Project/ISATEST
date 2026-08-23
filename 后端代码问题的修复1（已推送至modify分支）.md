# 后端接口 10/11 相关修改总结报告

> 已推送至 分支 modify-branch

## 一、背景

联调后端 B 负责的接口 10（情绪分布/饼图）、接口 11（词云/按天统计）时，项目启动与运行报错。
经排查，问题集中在四类：① MyBatis Mapper 接口与 XML 类型不一致且缺失 `Summary` 类；② `DiaryMapper.xml` 存在 3 处 SQL bug（合并冲突残留 + 列名写错）；③ `diary` 表缺少 `title`/`is_deleted` 两列；④ `mybatis-spring` 版本与 Spring Boot 3.5.4 不兼容导致应用无法启动。
本次修改已逐项修复，应用可正常启动，接口可联调。

## 二、改动清单

### 1. pom.xml —— 升级 mybatis-spring 至 3.0.4

- 新增显式依赖：`org.mybatis:mybatis-spring:3.0.4`
- 原因：MyBatis-Plus 3.5.7 传递引入了 `mybatis-spring 3.0.3`，与 Spring Boot 3.5.4（Spring Framework 6.2.9）不兼容，启动报
  `Invalid value type for attribute 'factoryBeanObjectType': java.lang.String`。3.0.4 修复了该兼容问题。
- 影响：解决全部 Mapper 注册失败、应用无法启动的问题。

### 2. DiaryMapper.xml —— 统一类型 + 修复 3 处 SQL bug

- 2.1 `resultMap` 与 `insert`/`update` 的 `parameterType` 由缺失的 `VO.Summary` 改为 `pojo.Diary`，与 `DiaryMapper` 接口方法签名（本就用 `Diary`）一致。
- 2.2 `countByEmotionType`（接口10）：恢复被注释的 `is_deleted = 0` 过滤；补上缺失的 `</select>` 闭合标签（合并冲突残留导致 XML 解析失败）。
- 2.3 `selectDailyStats`（接口11）：`AVG(emotion_score)` → `AVG(sentiment_score)`；`GROUP_CONCAT(emotion_type ...)` → `GROUP_CONCAT(mood_type ...)`（修正原误写的列名，真实列名为 `sentiment_score`/`mood_type`）。
- 2.4 `selectTopKeywords`（接口11）：原 `SELECT keyword FROM diary_keyword WHERE user_id/created_at`（该表仅有 `diary_id/word/weight`，无这些列）改为 `JOIN diary` 关联查询，使用 `dk.word`、`d.user_id`、`d.is_deleted`、`d.created_at`。

### 3. pojo/Diary.java —— 补充 isDeleted 字段

- 新增：`private Integer isDeleted;`
- 原因：`diary` 表有 `is_deleted` 列，原实体漏映射；改用 `Diary` 承载 resultMap/参数后需补齐，否则 MyBatis 映射失败。

### 4. 数据库 diary 表 —— 新增两列

- 执行 SQL：
  ALTER TABLE diary
  
      ADD COLUMN title VARCHAR(100) NULL COMMENT '日记标题' AFTER content,
      ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 NOT NULL COMMENT '逻辑删除标记：0-未删 1-已删' AFTER updated_at;
- 原因：原表缺 `title`、`is_deleted`，代码引用必报 Unknown column。`is_deleted` 必须 `DEFAULT 0 NOT NULL`，保证历史数据参与 `is_deleted = 0` 过滤。

### 5. application.yaml —— 确认 mybatis-plus 前缀（此前已改）

- `mybatis:` → `mybatis-plus:`，确保 MyBatis-Plus 正确加载 `classpath:mybatis/*.xml`（否则报 Invalid bound statement）。

## 三、核验结论

- pom.xml / DiaryMapper.xml / Diary.java / application.yaml 均已本地读确认改动正确一致。
- 数据库 diary 表 `title`、`is_deleted` 两列需已在团队数据库执行（建议执行后 `DESCRIBE diary;` 复核）。
- 当前接口 10/11 的 service、controller 层需进行一次联调，确认返回数据结构符合前端预期。

## 四、后续注意（建议）

1. `Diary.java` 中 `sentimentLable` 字段拼写与表列 `sentiment_label` 不一致，MyBatis-Plus 自动 CRUD 可能映射失败，建议改为 `sentimentLabel` 并加 `@TableField("sentiment_label")`。
2. `application.yaml` 中 `log-impl: StdOutImpl` 仅开发期打印 SQL，上线前请删除。
3. `pom.xml` 中 `spring-boot-starter-test` 重复声明（无害），可顺手清理。

## 五、涉及文件

- soul_trail/pom.xml
- soul_trail/src/main/resources/mybatis/DiaryMapper.xml
- soul_trail/src/main/java/com/sixth/soul_trail/pojo/Diary.java
- soul_trail/src/main/resources/application.yaml
- 数据库：diary 表（DDL 变更）
