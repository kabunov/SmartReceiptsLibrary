package co.smartreceipts.android.model.impl;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import co.smartreceipts.android.filters.Filter;
import co.smartreceipts.android.model.Price;
import co.smartreceipts.android.model.Receipt;
import co.smartreceipts.android.model.Source;
import co.smartreceipts.android.model.Trip;
import co.smartreceipts.android.model.WBCurrency;
import co.smartreceipts.android.model.factory.PriceBuilderFactory;
import co.smartreceipts.android.model.utils.ModelUtils;

public class DefaultTripImpl implements Trip {

    private final File mReportDirectory;
    private String mComment, mCostCenter;
    private Price mPrice, mDailySubTotal;
    private Date mStartDate, mEndDate;
    private TimeZone mStartTimeZone, mEndTimeZone;
    private WBCurrency mDefaultCurrency;
    private Source mSource;
    private Filter<Receipt> mFilter;

    public DefaultTripImpl(@NonNull File directory, Date startDate, TimeZone startTimeZone, Date endDate, TimeZone endTimeZone, @NonNull WBCurrency defaultCurrency, String comment, String costCenter, Filter<Receipt> filter, Source source) {
        mReportDirectory = directory;
        mStartDate = startDate;
        mStartTimeZone = startTimeZone;
        mEndDate = endDate;
        mEndTimeZone = endTimeZone;
        mDefaultCurrency = defaultCurrency;
        mComment = comment;
        mCostCenter = costCenter;
        mFilter = filter;
        mSource = source;
        // Sets a simple default for price and daily of 0
        mPrice = new PriceBuilderFactory().setPrice(0).setCurrency(defaultCurrency).build();
        mDailySubTotal = new PriceBuilderFactory().setPrice(0).setCurrency(defaultCurrency).build();
    }

    private DefaultTripImpl(Parcel in) {
        mReportDirectory = new File(in.readString());
        mPrice = in.readParcelable(Price.class.getClassLoader());
        mStartDate = new Date(in.readLong());
        mEndDate = new Date(in.readLong());
        mStartTimeZone = TimeZone.getTimeZone(in.readString());
        mEndTimeZone = TimeZone.getTimeZone(in.readString());
        mDailySubTotal = in.readParcelable(Price.class.getClassLoader());
        mComment = in.readString();
        mCostCenter = in.readString();
        mDefaultCurrency = WBCurrency.getInstance(in.readString());
        mSource = Source.Parcel;
    }

    @Override
    @NonNull
    public String getName() {
        return mReportDirectory.getName();
    }

    @Override
    @NonNull
    public File getDirectory() {
        return mReportDirectory;
    }

    @Override
    public String getDirectoryPath() {
        return mReportDirectory.getAbsolutePath();
    }

    @Override
    public Date getStartDate() {
        return mStartDate;
    }

    @Override
    public String getFormattedStartDate(Context context, String separator) {
        return ModelUtils.getFormattedDate(mStartDate, getStartTimeZone(), context, separator);
    }

    @Override
    public TimeZone getStartTimeZone() {
        return (mStartTimeZone != null) ? mStartTimeZone : TimeZone.getDefault();
    }

    @Override
    public Date getEndDate() {
        return mEndDate;
    }

    @Override
    public String getFormattedEndDate(Context context, String separator) {
        return ModelUtils.getFormattedDate(mEndDate, getEndTimeZone(), context, separator);
    }

    @Override
    public TimeZone getEndTimeZone() {
        return (mEndTimeZone != null) ? mEndTimeZone : TimeZone.getDefault();
    }

    /**
     * Tests if a particular date is included with the bounds of this particular trip When performing the test, it uses
     * the local time zone for the date, and the defined time zones for the start and end date bounds. The start date
     * time is assumed to occur at 00:01 of the start day and the end date is assumed to occur at 23:59 of the end day.
     * The reasoning behind this is to ensure that it appears properly from a UI perspective. Since the initial date
     * only shows the day itself, it may include an arbitrary time that is never shown to the user. Setting the time
     * aspect manually accounts for this. This returns false if the date is null.
     *
     * @param date - the date to test
     * @return true if it is contained within. false otherwise
     */
    @Override
    public boolean isDateInsideTripBounds(Date date) {
        if (date == null)
            return false;

        // Build a calendar for the date we intend to test
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.setTime(date);
        testCalendar.setTimeZone(TimeZone.getDefault());

        // Build a calendar for the start date
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(mStartDate);
        startCalendar.setTimeZone((mStartTimeZone != null) ? mStartTimeZone : TimeZone.getDefault());
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        // Build a calendar for the end date
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(mEndDate);
        endCalendar.setTimeZone((mEndTimeZone != null) ? mEndTimeZone : TimeZone.getDefault());
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);

