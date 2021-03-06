package me.kalmanolah.forumlistener;

import me.kalmanolah.extras.Tools;
import me.kalmanolah.okb3.OKBSync;
import me.kalmanolah.okb3.OKDatabase;
import me.kalmanolah.okb3.OKConfig;
import me.kalmanolah.okb3.OKLogger;
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
    OKDatabase.dbm.prepare(
    "SELECT password FROM " + OKConfig.config.get("db.prefix")
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
                
                HashMap<String, String> rank = new HashMap();
                if(OKConfig.config.get("ranks.identifiers") instanceof HashMap){
                    rank = (HashMap)OKConfig.config.get("ranks.identifiers");
                }
                ResultSet rs = OKDatabase.dbm.prepare("SELECT id FROM " + OKConfig.config.get("db.prefix") + "users WHERE username = '" + username + "'").executeQuery();
                int userid = 0;
                if (rs.next()) {
                    do {
                    userid = rs.getInt("id");

                    } while (rs.next());
                }
                rs.close();
                if(userid != 0){
                    OKDatabase.dbm.prepare("UPDATE " + OKConfig.config.get("db.prefix") + "users SET usertype=" + rank.get(forumGroupId) + " WHERE username = '" + username + "'").executeUpdate();
                    OKDatabase.dbm.prepare("UPDATE " + OKConfig.config.get("db.prefix") + "user_usergroup_map SET group_id=" + forumGroupId + " WHERE user_id = '" + userid + "'").executeUpdate();
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
    ResultSet rs = OKDatabase.dbm.prepare("SELECT usertype FROM " + OKConfig.config.get("db.prefix") + "users WHERE username = '" + username + "'").executeQuery();
    if (rs.next())
    {
    do
    {
    group.add(rs.getInt("usergroupid"));
    }
    while(rs.next());
    }
    rs.close();
    rs = OKDatabase.dbm.prepare("SELECT membergroupids FROM " + OKConfig.config.get("db.prefix") + "users WHERE username = '" + username + "'").executeQuery();
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