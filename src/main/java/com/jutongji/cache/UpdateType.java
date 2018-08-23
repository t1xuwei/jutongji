package com.jutongji.cache;

public enum UpdateType
{
    CACHE_UPDATE_SINGLE(1),
    CACHE_UPDATE_ALL(2);
    
    private final int value;
    
    private UpdateType(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
}
