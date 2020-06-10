package nl.invissvenska.modalbottomsheetdialog;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class Item {
    private Integer id;
    private CharSequence title;
    private Drawable icon;

    public Item(Integer id, CharSequence title, @Nullable Drawable icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public CharSequence getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }
}
