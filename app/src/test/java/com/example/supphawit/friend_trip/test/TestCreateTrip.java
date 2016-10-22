package com.example.supphawit.friend_trip.test;

import com.example.supphawit.friend_trip.model.Trip;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Created by Supphawit on 5/10/2559.
 */

public class TestCreateTrip {

    @Test
    public void testCreateTripCorrect() {
        Date start_date = createTestDate(2559, Calendar.OCTOBER, 4, 9, 0);
        Date end_date = createTestDate(2559, Calendar.OCTOBER, 5, 18, 0);
        Trip trip = setTripValue(new Trip());

        Assert.assertEquals("My Trip", trip.getName());
        Assert.assertEquals("Supphawit", trip.getCreatername());
        Assert.assertEquals("somewhere on earth", trip.getPlaces());
        Assert.assertEquals(10, trip.getMaxpeople());
        //Assert.assertEquals("04/10/2559 09.00", trip.getStartdate());
        //Assert.assertEquals("05/10/2559 18.00", trip.getEnddate());
        Assert.assertEquals("This is Description", trip.getDescription());
    }

    private Trip setTripValue(Trip trip){
        trip.setName("My Trip");
        trip.setCreatername("Supphawit");
        trip.setPlaces(java.util.Arrays.asList("someplace of earth"));
        trip.setMaxpeople("10");
        trip.setDescription("This is Description");
        return trip;
    }

    private Date createTestDate(int year, int month, int day, int hour, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
