package com.wb.numerousstudents.View.Moments;

public class MomentsItem {
    private String momentId;
    private String mUserName;
    private String mMomentTittle;
    private String mContent;
    private String mMomentImageAddress;
    private String mMomentTime;
    private String mMomentPicturePath;

    public String getMomentPicturePath() {
        return mMomentPicturePath;
    }

    public void setMomentPicturePath(String mMomentPicturePath) {
        this.mMomentPicturePath = mMomentPicturePath;
    }

    public String getmMomentNickName() {
        return mMomentNickName;
    }

    public void setMomentNickName(String mMomentNickName) {
        this.mMomentNickName = mMomentNickName;
    }

    private String mMomentNickName;

    public MomentsItem(){
    }

    public void setMomentTime(String time){
        mMomentTime = time;
    }

    public String getMomentTime(){
        return mMomentTime;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getMomentTittle() {
        return mMomentTittle;
    }

    public void setMomentTittle(String mMomentTittle) {
        this.mMomentTittle = mMomentTittle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getMomentImageAddress() {
        return mMomentImageAddress;
    }

    public void setMomentImageAddress(String mMomentImage) {
        this.mMomentImageAddress = mMomentImage;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

}
