package me.kalmanolah.forumlistener;

import com.greatmancode.extras.Tools;
import com.greatmancode.okb3.OKBSync;
import com.greatmancode.okb3.OKBWebsiteDB;
import com.greatmancode.okb3.OKConfig;
import com.greatmancode.okb3.OKLogger;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class JOOMLA implements OKBSync {

public JOOMLA() {
// TODO Auto-generated constructor stub
}

@Override
public boolean accountExist(String username, String password) {
    String encpass = "";
    boolean exist = false;
    try {
    ResultSet rs =
    OKBWebsiteDB.dbm.prepare(
    "SELECT password FROM " + OKConfig.tablePrefix
    + "users WHERE username = '" + username + "'").executeQuery();
    if (rs.next()) {
    do {
    encpass = Tools.md5(Tools.md5(password));
    if (encpass.equals(rs.getString("password"))) {
    exist = true;
    }
    } while (rs.next());
    }
    rs.close();
    } catch (SQLException e) {
    e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
    }
    return exist;
}

@Override
public void changeRank(String username, int forumGroupId) {
    try {
                 Object map = OKConfig.groupList;
                 HashMap<Integer, String> groupmap = null;
                 if(map instanceof HashMap){
                     groupmap = (HashMap)map;
                 }
                
                String rank = groupmap.get(forumGroupId);
                if(groupmap != null){
                    OKBWebsiteDB.dbm.prepare("UPDATE " + OKConfig.tablePrefix + "users SET usertype=" + rank + " WHERE username = '" + username + "'").executeUpdate();
                }
                else{
                    OKLogger.info("[OKB3] Fail at line: '52-54' Contact Somers and tell him he failed.");
                }
    } catch (SQLException e) {
    e.printStackTrace();
    }
}

@Override
public void ban(String username, int forumGroupId) {
    // TODO use vBulletin ban system
    changeRank(username,forumGroupId);
}

@Override
public void unban(String username, int forumGroupId) {
    // TODO use vBulletin ban system
    changeRank(username,forumGroupId);
}

@Override
public List<Integer> getGroup(String username) {
    List<Integer> group = new ArrayList<Integer>();
    try
    {
    ResultSet rs = OKBWebsiteDB.dbm.prepare("SELECT usertype FROM " + OKConfig.tablePrefix + "users WHERE username = '" + username + "'").executeQuery();
    if (rs.next())
    {
    do
    {
    group.add(rs.getInt("usergroupid"));
    }
    while(rs.next());
    }
    rs.close();
    rs = OKBWebsiteDB.dbm.prepare("SELECT membergroupids FROM " + OKConfig.tablePrefix + "users WHERE username = '" + username + "'").executeQuery();
    if (rs.next())
    {
    do
    {
    group.add(rs.getInt("membergroupids"));
    }
    while(rs.next());
    }
    rs.close();
    }
    catch (SQLException e)
            {
                e.printStackTrace();
            }

    return group;
}

}