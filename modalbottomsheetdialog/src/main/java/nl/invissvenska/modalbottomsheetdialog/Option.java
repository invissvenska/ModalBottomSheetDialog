package nl.invissvenska.modalbottomsheetdialog;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class Option {
    private Integer id;
    private CharSequence title;
    private Drawable icon;

    public Option(Integer id, CharSequence title, @Nullable Drawable icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }
}
