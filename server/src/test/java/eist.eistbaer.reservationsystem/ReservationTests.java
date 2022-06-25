package eist.eistbaer.reservationsystem;

import eist.eistbaer.reservationsystem.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationTests {

    private static Stream<Arguments> provideArgsForInterferingWithTimes0() {
        return Stream.of(
                Arguments.of(true, LocalTime.of(19, 30), LocalTime.of(20, 0)),
                Arguments.of(true, LocalTime.of(19, 30), LocalTime.of(22, 0)),
                Arguments.of(true, LocalTime.of(18, 0), LocalTime.of(20, 0)),
                Arguments.of(true, LocalTime.of(18, 30), LocalTime.of(22, 0)),
                Arguments.of(true, LocalTime.of(19, 30), LocalTime.of(20, 30)),
                Arguments.of(false, LocalTime.of(17, 0), LocalTime.of(18, 0)),
                Arguments.of(false, LocalTime.of(22, 0), LocalTime.of(23, 0)),
                Arguments.of(false, LocalTime.of(18, 0), LocalTime.of(19, 0)),
                Arguments.of(false, LocalTime.of(21, 0), LocalTime.of(22, 0)),
                Arguments.of(true, LocalTime.of(19, 0), LocalTime.of(21, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgsForInterferingWithTimes0")
    void testInterferingWithTimes0(boolean expected, LocalTime from, LocalTime to) {
        // Reservation 19h00 - 21h00
        Reservation reservation = new Reservation();
        reservation.setFromTime(LocalTime.of(19, 0));

        assertEquals(expected, reservation.interferingWithTimes(from, to));
    }

    private static Stream<Arguments> provideArgsForInterferingWithTimes1() {
        return Stream.of(
                Arguments.of(true, LocalTime.of(23, 30), LocalTime.of(0, 0)),
                Arguments.of(true, LocalTime.of(23, 30), LocalTime.of(2, 0)),
                Arguments.of(true, LocalTime.of(22, 0), LocalTime.of(0, 0)),
                Arguments.of(true, LocalTime.of(22, 30), LocalTime.of(2, 0)),
                Arguments.of(true, LocalTime.of(23, 30), LocalTime.of(0, 30)),
                Arguments.of(false, LocalTime.of(21, 0), LocalTime.of(22, 0)),
                Arguments.of(false, LocalTime.of(2, 0), LocalTime.of(3, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgsForInterferingWithTimes1")
    void testInterferingWithTimes1(boolean expected, LocalTime from, LocalTime to) {
        // Reservation 23h00 - 01h00
        Reservation reservation = new Reservation();
        reservation.setFromTime(LocalTime.of(23, 0));

        assertEquals(expected, reservation.interferingWithTimes(from, to));
    }
}
