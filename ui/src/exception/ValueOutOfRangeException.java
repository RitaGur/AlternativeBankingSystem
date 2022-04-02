package exception;

public class ValueOutOfRangeException extends Exception{
    private final int m_MaxValue;
    private final int m_MinValue;

    public ValueOutOfRangeException(String i_ExceptionMessage, int i_MaxValue, int i_MinValue){
        super(i_ExceptionMessage);
        m_MaxValue = i_MaxValue;
        m_MinValue = i_MinValue;
    }

    public int getMaxValue() {
        return m_MaxValue;
    }

    public int getM_MinValue() {
        return m_MinValue;
    }
}
