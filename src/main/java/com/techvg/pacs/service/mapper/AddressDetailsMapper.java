package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.AddressDetails;
import com.techvg.pacs.domain.District;
import com.techvg.pacs.domain.Member;
import com.techvg.pacs.domain.SecurityUser;
import com.techvg.pacs.domain.State;
import com.techvg.pacs.domain.Taluka;
import com.techvg.pacs.domain.Village;
import com.techvg.pacs.service.dto.AddressDetailsDTO;
import com.techvg.pacs.service.dto.DistrictDTO;
import com.techvg.pacs.service.dto.MemberDTO;
import com.techvg.pacs.service.dto.SecurityUserDTO;
import com.techvg.pacs.service.dto.StateDTO;
import com.techvg.pacs.service.dto.TalukaDTO;
import com.techvg.pacs.service.dto.VillageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AddressDetails} and its DTO {@link AddressDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressDetailsMapper extends EntityMapper<AddressDetailsDTO, AddressDetails> {
    @Mapping(target = "state", source = "state", qualifiedByName = "stateId")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    @Mapping(target = "taluka", source = "taluka", qualifiedByName = "talukaId")
    @Mapping(target = "taluka", source = "taluka", qualifiedByName = "villageId")
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "securityUserId")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    AddressDetailsDTO toDto(AddressDetails s);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);

    @Named("districtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DistrictDTO toDtoDistrictId(District district);

    @Named("talukaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TalukaDTO toDtoTalukaId(Taluka taluka);

    @Named("villageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VillageDTO toDtoVillageId(Village village);

    @Named("securityUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SecurityUserDTO toDtoSecurityUserId(SecurityUser securityUser);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}
