package com.harris.usmob.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * Entity for Cycle
 */

@Data
@AllArgsConstructor
@Document(collection = "cycle")
@CompoundIndex(name = "userIdMdnIndex", def = "{'userId': 1, 'mdn': 1}") // compound index as frequent queries on userId and mdn
public class Cycle {
    /**
     * Cycle ID - Primary Key
     */
    @MongoId
    private String id;
    /**
     * MDN (Phone number)
     */
    private String mdn;
    /**
     * Start Date of billing cycle
     */
    private Date startDate;
    /**
     * End Date of billing cycle
     */
    private Date endDate;
    /**
     * User ID
     */
    private String userId;
}
