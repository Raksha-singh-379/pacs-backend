package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.Village;
import com.techvg.pacs.service.dto.VillageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Village} and its DTO {@link VillageDTO}.
 */
@Mapper(componentModel = "spring")
public interface VillageMapper extends EntityMapper<VillageDTO, Village> {}
