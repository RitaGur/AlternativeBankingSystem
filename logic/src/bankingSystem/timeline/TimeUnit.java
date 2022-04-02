package bankingSystem.timeline;

public class TimeUnit {
    private static int m_CurrentTimeUnit;

    public TimeUnit() {
        this.m_CurrentTimeUnit = 1;
    }

    public int getCurrentTimeUnit() {
        return m_CurrentTimeUnit;
    }

    public static void addOneToTimeUnit() {
        m_CurrentTimeUnit++;
    }
}
