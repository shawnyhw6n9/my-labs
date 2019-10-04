/* 
 * Customer.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package iisigroup.trainging.app.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <pre>
 * Customer Model
 * </pre>
 * 
 * @since Jun 10, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jun 10, 2019,Sunkist Wang,new
 *          </ul>
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    @PrePersist
    public void perPersist() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.setId(uuid);
    }

    protected Customer() {
    }

    /**
     * a customer
     * 
     * @param firstName
     *            String
     * @param lastName
     *            String
     */
    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s, firstName='%s', lastName='%s']", id, firstName, lastName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
