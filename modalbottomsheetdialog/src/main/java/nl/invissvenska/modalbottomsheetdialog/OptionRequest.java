package nl.invissvenska.modalbottomsheetdialog;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class OptionRequest implements Parcelable {

    private Integer id;
    private String title;
    @DrawableRes
    private Integer icon;

    public OptionRequest(Integer id, String title, @Nullable @DrawableRes Integer icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public OptionRequest(Parcel source) {
        this.id = source.readInt();
        this.title = source.readString();
        this.icon = (Integer) source.readValue(getClass().getClassLoader());
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
