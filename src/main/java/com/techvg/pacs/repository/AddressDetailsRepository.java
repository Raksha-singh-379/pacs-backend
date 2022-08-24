package com.techvg.pacs.repository;

import com.techvg.pacs.domain.AddressDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AddressDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressDetailsRepository extends JpaRepository<AddressDetails, Long>, JpaSpecificationExecutor<AddressDetails> {}
