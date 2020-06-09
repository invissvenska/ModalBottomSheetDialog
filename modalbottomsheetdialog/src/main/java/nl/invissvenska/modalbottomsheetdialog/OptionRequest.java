package nl.invissvenska.modalbottomsheetdialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;

public class OptionRequest implements Parcelable {

    private Integer id;
    private String title;
    @DrawableRes
    private Integer icon;

    public OptionRequest(Parcel source) {
        this.id = source.readInt();
        this.title = source.readString();
        this.icon = (Integer) source.readValue(getClass().getClassLoader());
    }

    protected Option toOption(Context context) {
        Drawable drawable = icon != null ? ResourcesCompat.getDrawable(context.getResources(), icon, context.getTheme()) : null;
        return new Option(id, title, drawable);
    }

    public static final Creator<OptionRequest> CREATOR = new Creator<OptionRequest>() {
        @Override
        public OptionRequest createFromParcel(Parcel source) {
            return new OptionRequest(source);
        }

        @Override
        public OptionRequest[] newArray(int size) {
            return new OptionRequest[size];
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
