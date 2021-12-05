package dev.socketmods.socketnukes.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class HolidayTracker {

    private static final boolean ENABLE_HATS = isChristmas() || isAprilFools();

    /**
     * Whether to force enable the Vanity Hats <br>
     * We currently only enable the Hats for Christmas or April Fools
     */
    public static boolean enabledHats() {
        return ENABLE_HATS;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return true if the date is between to March 31 - April 2
     */
    private static boolean isAprilFools() {
        LocalDate now = now();

        LocalDate march30 = now.withMonth(MARCH).withDayOfMonth(30);
        LocalDate april3 = now.withMonth(APRIL).withDayOfMonth(3);

        return now.isAfter(march30) && now.isBefore(april3);
    }

    /**
     * @return true if the date is between to December 23 - January 2
     */
    private static boolean isChristmas() {
        LocalDate now = now();

        LocalDate december23 = now.withMonth(DECEMBER).withDayOfMonth(23);
        LocalDate january3 = now.withMonth(JANUARY).withDayOfMonth(3);

        return now.isAfter(december23) || now.isBefore(january3);
    }

    private static LocalDate now() {
        try {
            return LocalDate.now();
        } catch (Exception ignored) {
            return LocalDate.now(ZoneId.from(ZoneOffset.UTC));
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //@formatter:off
    private static final int JANUARY   =  1;
    private static final int FEBRUARY  =  2;
    private static final int MARCH     =  3;
    private static final int APRIL     =  4;
    private static final int MAY       =  5;
    private static final int JUNE      =  6;
    private static final int JULY      =  7;
    private static final int AUGUST    =  8;
    private static final int SEPTEMBER =  9;
    private static final int OCTOBER   = 10;
    private static final int NOVEMBER  = 11;
    private static final int DECEMBER  = 12;
    //@formatter:on
}
