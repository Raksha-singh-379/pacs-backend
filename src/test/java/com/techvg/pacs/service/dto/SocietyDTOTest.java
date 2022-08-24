package com.techvg.pacs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.techvg.pacs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocietyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocietyDTO.class);
        SocietyDTO societyDTO1 = new SocietyDTO();
        societyDTO1.setId(1L);
        SocietyDTO societyDTO2 = new SocietyDTO();
        assertThat(societyDTO1).isNotEqualTo(societyDTO2);
        societyDTO2.setId(societyDTO1.getId());
        assertThat(societyDTO1).isEqualTo(societyDTO2);
        societyDTO2.setId(2L);
        assertThat(societyDTO1).isNotEqualTo(societyDTO2);
        societyDTO1.setId(null);
        assertThat(societyDTO1).isNotEqualTo(societyDTO2);
    }
}
