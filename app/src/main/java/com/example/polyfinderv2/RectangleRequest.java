package com.example.polyfinderv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class RectangleRequest extends View {

    public View view;
    private TextView title;
    private TextView description;
    private ImageView imageView;
    private TextView categoryView;
    private TextView dateView;

    public RectangleRequest(Context context) {
        super(context);

        createView();
        findAllFields();
    }

    private void createView(){
        view = View.inflate(getContext(),R.layout.request_rectangle,null);
    }

    private void findAllFields() {
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        dateView = view.findViewById(R.id.dataview);
        categoryView = view.findViewById(R.id.category);
        imageView = view.findViewById(R.id.imageView);
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

    public void setDateView(String dateText){
        Date date = new Date();
        this.dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
        this.dateView.setTextColor(Color.LTGRAY);
        this.dateView.setEnabled(false);
    }

    public void setImageView(Bitmap bitmap){
        this.imageView.setImageBitmap(bitmap);
    }

    public CharSequence getTitleText(){
        return title.getText();
    }

    public CharSequence getDescriptionText(){
        return description.getText();
    }

    public TextView getCategoryView(){
        return categoryView;
    }

    public ImageView getImageView(){
        return imageView;
    }
}
