package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

public enum Privilege {
    READ(0), READWRITE(1), READWRITESUPER(2);


    private int level;
    Privilege(int level){
        this.level=level;
    }
    public int getLevel(){
        return this.level;
    }
    public boolean allowedToGivePrivilege(Privilege privilegeWantToGive){
        return this.level >= privilegeWantToGive.level;
    }
}
