package com.techvg.pacs.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.techvg.pacs.domain.Society} entity.
 */
public class SocietyDTO implements Serializable {

    private Long id;

    @NotNull
    private String societyName;

    private Double registrationNumber;

    private Double gstinNumber;

    private Double panNumber;

    private Double tanNumber;

    private Double phoneNumber;

    private String emailAddress;

    private Instant createdOn;

    private String createdBy;

    private String description;

    private Boolean isActivate;

    private Instant lastModified;

    private String lastModifiedBy;

    private String freeField1;

    private String freeField2;

    private String freeField3;

    private String freeField4;

    private AddressDetailsDTO addressDetails;

    private SocietyDTO society;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public Double getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(Double registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Double getGstinNumber() {
        return gstinNumber;
    }

    public void setGstinNumber(Double gstinNumber) {
        this.gstinNumber = gstinNumber;
    }

    public Double getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(Double panNumber) {
        this.panNumber = panNumber;
    }

    public Double getTanNumber() {
        return tanNumber;
    }

    public void setTanNumber(Double tanNumber) {
        this.tanNumber = tanNumber;
    }

    public Double getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Boolean isActivate) {
        this.isActivate = isActivate;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFreeField1() {
        return freeField1;
    }

    public void setFreeField1(String freeField1) {
        this.freeField1 = freeField1;
    }

    public String getFreeField2() {
        return freeField2;
    }

    public void setFreeField2(String freeField2) {
        this.freeField2 = freeField2;
    }

    public String getFreeField3() {
        return freeField3;
    }

    public void setFreeField3(String freeField3) {
        this.freeField3 = freeField3;
    }

    public String getFreeField4() {
        return freeField4;
    }

    public void setFreeField4(String freeField4) {
        this.freeField4 = freeField4;
    }

    public AddressDetailsDTO getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetailsDTO addressDetails) {
        this.addressDetails = addressDetails;
    }

    public SocietyDTO getSociety() {
        return society;
    }

    public void setSociety(SocietyDTO society) {
        this.society = society;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocietyDTO)) {
            return false;
        }

        SocietyDTO societyDTO = (SocietyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, societyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocietyDTO{" +
            "id=" + getId() +
            ", societyName='" + getSocietyName() + "'" +
            ", registrationNumber=" + getRegistrationNumber() +
            ", gstinNumber=" + getGstinNumber() +
            ", panNumber=" + getPanNumber() +
            ", tanNumber=" + getTanNumber() +
            ", phoneNumber=" + getPhoneNumber() +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActivate='" + getIsActivate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", freeField1='" + getFreeField1() + "'" +
            ", freeField2='" + getFreeField2() + "'" +
            ", freeField3='" + getFreeField3() + "'" +
            ", freeField4='" + getFreeField4() + "'" +
            ", addressDetails=" + getAddressDetails() +
            ", society=" + getSociety() +
            "}";
    }
}
