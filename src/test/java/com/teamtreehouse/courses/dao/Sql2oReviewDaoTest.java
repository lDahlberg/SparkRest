package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import com.teamtreehouse.courses.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by lDahlberg on 9/2/2016.
 */
public class Sql2oReviewDaoTest {

    private Sql2oReviewDao reviewDao;
    private Connection conn;
    private Sql2oCourseDao courseDao;
    private Course course;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        courseDao = new Sql2oCourseDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);

        //Keep connection open through entire test
        conn = sql2o.open();

        course = new Course("Test", "http://test.com");
        courseDao.add(course);
    }

    @After
    public void tearDown() {
        conn.close();
    }

    @Test
    public void addingReviewSetsNewId() throws Exception {
        Review review = new Review(course.getId(), 5, "Great");
        int originalId = review.getId();

        reviewDao.add(review);

        assertNotEquals(originalId, review.getId());
    }

    @Test
    public void multipleReviewsAreFoundWhenTheyExist() throws Exception {
        reviewDao.add(new Review(course.getId(), 5, "Great"));
        reviewDao.add(new Review(course.getId(), 1, "Great"));

        List<Review> foundReviews = reviewDao.findByCourseId(course.getId());
        assertEquals(2, foundReviews.size());
    }

    @Test(expected = DaoException.class)
    public void addingReviewToNonExistingCourseFails() throws Exception {
        Review review = new Review(4000, 5, "Great");
        reviewDao.add(review);
    }
}
