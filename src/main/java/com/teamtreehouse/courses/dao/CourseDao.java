package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;

import java.util.List;

/**
 * Created by lDahlberg on 8/31/2016.
 */
public interface CourseDao {

    void add(Course course) throws DaoException;

    List<Course> findAll();

    Course findById(int id);
}
