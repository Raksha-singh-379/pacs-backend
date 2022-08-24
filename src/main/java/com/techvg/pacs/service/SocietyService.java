package com.techvg.pacs.service;

import com.techvg.pacs.domain.Society;
import com.techvg.pacs.repository.SocietyRepository;
import com.techvg.pacs.service.dto.SocietyDTO;
import com.techvg.pacs.service.mapper.SocietyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Society}.
 */
@Service
@Transactional
public class SocietyService {

    private final Logger log = LoggerFactory.getLogger(SocietyService.class);

    private final SocietyRepository societyRepository;

    private final SocietyMapper societyMapper;

    public SocietyService(SocietyRepository societyRepository, SocietyMapper societyMapper) {
        this.societyRepository = societyRepository;
        this.societyMapper = societyMapper;
    }

    /**
     * Save a society.
     *
     * @param societyDTO the entity to save.
     * @return the persisted entity.
     */
    public SocietyDTO save(SocietyDTO societyDTO) {
        log.debug("Request to save Society : {}", societyDTO);
        Society society = societyMapper.toEntity(societyDTO);
        society = societyRepository.save(society);
        return societyMapper.toDto(society);
    }

    /**
     * Update a society.
     *
     * @param societyDTO the entity to save.
     * @return the persisted entity.
     */
    public SocietyDTO update(SocietyDTO societyDTO) {
        log.debug("Request to save Society : {}", societyDTO);
        Society society = societyMapper.toEntity(societyDTO);
        society = societyRepository.save(society);
        return societyMapper.toDto(society);
    }

    /**
     * Partially update a society.
     *
     * @param societyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SocietyDTO> partialUpdate(SocietyDTO societyDTO) {
        log.debug("Request to partially update Society : {}", societyDTO);

        return societyRepository
            .findById(societyDTO.getId())
            .map(existingSociety -> {
                societyMapper.partialUpdate(existingSociety, societyDTO);

                return existingSociety;
            })
            .map(societyRepository::save)
            .map(societyMapper::toDto);
    }

    /**
     * Get all the societies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocietyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Societies");
        return societyRepository.findAll(pageable).map(societyMapper::toDto);
    }

    /**
     * Get one society by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocietyDTO> findOne(Long id) {
        log.debug("Request to get Society : {}", id);
        return societyRepository.findById(id).map(societyMapper::toDto);
    }

    /**
     * Delete the society by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Society : {}", id);
        societyRepository.deleteById(id);
    }
}
