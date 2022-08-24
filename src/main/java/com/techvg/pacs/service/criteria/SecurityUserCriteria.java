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
 * Criteria class for the {@link com.techvg.pacs.domain.SecurityUser} entity. This class is used
 * in {@link com.techvg.pacs.web.rest.SecurityUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /security-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SecurityUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter designation;

    private StringFilter username;

    private StringFilter passwordHash;

    private StringFilter email;

    private StringFilter description;

    private StringFilter department;

    private StringFilter imageUrl;

    private BooleanFilter activated;

    private StringFilter langKey;

    private StringFilter activationKey;

    private StringFilter resetKey;

    private InstantFilter resetDate;

    private StringFilter mobileNo;

    private StringFilter createdBy;

    private InstantFilter createdOn;

    private LongFilter societyId;

    private LongFilter securityPermissionId;

    private LongFilter securityRoleId;

    private Boolean distinct;

    public SecurityUserCriteria() {}

    public SecurityUserCriteria(SecurityUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.passwordHash = other.passwordHash == null ? null : other.passwordHash.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.department = other.department == null ? null : other.department.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.activationKey = other.activationKey == null ? null : other.activationKey.copy();
        this.resetKey = other.resetKey == null ? null : other.resetKey.copy();
        this.resetDate = other.resetDate == null ? null : other.resetDate.copy();
        this.mobileNo = other.mobileNo == null ? null : other.mobileNo.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.societyId = other.societyId == null ? null : other.societyId.copy();
        this.securityPermissionId = other.securityPermissionId == null ? null : other.securityPermissionId.copy();
        this.securityRoleId = other.securityRoleId == null ? null : other.securityRoleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SecurityUserCriteria copy() {
        return new SecurityUserCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public StringFilter designation() {
        if (designation == null) {
            designation = new StringFilter();
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPasswordHash() {
        return passwordHash;
    }

    public StringFilter passwordHash() {
        if (passwordHash == null) {
            passwordHash = new StringFilter();
        }
        return passwordHash;
    }

    public void setPasswordHash(StringFilter passwordHash) {
        this.passwordHash = passwordHash;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public StringFilter getDepartment() {
        return department;
    }

    public StringFilter department() {
        if (department == null) {
            department = new StringFilter();
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getLangKey() {
        return langKey;
    }

    public StringFilter langKey() {
        if (langKey == null) {
            langKey = new StringFilter();
        }
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public StringFilter getActivationKey() {
        return activationKey;
    }

    public StringFilter activationKey() {
        if (activationKey == null) {
            activationKey = new StringFilter();
        }
        return activationKey;
    }

    public void setActivationKey(StringFilter activationKey) {
        this.activationKey = activationKey;
    }

    public StringFilter getResetKey() {
        return resetKey;
    }

    public StringFilter resetKey() {
        if (resetKey == null) {
            resetKey = new StringFilter();
        }
        return resetKey;
    }

    public void setResetKey(StringFilter resetKey) {
        this.resetKey = resetKey;
    }

    public InstantFilter getResetDate() {
        return resetDate;
    }

    public InstantFilter resetDate() {
        if (resetDate == null) {
            resetDate = new InstantFilter();
        }
        return resetDate;
    }

    public void setResetDate(InstantFilter resetDate) {
        this.resetDate = resetDate;
    }

    public StringFilter getMobileNo() {
        return mobileNo;
    }

    public StringFilter mobileNo() {
        if (mobileNo == null) {
            mobileNo = new StringFilter();
        }
        return mobileNo;
    }

    public void setMobileNo(StringFilter mobileNo) {
        this.mobileNo = mobileNo;
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

    public LongFilter getSecurityPermissionId() {
        return securityPermissionId;
    }

    public LongFilter securityPermissionId() {
        if (securityPermissionId == null) {
            securityPermissionId = new LongFilter();
        }
        return securityPermissionId;
    }

    public void setSecurityPermissionId(LongFilter securityPermissionId) {
        this.securityPermissionId = securityPermissionId;
    }

    public LongFilter getSecurityRoleId() {
        return securityRoleId;
    }

    public LongFilter securityRoleId() {
        if (securityRoleId == null) {
            securityRoleId = new LongFilter();
        }
        return securityRoleId;
    }

    public void setSecurityRoleId(LongFilter securityRoleId) {
        this.securityRoleId = securityRoleId;
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
        final SecurityUserCriteria that = (SecurityUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(username, that.username) &&
            Objects.equals(passwordHash, that.passwordHash) &&
            Objects.equals(email, that.email) &&
            Objects.equals(description, that.description) &&
            Objects.equals(department, that.department) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(langKey, that.langKey) &&
            Objects.equals(activationKey, that.activationKey) &&
            Objects.equals(resetKey, that.resetKey) &&
            Objects.equals(resetDate, that.resetDate) &&
            Objects.equals(mobileNo, that.mobileNo) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(societyId, that.societyId) &&
            Objects.equals(securityPermissionId, that.securityPermissionId) &&
            Objects.equals(securityRoleId, that.securityRoleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            designation,
            username,
            passwordHash,
            email,
            description,
            department,
            imageUrl,
            activated,
            langKey,
            activationKey,
            resetKey,
            resetDate,
            mobileNo,
            createdBy,
            createdOn,
            societyId,
            securityPermissionId,
            securityRoleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (designation != null ? "designation=" + designation + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (passwordHash != null ? "passwordHash=" + passwordHash + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (department != null ? "department=" + department + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (langKey != null ? "langKey=" + langKey + ", " : "") +
            (activationKey != null ? "activationKey=" + activationKey + ", " : "") +
            (resetKey != null ? "resetKey=" + resetKey + ", " : "") +
            (resetDate != null ? "resetDate=" + resetDate + ", " : "") +
            (mobileNo != null ? "mobileNo=" + mobileNo + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
            (societyId != null ? "societyId=" + societyId + ", " : "") +
            (securityPermissionId != null ? "securityPermissionId=" + securityPermissionId + ", " : "") +
            (securityRoleId != null ? "securityRoleId=" + securityRoleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
