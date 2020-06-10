package nl.invissvenska.modalbottomsheetdialog;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class ItemHolder implements Parcelable {

    private Integer resource;
    private ItemRequest itemRequest;

    public ItemHolder(@Nullable Integer resource) {
        this.resource = resource;
    }

    public ItemHolder(Parcel source) {
        this.resource = (Integer) source.readValue(getClass().getClassLoader());
        this.itemRequest = source.readParcelable(ItemRequest.class.getClassLoader());
    }

    public static final Creator<ItemHolder> CREATOR = new Creator<ItemHolder>() {
        @Override
        public ItemHolder createFromParcel(Parcel source) {
            return new ItemHolder(source);
        }

        @Override
        public ItemHolder[] newArray(int size) {
            return new ItemHolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(resource);
        dest.writeParcelable(itemRequest, 0);
    }

    public Integer getResource() {
        return resource;
    }

    public ItemRequest getItemRequest() {
        return itemRequest;
    }
}
