package com.techvg.pacs.service;

import com.techvg.pacs.domain.Village;
import com.techvg.pacs.repository.VillageRepository;
import com.techvg.pacs.service.dto.VillageDTO;
import com.techvg.pacs.service.mapper.VillageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Village}.
 */
@Service
@Transactional
public class VillageService {

    private final Logger log = LoggerFactory.getLogger(VillageService.class);

    private final VillageRepository villageRepository;

    private final VillageMapper villageMapper;

    public VillageService(VillageRepository villageRepository, VillageMapper villageMapper) {
        this.villageRepository = villageRepository;
        this.villageMapper = villageMapper;
    }

    /**
     * Save a village.
     *
     * @param villageDTO the entity to save.
     * @return the persisted entity.
     */
    public VillageDTO save(VillageDTO villageDTO) {
        log.debug("Request to save Village : {}", villageDTO);
        Village village = villageMapper.toEntity(villageDTO);
        village = villageRepository.save(village);
        return villageMapper.toDto(village);
    }

    /**
     * Update a village.
     *
     * @param villageDTO the entity to save.
     * @return the persisted entity.
     */
    public VillageDTO update(VillageDTO villageDTO) {
        log.debug("Request to save Village : {}", villageDTO);
        Village village = villageMapper.toEntity(villageDTO);
        village = villageRepository.save(village);
        return villageMapper.toDto(village);
    }

    /**
     * Partially update a village.
     *
     * @param villageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VillageDTO> partialUpdate(VillageDTO villageDTO) {
        log.debug("Request to partially update Village : {}", villageDTO);

        return villageRepository
            .findById(villageDTO.getId())
            .map(existingVillage -> {
                villageMapper.partialUpdate(existingVillage, villageDTO);

                return existingVillage;
            })
            .map(villageRepository::save)
            .map(villageMapper::toDto);
    }

    /**
     * Get all the villages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VillageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Villages");
        return villageRepository.findAll(pageable).map(villageMapper::toDto);
    }

    /**
     * Get one village by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VillageDTO> findOne(Long id) {
        log.debug("Request to get Village : {}", id);
        return villageRepository.findById(id).map(villageMapper::toDto);
    }

    /**
     * Delete the village by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Village : {}", id);
        villageRepository.deleteById(id);
    }
}
