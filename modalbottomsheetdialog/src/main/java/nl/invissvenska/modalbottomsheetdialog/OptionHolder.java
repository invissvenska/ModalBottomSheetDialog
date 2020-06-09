package nl.invissvenska.modalbottomsheetdialog;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class OptionHolder implements Parcelable {

    private Integer resource;
    private OptionRequest optionRequest;

    public OptionHolder(@Nullable Integer resource) {
        this.resource = resource;
    }

    public OptionHolder(Parcel source) {
        this.resource = (Integer) source.readValue(getClass().getClassLoader());
        this.optionRequest = source.readParcelable(OptionRequest.class.getClassLoader());
    }

    public static final Creator<OptionHolder> CREATOR = new Creator<OptionHolder>() {
        @Override
        public OptionHolder createFromParcel(Parcel source) {
            return new OptionHolder(source);
        }

        @Override
        public OptionHolder[] newArray(int size) {
            return new OptionHolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(resource);
        dest.writeParcelable(optionRequest, 0);
    }

    public Integer getResource() {
        return resource;
    }

    public OptionRequest getOptionRequest() {
        return optionRequest;
    }
}
