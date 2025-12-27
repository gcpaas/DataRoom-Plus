package com.gccloud.gapaas.core.dataset.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MybatisSqlParserService {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        String sql = """
                SELECT id, name, age FROM t_user
                        <where>
                            <if test="name != null">AND name LIKE CONCAT('%', '#{name}', '%')</if>
                            <if test="age != null">AND age > #{age}</if>
                            <if test="ids != null and ids.size() > 0">
                                    AND id IN
                                    <foreach collection="ids" item="id" open="(" separator="," close=")">
                                        '#{id}'
                                    </foreach>
                                </if>
                        </where>
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("name", "tom");
        params.put("age", 20);
        ArrayList<String> idList = new ArrayList<>();
        idList.add("a");
        idList.add("b");
        idList.add("c");
        params.put("ids", idList);
        log.info("SQL : {}", parseDynamicSql(sql, params));
    }

    public String parseDynamicSql(String dynamicSql, Map<String, Object> params) {
        try {
            Configuration configuration = sqlSessionFactory.getConfiguration();
            // 1. 将SQL字符串包装为XML节点（MyBatis需要XML格式的输入）
            String xml = "<script>" + dynamicSql + "</script>";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            XPathParser xpathParser = new XPathParser(doc);
            Node node = doc.getFirstChild();
            XNode xNode = new XNode(xpathParser, node, null);
            // 2. 使用XMLScriptBuilder解析XML动态标签（支持<where>、<if>等）
            XMLScriptBuilder scriptBuilder = new XMLScriptBuilder(configuration, xNode, Map.class);
            SqlSource sqlSource = scriptBuilder.parseScriptNode();
            // 3. 生成最终SQL
            BoundSql boundSql = sqlSource.getBoundSql(params);
            String parsedSql = boundSql.getSql();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            Map<String, Object> additionalParameters = boundSql.getAdditionalParameters();
            // 4. 将?替换回原始变量名（#{variable}）
            for (ParameterMapping mapping : parameterMappings) {
                String property = mapping.getProperty();
                Object val = additionalParameters.get(property);
                if (val == null) {
                    Map<String, Object> map = (Map<String, Object>) additionalParameters.get("_parameter");
                    val = map.get(property);
                }
                String valueStr = val.toString().replace("'", "''");
                parsedSql = parsedSql.replaceFirst("\\?", "" + valueStr + "");
            }
            return parsedSql;
        } catch (Exception e) {
            log.error("解析动态SQL失败", e);
            throw new RuntimeException("解析动态SQL失败", e);
        }
    }
}