        if (testCalendar.compareTo(startCalendar) >= 0 && testCalendar.compareTo(endCalendar) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @NonNull
    public WBCurrency getTripCurrency() {
        return mDefaultCurrency;
    }

    @Override
    @NonNull
    public String getDefaultCurrencyCode() {
        if (mDefaultCurrency != null) {
            return mDefaultCurrency.getCurrencyCode();
        } else {
            return Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        }
    }

    @Override
    public float getMileage() {
        return -1f;
    }

    @Override
    public String getMilesAsString() {
        return "0";
    }

    @Override
    public void setMileage(float mileage) {
        // Stub
    }

    @Override
    public String getComment() {
        return mComment;
    }

    @Override
    public String getCostCenter() {
        return mCostCenter;
    }

    @Override
    public Source getSource() {
        return mSource;
    }

    @NonNull
    @Override
    public Price getPrice() {
        return mPrice;
    }

    @Override
    public void setPrice(@NonNull Price price) {
        mPrice = price;
    }

    @Override
    public Price getDailySubTotal() {
        return mDailySubTotal;
    }

    @Override
    public void setDailySubTotal(@NonNull Price dailyTotal) {
        mDailySubTotal = dailyTotal;
    }

    @Override
    public Filter<Receipt> getFilter() {
        return mFilter;
    }

    @Override
    public boolean hasFilter() {
        return mFilter != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultTripImpl)) return false;

        DefaultTripImpl that = (DefaultTripImpl) o;

        if (!mReportDirectory.equals(that.mReportDirectory)) return false;
        if (mComment != null ? !mComment.equals(that.mComment) : that.mComment != null)
            return false;
        if (mCostCenter != null ? !mCostCenter.equals(that.mCostCenter) : that.mCostCenter != null)
            return false;
        if (mStartDate != null ? !mStartDate.equals(that.mStartDate) : that.mStartDate != null)
            return false;
        if (mEndDate != null ? !mEndDate.equals(that.mEndDate) : that.mEndDate != null)
            return false;
        if (mStartTimeZone != null ? !mStartTimeZone.equals(that.mStartTimeZone) : that.mStartTimeZone != null)
            return false;
        if (mEndTimeZone != null ? !mEndTimeZone.equals(that.mEndTimeZone) : that.mEndTimeZone != null)
            return false;
        if (!mDefaultCurrency.equals(that.mDefaultCurrency)) return false;
        return mFilter != null ? mFilter.equals(that.mFilter) : that.mFilter == null;

    }

    @Override
    public int hashCode() {
        int result = mReportDirectory.hashCode();
        result = 31 * result + (mComment != null ? mComment.hashCode() : 0);
        result = 31 * result + (mCostCenter != null ? mCostCenter.hashCode() : 0);
        result = 31 * result + (mStartDate != null ? mStartDate.hashCode() : 0);
        result = 31 * result + (mEndDate != null ? mEndDate.hashCode() : 0);
        result = 31 * result + (mStartTimeZone != null ? mStartTimeZone.hashCode() : 0);
        result = 31 * result + (mEndTimeZone != null ? mEndTimeZone.hashCode() : 0);
        result = 31 * result + mDefaultCurrency.hashCode();
        result = 31 * result + (mFilter != null ? mFilter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultTripImpl{" +
                "mReportDirectory=" + mReportDirectory +
                ", mComment='" + mComment + '\'' +
                ", mCostCenter='" + mCostCenter + '\'' +
                ", mPrice=" + mPrice +
                ", mDailySubTotal=" + mDailySubTotal +
                ", mStartDate=" + mStartDate +
                ", mEndDate=" + mEndDate +
                ", mStartTimeZone=" + mStartTimeZone +
                ", mEndTimeZone=" + mEndTimeZone +
                ", mDefaultCurrency=" + mDefaultCurrency +
                ", mSource=" + mSource +
                ", mFilter=" + mFilter +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getDirectoryPath());
        dest.writeParcelable(getPrice(), flags);
        dest.writeLong(getStartDate().getTime());
        dest.writeLong(getEndDate().getTime());
        dest.writeString(getStartTimeZone().getID());
        dest.writeString(getEndTimeZone().getID());
        dest.writeParcelable(getDailySubTotal(), flags);
        dest.writeString(getComment());
        dest.writeString(getCostCenter());
        dest.writeString(getDefaultCurrencyCode());
    }

    public static final Creator<DefaultTripImpl> CREATOR = new Creator<DefaultTripImpl>() {

        @Override
        public DefaultTripImpl createFromParcel(Parcel source) {
            return new DefaultTripImpl(source);
        }

        @Override
        public DefaultTripImpl[] newArray(int size) {
            return new DefaultTripImpl[size];
        }

    };

}
