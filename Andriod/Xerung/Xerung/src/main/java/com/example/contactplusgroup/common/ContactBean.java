package com.example.contactplusgroup.common;

import android.graphics.Bitmap;

public class ContactBean implements Comparable<ContactBean>{
	
	public int getIsMyContact() {
		return isMyContact;
	}

	public void setIsMyContact(int isMyContact) {
		this.isMyContact = isMyContact;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOrignalName() {
		return orignalName;
	}

	public void setOrignalName(String orignalName) {
		this.orignalName = orignalName;
	}

	public String getMyPhoneBookName() {
		return myPhoneBookName;
	}

	public void setMyPhoneBookName(String myPhoneBookName) {
		this.myPhoneBookName = myPhoneBookName;
	}

	public String getAltPhone() {
		return altPhone;
	}

	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public void setFirstChar(char firstChar) {
		this.firstChar = firstChar;
	}

	String id;
	String Name;
	String Number;
	String photoURI;
	String Email;
	String RequestFlag;

	public String getmCreatedDate() {
		return mCreatedDate;
	}

	public void setmCreatedDate(String mCreatedDate) {
		this.mCreatedDate = mCreatedDate;
	}

	String mCreatedDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	String userName;

	public String getInvitationFlag() {
		return invitationFlag;
	}

	public void setInvitationFlag(String invitationFlag) {
		this.invitationFlag = invitationFlag;
	}

	String invitationFlag;

	public String getmBloodGroup() {
		return mBloodGroup;
	}

	public void setmBloodGroup(String mBloodGroup) {
		this.mBloodGroup = mBloodGroup;
	}

	String mBloodGroup="";

	public String getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}

	String adminFlag;

	public String getSento() {
		return sento;
	}

	public void setSento(String sento) {
		this.sento = sento;
	}

	String sento;
	Bitmap thumb;
	boolean isChecked;
	private char firstChar;
	private String pinyin;
	String searchKey,UID;
	String altPhone, profession, address, city, country,photo,company;
	String myPhoneBookName="0",orignalName;
	int isMyContact ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public Bitmap getThumb() {
		return thumb;
	}

	public void setThumb(Bitmap thumb) {
		this.thumb = thumb;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getPhotoURI() {
		return photoURI;
	}

	public void setPhotoURI(String photoURI) {
		this.photoURI = photoURI;
	}

	public char getFirstChar() {
	        return firstChar;
	 }
	
	 public String getPinyin() {
	        return pinyin;
	 }

	 public void setPinyin(String pinyin) {
	     this.pinyin = pinyin;
	      String first = pinyin.substring(0, 1);
	      if (first.matches("[A-Za-z]")) {
	            firstChar = first.toUpperCase().charAt(0);
	      } else {
	            firstChar = '#';
	      }
	 }

	    @Override
	    public int compareTo(ContactBean another) {
	        return this.pinyin.compareTo(another.getPinyin());
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (o instanceof ContactBean) {
	            return this.id == ((ContactBean) o).getId();
	        } else {
	            return super.equals(o);
	        }
	    }

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getRequestFlag() {
			return RequestFlag;
		}

		public void setRequestFlag(String requestFlag) {
			RequestFlag = requestFlag;
		}


}
