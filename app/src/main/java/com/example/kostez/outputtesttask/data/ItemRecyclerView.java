package com.example.kostez.outputtesttask.data;

/**
 * Created by Kostez on 12.09.2016.
 */
public class ItemRecyclerView {

    private int id;
    private String Title;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
