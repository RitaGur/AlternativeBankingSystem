package bankingSystem.timeline;

public class TimeUnitSingleton {
    private static TimeUnitSingleton m_SingleInstance = null;
    public int m_CurrentTimeUnit;

    private TimeUnitSingleton() {
        m_CurrentTimeUnit = 1;
    }

    public static TimeUnitSingleton getInstance() {
        if(m_SingleInstance == null) {
            m_SingleInstance = new TimeUnitSingleton();
        }

        return m_SingleInstance;
    }

    public int getCurrentTimeUnit() {
        return m_CurrentTimeUnit;
    }

    public void addOneToTimeUnit() {
        m_CurrentTimeUnit++;
    }
}
