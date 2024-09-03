package com.coedev.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomerRowMapperTest {

    @Test
    void itShouldMapRow() throws SQLException {
        //GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("jamila");
        when(resultSet.getString("email")).thenReturn("jamila@gmail.com");

        //WHEN
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //THEN
        Customer expected = new Customer(
                1, "jamila", "jamila@gmail.com", 19);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}