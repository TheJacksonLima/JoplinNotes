package org.jfl.repository;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.exception.PaymentNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Payment mapPayment(ResultSet rs, int rowNum) throws SQLException {
        return new Payment(
                rs.getBigDecimal("amount"),
                PaymentMethod.valueOf(rs.getString( "method" ))
        );
    }

    private final RowMapper<Payment>
            paymentRowMapper = ( rs, rowNum ) ->
            new Payment(rs.getBigDecimal( "amount"),PaymentMethod.valueOf(rs.getString("method")));


    public List<Payment> findAll() {
        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 order by id
                """,
                //this::mapPayment
                paymentRowMapper
        );
    }

    public Optional<Payment> findById(
            long id) {

        List<Payment> payments =
                jdbcTemplate.query(
                        """
                        select amount,
                               method
                          from payment
                         where id = ?
                        """,
                        //this::mapPayment,
                        paymentRowMapper,
                        id
                );

        return payments.stream()
                .findFirst();
    }

    public List<Payment> findPositivePayments() {

        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 where amount > 0
                 order by id
                """,
                //this::mapPayment
                paymentRowMapper
        );
    }

    public List<Payment> findPaymentsByType(PaymentMethod method) {
        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 where method = ?
                 order by id
                """,
                ///this::mapPayment,
                paymentRowMapper,
                method.name()
        );
    }

}