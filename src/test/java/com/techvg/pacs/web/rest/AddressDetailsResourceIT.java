package com.techvg.pacs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.pacs.IntegrationTest;
import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.domain.District;
import com.techvg.pacs.domain.Member;
import com.techvg.pacs.domain.SecurityUser;
import com.techvg.pacs.domain.State;
import com.techvg.pacs.domain.Taluka;
import com.techvg.pacs.domain.Village;
import com.techvg.pacs.domain.enumeration.AddressType;
import com.techvg.pacs.repository.AddressDetailsRepository;
import com.techvg.pacs.service.criteria.AddressDetailsCriteria;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.service.mapper.AddressDetailsMapper;
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
 * Integration tests for the {@link AddressDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressDetailsResourceIT {

    private static final AddressType DEFAULT_TYPE = AddressType.CURRENT_ADDRESS;
    private static final AddressType UPDATED_TYPE = AddressType.PERMANENT_ADDRESS;

    private static final String DEFAULT_HOUSE_NO = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ROAD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROAD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAND_MARK = "AAAAAAAAAA";
    private static final String UPDATED_LAND_MARK = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

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

    private static final String DEFAULT_FREE_FIELD_3 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_3 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/address-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressDetailsRepository addressDetailsRepository;

    @Autowired
    private AddressDetailsMapper addressDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressDetailsMockMvc;

    private AddressDetails addressDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressDetails createEntity(EntityManager em) {
        AddressDetails addressDetails = new AddressDetails()
            .type(DEFAULT_TYPE)
            .houseNo(DEFAULT_HOUSE_NO)
            .roadName(DEFAULT_ROAD_NAME)
            .landMark(DEFAULT_LAND_MARK)
            .pincode(DEFAULT_PINCODE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isDeleted(DEFAULT_IS_DELETED)
            .freeField1(DEFAULT_FREE_FIELD_1)
            .freeField2(DEFAULT_FREE_FIELD_2)
            .freeField3(DEFAULT_FREE_FIELD_3);
        return addressDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressDetails createUpdatedEntity(EntityManager em) {
        AddressDetails addressDetails = new AddressDetails()
            .type(UPDATED_TYPE)
            .houseNo(UPDATED_HOUSE_NO)
            .roadName(UPDATED_ROAD_NAME)
            .landMark(UPDATED_LAND_MARK)
            .pincode(UPDATED_PINCODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3);
        return addressDetails;
    }

    @BeforeEach
    public void initTest() {
        addressDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createAddressDetails() throws Exception {
        int databaseSizeBeforeCreate = addressDetailsRepository.findAll().size();
        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);
        restAddressDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAddressDetails.getHouseNo()).isEqualTo(DEFAULT_HOUSE_NO);
        assertThat(testAddressDetails.getRoadName()).isEqualTo(DEFAULT_ROAD_NAME);
        assertThat(testAddressDetails.getLandMark()).isEqualTo(DEFAULT_LAND_MARK);
        assertThat(testAddressDetails.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testAddressDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAddressDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAddressDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAddressDetails.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAddressDetails.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testAddressDetails.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testAddressDetails.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
        assertThat(testAddressDetails.getFreeField3()).isEqualTo(DEFAULT_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void createAddressDetailsWithExistingId() throws Exception {
        // Create the AddressDetails with an existing ID
        addressDetails.setId(1L);
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        int databaseSizeBeforeCreate = addressDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO)))
            .andExpect(jsonPath("$.[*].roadName").value(hasItem(DEFAULT_ROAD_NAME)))
            .andExpect(jsonPath("$.[*].landMark").value(hasItem(DEFAULT_LAND_MARK)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)))
            .andExpect(jsonPath("$.[*].freeField3").value(hasItem(DEFAULT_FREE_FIELD_3)));
    }

    @Test
    @Transactional
    void getAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get the addressDetails
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, addressDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addressDetails.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.houseNo").value(DEFAULT_HOUSE_NO))
            .andExpect(jsonPath("$.roadName").value(DEFAULT_ROAD_NAME))
            .andExpect(jsonPath("$.landMark").value(DEFAULT_LAND_MARK))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.freeField1").value(DEFAULT_FREE_FIELD_1))
            .andExpect(jsonPath("$.freeField2").value(DEFAULT_FREE_FIELD_2))
            .andExpect(jsonPath("$.freeField3").value(DEFAULT_FREE_FIELD_3));
    }

    @Test
    @Transactional
    void getAddressDetailsByIdFiltering() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        Long id = addressDetails.getId();

        defaultAddressDetailsShouldBeFound("id.equals=" + id);
        defaultAddressDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultAddressDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where type equals to DEFAULT_TYPE
        defaultAddressDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the addressDetailsList where type equals to UPDATED_TYPE
        defaultAddressDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAddressDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the addressDetailsList where type equals to UPDATED_TYPE
        defaultAddressDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where type is not null
        defaultAddressDetailsShouldBeFound("type.specified=true");

        // Get all the addressDetailsList where type is null
        defaultAddressDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByHouseNoIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where houseNo equals to DEFAULT_HOUSE_NO
        defaultAddressDetailsShouldBeFound("houseNo.equals=" + DEFAULT_HOUSE_NO);

        // Get all the addressDetailsList where houseNo equals to UPDATED_HOUSE_NO
        defaultAddressDetailsShouldNotBeFound("houseNo.equals=" + UPDATED_HOUSE_NO);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByHouseNoIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where houseNo in DEFAULT_HOUSE_NO or UPDATED_HOUSE_NO
        defaultAddressDetailsShouldBeFound("houseNo.in=" + DEFAULT_HOUSE_NO + "," + UPDATED_HOUSE_NO);

        // Get all the addressDetailsList where houseNo equals to UPDATED_HOUSE_NO
        defaultAddressDetailsShouldNotBeFound("houseNo.in=" + UPDATED_HOUSE_NO);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByHouseNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where houseNo is not null
        defaultAddressDetailsShouldBeFound("houseNo.specified=true");

        // Get all the addressDetailsList where houseNo is null
        defaultAddressDetailsShouldNotBeFound("houseNo.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByHouseNoContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where houseNo contains DEFAULT_HOUSE_NO
        defaultAddressDetailsShouldBeFound("houseNo.contains=" + DEFAULT_HOUSE_NO);

        // Get all the addressDetailsList where houseNo contains UPDATED_HOUSE_NO
        defaultAddressDetailsShouldNotBeFound("houseNo.contains=" + UPDATED_HOUSE_NO);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByHouseNoNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where houseNo does not contain DEFAULT_HOUSE_NO
        defaultAddressDetailsShouldNotBeFound("houseNo.doesNotContain=" + DEFAULT_HOUSE_NO);

        // Get all the addressDetailsList where houseNo does not contain UPDATED_HOUSE_NO
        defaultAddressDetailsShouldBeFound("houseNo.doesNotContain=" + UPDATED_HOUSE_NO);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByRoadNameIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where roadName equals to DEFAULT_ROAD_NAME
        defaultAddressDetailsShouldBeFound("roadName.equals=" + DEFAULT_ROAD_NAME);

        // Get all the addressDetailsList where roadName equals to UPDATED_ROAD_NAME
        defaultAddressDetailsShouldNotBeFound("roadName.equals=" + UPDATED_ROAD_NAME);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByRoadNameIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where roadName in DEFAULT_ROAD_NAME or UPDATED_ROAD_NAME
        defaultAddressDetailsShouldBeFound("roadName.in=" + DEFAULT_ROAD_NAME + "," + UPDATED_ROAD_NAME);

        // Get all the addressDetailsList where roadName equals to UPDATED_ROAD_NAME
        defaultAddressDetailsShouldNotBeFound("roadName.in=" + UPDATED_ROAD_NAME);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByRoadNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where roadName is not null
        defaultAddressDetailsShouldBeFound("roadName.specified=true");

        // Get all the addressDetailsList where roadName is null
        defaultAddressDetailsShouldNotBeFound("roadName.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByRoadNameContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where roadName contains DEFAULT_ROAD_NAME
        defaultAddressDetailsShouldBeFound("roadName.contains=" + DEFAULT_ROAD_NAME);

        // Get all the addressDetailsList where roadName contains UPDATED_ROAD_NAME
        defaultAddressDetailsShouldNotBeFound("roadName.contains=" + UPDATED_ROAD_NAME);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByRoadNameNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where roadName does not contain DEFAULT_ROAD_NAME
        defaultAddressDetailsShouldNotBeFound("roadName.doesNotContain=" + DEFAULT_ROAD_NAME);

        // Get all the addressDetailsList where roadName does not contain UPDATED_ROAD_NAME
        defaultAddressDetailsShouldBeFound("roadName.doesNotContain=" + UPDATED_ROAD_NAME);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLandMarkIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where landMark equals to DEFAULT_LAND_MARK
        defaultAddressDetailsShouldBeFound("landMark.equals=" + DEFAULT_LAND_MARK);

        // Get all the addressDetailsList where landMark equals to UPDATED_LAND_MARK
        defaultAddressDetailsShouldNotBeFound("landMark.equals=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLandMarkIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where landMark in DEFAULT_LAND_MARK or UPDATED_LAND_MARK
        defaultAddressDetailsShouldBeFound("landMark.in=" + DEFAULT_LAND_MARK + "," + UPDATED_LAND_MARK);

        // Get all the addressDetailsList where landMark equals to UPDATED_LAND_MARK
        defaultAddressDetailsShouldNotBeFound("landMark.in=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLandMarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where landMark is not null
        defaultAddressDetailsShouldBeFound("landMark.specified=true");

        // Get all the addressDetailsList where landMark is null
        defaultAddressDetailsShouldNotBeFound("landMark.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLandMarkContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where landMark contains DEFAULT_LAND_MARK
        defaultAddressDetailsShouldBeFound("landMark.contains=" + DEFAULT_LAND_MARK);

        // Get all the addressDetailsList where landMark contains UPDATED_LAND_MARK
        defaultAddressDetailsShouldNotBeFound("landMark.contains=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLandMarkNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where landMark does not contain DEFAULT_LAND_MARK
        defaultAddressDetailsShouldNotBeFound("landMark.doesNotContain=" + DEFAULT_LAND_MARK);

        // Get all the addressDetailsList where landMark does not contain UPDATED_LAND_MARK
        defaultAddressDetailsShouldBeFound("landMark.doesNotContain=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where pincode equals to DEFAULT_PINCODE
        defaultAddressDetailsShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the addressDetailsList where pincode equals to UPDATED_PINCODE
        defaultAddressDetailsShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultAddressDetailsShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the addressDetailsList where pincode equals to UPDATED_PINCODE
        defaultAddressDetailsShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where pincode is not null
        defaultAddressDetailsShouldBeFound("pincode.specified=true");

        // Get all the addressDetailsList where pincode is null
        defaultAddressDetailsShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByPincodeContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where pincode contains DEFAULT_PINCODE
        defaultAddressDetailsShouldBeFound("pincode.contains=" + DEFAULT_PINCODE);

        // Get all the addressDetailsList where pincode contains UPDATED_PINCODE
        defaultAddressDetailsShouldNotBeFound("pincode.contains=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByPincodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where pincode does not contain DEFAULT_PINCODE
        defaultAddressDetailsShouldNotBeFound("pincode.doesNotContain=" + DEFAULT_PINCODE);

        // Get all the addressDetailsList where pincode does not contain UPDATED_PINCODE
        defaultAddressDetailsShouldBeFound("pincode.doesNotContain=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultAddressDetailsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the addressDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAddressDetailsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultAddressDetailsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the addressDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAddressDetailsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModified is not null
        defaultAddressDetailsShouldBeFound("lastModified.specified=true");

        // Get all the addressDetailsList where lastModified is null
        defaultAddressDetailsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAddressDetailsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressDetailsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAddressDetailsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the addressDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressDetailsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModifiedBy is not null
        defaultAddressDetailsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the addressDetailsList where lastModifiedBy is null
        defaultAddressDetailsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAddressDetailsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressDetailsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAddressDetailsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAddressDetailsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressDetailsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAddressDetailsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdBy equals to DEFAULT_CREATED_BY
        defaultAddressDetailsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the addressDetailsList where createdBy equals to UPDATED_CREATED_BY
        defaultAddressDetailsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAddressDetailsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the addressDetailsList where createdBy equals to UPDATED_CREATED_BY
        defaultAddressDetailsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdBy is not null
        defaultAddressDetailsShouldBeFound("createdBy.specified=true");

        // Get all the addressDetailsList where createdBy is null
        defaultAddressDetailsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdBy contains DEFAULT_CREATED_BY
        defaultAddressDetailsShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the addressDetailsList where createdBy contains UPDATED_CREATED_BY
        defaultAddressDetailsShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdBy does not contain DEFAULT_CREATED_BY
        defaultAddressDetailsShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the addressDetailsList where createdBy does not contain UPDATED_CREATED_BY
        defaultAddressDetailsShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdOn equals to DEFAULT_CREATED_ON
        defaultAddressDetailsShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the addressDetailsList where createdOn equals to UPDATED_CREATED_ON
        defaultAddressDetailsShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultAddressDetailsShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the addressDetailsList where createdOn equals to UPDATED_CREATED_ON
        defaultAddressDetailsShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where createdOn is not null
        defaultAddressDetailsShouldBeFound("createdOn.specified=true");

        // Get all the addressDetailsList where createdOn is null
        defaultAddressDetailsShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where isDeleted equals to DEFAULT_IS_DELETED
        defaultAddressDetailsShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the addressDetailsList where isDeleted equals to UPDATED_IS_DELETED
        defaultAddressDetailsShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultAddressDetailsShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the addressDetailsList where isDeleted equals to UPDATED_IS_DELETED
        defaultAddressDetailsShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where isDeleted is not null
        defaultAddressDetailsShouldBeFound("isDeleted.specified=true");

        // Get all the addressDetailsList where isDeleted is null
        defaultAddressDetailsShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField1IsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField1 equals to DEFAULT_FREE_FIELD_1
        defaultAddressDetailsShouldBeFound("freeField1.equals=" + DEFAULT_FREE_FIELD_1);

        // Get all the addressDetailsList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultAddressDetailsShouldNotBeFound("freeField1.equals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField1IsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField1 in DEFAULT_FREE_FIELD_1 or UPDATED_FREE_FIELD_1
        defaultAddressDetailsShouldBeFound("freeField1.in=" + DEFAULT_FREE_FIELD_1 + "," + UPDATED_FREE_FIELD_1);

        // Get all the addressDetailsList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultAddressDetailsShouldNotBeFound("freeField1.in=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField1IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField1 is not null
        defaultAddressDetailsShouldBeFound("freeField1.specified=true");

        // Get all the addressDetailsList where freeField1 is null
        defaultAddressDetailsShouldNotBeFound("freeField1.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField1ContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField1 contains DEFAULT_FREE_FIELD_1
        defaultAddressDetailsShouldBeFound("freeField1.contains=" + DEFAULT_FREE_FIELD_1);

        // Get all the addressDetailsList where freeField1 contains UPDATED_FREE_FIELD_1
        defaultAddressDetailsShouldNotBeFound("freeField1.contains=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField1NotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField1 does not contain DEFAULT_FREE_FIELD_1
        defaultAddressDetailsShouldNotBeFound("freeField1.doesNotContain=" + DEFAULT_FREE_FIELD_1);

        // Get all the addressDetailsList where freeField1 does not contain UPDATED_FREE_FIELD_1
        defaultAddressDetailsShouldBeFound("freeField1.doesNotContain=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField2 equals to DEFAULT_FREE_FIELD_2
        defaultAddressDetailsShouldBeFound("freeField2.equals=" + DEFAULT_FREE_FIELD_2);

        // Get all the addressDetailsList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultAddressDetailsShouldNotBeFound("freeField2.equals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField2IsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField2 in DEFAULT_FREE_FIELD_2 or UPDATED_FREE_FIELD_2
        defaultAddressDetailsShouldBeFound("freeField2.in=" + DEFAULT_FREE_FIELD_2 + "," + UPDATED_FREE_FIELD_2);

        // Get all the addressDetailsList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultAddressDetailsShouldNotBeFound("freeField2.in=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField2 is not null
        defaultAddressDetailsShouldBeFound("freeField2.specified=true");

        // Get all the addressDetailsList where freeField2 is null
        defaultAddressDetailsShouldNotBeFound("freeField2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField2ContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField2 contains DEFAULT_FREE_FIELD_2
        defaultAddressDetailsShouldBeFound("freeField2.contains=" + DEFAULT_FREE_FIELD_2);

        // Get all the addressDetailsList where freeField2 contains UPDATED_FREE_FIELD_2
        defaultAddressDetailsShouldNotBeFound("freeField2.contains=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField2NotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField2 does not contain DEFAULT_FREE_FIELD_2
        defaultAddressDetailsShouldNotBeFound("freeField2.doesNotContain=" + DEFAULT_FREE_FIELD_2);

        // Get all the addressDetailsList where freeField2 does not contain UPDATED_FREE_FIELD_2
        defaultAddressDetailsShouldBeFound("freeField2.doesNotContain=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField3IsEqualToSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField3 equals to DEFAULT_FREE_FIELD_3
        defaultAddressDetailsShouldBeFound("freeField3.equals=" + DEFAULT_FREE_FIELD_3);

        // Get all the addressDetailsList where freeField3 equals to UPDATED_FREE_FIELD_3
        defaultAddressDetailsShouldNotBeFound("freeField3.equals=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField3IsInShouldWork() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField3 in DEFAULT_FREE_FIELD_3 or UPDATED_FREE_FIELD_3
        defaultAddressDetailsShouldBeFound("freeField3.in=" + DEFAULT_FREE_FIELD_3 + "," + UPDATED_FREE_FIELD_3);

        // Get all the addressDetailsList where freeField3 equals to UPDATED_FREE_FIELD_3
        defaultAddressDetailsShouldNotBeFound("freeField3.in=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField3IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField3 is not null
        defaultAddressDetailsShouldBeFound("freeField3.specified=true");

        // Get all the addressDetailsList where freeField3 is null
        defaultAddressDetailsShouldNotBeFound("freeField3.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField3ContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField3 contains DEFAULT_FREE_FIELD_3
        defaultAddressDetailsShouldBeFound("freeField3.contains=" + DEFAULT_FREE_FIELD_3);

        // Get all the addressDetailsList where freeField3 contains UPDATED_FREE_FIELD_3
        defaultAddressDetailsShouldNotBeFound("freeField3.contains=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByFreeField3NotContainsSomething() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList where freeField3 does not contain DEFAULT_FREE_FIELD_3
        defaultAddressDetailsShouldNotBeFound("freeField3.doesNotContain=" + DEFAULT_FREE_FIELD_3);

        // Get all the addressDetailsList where freeField3 does not contain UPDATED_FREE_FIELD_3
        defaultAddressDetailsShouldBeFound("freeField3.doesNotContain=" + UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void getAllAddressDetailsByStateIsEqualToSomething() throws Exception {
        State state;
        if (TestUtil.findAll(em, State.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            state = StateResourceIT.createEntity(em);
        } else {
            state = TestUtil.findAll(em, State.class).get(0);
        }
        em.persist(state);
        em.flush();
        addressDetails.setState(state);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long stateId = state.getId();

        // Get all the addressDetailsList where state equals to stateId
        defaultAddressDetailsShouldBeFound("stateId.equals=" + stateId);

        // Get all the addressDetailsList where state equals to (stateId + 1)
        defaultAddressDetailsShouldNotBeFound("stateId.equals=" + (stateId + 1));
    }

    @Test
    @Transactional
    void getAllAddressDetailsByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        addressDetails.setDistrict(district);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long districtId = district.getId();

        // Get all the addressDetailsList where district equals to districtId
        defaultAddressDetailsShouldBeFound("districtId.equals=" + districtId);

        // Get all the addressDetailsList where district equals to (districtId + 1)
        defaultAddressDetailsShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    @Test
    @Transactional
    void getAllAddressDetailsByTalukaIsEqualToSomething() throws Exception {
        Taluka taluka;
        if (TestUtil.findAll(em, Taluka.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            taluka = TalukaResourceIT.createEntity(em);
        } else {
            taluka = TestUtil.findAll(em, Taluka.class).get(0);
        }
        em.persist(taluka);
        em.flush();
        addressDetails.setTaluka(taluka);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long talukaId = taluka.getId();

        // Get all the addressDetailsList where taluka equals to talukaId
        defaultAddressDetailsShouldBeFound("talukaId.equals=" + talukaId);

        // Get all the addressDetailsList where taluka equals to (talukaId + 1)
        defaultAddressDetailsShouldNotBeFound("talukaId.equals=" + (talukaId + 1));
    }

    @Test
    @Transactional
    void getAllAddressDetailsByTalukaIsEqualToSomething() throws Exception {
        Village taluka;
        if (TestUtil.findAll(em, Village.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            taluka = VillageResourceIT.createEntity(em);
        } else {
            taluka = TestUtil.findAll(em, Village.class).get(0);
        }
        em.persist(taluka);
        em.flush();
        addressDetails.setTaluka(taluka);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long talukaId = taluka.getId();

        // Get all the addressDetailsList where taluka equals to talukaId
        defaultAddressDetailsShouldBeFound("talukaId.equals=" + talukaId);

        // Get all the addressDetailsList where taluka equals to (talukaId + 1)
        defaultAddressDetailsShouldNotBeFound("talukaId.equals=" + (talukaId + 1));
    }

    @Test
    @Transactional
    void getAllAddressDetailsBySecurityUserIsEqualToSomething() throws Exception {
        SecurityUser securityUser;
        if (TestUtil.findAll(em, SecurityUser.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            securityUser = SecurityUserResourceIT.createEntity(em);
        } else {
            securityUser = TestUtil.findAll(em, SecurityUser.class).get(0);
        }
        em.persist(securityUser);
        em.flush();
        addressDetails.setSecurityUser(securityUser);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long securityUserId = securityUser.getId();

        // Get all the addressDetailsList where securityUser equals to securityUserId
        defaultAddressDetailsShouldBeFound("securityUserId.equals=" + securityUserId);

        // Get all the addressDetailsList where securityUser equals to (securityUserId + 1)
        defaultAddressDetailsShouldNotBeFound("securityUserId.equals=" + (securityUserId + 1));
    }

    @Test
    @Transactional
    void getAllAddressDetailsByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            addressDetailsRepository.saveAndFlush(addressDetails);
            member = MemberResourceIT.createEntity(em);
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        addressDetails.setMember(member);
        addressDetailsRepository.saveAndFlush(addressDetails);
        Long memberId = member.getId();

        // Get all the addressDetailsList where member equals to memberId
        defaultAddressDetailsShouldBeFound("memberId.equals=" + memberId);

        // Get all the addressDetailsList where member equals to (memberId + 1)
        defaultAddressDetailsShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressDetailsShouldBeFound(String filter) throws Exception {
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO)))
            .andExpect(jsonPath("$.[*].roadName").value(hasItem(DEFAULT_ROAD_NAME)))
            .andExpect(jsonPath("$.[*].landMark").value(hasItem(DEFAULT_LAND_MARK)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)))
            .andExpect(jsonPath("$.[*].freeField3").value(hasItem(DEFAULT_FREE_FIELD_3)));

        // Check, that the count call also returns 1
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressDetailsShouldNotBeFound(String filter) throws Exception {
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddressDetails() throws Exception {
        // Get the addressDetails
        restAddressDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails
        AddressDetails updatedAddressDetails = addressDetailsRepository.findById(addressDetails.getId()).get();
        // Disconnect from session so that the updates on updatedAddressDetails are not directly saved in db
        em.detach(updatedAddressDetails);
        updatedAddressDetails
            .type(UPDATED_TYPE)
            .houseNo(UPDATED_HOUSE_NO)
            .roadName(UPDATED_ROAD_NAME)
            .landMark(UPDATED_LAND_MARK)
            .pincode(UPDATED_PINCODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3);
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(updatedAddressDetails);

        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAddressDetails.getHouseNo()).isEqualTo(UPDATED_HOUSE_NO);
        assertThat(testAddressDetails.getRoadName()).isEqualTo(UPDATED_ROAD_NAME);
        assertThat(testAddressDetails.getLandMark()).isEqualTo(UPDATED_LAND_MARK);
        assertThat(testAddressDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddressDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAddressDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddressDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddressDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAddressDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAddressDetails.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testAddressDetails.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
        assertThat(testAddressDetails.getFreeField3()).isEqualTo(UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void putNonExistingAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressDetailsWithPatch() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails using partial update
        AddressDetails partialUpdatedAddressDetails = new AddressDetails();
        partialUpdatedAddressDetails.setId(addressDetails.getId());

        partialUpdatedAddressDetails
            .landMark(UPDATED_LAND_MARK)
            .pincode(UPDATED_PINCODE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField3(UPDATED_FREE_FIELD_3);

        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressDetails))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAddressDetails.getHouseNo()).isEqualTo(DEFAULT_HOUSE_NO);
        assertThat(testAddressDetails.getRoadName()).isEqualTo(DEFAULT_ROAD_NAME);
        assertThat(testAddressDetails.getLandMark()).isEqualTo(UPDATED_LAND_MARK);
        assertThat(testAddressDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddressDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAddressDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddressDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddressDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAddressDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAddressDetails.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testAddressDetails.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
        assertThat(testAddressDetails.getFreeField3()).isEqualTo(UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void fullUpdateAddressDetailsWithPatch() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails using partial update
        AddressDetails partialUpdatedAddressDetails = new AddressDetails();
        partialUpdatedAddressDetails.setId(addressDetails.getId());

        partialUpdatedAddressDetails
            .type(UPDATED_TYPE)
            .houseNo(UPDATED_HOUSE_NO)
            .roadName(UPDATED_ROAD_NAME)
            .landMark(UPDATED_LAND_MARK)
            .pincode(UPDATED_PINCODE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isDeleted(UPDATED_IS_DELETED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2)
            .freeField3(UPDATED_FREE_FIELD_3);

        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressDetails))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAddressDetails.getHouseNo()).isEqualTo(UPDATED_HOUSE_NO);
        assertThat(testAddressDetails.getRoadName()).isEqualTo(UPDATED_ROAD_NAME);
        assertThat(testAddressDetails.getLandMark()).isEqualTo(UPDATED_LAND_MARK);
        assertThat(testAddressDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddressDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAddressDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddressDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddressDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAddressDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAddressDetails.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testAddressDetails.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
        assertThat(testAddressDetails.getFreeField3()).isEqualTo(UPDATED_FREE_FIELD_3);
    }

    @Test
    @Transactional
    void patchNonExistingAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // Create the AddressDetails
        AddressDetailsDTO addressDetailsDTO = addressDetailsMapper.toDto(addressDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeDelete = addressDetailsRepository.findAll().size();

        // Delete the addressDetails
        restAddressDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, addressDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
