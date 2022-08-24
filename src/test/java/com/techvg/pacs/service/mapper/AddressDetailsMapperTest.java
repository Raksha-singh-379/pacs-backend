package com.techvg.pacs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddressDetailsMapperTest {

    private AddressDetailsMapper addressDetailsMapper;

    @BeforeEach
    public void setUp() {
        addressDetailsMapper = new AddressDetailsMapperImpl();
    }
}
