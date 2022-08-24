package com.techvg.pacs.service;

import com.techvg.pacs.domain.*; // for static metamodels
import com.techvg.pacs.domain.ParameterLookup;
import com.techvg.pacs.repository.ParameterLookupRepository;
import com.techvg.pacs.service.criteria.ParameterLookupCriteria;
import com.techvg.pacs.service.dto.ParameterLookupDTO;
import com.techvg.pacs.service.mapper.ParameterLookupMapper;
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
 * Service for executing complex queries for {@link ParameterLookup} entities in the database.
 * The main input is a {@link ParameterLookupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParameterLookupDTO} or a {@link Page} of {@link ParameterLookupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParameterLookupQueryService extends QueryService<ParameterLookup> {

    private final Logger log = LoggerFactory.getLogger(ParameterLookupQueryService.class);

    private final ParameterLookupRepository parameterLookupRepository;

    private final ParameterLookupMapper parameterLookupMapper;

    public ParameterLookupQueryService(ParameterLookupRepository parameterLookupRepository, ParameterLookupMapper parameterLookupMapper) {
        this.parameterLookupRepository = parameterLookupRepository;
        this.parameterLookupMapper = parameterLookupMapper;
    }

    /**
     * Return a {@link List} of {@link ParameterLookupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterLookupDTO> findByCriteria(ParameterLookupCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<ParameterLookup> specification = createSpecification(criteria);
        return parameterLookupMapper.toDto(parameterLookupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParameterLookupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParameterLookupDTO> findByCriteria(ParameterLookupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<ParameterLookup> specification = createSpecification(criteria);
        return parameterLookupRepository.findAll(specification, page).map(parameterLookupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParameterLookupCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<ParameterLookup> specification = createSpecification(criteria);
        return parameterLookupRepository.count(specification);
    }

    /**
     * Function to convert {@link ParameterLookupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParameterLookup> createSpecification(ParameterLookupCriteria criteria) {
        Specification<ParameterLookup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParameterLookup_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ParameterLookup_.type));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ParameterLookup_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ParameterLookup_.description));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), ParameterLookup_.value));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), ParameterLookup_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ParameterLookup_.lastModifiedBy));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ParameterLookup_.createdBy));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), ParameterLookup_.createdOn));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), ParameterLookup_.isDeleted));
            }
            if (criteria.getFreeField1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFreeField1(), ParameterLookup_.freeField1));
            }
            if (criteria.getFreeField2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFreeField2(), ParameterLookup_.freeField2));
            }
            if (criteria.getSocietyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSocietyId(),
                            root -> root.join(ParameterLookup_.society, JoinType.LEFT).get(Society_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
