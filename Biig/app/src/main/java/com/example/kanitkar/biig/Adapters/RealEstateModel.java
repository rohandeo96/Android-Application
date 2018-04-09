package com.example.kanitkar.biig.Adapters;

/**
 * Created by Kanitkar on 04-04-2018.
 */
public class RealEstateModel {
    public int Id;
    public String Title;
    public Double Price;
    public String Location;
    public int Size;
    public String AdImage;

    public RealEstateModel(int Id, String Title, double Price, String Location, int Size, String AdImage ) {
        this.Id = Id;
        this.Title = Title;
        this.Price = Price;
        this.Location = Location;
        this.Size = Size;
        this.AdImage = AdImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getArea() {
        return Location;
    }

    public void setArea(String Location) {
        Location = Location;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int Size) {
        Size = Size;
    }

    public String getAdImage() {
        return AdImage;
    }

    public void setAdImage(String adImage) {
        AdImage = adImage;
    }
}
