package com.techvg.pacs.service;

import com.techvg.pacs.domain.*; // for static metamodels
import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.repository.AddressDetailsRepository;
import com.techvg.pacs.service.criteria.AddressDetailsCriteria;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.service.mapper.AddressDetailsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AddressDetails} entities in the database.
 * The main input is a {@link AddressDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddressDetailsDTO} or a {@link Page} of {@link AddressDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressDetailsQueryService extends QueryService<AddressDetails> {

    private final Logger log = LoggerFactory.getLogger(AddressDetailsQueryService.class);

    private final AddressDetailsRepository addressDetailsRepository;

    private final AddressDetailsMapper addressDetailsMapper;

    public AddressDetailsQueryService(AddressDetailsRepository addressDetailsRepository, AddressDetailsMapper addressDetailsMapper) {
        this.addressDetailsRepository = addressDetailsRepository;
        this.addressDetailsMapper = addressDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link AddressDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDetailsDTO> findByCriteria(AddressDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<AddressDetails> specification = createSpecification(criteria);
        return addressDetailsMapper.toDto(addressDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddressDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDetailsDTO> findByCriteria(AddressDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<AddressDetails> specification = createSpecification(criteria);
        return addressDetailsRepository.findAll(specification, page).map(addressDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddressDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<AddressDetails> specification = createSpecification(criteria);
        return addressDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link AddressDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AddressDetails> createSpecification(AddressDetailsCriteria criteria) {
        Specification<AddressDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AddressDetails_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AddressDetails_.type));
            }
            if (criteria.getHouseNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHouseNo(), AddressDetails_.houseNo));
            }
            if (criteria.getRoadName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoadName(), AddressDetails_.roadName));
            }
            if (criteria.getLandMark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLandMark(), AddressDetails_.landMark));
            }
            if (criteria.getPincode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPincode(), AddressDetails_.pincode));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), AddressDetails_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), AddressDetails_.lastModifiedBy));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), AddressDetails_.createdBy));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), AddressDetails_.createdOn));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), AddressDetails_.isDeleted));
            }
            if (criteria.getFreeField1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFreeField1(), AddressDetails_.freeField1));
            }
            if (criteria.getFreeField2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFreeField2(), AddressDetails_.freeField2));
            }
            if (criteria.getFreeField3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFreeField3(), AddressDetails_.freeField3));
            }
            if (criteria.getStateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStateId(), root -> root.join(AddressDetails_.state, JoinType.LEFT).get(State_.id))
                    );
            }
            if (criteria.getDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDistrictId(),
                            root -> root.join(AddressDetails_.district, JoinType.LEFT).get(District_.id)
                        )
                    );
            }
            if (criteria.getTalukaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTalukaId(), root -> root.join(AddressDetails_.taluka, JoinType.LEFT).get(Taluka_.id))
                    );
            }
            if (criteria.getTalukaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTalukaId(),
                            root -> root.join(AddressDetails_.taluka, JoinType.LEFT).get(Village_.id)
                        )
                    );
            }
            if (criteria.getSecurityUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityUserId(),
                            root -> root.join(AddressDetails_.securityUser, JoinType.LEFT).get(SecurityUser_.id)
                        )
                    );
            }
            if (criteria.getMemberId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMemberId(), root -> root.join(AddressDetails_.member, JoinType.LEFT).get(Member_.id))
                    );
            }
        }
        return specification;
    }
}
