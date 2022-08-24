package com.techvg.pacs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.pacs.IntegrationTest;
import com.techvg.pacs.domain.ParameterLookup;
import com.techvg.pacs.domain.Society;
import com.techvg.pacs.domain.enumeration.ParameterLookupType;
import com.techvg.pacs.repository.ParameterLookupRepository;
import com.techvg.pacs.service.criteria.ParameterLookupCriteria;
import com.techvg.pacs.service.dto.ParameterLookupDTO;
import com.techvg.pacs.service.mapper.ParameterLookupMapper;
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
 * Integration tests for the {@link ParameterLookupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParameterLookupResourceIT {

    private static final ParameterLookupType DEFAULT_TYPE = ParameterLookupType.ACCOUNT;
    private static final ParameterLookupType UPDATED_TYPE = ParameterLookupType.RELIGION;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_FREE_FIELD_1 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_1 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_2 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parameter-lookups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParameterLookupRepository parameterLookupRepository;

    @Autowired
    private ParameterLookupMapper parameterLookupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParameterLookupMockMvc;

    private ParameterLookup parameterLookup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterLookup createEntity(EntityManager em) {
        ParameterLookup parameterLookup = new ParameterLookup()
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .value(DEFAULT_VALUE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isDeleted(DEFAULT_IS_DELETED)
            .freeField1(DEFAULT_FREE_FIELD_1)
            .freeField2(DEFAULT_FREE_FIELD_2);
        return parameterLookup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterLookup createUpdatedEntity(EntityManager em) {
        ParameterLookup parameterLookup = new ParameterLookup()
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .value(UPDATED_VALUE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        return parameterLookup;
    }

    @BeforeEach
    public void initTest() {
        parameterLookup = createEntity(em);
    }

    @Test
    @Transactional
    void createParameterLookup() throws Exception {
        int databaseSizeBeforeCreate = parameterLookupRepository.findAll().size();
        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);
        restParameterLookupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeCreate + 1);
        ParameterLookup testParameterLookup = parameterLookupList.get(parameterLookupList.size() - 1);
        assertThat(testParameterLookup.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParameterLookup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameterLookup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParameterLookup.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testParameterLookup.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testParameterLookup.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testParameterLookup.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testParameterLookup.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testParameterLookup.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testParameterLookup.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testParameterLookup.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void createParameterLookupWithExistingId() throws Exception {
        // Create the ParameterLookup with an existing ID
        parameterLookup.setId(1L);
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        int databaseSizeBeforeCreate = parameterLookupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterLookupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParameterLookups() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameterLookup.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));
    }

    @Test
    @Transactional
    void getParameterLookup() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get the parameterLookup
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL_ID, parameterLookup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameterLookup.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.freeField1").value(DEFAULT_FREE_FIELD_1))
            .andExpect(jsonPath("$.freeField2").value(DEFAULT_FREE_FIELD_2));
    }

    @Test
    @Transactional
    void getParameterLookupsByIdFiltering() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        Long id = parameterLookup.getId();

        defaultParameterLookupShouldBeFound("id.equals=" + id);
        defaultParameterLookupShouldNotBeFound("id.notEquals=" + id);

        defaultParameterLookupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParameterLookupShouldNotBeFound("id.greaterThan=" + id);

        defaultParameterLookupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParameterLookupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where type equals to DEFAULT_TYPE
        defaultParameterLookupShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the parameterLookupList where type equals to UPDATED_TYPE
        defaultParameterLookupShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultParameterLookupShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the parameterLookupList where type equals to UPDATED_TYPE
        defaultParameterLookupShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where type is not null
        defaultParameterLookupShouldBeFound("type.specified=true");

        // Get all the parameterLookupList where type is null
        defaultParameterLookupShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where name equals to DEFAULT_NAME
        defaultParameterLookupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the parameterLookupList where name equals to UPDATED_NAME
        defaultParameterLookupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultParameterLookupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the parameterLookupList where name equals to UPDATED_NAME
        defaultParameterLookupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where name is not null
        defaultParameterLookupShouldBeFound("name.specified=true");

        // Get all the parameterLookupList where name is null
        defaultParameterLookupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByNameContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where name contains DEFAULT_NAME
        defaultParameterLookupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the parameterLookupList where name contains UPDATED_NAME
        defaultParameterLookupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where name does not contain DEFAULT_NAME
        defaultParameterLookupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the parameterLookupList where name does not contain UPDATED_NAME
        defaultParameterLookupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where description equals to DEFAULT_DESCRIPTION
        defaultParameterLookupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the parameterLookupList where description equals to UPDATED_DESCRIPTION
        defaultParameterLookupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultParameterLookupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the parameterLookupList where description equals to UPDATED_DESCRIPTION
        defaultParameterLookupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where description is not null
        defaultParameterLookupShouldBeFound("description.specified=true");

        // Get all the parameterLookupList where description is null
        defaultParameterLookupShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where description contains DEFAULT_DESCRIPTION
        defaultParameterLookupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the parameterLookupList where description contains UPDATED_DESCRIPTION
        defaultParameterLookupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where description does not contain DEFAULT_DESCRIPTION
        defaultParameterLookupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the parameterLookupList where description does not contain UPDATED_DESCRIPTION
        defaultParameterLookupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where value equals to DEFAULT_VALUE
        defaultParameterLookupShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the parameterLookupList where value equals to UPDATED_VALUE
        defaultParameterLookupShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultParameterLookupShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the parameterLookupList where value equals to UPDATED_VALUE
        defaultParameterLookupShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where value is not null
        defaultParameterLookupShouldBeFound("value.specified=true");

        // Get all the parameterLookupList where value is null
        defaultParameterLookupShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByValueContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where value contains DEFAULT_VALUE
        defaultParameterLookupShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the parameterLookupList where value contains UPDATED_VALUE
        defaultParameterLookupShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where value does not contain DEFAULT_VALUE
        defaultParameterLookupShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the parameterLookupList where value does not contain UPDATED_VALUE
        defaultParameterLookupShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultParameterLookupShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the parameterLookupList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultParameterLookupShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultParameterLookupShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the parameterLookupList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultParameterLookupShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModified is not null
        defaultParameterLookupShouldBeFound("lastModified.specified=true");

        // Get all the parameterLookupList where lastModified is null
        defaultParameterLookupShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultParameterLookupShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parameterLookupList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultParameterLookupShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultParameterLookupShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the parameterLookupList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultParameterLookupShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModifiedBy is not null
        defaultParameterLookupShouldBeFound("lastModifiedBy.specified=true");

        // Get all the parameterLookupList where lastModifiedBy is null
        defaultParameterLookupShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultParameterLookupShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parameterLookupList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultParameterLookupShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultParameterLookupShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parameterLookupList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultParameterLookupShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdBy equals to DEFAULT_CREATED_BY
        defaultParameterLookupShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the parameterLookupList where createdBy equals to UPDATED_CREATED_BY
        defaultParameterLookupShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultParameterLookupShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the parameterLookupList where createdBy equals to UPDATED_CREATED_BY
        defaultParameterLookupShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdBy is not null
        defaultParameterLookupShouldBeFound("createdBy.specified=true");

        // Get all the parameterLookupList where createdBy is null
        defaultParameterLookupShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdBy contains DEFAULT_CREATED_BY
        defaultParameterLookupShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the parameterLookupList where createdBy contains UPDATED_CREATED_BY
        defaultParameterLookupShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdBy does not contain DEFAULT_CREATED_BY
        defaultParameterLookupShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the parameterLookupList where createdBy does not contain UPDATED_CREATED_BY
        defaultParameterLookupShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdOn equals to DEFAULT_CREATED_ON
        defaultParameterLookupShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the parameterLookupList where createdOn equals to UPDATED_CREATED_ON
        defaultParameterLookupShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultParameterLookupShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the parameterLookupList where createdOn equals to UPDATED_CREATED_ON
        defaultParameterLookupShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where createdOn is not null
        defaultParameterLookupShouldBeFound("createdOn.specified=true");

        // Get all the parameterLookupList where createdOn is null
        defaultParameterLookupShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where isDeleted equals to DEFAULT_IS_DELETED
        defaultParameterLookupShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the parameterLookupList where isDeleted equals to UPDATED_IS_DELETED
        defaultParameterLookupShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultParameterLookupShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the parameterLookupList where isDeleted equals to UPDATED_IS_DELETED
        defaultParameterLookupShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where isDeleted is not null
        defaultParameterLookupShouldBeFound("isDeleted.specified=true");

        // Get all the parameterLookupList where isDeleted is null
        defaultParameterLookupShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField1IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField1 equals to DEFAULT_FREE_FIELD_1
        defaultParameterLookupShouldBeFound("freeField1.equals=" + DEFAULT_FREE_FIELD_1);

        // Get all the parameterLookupList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultParameterLookupShouldNotBeFound("freeField1.equals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField1IsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField1 in DEFAULT_FREE_FIELD_1 or UPDATED_FREE_FIELD_1
        defaultParameterLookupShouldBeFound("freeField1.in=" + DEFAULT_FREE_FIELD_1 + "," + UPDATED_FREE_FIELD_1);

        // Get all the parameterLookupList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultParameterLookupShouldNotBeFound("freeField1.in=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField1IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField1 is not null
        defaultParameterLookupShouldBeFound("freeField1.specified=true");

        // Get all the parameterLookupList where freeField1 is null
        defaultParameterLookupShouldNotBeFound("freeField1.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField1ContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField1 contains DEFAULT_FREE_FIELD_1
        defaultParameterLookupShouldBeFound("freeField1.contains=" + DEFAULT_FREE_FIELD_1);

        // Get all the parameterLookupList where freeField1 contains UPDATED_FREE_FIELD_1
        defaultParameterLookupShouldNotBeFound("freeField1.contains=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField1NotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField1 does not contain DEFAULT_FREE_FIELD_1
        defaultParameterLookupShouldNotBeFound("freeField1.doesNotContain=" + DEFAULT_FREE_FIELD_1);

        // Get all the parameterLookupList where freeField1 does not contain UPDATED_FREE_FIELD_1
        defaultParameterLookupShouldBeFound("freeField1.doesNotContain=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField2IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField2 equals to DEFAULT_FREE_FIELD_2
        defaultParameterLookupShouldBeFound("freeField2.equals=" + DEFAULT_FREE_FIELD_2);

        // Get all the parameterLookupList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultParameterLookupShouldNotBeFound("freeField2.equals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField2IsInShouldWork() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField2 in DEFAULT_FREE_FIELD_2 or UPDATED_FREE_FIELD_2
        defaultParameterLookupShouldBeFound("freeField2.in=" + DEFAULT_FREE_FIELD_2 + "," + UPDATED_FREE_FIELD_2);

        // Get all the parameterLookupList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultParameterLookupShouldNotBeFound("freeField2.in=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField2IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField2 is not null
        defaultParameterLookupShouldBeFound("freeField2.specified=true");

        // Get all the parameterLookupList where freeField2 is null
        defaultParameterLookupShouldNotBeFound("freeField2.specified=false");
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField2ContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField2 contains DEFAULT_FREE_FIELD_2
        defaultParameterLookupShouldBeFound("freeField2.contains=" + DEFAULT_FREE_FIELD_2);

        // Get all the parameterLookupList where freeField2 contains UPDATED_FREE_FIELD_2
        defaultParameterLookupShouldNotBeFound("freeField2.contains=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllParameterLookupsByFreeField2NotContainsSomething() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        // Get all the parameterLookupList where freeField2 does not contain DEFAULT_FREE_FIELD_2
        defaultParameterLookupShouldNotBeFound("freeField2.doesNotContain=" + DEFAULT_FREE_FIELD_2);

        // Get all the parameterLookupList where freeField2 does not contain UPDATED_FREE_FIELD_2
        defaultParameterLookupShouldBeFound("freeField2.doesNotContain=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllParameterLookupsBySocietyIsEqualToSomething() throws Exception {
        Society society;
        if (TestUtil.findAll(em, Society.class).isEmpty()) {
            parameterLookupRepository.saveAndFlush(parameterLookup);
            society = SocietyResourceIT.createEntity(em);
        } else {
            society = TestUtil.findAll(em, Society.class).get(0);
        }
        em.persist(society);
        em.flush();
        parameterLookup.setSociety(society);
        parameterLookupRepository.saveAndFlush(parameterLookup);
        Long societyId = society.getId();

        // Get all the parameterLookupList where society equals to societyId
        defaultParameterLookupShouldBeFound("societyId.equals=" + societyId);

        // Get all the parameterLookupList where society equals to (societyId + 1)
        defaultParameterLookupShouldNotBeFound("societyId.equals=" + (societyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParameterLookupShouldBeFound(String filter) throws Exception {
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameterLookup.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));

        // Check, that the count call also returns 1
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParameterLookupShouldNotBeFound(String filter) throws Exception {
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParameterLookupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParameterLookup() throws Exception {
        // Get the parameterLookup
        restParameterLookupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParameterLookup() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();

        // Update the parameterLookup
        ParameterLookup updatedParameterLookup = parameterLookupRepository.findById(parameterLookup.getId()).get();
        // Disconnect from session so that the updates on updatedParameterLookup are not directly saved in db
        em.detach(updatedParameterLookup);
        updatedParameterLookup
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .value(UPDATED_VALUE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(updatedParameterLookup);

        restParameterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameterLookupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isOk());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
        ParameterLookup testParameterLookup = parameterLookupList.get(parameterLookupList.size() - 1);
        assertThat(testParameterLookup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParameterLookup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameterLookup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParameterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testParameterLookup.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testParameterLookup.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testParameterLookup.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testParameterLookup.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testParameterLookup.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testParameterLookup.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void putNonExistingParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameterLookupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParameterLookupWithPatch() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();

        // Update the parameterLookup using partial update
        ParameterLookup partialUpdatedParameterLookup = new ParameterLookup();
        partialUpdatedParameterLookup.setId(parameterLookup.getId());

        partialUpdatedParameterLookup.value(UPDATED_VALUE).lastModified(UPDATED_LAST_MODIFIED);

        restParameterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameterLookup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterLookup))
            )
            .andExpect(status().isOk());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
        ParameterLookup testParameterLookup = parameterLookupList.get(parameterLookupList.size() - 1);
        assertThat(testParameterLookup.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParameterLookup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameterLookup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParameterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParameterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testParameterLookup.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testParameterLookup.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testParameterLookup.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testParameterLookup.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testParameterLookup.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testParameterLookup.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void fullUpdateParameterLookupWithPatch() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();

        // Update the parameterLookup using partial update
        ParameterLookup partialUpdatedParameterLookup = new ParameterLookup();
        partialUpdatedParameterLookup.setId(parameterLookup.getId());

        partialUpdatedParameterLookup
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .value(UPDATED_VALUE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);

        restParameterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameterLookup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterLookup))
            )
            .andExpect(status().isOk());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
        ParameterLookup testParameterLookup = parameterLookupList.get(parameterLookupList.size() - 1);
        assertThat(testParameterLookup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParameterLookup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameterLookup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParameterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testParameterLookup.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testParameterLookup.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testParameterLookup.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testParameterLookup.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testParameterLookup.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testParameterLookup.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void patchNonExistingParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parameterLookupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParameterLookup() throws Exception {
        int databaseSizeBeforeUpdate = parameterLookupRepository.findAll().size();
        parameterLookup.setId(count.incrementAndGet());

        // Create the ParameterLookup
        ParameterLookupDTO parameterLookupDTO = parameterLookupMapper.toDto(parameterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parameterLookupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParameterLookup in the database
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParameterLookup() throws Exception {
        // Initialize the database
        parameterLookupRepository.saveAndFlush(parameterLookup);

        int databaseSizeBeforeDelete = parameterLookupRepository.findAll().size();

        // Delete the parameterLookup
        restParameterLookupMockMvc
            .perform(delete(ENTITY_API_URL_ID, parameterLookup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParameterLookup> parameterLookupList = parameterLookupRepository.findAll();
        assertThat(parameterLookupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
