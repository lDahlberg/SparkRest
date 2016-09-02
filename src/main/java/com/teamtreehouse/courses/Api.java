package com.teamtreehouse.courses;

import com.google.gson.Gson;
import com.teamtreehouse.courses.dao.CourseDao;
import com.teamtreehouse.courses.dao.ReviewDao;
import com.teamtreehouse.courses.dao.Sql2oCourseDao;
import com.teamtreehouse.courses.dao.Sql2oReviewDao;
import com.teamtreehouse.courses.exc.ApiError;
import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import com.teamtreehouse.courses.model.Review;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by lDahlberg on 9/1/2016.
 */
public class Api {
    public static void main(String[] args) {

        String dataSource = "jdbc:h2:~/reviews.db";
        if(args.length > 0) {
            if(args.length !=2) {
                System.out.println("java Api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            dataSource = args[1];
        }

        String connectionString = dataSource+";INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        CourseDao courseDao = new Sql2oCourseDao(sql2o);
        ReviewDao reviewDao = new Sql2oReviewDao(sql2o);
        Gson gson = new Gson();

        post("/courses", "application/json", (req, res) -> {
            Course course = gson.fromJson(req.body(), Course.class);
            courseDao.add(course);
            res.status(201);
            return course;
        }, gson::toJson);

        get("/courses", "application/json",
                (req, res) -> courseDao.findAll(), gson::toJson);

        get("/courses/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Course course = courseDao.findById(id);
            if (course == null) {
                throw new ApiError(404, "Could not find course with id: "+ id);
            }
            return course;
        }, gson::toJson);

        post("/courses/:id/reviews", "application/json", (req, res) -> {
            int courseId = Integer.parseInt(req.params("id"));
            Review review = gson.fromJson(req.body(), Review.class);
            review.setCourseId(courseId);
            try {
                reviewDao.add(review);
            } catch (DaoException ex) {
                throw new ApiError(500, ex.getMessage());
            }
            res.status(201);
            return review;
        }, gson::toJson);

        get("/courses/:id/reviews", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            List<Review> reviews = reviewDao.findByCourseId(id);
            if (reviews == null) {
                throw new ApiError(404, "Could not find reviews with course id: "+ id);
            }
            return reviews;
        }, gson::toJson);

        exception(ApiError.class, (exc, req, res) -> {
            ApiError err = (ApiError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) -> {
            res.type("application/json");
        });
    }
}
