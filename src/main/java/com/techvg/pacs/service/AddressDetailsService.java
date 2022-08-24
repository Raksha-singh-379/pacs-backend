package com.techvg.pacs.service;

import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.repository.AddressDetailsRepository;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.service.mapper.AddressDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AddressDetails}.
 */
@Service
@Transactional
public class AddressDetailsService {

    private final Logger log = LoggerFactory.getLogger(AddressDetailsService.class);

    private final AddressDetailsRepository addressDetailsRepository;

    private final AddressDetailsMapper addressDetailsMapper;

    public AddressDetailsService(AddressDetailsRepository addressDetailsRepository, AddressDetailsMapper addressDetailsMapper) {
        this.addressDetailsRepository = addressDetailsRepository;
        this.addressDetailsMapper = addressDetailsMapper;
    }

    /**
     * Save a addressDetails.
     *
     * @param addressDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDetailsDTO save(AddressDetailsDTO addressDetailsDTO) {
        log.debug("Request to save AddressDetails : {}", addressDetailsDTO);
        AddressDetails addressDetails = addressDetailsMapper.toEntity(addressDetailsDTO);
        addressDetails = addressDetailsRepository.save(addressDetails);
        return addressDetailsMapper.toDto(addressDetails);
    }

    /**
     * Update a addressDetails.
     *
     * @param addressDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDetailsDTO update(AddressDetailsDTO addressDetailsDTO) {
        log.debug("Request to save AddressDetails : {}", addressDetailsDTO);
        AddressDetails addressDetails = addressDetailsMapper.toEntity(addressDetailsDTO);
        addressDetails = addressDetailsRepository.save(addressDetails);
        return addressDetailsMapper.toDto(addressDetails);
    }

    /**
     * Partially update a addressDetails.
     *
     * @param addressDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AddressDetailsDTO> partialUpdate(AddressDetailsDTO addressDetailsDTO) {
        log.debug("Request to partially update AddressDetails : {}", addressDetailsDTO);

        return addressDetailsRepository
            .findById(addressDetailsDTO.getId())
            .map(existingAddressDetails -> {
                addressDetailsMapper.partialUpdate(existingAddressDetails, addressDetailsDTO);

                return existingAddressDetails;
            })
            .map(addressDetailsRepository::save)
            .map(addressDetailsMapper::toDto);
    }

    /**
     * Get all the addressDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AddressDetails");
        return addressDetailsRepository.findAll(pageable).map(addressDetailsMapper::toDto);
    }

    /**
     * Get one addressDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AddressDetailsDTO> findOne(Long id) {
        log.debug("Request to get AddressDetails : {}", id);
        return addressDetailsRepository.findById(id).map(addressDetailsMapper::toDto);
    }

    /**
     * Delete the addressDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AddressDetails : {}", id);
        addressDetailsRepository.deleteById(id);
    }
}
