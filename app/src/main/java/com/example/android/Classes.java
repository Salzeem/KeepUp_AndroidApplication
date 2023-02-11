package com.example.android;

import java.util.ArrayList;

/**
 * Wrapper class for {@link ArrayList} of {@link String} courses
 */
public class Classes {

    String coursenames;
    String courseDesc;
    String CourseTitle;

    /**
     * Assigns {@code coursename} to {@link Classes#coursenames}
     * @param coursename {@link ArrayList} of {@link String} courses
     */
    public Classes(String coursename, String courseDesc, String CourseTitle)
    {

        this.coursenames = coursename;
        this.courseDesc = courseDesc;
        this.CourseTitle = CourseTitle;

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

    public String getCourseTitle()
    {
        return CourseTitle;
    }


}