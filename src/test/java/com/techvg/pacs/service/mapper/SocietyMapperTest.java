package com.techvg.pacs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocietyMapperTest {

    private SocietyMapper societyMapper;

    @BeforeEach
    public void setUp() {
        societyMapper = new SocietyMapperImpl();
    }
}
