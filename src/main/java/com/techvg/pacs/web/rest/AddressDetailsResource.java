package com.techvg.pacs.web.rest;

import com.techvg.pacs.repository.AddressDetailsRepository;
import com.techvg.pacs.service.AddressDetailsQueryService;
import com.techvg.pacs.service.AddressDetailsService;
import com.techvg.pacs.service.criteria.AddressDetailsCriteria;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.techvg.pacs.domain.AddressDetails}.
 */
@RestController
@RequestMapping("/api")
public class AddressDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AddressDetailsResource.class);

    private static final String ENTITY_NAME = "addressDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddressDetailsService addressDetailsService;

    private final AddressDetailsRepository addressDetailsRepository;

    private final AddressDetailsQueryService addressDetailsQueryService;

    public AddressDetailsResource(
        AddressDetailsService addressDetailsService,
        AddressDetailsRepository addressDetailsRepository,
        AddressDetailsQueryService addressDetailsQueryService
    ) {
        this.addressDetailsService = addressDetailsService;
        this.addressDetailsRepository = addressDetailsRepository;
        this.addressDetailsQueryService = addressDetailsQueryService;
    }

    /**
     * {@code POST  /address-details} : Create a new addressDetails.
     *
     * @param addressDetailsDTO the addressDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addressDetailsDTO, or with status {@code 400 (Bad Request)} if the addressDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/address-details")
    public ResponseEntity<AddressDetailsDTO> createAddressDetails(@RequestBody AddressDetailsDTO addressDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save AddressDetails : {}", addressDetailsDTO);
        if (addressDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new addressDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressDetailsDTO result = addressDetailsService.save(addressDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/address-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /address-details/:id} : Updates an existing addressDetails.
     *
     * @param id the id of the addressDetailsDTO to save.
     * @param addressDetailsDTO the addressDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the addressDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addressDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/address-details/{id}")
    public ResponseEntity<AddressDetailsDTO> updateAddressDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressDetailsDTO addressDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AddressDetails : {}, {}", id, addressDetailsDTO);
        if (addressDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AddressDetailsDTO result = addressDetailsService.update(addressDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /address-details/:id} : Partial updates given fields of an existing addressDetails, field will ignore if it is null
     *
     * @param id the id of the addressDetailsDTO to save.
     * @param addressDetailsDTO the addressDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the addressDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the addressDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the addressDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/address-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AddressDetailsDTO> partialUpdateAddressDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressDetailsDTO addressDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AddressDetails partially : {}, {}", id, addressDetailsDTO);
        if (addressDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AddressDetailsDTO> result = addressDetailsService.partialUpdate(addressDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /address-details} : get all the addressDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addressDetails in body.
     */
    @GetMapping("/address-details")
    public ResponseEntity<List<AddressDetailsDTO>> getAllAddressDetails(
        AddressDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AddressDetails by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<AddressDetailsDTO> page = addressDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /address-details/count} : count all the addressDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/address-details/count")
    public ResponseEntity<Long> countAddressDetails(AddressDetailsCriteria criteria) {
        log.debug("REST request to count AddressDetails by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(addressDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /address-details/:id} : get the "id" addressDetails.
     *
     * @param id the id of the addressDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/address-details/{id}")
    public ResponseEntity<AddressDetailsDTO> getAddressDetails(@PathVariable Long id) {
        log.debug("REST request to get AddressDetails : {}", id);
        Optional<AddressDetailsDTO> addressDetailsDTO = addressDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addressDetailsDTO);
    }

    /**
     * {@code DELETE  /address-details/:id} : delete the "id" addressDetails.
     *
     * @param id the id of the addressDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/address-details/{id}")
    public ResponseEntity<Void> deleteAddressDetails(@PathVariable Long id) {
        log.debug("REST request to delete AddressDetails : {}", id);
        addressDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
