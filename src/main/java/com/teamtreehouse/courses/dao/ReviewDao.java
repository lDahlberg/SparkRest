package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Review;

import java.util.List;

/**
 * Created by lDahlberg on 8/31/2016.
 */
public interface ReviewDao {

    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findByCourseId(int courseId);
}
