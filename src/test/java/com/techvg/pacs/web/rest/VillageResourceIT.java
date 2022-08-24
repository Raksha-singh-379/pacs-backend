package com.techvg.pacs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.pacs.IntegrationTest;
import com.techvg.pacs.domain.Village;
import com.techvg.pacs.repository.VillageRepository;
import com.techvg.pacs.service.criteria.VillageCriteria;
import com.techvg.pacs.service.dto.VillageDTO;
import com.techvg.pacs.service.mapper.VillageMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VillageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VillageResourceIT {

    private static final String DEFAULT_VILLAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Long DEFAULT_LGD_CODE = 1L;
    private static final Long UPDATED_LGD_CODE = 2L;
    private static final Long SMALLER_LGD_CODE = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/villages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VillageRepository villageRepository;

    @Autowired
    private VillageMapper villageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVillageMockMvc;

    private Village village;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createEntity(EntityManager em) {
        Village village = new Village()
            .villageName(DEFAULT_VILLAGE_NAME)
            .deleted(DEFAULT_DELETED)
            .lgdCode(DEFAULT_LGD_CODE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return village;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createUpdatedEntity(EntityManager em) {
        Village village = new Village()
            .villageName(UPDATED_VILLAGE_NAME)
            .deleted(UPDATED_DELETED)
            .lgdCode(UPDATED_LGD_CODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return village;
    }

    @BeforeEach
    public void initTest() {
        village = createEntity(em);
    }

    @Test
    @Transactional
    void createVillage() throws Exception {
        int databaseSizeBeforeCreate = villageRepository.findAll().size();
        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isCreated());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate + 1);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageName()).isEqualTo(DEFAULT_VILLAGE_NAME);
        assertThat(testVillage.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testVillage.getLgdCode()).isEqualTo(DEFAULT_LGD_CODE);
        assertThat(testVillage.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testVillage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createVillageWithExistingId() throws Exception {
        // Create the Village with an existing ID
        village.setId(1L);
        VillageDTO villageDTO = villageMapper.toDto(village);

        int databaseSizeBeforeCreate = villageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVillageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = villageRepository.findAll().size();
        // set the field null
        village.setVillageName(null);

        // Create the Village, which fails.
        VillageDTO villageDTO = villageMapper.toDto(village);

        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVillages() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageName").value(hasItem(DEFAULT_VILLAGE_NAME)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].lgdCode").value(hasItem(DEFAULT_LGD_CODE.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get the village
        restVillageMockMvc
            .perform(get(ENTITY_API_URL_ID, village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(village.getId().intValue()))
            .andExpect(jsonPath("$.villageName").value(DEFAULT_VILLAGE_NAME))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.lgdCode").value(DEFAULT_LGD_CODE.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getVillagesByIdFiltering() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        Long id = village.getId();

        defaultVillageShouldBeFound("id.equals=" + id);
        defaultVillageShouldNotBeFound("id.notEquals=" + id);

        defaultVillageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.greaterThan=" + id);

        defaultVillageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName equals to DEFAULT_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.equals=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName equals to UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.equals=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName in DEFAULT_VILLAGE_NAME or UPDATED_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.in=" + DEFAULT_VILLAGE_NAME + "," + UPDATED_VILLAGE_NAME);

        // Get all the villageList where villageName equals to UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.in=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName is not null
        defaultVillageShouldBeFound("villageName.specified=true");

        // Get all the villageList where villageName is null
        defaultVillageShouldNotBeFound("villageName.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName contains DEFAULT_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.contains=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName contains UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.contains=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName does not contain DEFAULT_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.doesNotContain=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName does not contain UPDATED_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.doesNotContain=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where deleted equals to DEFAULT_DELETED
        defaultVillageShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the villageList where deleted equals to UPDATED_DELETED
        defaultVillageShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllVillagesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultVillageShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the villageList where deleted equals to UPDATED_DELETED
        defaultVillageShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllVillagesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where deleted is not null
        defaultVillageShouldBeFound("deleted.specified=true");

        // Get all the villageList where deleted is null
        defaultVillageShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode equals to DEFAULT_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.equals=" + DEFAULT_LGD_CODE);

        // Get all the villageList where lgdCode equals to UPDATED_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.equals=" + UPDATED_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode in DEFAULT_LGD_CODE or UPDATED_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.in=" + DEFAULT_LGD_CODE + "," + UPDATED_LGD_CODE);

        // Get all the villageList where lgdCode equals to UPDATED_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.in=" + UPDATED_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode is not null
        defaultVillageShouldBeFound("lgdCode.specified=true");

        // Get all the villageList where lgdCode is null
        defaultVillageShouldNotBeFound("lgdCode.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode is greater than or equal to DEFAULT_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.greaterThanOrEqual=" + DEFAULT_LGD_CODE);

        // Get all the villageList where lgdCode is greater than or equal to UPDATED_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.greaterThanOrEqual=" + UPDATED_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode is less than or equal to DEFAULT_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.lessThanOrEqual=" + DEFAULT_LGD_CODE);

        // Get all the villageList where lgdCode is less than or equal to SMALLER_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.lessThanOrEqual=" + SMALLER_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode is less than DEFAULT_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.lessThan=" + DEFAULT_LGD_CODE);

        // Get all the villageList where lgdCode is less than UPDATED_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.lessThan=" + UPDATED_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLgdCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lgdCode is greater than DEFAULT_LGD_CODE
        defaultVillageShouldNotBeFound("lgdCode.greaterThan=" + DEFAULT_LGD_CODE);

        // Get all the villageList where lgdCode is greater than SMALLER_LGD_CODE
        defaultVillageShouldBeFound("lgdCode.greaterThan=" + SMALLER_LGD_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultVillageShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the villageList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultVillageShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultVillageShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the villageList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultVillageShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModified is not null
        defaultVillageShouldBeFound("lastModified.specified=true");

        // Get all the villageList where lastModified is null
        defaultVillageShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultVillageShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the villageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultVillageShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultVillageShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the villageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultVillageShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModifiedBy is not null
        defaultVillageShouldBeFound("lastModifiedBy.specified=true");

        // Get all the villageList where lastModifiedBy is null
        defaultVillageShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultVillageShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the villageList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultVillageShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllVillagesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultVillageShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the villageList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultVillageShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVillageShouldBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageName").value(hasItem(DEFAULT_VILLAGE_NAME)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].lgdCode").value(hasItem(DEFAULT_LGD_CODE.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVillageShouldNotBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVillage() throws Exception {
        // Get the village
        restVillageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village
        Village updatedVillage = villageRepository.findById(village.getId()).get();
        // Disconnect from session so that the updates on updatedVillage are not directly saved in db
        em.detach(updatedVillage);
        updatedVillage
            .villageName(UPDATED_VILLAGE_NAME)
            .deleted(UPDATED_DELETED)
            .lgdCode(UPDATED_LGD_CODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        VillageDTO villageDTO = villageMapper.toDto(updatedVillage);

        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testVillage.getLgdCode()).isEqualTo(UPDATED_LGD_CODE);
        assertThat(testVillage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testVillage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage
            .villageName(UPDATED_VILLAGE_NAME)
            .lgdCode(UPDATED_LGD_CODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testVillage.getLgdCode()).isEqualTo(UPDATED_LGD_CODE);
        assertThat(testVillage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testVillage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage
            .villageName(UPDATED_VILLAGE_NAME)
            .deleted(UPDATED_DELETED)
            .lgdCode(UPDATED_LGD_CODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testVillage.getLgdCode()).isEqualTo(UPDATED_LGD_CODE);
        assertThat(testVillage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testVillage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeDelete = villageRepository.findAll().size();

        // Delete the village
        restVillageMockMvc
            .perform(delete(ENTITY_API_URL_ID, village.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
