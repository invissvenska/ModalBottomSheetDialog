package nl.invissvenska.modalbottomsheetdialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;

public class ItemRequest implements Parcelable {

    private Integer id;
    private String title;
    @DrawableRes
    private Integer icon;

    public ItemRequest(Parcel source) {
        this.id = source.readInt();
        this.title = source.readString();
        this.icon = (Integer) source.readValue(getClass().getClassLoader());
    }

    protected Item toItem(Context context) {
        Drawable drawable = icon != null ? ResourcesCompat.getDrawable(context.getResources(), icon, context.getTheme()) : null;
        return new Item(id, title, drawable);
    }

    public static final Creator<ItemRequest> CREATOR = new Creator<ItemRequest>() {
        @Override
        public ItemRequest createFromParcel(Parcel source) {
            return new ItemRequest(source);
        }

        @Override
        public ItemRequest[] newArray(int size) {
            return new ItemRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeValue(icon);
    }
}
