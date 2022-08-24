package com.techvg.pacs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.techvg.pacs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDetailsDTO.class);
        AddressDetailsDTO addressDetailsDTO1 = new AddressDetailsDTO();
        addressDetailsDTO1.setId(1L);
        AddressDetailsDTO addressDetailsDTO2 = new AddressDetailsDTO();
        assertThat(addressDetailsDTO1).isNotEqualTo(addressDetailsDTO2);
        addressDetailsDTO2.setId(addressDetailsDTO1.getId());
        assertThat(addressDetailsDTO1).isEqualTo(addressDetailsDTO2);
        addressDetailsDTO2.setId(2L);
        assertThat(addressDetailsDTO1).isNotEqualTo(addressDetailsDTO2);
        addressDetailsDTO1.setId(null);
        assertThat(addressDetailsDTO1).isNotEqualTo(addressDetailsDTO2);
    }
}
