package com.superjb.mycolloc;

/**
 * Created by Jean-Bryce on 14/06/2016.
 */
public class EtatListItem {

    private String name;
    private String email;
    private int imageId;
    private float age;

    public EtatListItem(String name, float age, int imageId, String email) {
        this.name = name;
        this.imageId = imageId;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String email() {
        return email;
    }

    public void email(String email) {
        this.email = email;
    }


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public float getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
