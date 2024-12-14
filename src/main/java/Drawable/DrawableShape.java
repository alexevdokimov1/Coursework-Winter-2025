package Drawable;

public enum DrawableShape {
    CIRCLE("CIRCLE"), HEART("HEART");

    private final String name;

    DrawableShape(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static DrawableShape getType(String name){
        return switch (name) {
            case "CIRCLE" -> DrawableShape.CIRCLE;
            case "HEART" -> DrawableShape.HEART;
            default -> throw new ArithmeticException("No such shape type");
        };
    }
}
