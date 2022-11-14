package com.xjhqre.common.domain.search;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

/**
 * ESBlogIndex
 */
@Data
@Document(indexName = "xjhqrebbs-article")
public class ESArticleIndex {
    @Id
    @Field(type = FieldType.Keyword)
    private Long articleId;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String summary;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String content;
}
