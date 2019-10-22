// TEDM Finish

package com.tedm.musicblog.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import com.tedm.musicblog.utils.TimberUtils;

public class MusicPlaybackTrack implements Parcelable {

    public static final Creator<MusicPlaybackTrack> CREATOR = new Creator<MusicPlaybackTrack>() {
        @Override
        public MusicPlaybackTrack createFromParcel(Parcel source) {
            return new MusicPlaybackTrack(source);
        }

        @Override
        public MusicPlaybackTrack[] newArray(int size) {
            return new MusicPlaybackTrack[size];
        }
    };

    public long mId;
    public long mSourceId;
    public TimberUtils.IdType mSourceType;
    public int mSourcePosition;

    public MusicPlaybackTrack(long mId, long mSourceId, TimberUtils.IdType mSourceType, int mSourcePosition) {
        this.mId = mId;
        this.mSourceId = mSourceId;
        this.mSourceType = mSourceType;
        this.mSourcePosition = mSourcePosition;
    }

    public MusicPlaybackTrack(Parcel in){
        mId = in.readLong();
        mSourceId = in.readLong();
        mSourceType = TimberUtils.IdType.getTypeById(in.readInt());
        mSourcePosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mSourceType.mId);
        dest.writeInt(mSourcePosition);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MusicPlaybackTrack) {
            MusicPlaybackTrack other = (MusicPlaybackTrack) o;
            if (other != null) {
                return mId == other.mId
                        && mSourceId == other.mSourceId
                        && mSourceType == other.mSourceType
                        && mSourcePosition == other.mSourcePosition;

            }
        }

        return super.equals(o);
    }
}
