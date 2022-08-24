package com.techvg.pacs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.pacs.IntegrationTest;
import com.techvg.pacs.domain.SecurityPermission;
import com.techvg.pacs.domain.SecurityRole;
import com.techvg.pacs.domain.SecurityUser;
import com.techvg.pacs.domain.Society;
import com.techvg.pacs.repository.SecurityUserRepository;
import com.techvg.pacs.service.SecurityUserService;
import com.techvg.pacs.service.criteria.SecurityUserCriteria;
import com.techvg.pacs.service.dto.SecurityUserDTO;
import com.techvg.pacs.service.mapper.SecurityUserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SecurityUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecurityUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_RESET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESET_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESET_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESET_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/security-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityUserRepository securityUserRepository;

    @Mock
    private SecurityUserRepository securityUserRepositoryMock;

    @Autowired
    private SecurityUserMapper securityUserMapper;

    @Mock
    private SecurityUserService securityUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityUserMockMvc;

    private SecurityUser securityUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityUser createEntity(EntityManager em) {
        SecurityUser securityUser = new SecurityUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .designation(DEFAULT_DESIGNATION)
            .username(DEFAULT_USERNAME)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .email(DEFAULT_EMAIL)
            .description(DEFAULT_DESCRIPTION)
            .department(DEFAULT_DEPARTMENT)
            .imageUrl(DEFAULT_IMAGE_URL)
            .activated(DEFAULT_ACTIVATED)
            .langKey(DEFAULT_LANG_KEY)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .resetKey(DEFAULT_RESET_KEY)
            .resetDate(DEFAULT_RESET_DATE)
            .mobileNo(DEFAULT_MOBILE_NO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON);
        return securityUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityUser createUpdatedEntity(EntityManager em) {
        SecurityUser securityUser = new SecurityUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .designation(UPDATED_DESIGNATION)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .department(UPDATED_DEPARTMENT)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .mobileNo(UPDATED_MOBILE_NO)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON);
        return securityUser;
    }

    @BeforeEach
    public void initTest() {
        securityUser = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityUser() throws Exception {
        int databaseSizeBeforeCreate = securityUserRepository.findAll().size();
        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);
        restSecurityUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityUser testSecurityUser = securityUserList.get(securityUserList.size() - 1);
        assertThat(testSecurityUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSecurityUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSecurityUser.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testSecurityUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSecurityUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityUser.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testSecurityUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testSecurityUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSecurityUser.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testSecurityUser.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testSecurityUser.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
        assertThat(testSecurityUser.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testSecurityUser.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testSecurityUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSecurityUser.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    void createSecurityUserWithExistingId() throws Exception {
        // Create the SecurityUser with an existing ID
        securityUser.setId(1L);
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        int databaseSizeBeforeCreate = securityUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityUserRepository.findAll().size();
        // set the field null
        securityUser.setUsername(null);

        // Create the SecurityUser, which fails.
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        restSecurityUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityUserRepository.findAll().size();
        // set the field null
        securityUser.setPasswordHash(null);

        // Create the SecurityUser, which fails.
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        restSecurityUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityUsers() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY)))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(DEFAULT_RESET_DATE.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(securityUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(securityUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(securityUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSecurityUser() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get the securityUser
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL_ID, securityUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY))
            .andExpect(jsonPath("$.resetKey").value(DEFAULT_RESET_KEY))
            .andExpect(jsonPath("$.resetDate").value(DEFAULT_RESET_DATE.toString()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    void getSecurityUsersByIdFiltering() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        Long id = securityUser.getId();

        defaultSecurityUserShouldBeFound("id.equals=" + id);
        defaultSecurityUserShouldNotBeFound("id.notEquals=" + id);

        defaultSecurityUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSecurityUserShouldNotBeFound("id.greaterThan=" + id);

        defaultSecurityUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSecurityUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultSecurityUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the securityUserList where firstName equals to UPDATED_FIRST_NAME
        defaultSecurityUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultSecurityUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the securityUserList where firstName equals to UPDATED_FIRST_NAME
        defaultSecurityUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where firstName is not null
        defaultSecurityUserShouldBeFound("firstName.specified=true");

        // Get all the securityUserList where firstName is null
        defaultSecurityUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where firstName contains DEFAULT_FIRST_NAME
        defaultSecurityUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the securityUserList where firstName contains UPDATED_FIRST_NAME
        defaultSecurityUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultSecurityUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the securityUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultSecurityUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where lastName equals to DEFAULT_LAST_NAME
        defaultSecurityUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the securityUserList where lastName equals to UPDATED_LAST_NAME
        defaultSecurityUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultSecurityUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the securityUserList where lastName equals to UPDATED_LAST_NAME
        defaultSecurityUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where lastName is not null
        defaultSecurityUserShouldBeFound("lastName.specified=true");

        // Get all the securityUserList where lastName is null
        defaultSecurityUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where lastName contains DEFAULT_LAST_NAME
        defaultSecurityUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the securityUserList where lastName contains UPDATED_LAST_NAME
        defaultSecurityUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultSecurityUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the securityUserList where lastName does not contain UPDATED_LAST_NAME
        defaultSecurityUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where designation equals to DEFAULT_DESIGNATION
        defaultSecurityUserShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the securityUserList where designation equals to UPDATED_DESIGNATION
        defaultSecurityUserShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultSecurityUserShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the securityUserList where designation equals to UPDATED_DESIGNATION
        defaultSecurityUserShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where designation is not null
        defaultSecurityUserShouldBeFound("designation.specified=true");

        // Get all the securityUserList where designation is null
        defaultSecurityUserShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDesignationContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where designation contains DEFAULT_DESIGNATION
        defaultSecurityUserShouldBeFound("designation.contains=" + DEFAULT_DESIGNATION);

        // Get all the securityUserList where designation contains UPDATED_DESIGNATION
        defaultSecurityUserShouldNotBeFound("designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where designation does not contain DEFAULT_DESIGNATION
        defaultSecurityUserShouldNotBeFound("designation.doesNotContain=" + DEFAULT_DESIGNATION);

        // Get all the securityUserList where designation does not contain UPDATED_DESIGNATION
        defaultSecurityUserShouldBeFound("designation.doesNotContain=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where username equals to DEFAULT_USERNAME
        defaultSecurityUserShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the securityUserList where username equals to UPDATED_USERNAME
        defaultSecurityUserShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultSecurityUserShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the securityUserList where username equals to UPDATED_USERNAME
        defaultSecurityUserShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where username is not null
        defaultSecurityUserShouldBeFound("username.specified=true");

        // Get all the securityUserList where username is null
        defaultSecurityUserShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where username contains DEFAULT_USERNAME
        defaultSecurityUserShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the securityUserList where username contains UPDATED_USERNAME
        defaultSecurityUserShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where username does not contain DEFAULT_USERNAME
        defaultSecurityUserShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the securityUserList where username does not contain UPDATED_USERNAME
        defaultSecurityUserShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByPasswordHashIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where passwordHash equals to DEFAULT_PASSWORD_HASH
        defaultSecurityUserShouldBeFound("passwordHash.equals=" + DEFAULT_PASSWORD_HASH);

        // Get all the securityUserList where passwordHash equals to UPDATED_PASSWORD_HASH
        defaultSecurityUserShouldNotBeFound("passwordHash.equals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByPasswordHashIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where passwordHash in DEFAULT_PASSWORD_HASH or UPDATED_PASSWORD_HASH
        defaultSecurityUserShouldBeFound("passwordHash.in=" + DEFAULT_PASSWORD_HASH + "," + UPDATED_PASSWORD_HASH);

        // Get all the securityUserList where passwordHash equals to UPDATED_PASSWORD_HASH
        defaultSecurityUserShouldNotBeFound("passwordHash.in=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByPasswordHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where passwordHash is not null
        defaultSecurityUserShouldBeFound("passwordHash.specified=true");

        // Get all the securityUserList where passwordHash is null
        defaultSecurityUserShouldNotBeFound("passwordHash.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByPasswordHashContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where passwordHash contains DEFAULT_PASSWORD_HASH
        defaultSecurityUserShouldBeFound("passwordHash.contains=" + DEFAULT_PASSWORD_HASH);

        // Get all the securityUserList where passwordHash contains UPDATED_PASSWORD_HASH
        defaultSecurityUserShouldNotBeFound("passwordHash.contains=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByPasswordHashNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where passwordHash does not contain DEFAULT_PASSWORD_HASH
        defaultSecurityUserShouldNotBeFound("passwordHash.doesNotContain=" + DEFAULT_PASSWORD_HASH);

        // Get all the securityUserList where passwordHash does not contain UPDATED_PASSWORD_HASH
        defaultSecurityUserShouldBeFound("passwordHash.doesNotContain=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where email equals to DEFAULT_EMAIL
        defaultSecurityUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the securityUserList where email equals to UPDATED_EMAIL
        defaultSecurityUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSecurityUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the securityUserList where email equals to UPDATED_EMAIL
        defaultSecurityUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where email is not null
        defaultSecurityUserShouldBeFound("email.specified=true");

        // Get all the securityUserList where email is null
        defaultSecurityUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where email contains DEFAULT_EMAIL
        defaultSecurityUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the securityUserList where email contains UPDATED_EMAIL
        defaultSecurityUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where email does not contain DEFAULT_EMAIL
        defaultSecurityUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the securityUserList where email does not contain UPDATED_EMAIL
        defaultSecurityUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where description equals to DEFAULT_DESCRIPTION
        defaultSecurityUserShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the securityUserList where description equals to UPDATED_DESCRIPTION
        defaultSecurityUserShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSecurityUserShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the securityUserList where description equals to UPDATED_DESCRIPTION
        defaultSecurityUserShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where description is not null
        defaultSecurityUserShouldBeFound("description.specified=true");

        // Get all the securityUserList where description is null
        defaultSecurityUserShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where description contains DEFAULT_DESCRIPTION
        defaultSecurityUserShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the securityUserList where description contains UPDATED_DESCRIPTION
        defaultSecurityUserShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where description does not contain DEFAULT_DESCRIPTION
        defaultSecurityUserShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the securityUserList where description does not contain UPDATED_DESCRIPTION
        defaultSecurityUserShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where department equals to DEFAULT_DEPARTMENT
        defaultSecurityUserShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the securityUserList where department equals to UPDATED_DEPARTMENT
        defaultSecurityUserShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultSecurityUserShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the securityUserList where department equals to UPDATED_DEPARTMENT
        defaultSecurityUserShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where department is not null
        defaultSecurityUserShouldBeFound("department.specified=true");

        // Get all the securityUserList where department is null
        defaultSecurityUserShouldNotBeFound("department.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where department contains DEFAULT_DEPARTMENT
        defaultSecurityUserShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the securityUserList where department contains UPDATED_DEPARTMENT
        defaultSecurityUserShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where department does not contain DEFAULT_DEPARTMENT
        defaultSecurityUserShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the securityUserList where department does not contain UPDATED_DEPARTMENT
        defaultSecurityUserShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultSecurityUserShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the securityUserList where imageUrl equals to UPDATED_IMAGE_URL
        defaultSecurityUserShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultSecurityUserShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the securityUserList where imageUrl equals to UPDATED_IMAGE_URL
        defaultSecurityUserShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where imageUrl is not null
        defaultSecurityUserShouldBeFound("imageUrl.specified=true");

        // Get all the securityUserList where imageUrl is null
        defaultSecurityUserShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where imageUrl contains DEFAULT_IMAGE_URL
        defaultSecurityUserShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the securityUserList where imageUrl contains UPDATED_IMAGE_URL
        defaultSecurityUserShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultSecurityUserShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the securityUserList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultSecurityUserShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activated equals to DEFAULT_ACTIVATED
        defaultSecurityUserShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the securityUserList where activated equals to UPDATED_ACTIVATED
        defaultSecurityUserShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultSecurityUserShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the securityUserList where activated equals to UPDATED_ACTIVATED
        defaultSecurityUserShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activated is not null
        defaultSecurityUserShouldBeFound("activated.specified=true");

        // Get all the securityUserList where activated is null
        defaultSecurityUserShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLangKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where langKey equals to DEFAULT_LANG_KEY
        defaultSecurityUserShouldBeFound("langKey.equals=" + DEFAULT_LANG_KEY);

        // Get all the securityUserList where langKey equals to UPDATED_LANG_KEY
        defaultSecurityUserShouldNotBeFound("langKey.equals=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLangKeyIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where langKey in DEFAULT_LANG_KEY or UPDATED_LANG_KEY
        defaultSecurityUserShouldBeFound("langKey.in=" + DEFAULT_LANG_KEY + "," + UPDATED_LANG_KEY);

        // Get all the securityUserList where langKey equals to UPDATED_LANG_KEY
        defaultSecurityUserShouldNotBeFound("langKey.in=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLangKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where langKey is not null
        defaultSecurityUserShouldBeFound("langKey.specified=true");

        // Get all the securityUserList where langKey is null
        defaultSecurityUserShouldNotBeFound("langKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLangKeyContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where langKey contains DEFAULT_LANG_KEY
        defaultSecurityUserShouldBeFound("langKey.contains=" + DEFAULT_LANG_KEY);

        // Get all the securityUserList where langKey contains UPDATED_LANG_KEY
        defaultSecurityUserShouldNotBeFound("langKey.contains=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByLangKeyNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where langKey does not contain DEFAULT_LANG_KEY
        defaultSecurityUserShouldNotBeFound("langKey.doesNotContain=" + DEFAULT_LANG_KEY);

        // Get all the securityUserList where langKey does not contain UPDATED_LANG_KEY
        defaultSecurityUserShouldBeFound("langKey.doesNotContain=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivationKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activationKey equals to DEFAULT_ACTIVATION_KEY
        defaultSecurityUserShouldBeFound("activationKey.equals=" + DEFAULT_ACTIVATION_KEY);

        // Get all the securityUserList where activationKey equals to UPDATED_ACTIVATION_KEY
        defaultSecurityUserShouldNotBeFound("activationKey.equals=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivationKeyIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activationKey in DEFAULT_ACTIVATION_KEY or UPDATED_ACTIVATION_KEY
        defaultSecurityUserShouldBeFound("activationKey.in=" + DEFAULT_ACTIVATION_KEY + "," + UPDATED_ACTIVATION_KEY);

        // Get all the securityUserList where activationKey equals to UPDATED_ACTIVATION_KEY
        defaultSecurityUserShouldNotBeFound("activationKey.in=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivationKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activationKey is not null
        defaultSecurityUserShouldBeFound("activationKey.specified=true");

        // Get all the securityUserList where activationKey is null
        defaultSecurityUserShouldNotBeFound("activationKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivationKeyContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activationKey contains DEFAULT_ACTIVATION_KEY
        defaultSecurityUserShouldBeFound("activationKey.contains=" + DEFAULT_ACTIVATION_KEY);

        // Get all the securityUserList where activationKey contains UPDATED_ACTIVATION_KEY
        defaultSecurityUserShouldNotBeFound("activationKey.contains=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByActivationKeyNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where activationKey does not contain DEFAULT_ACTIVATION_KEY
        defaultSecurityUserShouldNotBeFound("activationKey.doesNotContain=" + DEFAULT_ACTIVATION_KEY);

        // Get all the securityUserList where activationKey does not contain UPDATED_ACTIVATION_KEY
        defaultSecurityUserShouldBeFound("activationKey.doesNotContain=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetKey equals to DEFAULT_RESET_KEY
        defaultSecurityUserShouldBeFound("resetKey.equals=" + DEFAULT_RESET_KEY);

        // Get all the securityUserList where resetKey equals to UPDATED_RESET_KEY
        defaultSecurityUserShouldNotBeFound("resetKey.equals=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetKeyIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetKey in DEFAULT_RESET_KEY or UPDATED_RESET_KEY
        defaultSecurityUserShouldBeFound("resetKey.in=" + DEFAULT_RESET_KEY + "," + UPDATED_RESET_KEY);

        // Get all the securityUserList where resetKey equals to UPDATED_RESET_KEY
        defaultSecurityUserShouldNotBeFound("resetKey.in=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetKey is not null
        defaultSecurityUserShouldBeFound("resetKey.specified=true");

        // Get all the securityUserList where resetKey is null
        defaultSecurityUserShouldNotBeFound("resetKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetKeyContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetKey contains DEFAULT_RESET_KEY
        defaultSecurityUserShouldBeFound("resetKey.contains=" + DEFAULT_RESET_KEY);

        // Get all the securityUserList where resetKey contains UPDATED_RESET_KEY
        defaultSecurityUserShouldNotBeFound("resetKey.contains=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetKeyNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetKey does not contain DEFAULT_RESET_KEY
        defaultSecurityUserShouldNotBeFound("resetKey.doesNotContain=" + DEFAULT_RESET_KEY);

        // Get all the securityUserList where resetKey does not contain UPDATED_RESET_KEY
        defaultSecurityUserShouldBeFound("resetKey.doesNotContain=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetDate equals to DEFAULT_RESET_DATE
        defaultSecurityUserShouldBeFound("resetDate.equals=" + DEFAULT_RESET_DATE);

        // Get all the securityUserList where resetDate equals to UPDATED_RESET_DATE
        defaultSecurityUserShouldNotBeFound("resetDate.equals=" + UPDATED_RESET_DATE);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetDateIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetDate in DEFAULT_RESET_DATE or UPDATED_RESET_DATE
        defaultSecurityUserShouldBeFound("resetDate.in=" + DEFAULT_RESET_DATE + "," + UPDATED_RESET_DATE);

        // Get all the securityUserList where resetDate equals to UPDATED_RESET_DATE
        defaultSecurityUserShouldNotBeFound("resetDate.in=" + UPDATED_RESET_DATE);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByResetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where resetDate is not null
        defaultSecurityUserShouldBeFound("resetDate.specified=true");

        // Get all the securityUserList where resetDate is null
        defaultSecurityUserShouldNotBeFound("resetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByMobileNoIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where mobileNo equals to DEFAULT_MOBILE_NO
        defaultSecurityUserShouldBeFound("mobileNo.equals=" + DEFAULT_MOBILE_NO);

        // Get all the securityUserList where mobileNo equals to UPDATED_MOBILE_NO
        defaultSecurityUserShouldNotBeFound("mobileNo.equals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByMobileNoIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where mobileNo in DEFAULT_MOBILE_NO or UPDATED_MOBILE_NO
        defaultSecurityUserShouldBeFound("mobileNo.in=" + DEFAULT_MOBILE_NO + "," + UPDATED_MOBILE_NO);

        // Get all the securityUserList where mobileNo equals to UPDATED_MOBILE_NO
        defaultSecurityUserShouldNotBeFound("mobileNo.in=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByMobileNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where mobileNo is not null
        defaultSecurityUserShouldBeFound("mobileNo.specified=true");

        // Get all the securityUserList where mobileNo is null
        defaultSecurityUserShouldNotBeFound("mobileNo.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByMobileNoContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where mobileNo contains DEFAULT_MOBILE_NO
        defaultSecurityUserShouldBeFound("mobileNo.contains=" + DEFAULT_MOBILE_NO);

        // Get all the securityUserList where mobileNo contains UPDATED_MOBILE_NO
        defaultSecurityUserShouldNotBeFound("mobileNo.contains=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByMobileNoNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where mobileNo does not contain DEFAULT_MOBILE_NO
        defaultSecurityUserShouldNotBeFound("mobileNo.doesNotContain=" + DEFAULT_MOBILE_NO);

        // Get all the securityUserList where mobileNo does not contain UPDATED_MOBILE_NO
        defaultSecurityUserShouldBeFound("mobileNo.doesNotContain=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdBy equals to DEFAULT_CREATED_BY
        defaultSecurityUserShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the securityUserList where createdBy equals to UPDATED_CREATED_BY
        defaultSecurityUserShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSecurityUserShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the securityUserList where createdBy equals to UPDATED_CREATED_BY
        defaultSecurityUserShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdBy is not null
        defaultSecurityUserShouldBeFound("createdBy.specified=true");

        // Get all the securityUserList where createdBy is null
        defaultSecurityUserShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdBy contains DEFAULT_CREATED_BY
        defaultSecurityUserShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the securityUserList where createdBy contains UPDATED_CREATED_BY
        defaultSecurityUserShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdBy does not contain DEFAULT_CREATED_BY
        defaultSecurityUserShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the securityUserList where createdBy does not contain UPDATED_CREATED_BY
        defaultSecurityUserShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdOn equals to DEFAULT_CREATED_ON
        defaultSecurityUserShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the securityUserList where createdOn equals to UPDATED_CREATED_ON
        defaultSecurityUserShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultSecurityUserShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the securityUserList where createdOn equals to UPDATED_CREATED_ON
        defaultSecurityUserShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllSecurityUsersByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUserList where createdOn is not null
        defaultSecurityUserShouldBeFound("createdOn.specified=true");

        // Get all the securityUserList where createdOn is null
        defaultSecurityUserShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityUsersBySocietyIsEqualToSomething() throws Exception {
        Society society;
        if (TestUtil.findAll(em, Society.class).isEmpty()) {
            securityUserRepository.saveAndFlush(securityUser);
            society = SocietyResourceIT.createEntity(em);
        } else {
            society = TestUtil.findAll(em, Society.class).get(0);
        }
        em.persist(society);
        em.flush();
        securityUser.setSociety(society);
        securityUserRepository.saveAndFlush(securityUser);
        Long societyId = society.getId();

        // Get all the securityUserList where society equals to societyId
        defaultSecurityUserShouldBeFound("societyId.equals=" + societyId);

        // Get all the securityUserList where society equals to (societyId + 1)
        defaultSecurityUserShouldNotBeFound("societyId.equals=" + (societyId + 1));
    }

    @Test
    @Transactional
    void getAllSecurityUsersBySecurityPermissionIsEqualToSomething() throws Exception {
        SecurityPermission securityPermission;
        if (TestUtil.findAll(em, SecurityPermission.class).isEmpty()) {
            securityUserRepository.saveAndFlush(securityUser);
            securityPermission = SecurityPermissionResourceIT.createEntity(em);
        } else {
            securityPermission = TestUtil.findAll(em, SecurityPermission.class).get(0);
        }
        em.persist(securityPermission);
        em.flush();
        securityUser.addSecurityPermission(securityPermission);
        securityUserRepository.saveAndFlush(securityUser);
        Long securityPermissionId = securityPermission.getId();

        // Get all the securityUserList where securityPermission equals to securityPermissionId
        defaultSecurityUserShouldBeFound("securityPermissionId.equals=" + securityPermissionId);

        // Get all the securityUserList where securityPermission equals to (securityPermissionId + 1)
        defaultSecurityUserShouldNotBeFound("securityPermissionId.equals=" + (securityPermissionId + 1));
    }

    @Test
    @Transactional
    void getAllSecurityUsersBySecurityRoleIsEqualToSomething() throws Exception {
        SecurityRole securityRole;
        if (TestUtil.findAll(em, SecurityRole.class).isEmpty()) {
            securityUserRepository.saveAndFlush(securityUser);
            securityRole = SecurityRoleResourceIT.createEntity(em);
        } else {
            securityRole = TestUtil.findAll(em, SecurityRole.class).get(0);
        }
        em.persist(securityRole);
        em.flush();
        securityUser.addSecurityRole(securityRole);
        securityUserRepository.saveAndFlush(securityUser);
        Long securityRoleId = securityRole.getId();

        // Get all the securityUserList where securityRole equals to securityRoleId
        defaultSecurityUserShouldBeFound("securityRoleId.equals=" + securityRoleId);

        // Get all the securityUserList where securityRole equals to (securityRoleId + 1)
        defaultSecurityUserShouldNotBeFound("securityRoleId.equals=" + (securityRoleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityUserShouldBeFound(String filter) throws Exception {
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY)))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(DEFAULT_RESET_DATE.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));

        // Check, that the count call also returns 1
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityUserShouldNotBeFound(String filter) throws Exception {
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityUser() throws Exception {
        // Get the securityUser
        restSecurityUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityUser() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();

        // Update the securityUser
        SecurityUser updatedSecurityUser = securityUserRepository.findById(securityUser.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityUser are not directly saved in db
        em.detach(updatedSecurityUser);
        updatedSecurityUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .designation(UPDATED_DESIGNATION)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .department(UPDATED_DEPARTMENT)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .mobileNo(UPDATED_MOBILE_NO)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON);
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(updatedSecurityUser);

        restSecurityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
        SecurityUser testSecurityUser = securityUserList.get(securityUserList.size() - 1);
        assertThat(testSecurityUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSecurityUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSecurityUser.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testSecurityUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSecurityUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityUser.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testSecurityUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testSecurityUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSecurityUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testSecurityUser.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testSecurityUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testSecurityUser.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testSecurityUser.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testSecurityUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSecurityUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSecurityUserWithPatch() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();

        // Update the securityUser using partial update
        SecurityUser partialUpdatedSecurityUser = new SecurityUser();
        partialUpdatedSecurityUser.setId(securityUser.getId());

        partialUpdatedSecurityUser
            .lastName(UPDATED_LAST_NAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .langKey(UPDATED_LANG_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON);

        restSecurityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityUser))
            )
            .andExpect(status().isOk());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
        SecurityUser testSecurityUser = securityUserList.get(securityUserList.size() - 1);
        assertThat(testSecurityUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSecurityUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSecurityUser.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testSecurityUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSecurityUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityUser.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testSecurityUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testSecurityUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSecurityUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testSecurityUser.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testSecurityUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testSecurityUser.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testSecurityUser.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testSecurityUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSecurityUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateSecurityUserWithPatch() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();

        // Update the securityUser using partial update
        SecurityUser partialUpdatedSecurityUser = new SecurityUser();
        partialUpdatedSecurityUser.setId(securityUser.getId());

        partialUpdatedSecurityUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .designation(UPDATED_DESIGNATION)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .department(UPDATED_DEPARTMENT)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .mobileNo(UPDATED_MOBILE_NO)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON);

        restSecurityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityUser))
            )
            .andExpect(status().isOk());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
        SecurityUser testSecurityUser = securityUserList.get(securityUserList.size() - 1);
        assertThat(testSecurityUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSecurityUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSecurityUser.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testSecurityUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSecurityUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityUser.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testSecurityUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testSecurityUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSecurityUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testSecurityUser.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testSecurityUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testSecurityUser.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testSecurityUser.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testSecurityUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSecurityUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityUser() throws Exception {
        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();
        securityUser.setId(count.incrementAndGet());

        // Create the SecurityUser
        SecurityUserDTO securityUserDTO = securityUserMapper.toDto(securityUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSecurityUser() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        int databaseSizeBeforeDelete = securityUserRepository.findAll().size();

        // Delete the securityUser
        restSecurityUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityUser> securityUserList = securityUserRepository.findAll();
        assertThat(securityUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
