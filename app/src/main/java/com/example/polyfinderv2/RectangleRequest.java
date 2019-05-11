package com.example.polyfinderv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class RectangleRequest extends View {

    public View view;
    private TextView title;
    private TextView description;
    private ImageView imageView;
    private TextView categoryView;
    private TextView dateView;
    private String type;

    public RectangleRequest(Context context) {
        super(context);

        createView();
        findAllFields();
        setDateView();
    }

    private void createView(){
        view = View.inflate(getContext(),R.layout.request_rectangle,null);
    }

    private void findAllFields() {
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        dateView = view.findViewById(R.id.dataview);
        categoryView = view.findViewById(R.id.category);
        imageView = view.findViewById(R.id.request_img);
    }

    public View getRectangleRequestView(){
        return view;
    }

    public void setTitle(String title){
        this.title.setText(title);
        this.title.setLines(1);
        this.title.setEnabled(false);
    }

    public void setDescription(String description){
        this.description.setText(description);
        this.description.setLines(2);
        this.description.setEnabled(false);
    }

    public void setCategoryView(String category){
        this.categoryView.setText(category);
    }

    public void setDateView(){
        Date date = new Date();
        this.dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
        this.dateView.setTextColor(Color.LTGRAY);
        this.dateView.setEnabled(false);
    }

    public void setImageView(String image, String type){
        if(type.equals("lost")) {
            //Picasso.with(view.getContext()).load(image).placeholder(R.mipmap.lost).into(imageView);
        } else {
            //Picasso.with(view.getContext()).load(image).placeholder(R.mipmap.found).into(imageView);
        }
    }

    public CharSequence getTitleText(){
        return title.getText();
    }

    public CharSequence getDescriptionText(){
        return description.getText();
    }

    public CharSequence getCategoryView(){
        return categoryView.getText();
    }

    public ImageView getImageView(){
        return imageView;
    }
}
