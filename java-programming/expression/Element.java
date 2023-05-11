package expression;

public abstract class Element implements Basic{
    public static int priority = 3;
    protected String type;
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() == obj.getClass()) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public String getType() {
        return "element";
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return false;
    }



    public int getPriority() {
        return 3;
    }

}
