package com.example.contactplusgroup.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.GroupBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;

public class GroupDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbCellIndex";
    private static final int DATABASE_VERSION = 7; //change db version if changes made in DB

    /////--------------------------------------Group DB----------------------------
    private static final String TABLE_NAME = "GROUP_LIST";
    private static final String T_ID = "ID";
    private static final String T_MADEBYNAME = "MADEBYNAME";
    private static final String T_MADEBYPHONENO = "MADEBYPHONENO";
    private static final String T_GROUPNAME = "GROUPNAME";
    private static final String T_GROUPID = "GROUPID";
    private static final String T_TAGNAME = "TAGNAME";
    private static final String T_DESCRITION = "DESCRITION";
    private static final String T_GROUPPHOTO = "GROUPPHOTO";
    private static final String T_MEMBERCOUNT = "MEMBERCOUNT";
    private static final String T_ADMINFLAG = "ADMINFLAG";
    private static final String T_CREATEUPDATETS = "CREATEUPDATETS";
    private static final String T_ACCESS_TYPE = "ACCESSTYPE";

    //----------------------------------------Contact Db-------------
    private static final String CONTACT_DB = "CONTACT_LIST";
    private static final String T_CON_ID = "ID";
    private static final String T_CON_NAME = "NAME";
    private static final String T_CON_NUMBER = "NUMBER";

    //---------------------------------Member Db----------------
    private static final String MEMBER_DB = "MEMBER_LIST";

    private static final String MEMBER_NAME = "MEMBER_NAME";
    private static final String MEMBER_PHONE = "MEMBER_PHONE";
    private static final String MEMBER_SEARCHKEY = "SEARCHKEY";
    private static final String MEMBER_UID = "UID";
    private static final String MEMBER_ID = "ID";
    private static final String MEMBER_FLAG = "FLAG";
    private static final String MEMBER_EMAIL = "EMAIL";
    private static final String MEMBER_ALTERNATE = "ALT_PHONE";
    private static final String MEMBER_CITY = "MEMBER_CITY";
    private static final String MEMBER_COUNTRY = "MEMBER_COUNTRY";
    private static final String MEMBER_PROFESSION = "PROFESSION";
    private static final String MEMBER_ADDRESS = "ADDRESS";
    private static final String MEMBER_PHOTO = "PHOTO";
    private static final String MEMBER_ORG_NAME = "ORG_NAME";
    private static final String MEMBER_PH_BOK_NAME = "PHONE_BOOK_NAME";
    private static final String MEMBER_COMPANY_NAME = "COMPANY_NAME";
    private static final String MEMBER_ISMY_CONTACT = "ISMYCONTACT";
    private static final String MEMBER_BLOOD_GROUP = "MEMBER_BLOOD_GROUP";
    private static final String MEMBER_ADMIN_FLAG = "MEMBER_ADMIN_FLAG";
    private static final String MEMBER_CREATED_DATE = "MEMBER_CREATED_DATE";

    Context context = null;

    public GroupDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + T_ID + "AUTO_INCREMENT INTEGER PRIMARY KEY," +
                T_GROUPID + " INTEGER NOT NULL," + T_GROUPNAME + " VARCHAR(45) NOT NULL," + T_MADEBYNAME + " VARCHAR(100) NOT NULL," +
                T_MADEBYPHONENO + " VARCHAR(20) NOT NULL," + T_TAGNAME + " VARCHAR(200)," + T_DESCRITION + " VARCHAR(200)," + T_GROUPPHOTO + " TEXT," +
                T_MEMBERCOUNT + " VARCHAR(10), " + T_ACCESS_TYPE + " VARCHAR(10), " + T_ADMINFLAG + " VARCHAR(20), " + T_CREATEUPDATETS + " VARCHAR(30)" + ")";
        db.execSQL(createTable);
        createTable = "CREATE TABLE " + CONTACT_DB + "(" + T_CON_ID + "AUTO_INCREMENT INTEGER PRIMARY KEY," +
                T_CON_NAME + " VARCHAR(100) NOT NULL, " + T_CON_NUMBER + " VARCHAR(45)" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        SharedPreferanceData shared = new SharedPreferanceData(context);
        ArrayList<GroupBean> tb = getAllGroup();
        for (GroupBean d : tb) {
            db.execSQL("DROP TABLE IF EXISTS MG" + d.getmGroupId());
            shared.saveSharedData("MG" + d.getmGroupId(), "N/A");
        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_DB);
        // Create tables again
        onCreate(db);
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        try {
            truncateContactTable();
            truncateTableGroup();
            ArrayList<GroupBean> tb = getAllGroup();
            for (GroupBean d : tb) {
                truncateMemberTable("MG" + d.getmGroupId());
            }

        } catch (Exception ex) {

        }
    }

    // Used to add Group in Group Table
    public int addGroup(GroupBean gb) {
        //Log.e("ADD GROUP", "ADD GROUP");
        int status = 0;
        try {
            SQLiteDatabase sq = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(T_GROUPID, Integer.parseInt(gb.getmGroupId().trim()));
            cv.put(T_GROUPNAME, gb.getmGroupName().trim());

            cv.put(T_MADEBYNAME, gb.getmGroupMadeByName().trim());
            cv.put(T_MADEBYPHONENO, gb.getmGroupMadeByNum().trim());
            cv.put(T_TAGNAME, gb.getmGroupTag().trim());
            cv.put(T_DESCRITION, gb.getmGroupDesc().trim());
            cv.put(T_GROUPPHOTO, gb.getmPhoto().trim());
            cv.put(T_MEMBERCOUNT, String.valueOf(gb.getmGroupSize()));
            cv.put(T_ACCESS_TYPE, gb.getmGroupAccessType().trim());
            cv.put(T_ADMINFLAG, gb.getmGroupAdmin());
            cv.put(T_CREATEUPDATETS, gb.getmGroupCreatedDate());
            sq.insert(TABLE_NAME, null, cv);
            status = 1;
            sq.close();
        } catch (Exception e) {
            // TODO: handle exception
            status = 0;
            //Log.e("GroupDBErro", "AddGroup "+e.getMessage());
            e.printStackTrace();
        }
        return status;
    }

    // Used to check exist Group in Group Table
    public int checkGroupExist(int gUid, GroupBean gb) {
        try {
            String exist = "SELECT COUNT(*) as m FROM " + TABLE_NAME + " Where " + T_GROUPID + " = " + gUid;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(exist, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //Log.e("ddd "," Exist "+cursor.getInt(0));
                    if (cursor.getInt(0) != 0) {
                        //String updateTable= "UPDATE "+TABLE_NAME+" SET "+T_TAGNAME+"= '"+gb.getmGroupTag().trim()+"', "+T_DESCRITION+"= '"+gb.getmGroupTag().trim()+"', "+T_GROUPPHOTO+"= '"+gb.getmPhoto().trim()+"', "+T_MEMBERCOUNT+"= '"+String.valueOf(gb.getmGroupSize())+"', "+T_ADMINFLAG+"= '"+gb.getmGroupAdmin()+" Where "+T_GROUPID+" = "+gUid+";";
                        ContentValues cv = new ContentValues();
                        cv.put(T_TAGNAME, gb.getmGroupTag().trim());
                        cv.put(T_DESCRITION, gb.getmGroupDesc().trim());
                        cv.put(T_GROUPPHOTO, gb.getmPhoto().trim());
                        cv.put(T_MEMBERCOUNT, String.valueOf(gb.getmGroupSize()));
                        cv.put(T_ADMINFLAG, gb.getmGroupAdmin());
                        cv.put(T_CREATEUPDATETS, gb.getmGroupCreatedDate());
                        db.update(TABLE_NAME, cv, T_GROUPID + " = ?",
                                new String[]{String.valueOf(gb.getmGroupId())});
                        return 0;
                    } else {
                        addGroup(gb);
                    }
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }

    // Used to check exist Group in Group Table
    public int checkGroupMemberExist(int gUid, ContactBean cb, String table) {
        try {
            String exist = "SELECT COUNT(*) as m FROM " + table + " Where " + MEMBER_UID + " = " + gUid;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(exist, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //Log.e("ddd "," Exist "+cursor.getInt(0));
                    if (cursor.getInt(0) != 0) {
                        //String updateTable= "UPDATE "+TABLE_NAME+" SET "+T_TAGNAME+"= '"+gb.getmGroupTag().trim()+"', "+T_DESCRITION+"= '"+gb.getmGroupTag().trim()+"', "+T_GROUPPHOTO+"= '"+gb.getmPhoto().trim()+"', "+T_MEMBERCOUNT+"= '"+String.valueOf(gb.getmGroupSize())+"', "+T_ADMINFLAG+"= '"+gb.getmGroupAdmin()+" Where "+T_GROUPID+" = "+gUid+";";
                        ContentValues cv = new ContentValues();
                        cv.put(MEMBER_NAME, cb.getName().trim());
                        cv.put(MEMBER_PHONE, cb.getNumber().trim());
                        cv.put(MEMBER_SEARCHKEY, cb.getSearchKey().trim());
                        cv.put(MEMBER_UID, cb.getUID().trim());
                        cv.put(MEMBER_FLAG, cb.getRequestFlag().trim());
                        cv.put(MEMBER_ORG_NAME, cb.getOrignalName().trim());
                        cv.put(MEMBER_PH_BOK_NAME, cb.getMyPhoneBookName().trim());
                        cv.put(MEMBER_ISMY_CONTACT, cb.getIsMyContact());
                        cv.put(MEMBER_BLOOD_GROUP, cb.getmBloodGroup().trim());
                        cv.put(MEMBER_CITY, cb.getCity().trim());
                        cv.put(MEMBER_PROFESSION, cb.getProfession().trim());
                        cv.put(MEMBER_ADMIN_FLAG, cb.getAdminFlag());
                        cv.put(MEMBER_CREATED_DATE, cb.getmCreatedDate());
                        db.update(table, cv, MEMBER_UID + " = ?", new String[]{String.valueOf(cb.getUID())});
                        return 0;
                    } else {
                        addMember(cb, "MG" + Vars.gUID);
                    }
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }


    // Used to count number of Group in table Already
    public int getMemberTableCount(String table) {
        int count = 0;
        try {
            String countQuery = "SELECT COUNT(*) as m FROM " + table;
            SQLiteDatabase sq = this.getReadableDatabase();
            Cursor cursor = sq.rawQuery(countQuery, null);
            count = cursor.getInt(0);
            cursor.close();
            sq.close();

        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "GetGroup Count "+e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public long countMemberTest(String tablename) {
        SQLiteDatabase sq = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(sq, tablename);
    }

    // Used to count number of Group in table Already
    public int getGroupCount() {

        int count = 0;
        try {
            String countQuery = "SELECT COUNT(*) FROM " + TABLE_NAME;
            SQLiteDatabase sq = this.getReadableDatabase();
            Cursor cursor = sq.rawQuery(countQuery, null);
            count = cursor.getInt(0);
            cursor.close();
            sq.close();

        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "GetGroup Count "+e.getMessage());
            e.printStackTrace();
        }
        getAllGroup().size();
        return count;
    }

    public long countTest() {
        SQLiteDatabase sq = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(sq, TABLE_NAME);
    }

    public ArrayList<GroupBean> getAllGroup() {

        ArrayList<GroupBean> gbList = new ArrayList<GroupBean>();
        try {
            String selectQuery = "SELECT " + T_GROUPID + ", " + T_GROUPNAME + ", " + T_MADEBYNAME +
                    ", " + T_MADEBYPHONENO + ", " + T_TAGNAME + ", " + T_DESCRITION + ", " + T_GROUPPHOTO + ", " +
                    T_MEMBERCOUNT + ", " + T_ACCESS_TYPE + ", " + T_ADMINFLAG + ", " + T_CREATEUPDATETS + " FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GroupBean contact = new GroupBean();
                    contact.setmGroupId(String.valueOf(cursor.getInt(0)));
                    contact.setmGroupName(cursor.getString(1));
                    contact.setmGroupMadeByName(cursor.getString(2));
                    contact.setmGroupMadeByNum(cursor.getString(3));
                    contact.setmGroupTag(cursor.getString(4));
                    contact.setmGroupDesc(cursor.getString(5));
                    contact.setmPhoto(cursor.getString(6));
                    contact.setmGroupSize(Integer.parseInt(cursor.getString(7).trim()));
                    contact.setmGroupAccessType(cursor.getString(8));
                    contact.setmGroupAdmin(cursor.getString(9));
                    contact.setmGroupCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<GroupBean> getGroupLimit(int pageSkip, int maxCount) {
        //Log.e("adadad", "adadada===SKIP=="+pageSkip+"---MAX COUNT"+maxCount);
        ArrayList<GroupBean> gbList = new ArrayList<GroupBean>();
        try {
            String selectQuery = "SELECT " + T_GROUPID + ", " + T_GROUPNAME + ", " + T_MADEBYNAME +
                    ", " + T_MADEBYPHONENO + ", " + T_TAGNAME + ", " + T_DESCRITION + ", " + T_GROUPPHOTO + ", " +
                    T_MEMBERCOUNT + ", " + T_ACCESS_TYPE + ", " + T_ADMINFLAG + ", " + T_CREATEUPDATETS + " FROM " + TABLE_NAME + " Order by " + T_CREATEUPDATETS + " " + "LIMIT " + pageSkip + ", " + maxCount;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GroupBean contact = new GroupBean();
                    contact.setmGroupId(String.valueOf(cursor.getInt(0)));
                    contact.setmGroupName(cursor.getString(1));
                    contact.setmGroupMadeByName(cursor.getString(2));
                    contact.setmGroupMadeByNum(cursor.getString(3));
                    contact.setmGroupTag(cursor.getString(4));
                    contact.setmGroupDesc(cursor.getString(5));
                    contact.setmPhoto(cursor.getString(6));
                    contact.setmGroupSize(Integer.parseInt(cursor.getString(7).trim()));
                    contact.setmGroupAccessType(cursor.getString(8));
                    contact.setmGroupAdmin(cursor.getString(9));
                    contact.setmGroupCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public void truncateTableGroup() {
        try {
            String truncateTable = "DELETE FROM " + TABLE_NAME;
            SQLiteDatabase sql = this.getWritableDatabase();
            sql.execSQL(truncateTable);
            String vacuum = "VACUUM";
            sql = this.getWritableDatabase();
            sql.execSQL(vacuum);
            sql.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //Log.e("GroupDBErro", "TruncateTable " + e.getMessage());
        }
    }

    //-------------------------------Contact DB----------------------------------------------
    // Used to add Group in Group Table
    public int addContact(ContactBean gb) {
        int status = 0;
        try {
            if (gb.getName().length() > 0 && gb.getNumber().length() > 0) {
                SQLiteDatabase sq = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(T_CON_NAME, gb.getName().trim());
                cv.put(T_CON_NUMBER, gb.getNumber().trim());
                sq.insert(CONTACT_DB, null, cv);
                status = 1;
                sq.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            status = 0;
            //Log.e("GroupDBErro", "AddGroup "+e.getMessage());
            e.printStackTrace();
        }
        return status;
    }

    // Used to count number of Group in table Already
    public int getContactCount() {
        int count = 0;
        try {
            String countQuery = "SELECT COUNT(1) FROM " + CONTACT_DB;
            SQLiteDatabase sq = this.getReadableDatabase();
            Cursor cursor = sq.rawQuery(countQuery, null);
            cursor.close();
            count = cursor.getCount();
            sq.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "GetGroup Count "+e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<ContactBean> getAllContacts() {

        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + T_CON_NAME + ", " + T_CON_NUMBER + " FROM " + CONTACT_DB;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public void truncateContactTable() {
        try {
            String truncateTable = "DELETE FROM " + CONTACT_DB;
            SQLiteDatabase sql = this.getWritableDatabase();
            sql.execSQL(truncateTable);
            String vacuum = "VACUUM";
            sql = this.getWritableDatabase();
            sql.execSQL(vacuum);
            sql.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //Log.e("GroupDBErro", "TruncateTable "+e.getMessage());
        }
    }

    // Getting single contact
    public String getContactName(String number) {

        String name = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(CONTACT_DB, new String[]{T_CON_NAME}, T_CON_NUMBER + "=?",
                    new String[]{String.valueOf(number)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            name = cursor.getString(0);
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            if (name.length() > 0)
                return name;
            try {
                number = number.substring(3, number.length());
                String selectQuery = "SELECT " + T_CON_NAME + " FROM "
                        + CONTACT_DB + " WHERE " + T_CON_NUMBER + " LIKE '%" + number + "%' LIMIT 1";

                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    name = cursor.getString(0);
                }
                if (cursor != null)
                    cursor.close();
                ;
                db.close();
            } catch (Exception ex) {
            }
        }

        // return contact
        return name;
    }

    // ---------------------------- MEMBER TABLE------------------
    public void dropMemberTable(String memberTable) {
        try {
            SQLiteDatabase dbs = this.getWritableDatabase();
            dbs.execSQL("DROP TABLE IF EXISTS " + memberTable);
            dbs.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void createMemberTable(String memberTable) {
        try {
            dropMemberTable(memberTable);
            SQLiteDatabase dbs = this.getWritableDatabase();
            String query = "CREATE TABLE " + memberTable + "(" + MEMBER_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " + MEMBER_UID +
                    " INTEGER, " + MEMBER_NAME + " VARCHAR(45), " + MEMBER_PHONE + " VARCHAR(20)," + MEMBER_SEARCHKEY + " TEXT, " + MEMBER_FLAG +
                    " VARCHAR(4), " + MEMBER_ALTERNATE + " VARCHAR(45), " + MEMBER_EMAIL + " VARCHAR(100), " + MEMBER_ADDRESS + " VARCHAR(100), " +
                    MEMBER_CITY + " VARCHAR(70), " + MEMBER_COUNTRY + " VARCHAR(50), " + MEMBER_PROFESSION + " VARCHAR(60), " + MEMBER_ORG_NAME +
                    " VARCHAR(60), " + MEMBER_PH_BOK_NAME + " VARCHAR(60), " + MEMBER_COMPANY_NAME + " VARCHAR(60), " + MEMBER_ISMY_CONTACT + " INTEGER, "
                    + MEMBER_BLOOD_GROUP + " VARCHAR(10), " + MEMBER_ADMIN_FLAG + " VARCHAR(2), " + MEMBER_CREATED_DATE + " VARCHAR(30), " + MEMBER_PHOTO + " TEXT )";
            dbs.execSQL(query);
            dbs.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Error", "Error in Create Table " + e.getMessage());
        }
    }

    public void truncateMemberTable(String memberTable) {
        try {
            String truncateTable = "DELETE FROM " + memberTable;
            SQLiteDatabase sql = this.getWritableDatabase();
            sql.execSQL(truncateTable);
            String vacuum = "VACUUM";
            sql = this.getWritableDatabase();
            sql.execSQL(vacuum);
            sql.close();
            ;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //Log.e("GroupDBErro", "TruncateTable "+e.getMessage());
        }
    }

    public void addMember(ContactBean cb, String tableName) {
        try {
            SQLiteDatabase sq = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(MEMBER_NAME, cb.getName().trim());
            cv.put(MEMBER_PHONE, cb.getNumber().trim());
            cv.put(MEMBER_SEARCHKEY, cb.getSearchKey().trim());
            cv.put(MEMBER_UID, cb.getUID().trim());
            cv.put(MEMBER_FLAG, cb.getRequestFlag().trim());
            cv.put(MEMBER_ORG_NAME, cb.getOrignalName().trim());
            cv.put(MEMBER_PH_BOK_NAME, cb.getMyPhoneBookName().trim());
            cv.put(MEMBER_ISMY_CONTACT, cb.getIsMyContact());
            cv.put(MEMBER_BLOOD_GROUP, cb.getmBloodGroup().trim());
            cv.put(MEMBER_CITY, cb.getCity().trim());
            cv.put(MEMBER_PROFESSION, cb.getProfession().trim());
            cv.put(MEMBER_ADMIN_FLAG, cb.getAdminFlag());
            cv.put(MEMBER_CREATED_DATE, cb.getmCreatedDate());
            sq.insert(tableName, null, cv);
            sq.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "AddGroup "+e.getMessage());
            e.printStackTrace();
        }
    }

    public String deleteMember(String name, String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + tableName + " WHERE MEMBER_PHONE='" + name + "'");
            db.close();
            return "true";
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "AddGroup "+e.getMessage());
            e.printStackTrace();
            return "false";
        }
    }

    public void addMemberDetail(ContactBean cb, String tableName) {
        try {
            SQLiteDatabase sq = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(MEMBER_EMAIL, cb.getEmail().trim());
            cv.put(MEMBER_ALTERNATE, cb.getAltPhone().trim());
            cv.put(MEMBER_COUNTRY, cb.getCountry().trim());
            cv.put(MEMBER_PROFESSION, cb.getProfession().trim());
            cv.put(MEMBER_CITY, cb.getCity().trim());
            cv.put(MEMBER_ADDRESS, cb.getAddress().trim());
            cv.put(MEMBER_PHOTO, cb.getPhoto());
            cv.put(MEMBER_COMPANY_NAME, cb.getCompany());
            //sq.insert(tableName, null, cv);
            sq.update(tableName, cv, MEMBER_UID + " = ?",
                    new String[]{String.valueOf(cb.getUID())});
            sq.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public ArrayList<ContactBean> getAllMember(String tableName) {

        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
            ;
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }


    public ArrayList<ContactBean> getMemberLimit(String tableName) {

        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            ;
            db.close();
            ;
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }


    public ArrayList<ContactBean> getGroupMemberLimit(int pageSkip, int maxCount, String table) {

        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + table + " Order by " + MEMBER_CREATED_DATE + " " + "LIMIT " + pageSkip + ", " + maxCount;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB " + e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }


    public ArrayList<ContactBean> getBloodGroup(String tableName) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT Distinct " + MEMBER_BLOOD_GROUP + " , COUNT(*) " + " FROM " + tableName + " where " + MEMBER_BLOOD_GROUP + " != '-1' and " + MEMBER_BLOOD_GROUP + " != '' group by " + MEMBER_BLOOD_GROUP;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            ;
            db.close();
            ;
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<ContactBean> getBloodGroupMember(String tableName, String bloodGroup) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName + " where " + MEMBER_BLOOD_GROUP + " = '" + bloodGroup + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<ContactBean> getProfessionCount(String tableName) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT Distinct " + MEMBER_PROFESSION + " , COUNT(*) " + " FROM " + tableName + " where " + MEMBER_PROFESSION + " != '-1' and " + MEMBER_PROFESSION + " != '' group by upper(" + MEMBER_PROFESSION + ")";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<ContactBean> getProfessionMember(String tableName, String profession) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName + " where UPPER(" + MEMBER_PROFESSION + ") = UPPER('" + profession + "')";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }

            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<ContactBean> getCity(String tableName) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT Distinct " + MEMBER_CITY + " , COUNT(*) " + " FROM " + tableName + " where " + MEMBER_CITY + " != '-1' and " + MEMBER_CITY + " != '' group by " + MEMBER_CITY;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<ContactBean> getCityMember(String tableName, String bloodGroup) {
        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName + " where " + MEMBER_CITY + " = '" + bloodGroup + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }


    public ArrayList<ContactBean> searchMember(String tableName, String like) {

        ArrayList<ContactBean> gbList = new ArrayList<ContactBean>();
        try {
            String selectQuery = "SELECT " + MEMBER_NAME + ", " + MEMBER_PHONE + ", " + MEMBER_SEARCHKEY + ", " + MEMBER_UID + ", " + MEMBER_FLAG + ", " + MEMBER_ORG_NAME + ", "
                    + MEMBER_PH_BOK_NAME + ", " + MEMBER_ISMY_CONTACT + ", " + MEMBER_BLOOD_GROUP + ", " + MEMBER_ADMIN_FLAG + ", " + MEMBER_CREATED_DATE + " FROM " + tableName + " WHERE " + MEMBER_SEARCHKEY + " LIKE '%" + like + "%' LIMIT 10";


            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactBean contact = new ContactBean();
                    contact.setName(cursor.getString(0));
                    contact.setNumber(cursor.getString(1));
                    contact.setSearchKey(cursor.getString(2));
                    contact.setUID(cursor.getString(3));
                    contact.setRequestFlag(cursor.getString(4));
                    contact.setOrignalName(cursor.getString(5));
                    contact.setMyPhoneBookName(cursor.getString(6));
                    contact.setIsMyContact(cursor.getInt(7));
                    contact.setmBloodGroup(cursor.getString(8));
                    contact.setAdminFlag(cursor.getString(9));
                    contact.setmCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    public ArrayList<GroupBean> searchGroup(String like) {

        ArrayList<GroupBean> gbList = new ArrayList<GroupBean>();
        try {
            String selectQuery = "SELECT " + T_GROUPID + ", " + T_GROUPNAME + ", " + T_MADEBYNAME + ", " + T_MADEBYPHONENO + ", " + T_TAGNAME + ", " + T_DESCRITION + ", "
                    + T_GROUPPHOTO + ", " + T_MEMBERCOUNT + ", " + T_ACCESS_TYPE + ", " + T_ADMINFLAG + ", " + T_CREATEUPDATETS + " FROM " + TABLE_NAME + " WHERE " + T_GROUPNAME + " LIKE '%" + like + "%' LIMIT 10";


            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GroupBean contact = new GroupBean();
                    contact.setmGroupId(String.valueOf(cursor.getInt(0)));
                    contact.setmGroupName(cursor.getString(1));
                    contact.setmGroupMadeByName(cursor.getString(2));
                    contact.setmGroupMadeByNum(cursor.getString(3));
                    contact.setmGroupTag(cursor.getString(4));
                    contact.setmGroupDesc(cursor.getString(5));
                    contact.setmPhoto(cursor.getString(6));
                    contact.setmGroupSize(Integer.parseInt(cursor.getString(7).trim()));
                    contact.setmGroupAccessType(cursor.getString(8));
                    contact.setmGroupAdmin(cursor.getString(9));
                    contact.setmGroupCreatedDate(cursor.getString(10));
                    gbList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("GroupDBErro", "FetchAllDB "+e.getMessage());
            e.printStackTrace();
        }
        return gbList;
    }

    // Getting single contact
    public ContactBean getMemberDetail(int id, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName, new String[]{MEMBER_EMAIL,
                        MEMBER_ALTERNATE, MEMBER_CITY, MEMBER_ADDRESS, MEMBER_COUNTRY,
                        MEMBER_PROFESSION, MEMBER_PHOTO, MEMBER_NAME, MEMBER_PHONE,
                        MEMBER_ORG_NAME, MEMBER_PH_BOK_NAME, MEMBER_COMPANY_NAME, MEMBER_ISMY_CONTACT, MEMBER_BLOOD_GROUP, MEMBER_ADMIN_FLAG, MEMBER_CREATED_DATE}, MEMBER_UID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ContactBean cb = new ContactBean();
        cb.setEmail(cursor.getString(0));
        cb.setAltPhone(cursor.getString(1));
        cb.setCity(cursor.getString(2));
        cb.setAddress(cursor.getString(3));
        cb.setCountry(cursor.getString(4));
        cb.setProfession(cursor.getString(5));
        cb.setPhoto(cursor.getString(6));
        cb.setName(cursor.getString(7));
        cb.setNumber(cursor.getString(8));
        cb.setOrignalName(cursor.getString(9));
        cb.setMyPhoneBookName(cursor.getString(10));
        cb.setCompany(cursor.getString(11));
        cb.setIsMyContact(cursor.getInt(12));
        cb.setmBloodGroup(cursor.getString(13));
        cb.setAdminFlag(cursor.getString(14));
        cb.setmCreatedDate(cursor.getString(15));
        if (cursor != null)
            cursor.close();
        db.close();
        // return contact
        return cb;
    }

}
