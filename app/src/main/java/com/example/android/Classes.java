package com.example.android;

import java.util.ArrayList;

/**
 * Wrapper class for {@link ArrayList} of {@link String} courses
 */
public class Classes {

    String coursenames;
    String courseDesc;

    /**
     * Assigns {@code coursename} to {@link Classes#coursenames}
     * @param coursename {@link ArrayList} of {@link String} courses
     */
    public Classes(String coursename, String courseDesc)
    {

        this.coursenames = coursename;
        this.courseDesc = courseDesc;

    }

    /**
     * Returns {@link Classes#coursenames}
     * @return {@link ArrayList} of {@link String} courses
     */
    public String getCoursename() {
        return coursenames;
    }


    public String getCourseDesc() {
        return courseDesc;
    }


}