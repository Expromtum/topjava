package ru.javawebinar.topjava.util.sqltracker;

public class QueryCountInfo {
    private int selectCount;
    private int insertCount;
    private int updateCount;
    private int deleteCount;
    private int callCount;

    public void incrementSelectCount() {
        selectCount++;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public int getCallCount() {
        return callCount;
    }

    public void incrementInsertCount() {
        insertCount++;
    }

    public void incrementUpdateCount() {
        updateCount++;
    }

    public void incrementDeleteCount() {
        deleteCount++;
    }

    public void incrementCallCount() {
        callCount++;
    }

    public void clear() {
        selectCount = 0;
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        callCount = 0;
    }

    public int countAll() {
        return selectCount + insertCount + updateCount + deleteCount + callCount;
    }
}