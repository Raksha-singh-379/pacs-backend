package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.domain.Society;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.service.dto.SocietyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Society} and its DTO {@link SocietyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocietyMapper extends EntityMapper<SocietyDTO, Society> {
    @Mapping(target = "addressDetails", source = "addressDetails", qualifiedByName = "addressDetailsId")
    @Mapping(target = "society", source = "society", qualifiedByName = "societyId")
    SocietyDTO toDto(Society s);

    @Named("addressDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDetailsDTO toDtoAddressDetailsId(AddressDetails addressDetails);

    @Named("societyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocietyDTO toDtoSocietyId(Society society);
}
