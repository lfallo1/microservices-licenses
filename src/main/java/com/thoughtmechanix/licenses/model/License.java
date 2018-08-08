package com.thoughtmechanix.licenses.model;

import javax.persistence.*;

@Entity
@Table(name = "license", schema = "public")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "license_id")
    private String id;
    private String organizationId;
    private String productName;
    private String licenseType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public License withId(String id) {
        this.setId(id);
        return this;
    }

    public License withOrganizationId(String organizationId) {
        this.setOrganizationId(organizationId);
        return this;
    }

    public License withProductName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public License withLicenseType(String licenseType) {
        this.setLicenseType(licenseType);
        return this;
    }


}
