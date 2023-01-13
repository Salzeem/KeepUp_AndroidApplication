package com.example.android;

import java.util.ArrayList;

/**
 * Class for storing information about a group
 */
public class GroupsInformation
{
    private final String  id;  //Primary Key
    private String NameofGroup;
    private String GroupDescription;
    private  String coursename ;
    private ArrayList<String> memberEmails;

    public  ArrayList<String> members;

    /**
     * Creates a new {@link GroupsInformation} with given parameters
     * @param id {@link String} group identifier
     * @param NameofGroup {@link String} group name
     * @param GroupDescription {@link String} group description
     * @param coursename {@link String} name of the course the group is for
     * @param members {@link ArrayList} of the members names
     * @param memberEmails {@link ArrayList} of the members emails
     * @param memberIds unused
     */
    public GroupsInformation(String id , String NameofGroup, String GroupDescription,String coursename, ArrayList<String> members, ArrayList<String> memberEmails,  ArrayList<String> memberIds) {
        this.id = id;
        this.NameofGroup = NameofGroup ;
        this.GroupDescription =GroupDescription;
        this.coursename = coursename;
        this.members = members;
        this.memberEmails = memberEmails;


    }

    /**
     * Gets the name of the course the group is for
     * @return course name
     */
    public String getCoursename() {return  this.coursename;}

    /**
     * Gets the group identifier
     * @return group identifier
     */
    public String  getid()
    {
        return this.id;
    }

    /**
     * Gets the name of the group
     * @return name of group
     */
    public String getNameofGroup() {return this.NameofGroup;}

    /**
     * Gets the description of the group
     * @return group description
     */
    public String getGroupDescription() {return this.GroupDescription;}

    /**
     * Gets the members of the groups emails
     * @return member emails
     */
    public ArrayList<String> getMemberEmails(){return  this.memberEmails;}

    /**
     * Sets the members names of the group to {@code newmembers}
     * @param newmembers {@link ArrayList} of members names
     */
    public void setMembers(ArrayList<String> newmembers){this.members = newmembers;}

    /**
     * Sets the description of the group
     * @param newGroupDesc {@link String} description of the group
     */
    public void setGroupDescription(String newGroupDesc){this.GroupDescription = newGroupDesc; }

    /**
     * Sets the name of the group
     * @param newName {@link String} name of the group
     */
    public void setGroupName(String newName){this.NameofGroup = newName; }

    /**
     * Gets the members names of the group
     * @return members names of the group
     */
    public ArrayList<String> getMembers() {return this.members;}

}
