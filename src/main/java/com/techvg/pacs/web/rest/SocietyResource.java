package com.techvg.pacs.web.rest;

import com.techvg.pacs.repository.SocietyRepository;
import com.techvg.pacs.service.SocietyQueryService;
import com.techvg.pacs.service.SocietyService;
import com.techvg.pacs.service.criteria.SocietyCriteria;
import com.techvg.pacs.service.dto.SocietyDTO;
import com.techvg.pacs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.techvg.pacs.domain.Society}.
 */
@RestController
@RequestMapping("/api")
public class SocietyResource {

    private final Logger log = LoggerFactory.getLogger(SocietyResource.class);

    private static final String ENTITY_NAME = "society";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocietyService societyService;

    private final SocietyRepository societyRepository;

    private final SocietyQueryService societyQueryService;

    public SocietyResource(SocietyService societyService, SocietyRepository societyRepository, SocietyQueryService societyQueryService) {
        this.societyService = societyService;
        this.societyRepository = societyRepository;
        this.societyQueryService = societyQueryService;
    }

    /**
     * {@code POST  /societies} : Create a new society.
     *
     * @param societyDTO the societyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new societyDTO, or with status {@code 400 (Bad Request)} if the society has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/societies")
    public ResponseEntity<SocietyDTO> createSociety(@Valid @RequestBody SocietyDTO societyDTO) throws URISyntaxException {
        log.debug("REST request to save Society : {}", societyDTO);
        if (societyDTO.getId() != null) {
            throw new BadRequestAlertException("A new society cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocietyDTO result = societyService.save(societyDTO);
        return ResponseEntity
            .created(new URI("/api/societies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /societies/:id} : Updates an existing society.
     *
     * @param id the id of the societyDTO to save.
     * @param societyDTO the societyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated societyDTO,
     * or with status {@code 400 (Bad Request)} if the societyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the societyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/societies/{id}")
    public ResponseEntity<SocietyDTO> updateSociety(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocietyDTO societyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Society : {}, {}", id, societyDTO);
        if (societyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, societyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!societyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocietyDTO result = societyService.update(societyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, societyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /societies/:id} : Partial updates given fields of an existing society, field will ignore if it is null
     *
     * @param id the id of the societyDTO to save.
     * @param societyDTO the societyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated societyDTO,
     * or with status {@code 400 (Bad Request)} if the societyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the societyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the societyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/societies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocietyDTO> partialUpdateSociety(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocietyDTO societyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Society partially : {}, {}", id, societyDTO);
        if (societyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, societyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!societyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocietyDTO> result = societyService.partialUpdate(societyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, societyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /societies} : get all the societies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of societies in body.
     */
    @GetMapping("/societies")
    public ResponseEntity<List<SocietyDTO>> getAllSocieties(
        SocietyCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Societies by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<SocietyDTO> page = societyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /societies/count} : count all the societies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/societies/count")
    public ResponseEntity<Long> countSocieties(SocietyCriteria criteria) {
        log.debug("REST request to count Societies by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(societyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /societies/:id} : get the "id" society.
     *
     * @param id the id of the societyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the societyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/societies/{id}")
    public ResponseEntity<SocietyDTO> getSociety(@PathVariable Long id) {
        log.debug("REST request to get Society : {}", id);
        Optional<SocietyDTO> societyDTO = societyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(societyDTO);
    }

    /**
     * {@code DELETE  /societies/:id} : delete the "id" society.
     *
     * @param id the id of the societyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/societies/{id}")
    public ResponseEntity<Void> deleteSociety(@PathVariable Long id) {
        log.debug("REST request to delete Society : {}", id);
        societyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
