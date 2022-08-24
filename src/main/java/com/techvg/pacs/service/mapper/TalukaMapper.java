package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.Taluka;
import com.techvg.pacs.service.dto.TalukaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Taluka} and its DTO {@link TalukaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TalukaMapper extends EntityMapper<TalukaDTO, Taluka> {}
