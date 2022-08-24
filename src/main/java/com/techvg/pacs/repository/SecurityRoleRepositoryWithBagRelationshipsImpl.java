package com.techvg.pacs.repository;

import com.techvg.pacs.domain.SecurityRole;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SecurityRoleRepositoryWithBagRelationshipsImpl implements SecurityRoleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SecurityRole> fetchBagRelationships(Optional<SecurityRole> securityRole) {
        return securityRole.map(this::fetchSecurityPermissions);
    }

    @Override
    public Page<SecurityRole> fetchBagRelationships(Page<SecurityRole> securityRoles) {
        return new PageImpl<>(
            fetchBagRelationships(securityRoles.getContent()),
            securityRoles.getPageable(),
            securityRoles.getTotalElements()
        );
    }

    @Override
    public List<SecurityRole> fetchBagRelationships(List<SecurityRole> securityRoles) {
        return Optional.of(securityRoles).map(this::fetchSecurityPermissions).orElse(Collections.emptyList());
    }

    SecurityRole fetchSecurityPermissions(SecurityRole result) {
        return entityManager
            .createQuery(
                "select securityRole from SecurityRole securityRole left join fetch securityRole.securityPermissions where securityRole is :securityRole",
                SecurityRole.class
            )
            .setParameter("securityRole", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<SecurityRole> fetchSecurityPermissions(List<SecurityRole> securityRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, securityRoles.size()).forEach(index -> order.put(securityRoles.get(index).getId(), index));
        List<SecurityRole> result = entityManager
            .createQuery(
                "select distinct securityRole from SecurityRole securityRole left join fetch securityRole.securityPermissions where securityRole in :securityRoles",
                SecurityRole.class
            )
            .setParameter("securityRoles", securityRoles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
