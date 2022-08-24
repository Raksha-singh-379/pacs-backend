package com.techvg.pacs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.pacs.IntegrationTest;
import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.domain.Society;
import com.techvg.pacs.domain.Society;
import com.techvg.pacs.repository.SocietyRepository;
import com.techvg.pacs.service.criteria.SocietyCriteria;
import com.techvg.pacs.service.dto.SocietyDTO;
import com.techvg.pacs.service.mapper.SocietyMapper;
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
 * Integration tests for the {@link SocietyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocietyResourceIT {

    private static final String DEFAULT_SOCIETY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOCIETY_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_REGISTRATION_NUMBER = 1D;
    private static final Double UPDATED_REGISTRATION_NUMBER = 2D;
    private static final Double SMALLER_REGISTRATION_NUMBER = 1D - 1D;

    private static final Double DEFAULT_GSTIN_NUMBER = 1D;
    private static final Double UPDATED_GSTIN_NUMBER = 2D;
    private static final Double SMALLER_GSTIN_NUMBER = 1D - 1D;

    private static final Double DEFAULT_PAN_NUMBER = 1D;
    private static final Double UPDATED_PAN_NUMBER = 2D;
    private static final Double SMALLER_PAN_NUMBER = 1D - 1D;

    private static final Double DEFAULT_TAN_NUMBER = 1D;
    private static final Double UPDATED_TAN_NUMBER = 2D;
    private static final Double SMALLER_TAN_NUMBER = 1D - 1D;

    private static final Double DEFAULT_PHONE_NUMBER = 1D;
    private static final Double UPDATED_PHONE_NUMBER = 2D;
    private static final Double SMALLER_PHONE_NUMBER = 1D - 1D;

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVATE = false;
    private static final Boolean UPDATED_IS_ACTIVATE = true;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_1 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_1 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_2 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_2 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_3 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_3 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_4 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_4 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/societies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private SocietyMapper societyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocietyMockMvc;

    private Society society;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Society createEntity(EntityManager em) {
        Society society = new Society()
            .societyName(DEFAULT_SOCIETY_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .gstinNumber(DEFAULT_GSTIN_NUMBER)
            .panNumber(DEFAULT_PAN_NUMBER)
            .tanNumber(DEFAULT_TAN_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .description(DEFAULT_DESCRIPTION)
            .isActivate(DEFAULT_IS_ACTIVATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .freeField1(DEFAULT_FREE_FIELD_1)
            .freeField2(DEFAULT_FREE_FIELD_2)
            .freeField3(DEFAULT_FREE_FIELD_3)
            .freeField4(DEFAULT_FREE_FIELD_4);
        return society;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Society createUpdatedEntity(EntityManager em) {
        Society society = new Society()
            .societyName(UPDATED_SOCIETY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .gstinNumber(UPDATED_GSTIN_NUMBER)
            .panNumber(UPDATED_PAN_NUMBER)
            .tanNumber(UPDATED_TAN_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION)
            .isActivate(UPDATED_IS_ACTIVATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3)
            .freeField4(UPDATED_FREE_FIELD_4);
        return society;
    }

    @BeforeEach
    public void initTest() {
        society = createEntity(em);
    }

    @Test
    @Transactional
    void createSociety() throws Exception {
        int databaseSizeBeforeCreate = societyRepository.findAll().size();
        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);
        restSocietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(societyDTO)))
            .andExpect(status().isCreated());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeCreate + 1);
        Society testSociety = societyList.get(societyList.size() - 1);
        assertThat(testSociety.getSocietyName()).isEqualTo(DEFAULT_SOCIETY_NAME);
        assertThat(testSociety.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testSociety.getGstinNumber()).isEqualTo(DEFAULT_GSTIN_NUMBER);
        assertThat(testSociety.getPanNumber()).isEqualTo(DEFAULT_PAN_NUMBER);
        assertThat(testSociety.getTanNumber()).isEqualTo(DEFAULT_TAN_NUMBER);
        assertThat(testSociety.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSociety.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testSociety.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testSociety.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSociety.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSociety.getIsActivate()).isEqualTo(DEFAULT_IS_ACTIVATE);
        assertThat(testSociety.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSociety.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSociety.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testSociety.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
        assertThat(testSociety.getFreeField3()).isEqualTo(DEFAULT_FREE_FIELD_3);
        assertThat(testSociety.getFreeField4()).isEqualTo(DEFAULT_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void createSocietyWithExistingId() throws Exception {
        // Create the Society with an existing ID
        society.setId(1L);
        SocietyDTO societyDTO = societyMapper.toDto(society);

        int databaseSizeBeforeCreate = societyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(societyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSocietyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = societyRepository.findAll().size();
        // set the field null
        society.setSocietyName(null);

        // Create the Society, which fails.
        SocietyDTO societyDTO = societyMapper.toDto(society);

        restSocietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(societyDTO)))
            .andExpect(status().isBadRequest());

        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocieties() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(society.getId().intValue())))
            .andExpect(jsonPath("$.[*].societyName").value(hasItem(DEFAULT_SOCIETY_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].gstinNumber").value(hasItem(DEFAULT_GSTIN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].panNumber").value(hasItem(DEFAULT_PAN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].tanNumber").value(hasItem(DEFAULT_TAN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)))
            .andExpect(jsonPath("$.[*].freeField3").value(hasItem(DEFAULT_FREE_FIELD_3)))
            .andExpect(jsonPath("$.[*].freeField4").value(hasItem(DEFAULT_FREE_FIELD_4)));
    }

    @Test
    @Transactional
    void getSociety() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get the society
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL_ID, society.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(society.getId().intValue()))
            .andExpect(jsonPath("$.societyName").value(DEFAULT_SOCIETY_NAME))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.gstinNumber").value(DEFAULT_GSTIN_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.panNumber").value(DEFAULT_PAN_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.tanNumber").value(DEFAULT_TAN_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActivate").value(DEFAULT_IS_ACTIVATE.booleanValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.freeField1").value(DEFAULT_FREE_FIELD_1))
            .andExpect(jsonPath("$.freeField2").value(DEFAULT_FREE_FIELD_2))
            .andExpect(jsonPath("$.freeField3").value(DEFAULT_FREE_FIELD_3))
            .andExpect(jsonPath("$.freeField4").value(DEFAULT_FREE_FIELD_4));
    }

    @Test
    @Transactional
    void getSocietiesByIdFiltering() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        Long id = society.getId();

        defaultSocietyShouldBeFound("id.equals=" + id);
        defaultSocietyShouldNotBeFound("id.notEquals=" + id);

        defaultSocietyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSocietyShouldNotBeFound("id.greaterThan=" + id);

        defaultSocietyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSocietyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where societyName equals to DEFAULT_SOCIETY_NAME
        defaultSocietyShouldBeFound("societyName.equals=" + DEFAULT_SOCIETY_NAME);

        // Get all the societyList where societyName equals to UPDATED_SOCIETY_NAME
        defaultSocietyShouldNotBeFound("societyName.equals=" + UPDATED_SOCIETY_NAME);
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyNameIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where societyName in DEFAULT_SOCIETY_NAME or UPDATED_SOCIETY_NAME
        defaultSocietyShouldBeFound("societyName.in=" + DEFAULT_SOCIETY_NAME + "," + UPDATED_SOCIETY_NAME);

        // Get all the societyList where societyName equals to UPDATED_SOCIETY_NAME
        defaultSocietyShouldNotBeFound("societyName.in=" + UPDATED_SOCIETY_NAME);
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where societyName is not null
        defaultSocietyShouldBeFound("societyName.specified=true");

        // Get all the societyList where societyName is null
        defaultSocietyShouldNotBeFound("societyName.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyNameContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where societyName contains DEFAULT_SOCIETY_NAME
        defaultSocietyShouldBeFound("societyName.contains=" + DEFAULT_SOCIETY_NAME);

        // Get all the societyList where societyName contains UPDATED_SOCIETY_NAME
        defaultSocietyShouldNotBeFound("societyName.contains=" + UPDATED_SOCIETY_NAME);
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyNameNotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where societyName does not contain DEFAULT_SOCIETY_NAME
        defaultSocietyShouldNotBeFound("societyName.doesNotContain=" + DEFAULT_SOCIETY_NAME);

        // Get all the societyList where societyName does not contain UPDATED_SOCIETY_NAME
        defaultSocietyShouldBeFound("societyName.doesNotContain=" + UPDATED_SOCIETY_NAME);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber equals to DEFAULT_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber in DEFAULT_REGISTRATION_NUMBER or UPDATED_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber is not null
        defaultSocietyShouldBeFound("registrationNumber.specified=true");

        // Get all the societyList where registrationNumber is null
        defaultSocietyShouldNotBeFound("registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber is greater than or equal to DEFAULT_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.greaterThanOrEqual=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber is greater than or equal to UPDATED_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.greaterThanOrEqual=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber is less than or equal to DEFAULT_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.lessThanOrEqual=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber is less than or equal to SMALLER_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.lessThanOrEqual=" + SMALLER_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber is less than DEFAULT_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.lessThan=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber is less than UPDATED_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.lessThan=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByRegistrationNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where registrationNumber is greater than DEFAULT_REGISTRATION_NUMBER
        defaultSocietyShouldNotBeFound("registrationNumber.greaterThan=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the societyList where registrationNumber is greater than SMALLER_REGISTRATION_NUMBER
        defaultSocietyShouldBeFound("registrationNumber.greaterThan=" + SMALLER_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber equals to DEFAULT_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.equals=" + DEFAULT_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber equals to UPDATED_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.equals=" + UPDATED_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber in DEFAULT_GSTIN_NUMBER or UPDATED_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.in=" + DEFAULT_GSTIN_NUMBER + "," + UPDATED_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber equals to UPDATED_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.in=" + UPDATED_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber is not null
        defaultSocietyShouldBeFound("gstinNumber.specified=true");

        // Get all the societyList where gstinNumber is null
        defaultSocietyShouldNotBeFound("gstinNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber is greater than or equal to DEFAULT_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.greaterThanOrEqual=" + DEFAULT_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber is greater than or equal to UPDATED_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.greaterThanOrEqual=" + UPDATED_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber is less than or equal to DEFAULT_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.lessThanOrEqual=" + DEFAULT_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber is less than or equal to SMALLER_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.lessThanOrEqual=" + SMALLER_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber is less than DEFAULT_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.lessThan=" + DEFAULT_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber is less than UPDATED_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.lessThan=" + UPDATED_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByGstinNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where gstinNumber is greater than DEFAULT_GSTIN_NUMBER
        defaultSocietyShouldNotBeFound("gstinNumber.greaterThan=" + DEFAULT_GSTIN_NUMBER);

        // Get all the societyList where gstinNumber is greater than SMALLER_GSTIN_NUMBER
        defaultSocietyShouldBeFound("gstinNumber.greaterThan=" + SMALLER_GSTIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber equals to DEFAULT_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.equals=" + DEFAULT_PAN_NUMBER);

        // Get all the societyList where panNumber equals to UPDATED_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.equals=" + UPDATED_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber in DEFAULT_PAN_NUMBER or UPDATED_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.in=" + DEFAULT_PAN_NUMBER + "," + UPDATED_PAN_NUMBER);

        // Get all the societyList where panNumber equals to UPDATED_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.in=" + UPDATED_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber is not null
        defaultSocietyShouldBeFound("panNumber.specified=true");

        // Get all the societyList where panNumber is null
        defaultSocietyShouldNotBeFound("panNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber is greater than or equal to DEFAULT_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.greaterThanOrEqual=" + DEFAULT_PAN_NUMBER);

        // Get all the societyList where panNumber is greater than or equal to UPDATED_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.greaterThanOrEqual=" + UPDATED_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber is less than or equal to DEFAULT_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.lessThanOrEqual=" + DEFAULT_PAN_NUMBER);

        // Get all the societyList where panNumber is less than or equal to SMALLER_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.lessThanOrEqual=" + SMALLER_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber is less than DEFAULT_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.lessThan=" + DEFAULT_PAN_NUMBER);

        // Get all the societyList where panNumber is less than UPDATED_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.lessThan=" + UPDATED_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPanNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where panNumber is greater than DEFAULT_PAN_NUMBER
        defaultSocietyShouldNotBeFound("panNumber.greaterThan=" + DEFAULT_PAN_NUMBER);

        // Get all the societyList where panNumber is greater than SMALLER_PAN_NUMBER
        defaultSocietyShouldBeFound("panNumber.greaterThan=" + SMALLER_PAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber equals to DEFAULT_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.equals=" + DEFAULT_TAN_NUMBER);

        // Get all the societyList where tanNumber equals to UPDATED_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.equals=" + UPDATED_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber in DEFAULT_TAN_NUMBER or UPDATED_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.in=" + DEFAULT_TAN_NUMBER + "," + UPDATED_TAN_NUMBER);

        // Get all the societyList where tanNumber equals to UPDATED_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.in=" + UPDATED_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber is not null
        defaultSocietyShouldBeFound("tanNumber.specified=true");

        // Get all the societyList where tanNumber is null
        defaultSocietyShouldNotBeFound("tanNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber is greater than or equal to DEFAULT_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.greaterThanOrEqual=" + DEFAULT_TAN_NUMBER);

        // Get all the societyList where tanNumber is greater than or equal to UPDATED_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.greaterThanOrEqual=" + UPDATED_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber is less than or equal to DEFAULT_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.lessThanOrEqual=" + DEFAULT_TAN_NUMBER);

        // Get all the societyList where tanNumber is less than or equal to SMALLER_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.lessThanOrEqual=" + SMALLER_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber is less than DEFAULT_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.lessThan=" + DEFAULT_TAN_NUMBER);

        // Get all the societyList where tanNumber is less than UPDATED_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.lessThan=" + UPDATED_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByTanNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where tanNumber is greater than DEFAULT_TAN_NUMBER
        defaultSocietyShouldNotBeFound("tanNumber.greaterThan=" + DEFAULT_TAN_NUMBER);

        // Get all the societyList where tanNumber is greater than SMALLER_TAN_NUMBER
        defaultSocietyShouldBeFound("tanNumber.greaterThan=" + SMALLER_TAN_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the societyList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the societyList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber is not null
        defaultSocietyShouldBeFound("phoneNumber.specified=true");

        // Get all the societyList where phoneNumber is null
        defaultSocietyShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber is greater than or equal to DEFAULT_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.greaterThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the societyList where phoneNumber is greater than or equal to UPDATED_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.greaterThanOrEqual=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber is less than or equal to DEFAULT_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.lessThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the societyList where phoneNumber is less than or equal to SMALLER_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.lessThanOrEqual=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber is less than DEFAULT_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.lessThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the societyList where phoneNumber is less than UPDATED_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.lessThan=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByPhoneNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where phoneNumber is greater than DEFAULT_PHONE_NUMBER
        defaultSocietyShouldNotBeFound("phoneNumber.greaterThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the societyList where phoneNumber is greater than SMALLER_PHONE_NUMBER
        defaultSocietyShouldBeFound("phoneNumber.greaterThan=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSocietiesByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultSocietyShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the societyList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSocietyShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSocietiesByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultSocietyShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the societyList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSocietyShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSocietiesByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where emailAddress is not null
        defaultSocietyShouldBeFound("emailAddress.specified=true");

        // Get all the societyList where emailAddress is null
        defaultSocietyShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultSocietyShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the societyList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultSocietyShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSocietiesByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultSocietyShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the societyList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultSocietyShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdOn equals to DEFAULT_CREATED_ON
        defaultSocietyShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the societyList where createdOn equals to UPDATED_CREATED_ON
        defaultSocietyShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultSocietyShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the societyList where createdOn equals to UPDATED_CREATED_ON
        defaultSocietyShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdOn is not null
        defaultSocietyShouldBeFound("createdOn.specified=true");

        // Get all the societyList where createdOn is null
        defaultSocietyShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdBy equals to DEFAULT_CREATED_BY
        defaultSocietyShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the societyList where createdBy equals to UPDATED_CREATED_BY
        defaultSocietyShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSocietyShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the societyList where createdBy equals to UPDATED_CREATED_BY
        defaultSocietyShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdBy is not null
        defaultSocietyShouldBeFound("createdBy.specified=true");

        // Get all the societyList where createdBy is null
        defaultSocietyShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdBy contains DEFAULT_CREATED_BY
        defaultSocietyShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the societyList where createdBy contains UPDATED_CREATED_BY
        defaultSocietyShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where createdBy does not contain DEFAULT_CREATED_BY
        defaultSocietyShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the societyList where createdBy does not contain UPDATED_CREATED_BY
        defaultSocietyShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where description equals to DEFAULT_DESCRIPTION
        defaultSocietyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the societyList where description equals to UPDATED_DESCRIPTION
        defaultSocietyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSocietiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSocietyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the societyList where description equals to UPDATED_DESCRIPTION
        defaultSocietyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSocietiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where description is not null
        defaultSocietyShouldBeFound("description.specified=true");

        // Get all the societyList where description is null
        defaultSocietyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where description contains DEFAULT_DESCRIPTION
        defaultSocietyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the societyList where description contains UPDATED_DESCRIPTION
        defaultSocietyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSocietiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where description does not contain DEFAULT_DESCRIPTION
        defaultSocietyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the societyList where description does not contain UPDATED_DESCRIPTION
        defaultSocietyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSocietiesByIsActivateIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where isActivate equals to DEFAULT_IS_ACTIVATE
        defaultSocietyShouldBeFound("isActivate.equals=" + DEFAULT_IS_ACTIVATE);

        // Get all the societyList where isActivate equals to UPDATED_IS_ACTIVATE
        defaultSocietyShouldNotBeFound("isActivate.equals=" + UPDATED_IS_ACTIVATE);
    }

    @Test
    @Transactional
    void getAllSocietiesByIsActivateIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where isActivate in DEFAULT_IS_ACTIVATE or UPDATED_IS_ACTIVATE
        defaultSocietyShouldBeFound("isActivate.in=" + DEFAULT_IS_ACTIVATE + "," + UPDATED_IS_ACTIVATE);

        // Get all the societyList where isActivate equals to UPDATED_IS_ACTIVATE
        defaultSocietyShouldNotBeFound("isActivate.in=" + UPDATED_IS_ACTIVATE);
    }

    @Test
    @Transactional
    void getAllSocietiesByIsActivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where isActivate is not null
        defaultSocietyShouldBeFound("isActivate.specified=true");

        // Get all the societyList where isActivate is null
        defaultSocietyShouldNotBeFound("isActivate.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSocietyShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the societyList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSocietyShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSocietyShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the societyList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSocietyShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModified is not null
        defaultSocietyShouldBeFound("lastModified.specified=true");

        // Get all the societyList where lastModified is null
        defaultSocietyShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSocietyShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the societyList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSocietyShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSocietyShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the societyList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSocietyShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModifiedBy is not null
        defaultSocietyShouldBeFound("lastModifiedBy.specified=true");

        // Get all the societyList where lastModifiedBy is null
        defaultSocietyShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultSocietyShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the societyList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultSocietyShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultSocietyShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the societyList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultSocietyShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField1IsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField1 equals to DEFAULT_FREE_FIELD_1
        defaultSocietyShouldBeFound("freeField1.equals=" + DEFAULT_FREE_FIELD_1);

        // Get all the societyList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultSocietyShouldNotBeFound("freeField1.equals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField1IsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField1 in DEFAULT_FREE_FIELD_1 or UPDATED_FREE_FIELD_1
        defaultSocietyShouldBeFound("freeField1.in=" + DEFAULT_FREE_FIELD_1 + "," + UPDATED_FREE_FIELD_1);

        // Get all the societyList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultSocietyShouldNotBeFound("freeField1.in=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField1IsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField1 is not null
        defaultSocietyShouldBeFound("freeField1.specified=true");

        // Get all the societyList where freeField1 is null
        defaultSocietyShouldNotBeFound("freeField1.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField1ContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField1 contains DEFAULT_FREE_FIELD_1
        defaultSocietyShouldBeFound("freeField1.contains=" + DEFAULT_FREE_FIELD_1);

        // Get all the societyList where freeField1 contains UPDATED_FREE_FIELD_1
        defaultSocietyShouldNotBeFound("freeField1.contains=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField1NotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField1 does not contain DEFAULT_FREE_FIELD_1
        defaultSocietyShouldNotBeFound("freeField1.doesNotContain=" + DEFAULT_FREE_FIELD_1);

        // Get all the societyList where freeField1 does not contain UPDATED_FREE_FIELD_1
        defaultSocietyShouldBeFound("freeField1.doesNotContain=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField2IsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField2 equals to DEFAULT_FREE_FIELD_2
        defaultSocietyShouldBeFound("freeField2.equals=" + DEFAULT_FREE_FIELD_2);

        // Get all the societyList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultSocietyShouldNotBeFound("freeField2.equals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField2IsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField2 in DEFAULT_FREE_FIELD_2 or UPDATED_FREE_FIELD_2
        defaultSocietyShouldBeFound("freeField2.in=" + DEFAULT_FREE_FIELD_2 + "," + UPDATED_FREE_FIELD_2);

        // Get all the societyList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultSocietyShouldNotBeFound("freeField2.in=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField2IsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField2 is not null
        defaultSocietyShouldBeFound("freeField2.specified=true");

        // Get all the societyList where freeField2 is null
        defaultSocietyShouldNotBeFound("freeField2.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField2ContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField2 contains DEFAULT_FREE_FIELD_2
        defaultSocietyShouldBeFound("freeField2.contains=" + DEFAULT_FREE_FIELD_2);

        // Get all the societyList where freeField2 contains UPDATED_FREE_FIELD_2
        defaultSocietyShouldNotBeFound("freeField2.contains=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField2NotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField2 does not contain DEFAULT_FREE_FIELD_2
        defaultSocietyShouldNotBeFound("freeField2.doesNotContain=" + DEFAULT_FREE_FIELD_2);

        // Get all the societyList where freeField2 does not contain UPDATED_FREE_FIELD_2
        defaultSocietyShouldBeFound("freeField2.doesNotContain=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField3IsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField3 equals to DEFAULT_FREE_FIELD_3
        defaultSocietyShouldBeFound("freeField3.equals=" + DEFAULT_FREE_FIELD_3);

        // Get all the societyList where freeField3 equals to UPDATED_FREE_FIELD_3
        defaultSocietyShouldNotBeFound("freeField3.equals=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField3IsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField3 in DEFAULT_FREE_FIELD_3 or UPDATED_FREE_FIELD_3
        defaultSocietyShouldBeFound("freeField3.in=" + DEFAULT_FREE_FIELD_3 + "," + UPDATED_FREE_FIELD_3);

        // Get all the societyList where freeField3 equals to UPDATED_FREE_FIELD_3
        defaultSocietyShouldNotBeFound("freeField3.in=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField3IsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField3 is not null
        defaultSocietyShouldBeFound("freeField3.specified=true");

        // Get all the societyList where freeField3 is null
        defaultSocietyShouldNotBeFound("freeField3.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField3ContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField3 contains DEFAULT_FREE_FIELD_3
        defaultSocietyShouldBeFound("freeField3.contains=" + DEFAULT_FREE_FIELD_3);

        // Get all the societyList where freeField3 contains UPDATED_FREE_FIELD_3
        defaultSocietyShouldNotBeFound("freeField3.contains=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField3NotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField3 does not contain DEFAULT_FREE_FIELD_3
        defaultSocietyShouldNotBeFound("freeField3.doesNotContain=" + DEFAULT_FREE_FIELD_3);

        // Get all the societyList where freeField3 does not contain UPDATED_FREE_FIELD_3
        defaultSocietyShouldBeFound("freeField3.doesNotContain=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField4IsEqualToSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField4 equals to DEFAULT_FREE_FIELD_4
        defaultSocietyShouldBeFound("freeField4.equals=" + DEFAULT_FREE_FIELD_4);

        // Get all the societyList where freeField4 equals to UPDATED_FREE_FIELD_4
        defaultSocietyShouldNotBeFound("freeField4.equals=" + UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField4IsInShouldWork() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField4 in DEFAULT_FREE_FIELD_4 or UPDATED_FREE_FIELD_4
        defaultSocietyShouldBeFound("freeField4.in=" + DEFAULT_FREE_FIELD_4 + "," + UPDATED_FREE_FIELD_4);

        // Get all the societyList where freeField4 equals to UPDATED_FREE_FIELD_4
        defaultSocietyShouldNotBeFound("freeField4.in=" + UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField4IsNullOrNotNull() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField4 is not null
        defaultSocietyShouldBeFound("freeField4.specified=true");

        // Get all the societyList where freeField4 is null
        defaultSocietyShouldNotBeFound("freeField4.specified=false");
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField4ContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField4 contains DEFAULT_FREE_FIELD_4
        defaultSocietyShouldBeFound("freeField4.contains=" + DEFAULT_FREE_FIELD_4);

        // Get all the societyList where freeField4 contains UPDATED_FREE_FIELD_4
        defaultSocietyShouldNotBeFound("freeField4.contains=" + UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void getAllSocietiesByFreeField4NotContainsSomething() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        // Get all the societyList where freeField4 does not contain DEFAULT_FREE_FIELD_4
        defaultSocietyShouldNotBeFound("freeField4.doesNotContain=" + DEFAULT_FREE_FIELD_4);

        // Get all the societyList where freeField4 does not contain UPDATED_FREE_FIELD_4
        defaultSocietyShouldBeFound("freeField4.doesNotContain=" + UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void getAllSocietiesByAddressDetailsIsEqualToSomething() throws Exception {
        AddressDetails addressDetails;
        if (TestUtil.findAll(em, AddressDetails.class).isEmpty()) {
            societyRepository.saveAndFlush(society);
            addressDetails = AddressDetailsResourceIT.createEntity(em);
        } else {
            addressDetails = TestUtil.findAll(em, AddressDetails.class).get(0);
        }
        em.persist(addressDetails);
        em.flush();
        society.setAddressDetails(addressDetails);
        societyRepository.saveAndFlush(society);
        Long addressDetailsId = addressDetails.getId();

        // Get all the societyList where addressDetails equals to addressDetailsId
        defaultSocietyShouldBeFound("addressDetailsId.equals=" + addressDetailsId);

        // Get all the societyList where addressDetails equals to (addressDetailsId + 1)
        defaultSocietyShouldNotBeFound("addressDetailsId.equals=" + (addressDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllSocietiesBySocietyIsEqualToSomething() throws Exception {
        Society society;
        if (TestUtil.findAll(em, Society.class).isEmpty()) {
            societyRepository.saveAndFlush(society);
            society = SocietyResourceIT.createEntity(em);
        } else {
            society = TestUtil.findAll(em, Society.class).get(0);
        }
        em.persist(society);
        em.flush();
        society.setSociety(society);
        societyRepository.saveAndFlush(society);
        Long societyId = society.getId();

        // Get all the societyList where society equals to societyId
        defaultSocietyShouldBeFound("societyId.equals=" + societyId);

        // Get all the societyList where society equals to (societyId + 1)
        defaultSocietyShouldNotBeFound("societyId.equals=" + (societyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocietyShouldBeFound(String filter) throws Exception {
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(society.getId().intValue())))
            .andExpect(jsonPath("$.[*].societyName").value(hasItem(DEFAULT_SOCIETY_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].gstinNumber").value(hasItem(DEFAULT_GSTIN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].panNumber").value(hasItem(DEFAULT_PAN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].tanNumber").value(hasItem(DEFAULT_TAN_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)))
            .andExpect(jsonPath("$.[*].freeField3").value(hasItem(DEFAULT_FREE_FIELD_3)))
            .andExpect(jsonPath("$.[*].freeField4").value(hasItem(DEFAULT_FREE_FIELD_4)));

        // Check, that the count call also returns 1
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocietyShouldNotBeFound(String filter) throws Exception {
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSociety() throws Exception {
        // Get the society
        restSocietyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSociety() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        int databaseSizeBeforeUpdate = societyRepository.findAll().size();

        // Update the society
        Society updatedSociety = societyRepository.findById(society.getId()).get();
        // Disconnect from session so that the updates on updatedSociety are not directly saved in db
        em.detach(updatedSociety);
        updatedSociety
            .societyName(UPDATED_SOCIETY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .gstinNumber(UPDATED_GSTIN_NUMBER)
            .panNumber(UPDATED_PAN_NUMBER)
            .tanNumber(UPDATED_TAN_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION)
            .isActivate(UPDATED_IS_ACTIVATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3)
            .freeField4(UPDATED_FREE_FIELD_4);
        SocietyDTO societyDTO = societyMapper.toDto(updatedSociety);

        restSocietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
        Society testSociety = societyList.get(societyList.size() - 1);
        assertThat(testSociety.getSocietyName()).isEqualTo(UPDATED_SOCIETY_NAME);
        assertThat(testSociety.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testSociety.getGstinNumber()).isEqualTo(UPDATED_GSTIN_NUMBER);
        assertThat(testSociety.getPanNumber()).isEqualTo(UPDATED_PAN_NUMBER);
        assertThat(testSociety.getTanNumber()).isEqualTo(UPDATED_TAN_NUMBER);
        assertThat(testSociety.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSociety.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSociety.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSociety.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSociety.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSociety.getIsActivate()).isEqualTo(UPDATED_IS_ACTIVATE);
        assertThat(testSociety.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSociety.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSociety.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testSociety.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
        assertThat(testSociety.getFreeField3()).isEqualTo(UPDATED_FREE_FIELD_3);
        assertThat(testSociety.getFreeField4()).isEqualTo(UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void putNonExistingSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(societyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocietyWithPatch() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        int databaseSizeBeforeUpdate = societyRepository.findAll().size();

        // Update the society using partial update
        Society partialUpdatedSociety = new Society();
        partialUpdatedSociety.setId(society.getId());

        partialUpdatedSociety
            .societyName(UPDATED_SOCIETY_NAME)
            .gstinNumber(UPDATED_GSTIN_NUMBER)
            .panNumber(UPDATED_PAN_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField4(UPDATED_FREE_FIELD_4);

        restSocietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociety.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociety))
            )
            .andExpect(status().isOk());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
        Society testSociety = societyList.get(societyList.size() - 1);
        assertThat(testSociety.getSocietyName()).isEqualTo(UPDATED_SOCIETY_NAME);
        assertThat(testSociety.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testSociety.getGstinNumber()).isEqualTo(UPDATED_GSTIN_NUMBER);
        assertThat(testSociety.getPanNumber()).isEqualTo(UPDATED_PAN_NUMBER);
        assertThat(testSociety.getTanNumber()).isEqualTo(DEFAULT_TAN_NUMBER);
        assertThat(testSociety.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSociety.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testSociety.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSociety.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSociety.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSociety.getIsActivate()).isEqualTo(DEFAULT_IS_ACTIVATE);
        assertThat(testSociety.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSociety.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSociety.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testSociety.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
        assertThat(testSociety.getFreeField3()).isEqualTo(DEFAULT_FREE_FIELD_3);
        assertThat(testSociety.getFreeField4()).isEqualTo(UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void fullUpdateSocietyWithPatch() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        int databaseSizeBeforeUpdate = societyRepository.findAll().size();

        // Update the society using partial update
        Society partialUpdatedSociety = new Society();
        partialUpdatedSociety.setId(society.getId());

        partialUpdatedSociety
            .societyName(UPDATED_SOCIETY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .gstinNumber(UPDATED_GSTIN_NUMBER)
            .panNumber(UPDATED_PAN_NUMBER)
            .tanNumber(UPDATED_TAN_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION)
            .isActivate(UPDATED_IS_ACTIVATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3)
            .freeField4(UPDATED_FREE_FIELD_4);

        restSocietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociety.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociety))
            )
            .andExpect(status().isOk());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
        Society testSociety = societyList.get(societyList.size() - 1);
        assertThat(testSociety.getSocietyName()).isEqualTo(UPDATED_SOCIETY_NAME);
        assertThat(testSociety.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testSociety.getGstinNumber()).isEqualTo(UPDATED_GSTIN_NUMBER);
        assertThat(testSociety.getPanNumber()).isEqualTo(UPDATED_PAN_NUMBER);
        assertThat(testSociety.getTanNumber()).isEqualTo(UPDATED_TAN_NUMBER);
        assertThat(testSociety.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSociety.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSociety.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSociety.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSociety.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSociety.getIsActivate()).isEqualTo(UPDATED_IS_ACTIVATE);
        assertThat(testSociety.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSociety.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSociety.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testSociety.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
        assertThat(testSociety.getFreeField3()).isEqualTo(UPDATED_FREE_FIELD_3);
        assertThat(testSociety.getFreeField4()).isEqualTo(UPDATED_FREE_FIELD_4);
    }

    @Test
    @Transactional
    void patchNonExistingSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, societyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSociety() throws Exception {
        int databaseSizeBeforeUpdate = societyRepository.findAll().size();
        society.setId(count.incrementAndGet());

        // Create the Society
        SocietyDTO societyDTO = societyMapper.toDto(society);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocietyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(societyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Society in the database
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSociety() throws Exception {
        // Initialize the database
        societyRepository.saveAndFlush(society);

        int databaseSizeBeforeDelete = societyRepository.findAll().size();

        // Delete the society
        restSocietyMockMvc
            .perform(delete(ENTITY_API_URL_ID, society.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Society> societyList = societyRepository.findAll();
        assertThat(societyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
