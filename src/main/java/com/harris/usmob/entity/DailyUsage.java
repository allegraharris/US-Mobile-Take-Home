package com.harris.usmob.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * Entity for Daily Usage
 */
@Data
@Document(collection = "daily_usage")
@CompoundIndexes({
        @CompoundIndex(name = "userIdMdnIndex", def = "{'userId': 1, 'mdn': 1}"), // compound index as frequent queries on userId and mdn
        @CompoundIndex(name = "mdnUsageDateIndex", def = "{'mdn': 1, 'usageDate': 1}")} // compound index for quicker writes for updateUsedInMb
)

public class DailyUsage {
    /**
     * Daily Usage ID - Primary Key
     */
    @MongoId
    private String id;
    /**
     * MDN (Phone number)
     */
    private String mdn;
    /**
     * Usage Date
     */
    private Date usageDate;
    /**
     * Usage in Mb
     */
    private Integer usedInMb;
    /**
     * User ID - Foreign Key
     */
    private String userId;
}
