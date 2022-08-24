package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.State;
import com.techvg.pacs.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link State} and its DTO {@link StateDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateMapper extends EntityMapper<StateDTO, State> {}
