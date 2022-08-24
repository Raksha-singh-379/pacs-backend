package com.techvg.pacs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.techvg.pacs.domain.Society} entity. This class is used
 * in {@link com.techvg.pacs.web.rest.SocietyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /societies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SocietyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter societyName;

    private DoubleFilter registrationNumber;

    private DoubleFilter gstinNumber;

    private DoubleFilter panNumber;

    private DoubleFilter tanNumber;

    private DoubleFilter phoneNumber;

    private StringFilter emailAddress;

    private InstantFilter createdOn;

    private StringFilter createdBy;

    private StringFilter description;

    private BooleanFilter isActivate;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private StringFilter freeField1;

    private StringFilter freeField2;

    private StringFilter freeField3;

    private StringFilter freeField4;

    private LongFilter addressDetailsId;

    private LongFilter societyId;

    private Boolean distinct;

    public SocietyCriteria() {}

    public SocietyCriteria(SocietyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.societyName = other.societyName == null ? null : other.societyName.copy();
        this.registrationNumber = other.registrationNumber == null ? null : other.registrationNumber.copy();
        this.gstinNumber = other.gstinNumber == null ? null : other.gstinNumber.copy();
        this.panNumber = other.panNumber == null ? null : other.panNumber.copy();
        this.tanNumber = other.tanNumber == null ? null : other.tanNumber.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isActivate = other.isActivate == null ? null : other.isActivate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.freeField1 = other.freeField1 == null ? null : other.freeField1.copy();
        this.freeField2 = other.freeField2 == null ? null : other.freeField2.copy();
        this.freeField3 = other.freeField3 == null ? null : other.freeField3.copy();
        this.freeField4 = other.freeField4 == null ? null : other.freeField4.copy();
        this.addressDetailsId = other.addressDetailsId == null ? null : other.addressDetailsId.copy();
        this.societyId = other.societyId == null ? null : other.societyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SocietyCriteria copy() {
        return new SocietyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSocietyName() {
        return societyName;
    }

    public StringFilter societyName() {
        if (societyName == null) {
            societyName = new StringFilter();
        }
        return societyName;
    }

    public void setSocietyName(StringFilter societyName) {
        this.societyName = societyName;
    }

    public DoubleFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public DoubleFilter registrationNumber() {
        if (registrationNumber == null) {
            registrationNumber = new DoubleFilter();
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(DoubleFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public DoubleFilter getGstinNumber() {
        return gstinNumber;
    }

    public DoubleFilter gstinNumber() {
        if (gstinNumber == null) {
            gstinNumber = new DoubleFilter();
        }
        return gstinNumber;
    }

    public void setGstinNumber(DoubleFilter gstinNumber) {
        this.gstinNumber = gstinNumber;
    }

    public DoubleFilter getPanNumber() {
        return panNumber;
    }

    public DoubleFilter panNumber() {
        if (panNumber == null) {
            panNumber = new DoubleFilter();
        }
        return panNumber;
    }

    public void setPanNumber(DoubleFilter panNumber) {
        this.panNumber = panNumber;
    }

    public DoubleFilter getTanNumber() {
        return tanNumber;
    }

    public DoubleFilter tanNumber() {
        if (tanNumber == null) {
            tanNumber = new DoubleFilter();
        }
        return tanNumber;
    }

    public void setTanNumber(DoubleFilter tanNumber) {
        this.tanNumber = tanNumber;
    }

    public DoubleFilter getPhoneNumber() {
        return phoneNumber;
    }

    public DoubleFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new DoubleFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(DoubleFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            emailAddress = new StringFilter();
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public InstantFilter createdOn() {
        if (createdOn == null) {
            createdOn = new InstantFilter();
        }
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getIsActivate() {
        return isActivate;
    }

    public BooleanFilter isActivate() {
        if (isActivate == null) {
            isActivate = new BooleanFilter();
        }
        return isActivate;
    }

    public void setIsActivate(BooleanFilter isActivate) {
        this.isActivate = isActivate;
    }

    public InstantFilter getLastModified() {
        return lastModified;
    }

    public InstantFilter lastModified() {
        if (lastModified == null) {
            lastModified = new InstantFilter();
        }
        return lastModified;
    }

    public void setLastModified(InstantFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public StringFilter getFreeField1() {
        return freeField1;
    }

    public StringFilter freeField1() {
        if (freeField1 == null) {
            freeField1 = new StringFilter();
        }
        return freeField1;
    }

    public void setFreeField1(StringFilter freeField1) {
        this.freeField1 = freeField1;
    }

    public StringFilter getFreeField2() {
        return freeField2;
    }

    public StringFilter freeField2() {
        if (freeField2 == null) {
            freeField2 = new StringFilter();
        }
        return freeField2;
    }

    public void setFreeField2(StringFilter freeField2) {
        this.freeField2 = freeField2;
    }

    public StringFilter getFreeField3() {
        return freeField3;
    }

    public StringFilter freeField3() {
        if (freeField3 == null) {
            freeField3 = new StringFilter();
        }
        return freeField3;
    }

    public void setFreeField3(StringFilter freeField3) {
        this.freeField3 = freeField3;
    }

    public StringFilter getFreeField4() {
        return freeField4;
    }

    public StringFilter freeField4() {
        if (freeField4 == null) {
            freeField4 = new StringFilter();
        }
        return freeField4;
    }

    public void setFreeField4(StringFilter freeField4) {
        this.freeField4 = freeField4;
    }

    public LongFilter getAddressDetailsId() {
        return addressDetailsId;
    }

    public LongFilter addressDetailsId() {
        if (addressDetailsId == null) {
            addressDetailsId = new LongFilter();
        }
        return addressDetailsId;
    }

    public void setAddressDetailsId(LongFilter addressDetailsId) {
        this.addressDetailsId = addressDetailsId;
    }

    public LongFilter getSocietyId() {
        return societyId;
    }

    public LongFilter societyId() {
        if (societyId == null) {
            societyId = new LongFilter();
        }
        return societyId;
    }

    public void setSocietyId(LongFilter societyId) {
        this.societyId = societyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SocietyCriteria that = (SocietyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(societyName, that.societyName) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(gstinNumber, that.gstinNumber) &&
            Objects.equals(panNumber, that.panNumber) &&
            Objects.equals(tanNumber, that.tanNumber) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isActivate, that.isActivate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(freeField1, that.freeField1) &&
            Objects.equals(freeField2, that.freeField2) &&
            Objects.equals(freeField3, that.freeField3) &&
            Objects.equals(freeField4, that.freeField4) &&
            Objects.equals(addressDetailsId, that.addressDetailsId) &&
            Objects.equals(societyId, that.societyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            societyName,
            registrationNumber,
            gstinNumber,
            panNumber,
            tanNumber,
            phoneNumber,
            emailAddress,
            createdOn,
            createdBy,
            description,
            isActivate,
            lastModified,
            lastModifiedBy,
            freeField1,
            freeField2,
            freeField3,
            freeField4,
            addressDetailsId,
            societyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocietyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (societyName != null ? "societyName=" + societyName + ", " : "") +
            (registrationNumber != null ? "registrationNumber=" + registrationNumber + ", " : "") +
            (gstinNumber != null ? "gstinNumber=" + gstinNumber + ", " : "") +
            (panNumber != null ? "panNumber=" + panNumber + ", " : "") +
            (tanNumber != null ? "tanNumber=" + tanNumber + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isActivate != null ? "isActivate=" + isActivate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (freeField1 != null ? "freeField1=" + freeField1 + ", " : "") +
            (freeField2 != null ? "freeField2=" + freeField2 + ", " : "") +
            (freeField3 != null ? "freeField3=" + freeField3 + ", " : "") +
            (freeField4 != null ? "freeField4=" + freeField4 + ", " : "") +
            (addressDetailsId != null ? "addressDetailsId=" + addressDetailsId + ", " : "") +
            (societyId != null ? "societyId=" + societyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
